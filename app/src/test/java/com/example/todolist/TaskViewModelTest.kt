package com.example.todolist

import com.example.todolist.feature.tasks.data.Task
import com.example.todolist.feature.tasks.data.TaskDao
import com.example.todolist.feature.tasks.data.TaskRepository
import com.example.todolist.feature.tasks.QuadrantType
import com.example.todolist.feature.tasks.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeTaskDao : TaskDao {
    private val tasks = MutableStateFlow<List<Task>>(emptyList())

    override fun getActiveTasks(): Flow<List<Task>> =
        tasks.map { list -> list.filter { !it.isCompleted } }

    override fun getCompletedTasks(): Flow<List<Task>> =
        tasks.map { list -> list.filter { it.isCompleted } }

    override suspend fun clearCompletedTasks() {
        tasks.value = tasks.value.filter { !it.isCompleted }
    }

    override fun getTaskById(taskId: Long): Flow<Task?> =
        tasks.map { list -> list.find { it.id == taskId } }

    override suspend fun insertTask(task: Task): Long {
        val nextId = if (task.id == 0L) (tasks.value.maxOfOrNull { it.id } ?: 0L) + 1 else task.id
        tasks.value = tasks.value + task.copy(id = nextId)
        return nextId
    }

    override suspend fun updateTask(task: Task) {
        tasks.value = tasks.value.map { if (it.id == task.id) task else it }
    }

    override suspend fun deleteTask(task: Task) {
        tasks.value = tasks.value.filter { it.id != task.id }
    }
}

class TaskViewModelTest {

    private lateinit var fakeDao: FakeTaskDao
    private lateinit var repository: TaskRepository
    private lateinit var viewModel: TaskViewModel
    private val testScope = CoroutineScope(Dispatchers.Unconfined)

    @Before
    fun setUp() {
        fakeDao = FakeTaskDao()
        repository = TaskRepository(fakeDao)
        viewModel = TaskViewModel(repository, testScope)
    }

    @Test
    fun addTask_shouldAppearInActiveTasks() = runBlocking {
        val task = Task(title = "Buy milk", isImportant = true, isUrgent = true)
        viewModel.addTask(task)

        val active = viewModel.activeTasks.first()
        assertEquals(1, active.size)
        assertEquals("Buy milk", active[0].title)
        assertFalse(active[0].isCompleted)
    }

    @Test
    fun tasks_shouldBeCorrectlyClassifiedIntoFourQuadrants() = runBlocking {
        viewModel.addTask(Task(title = "Q1", isImportant = true, isUrgent = true))
        viewModel.addTask(Task(title = "Q2", isImportant = true, isUrgent = false))
        viewModel.addTask(Task(title = "Q3", isImportant = false, isUrgent = true))
        viewModel.addTask(Task(title = "Q4", isImportant = false, isUrgent = false))

        val byType = viewModel.quadrants.first { map -> map.values.all { it.isNotEmpty() } }

        assertEquals(listOf("Q1"), byType.getValue(QuadrantType.DO_FIRST).map { it.title })
        assertEquals(listOf("Q2"), byType.getValue(QuadrantType.SCHEDULE).map { it.title })
        assertEquals(listOf("Q3"), byType.getValue(QuadrantType.DELEGATE).map { it.title })
        assertEquals(listOf("Q4"), byType.getValue(QuadrantType.DONT_DO).map { it.title })
    }

    @Test
    fun completeTask_shouldMoveToCompletedTasks() = runBlocking {
        val task = Task(id = 1, title = "Finish report", isImportant = true, isUrgent = false)
        viewModel.addTask(task)

        viewModel.updateTask(task.copy(isCompleted = true))

        val completed = viewModel.completedTasks.first { it.isNotEmpty() }
        val active = viewModel.activeTasks.first()

        assertEquals(1, completed.size)
        assertEquals("Finish report", completed[0].title)
        assertTrue(active.isEmpty())
    }

    @Test
    fun setCompleted_shouldStampCompletionTimeAndRestoreShouldClearIt() = runBlocking {
        val clocked = TaskViewModel(repository, testScope, clock = { 1234L })
        val task = Task(id = 1, title = "Stamp", isImportant = true, isUrgent = true)
        clocked.addTask(task)

        clocked.setCompleted(task, completed = true)
        val done = clocked.completedTasks.first { it.isNotEmpty() }
        assertEquals(1234L, done[0].completedAt)

        clocked.restoreTask(done[0])
        val active = clocked.activeTasks.first { it.isNotEmpty() }
        assertEquals(null, active[0].completedAt)
        assertFalse(active[0].isCompleted)
    }

    @Test
    fun restoreTask_shouldMoveBackToActiveTasks() = runBlocking {
        val task = Task(id = 1, title = "Review PR", isImportant = true, isUrgent = true, isCompleted = true)
        fakeDao.insertTask(task)

        viewModel.restoreTask(task)

        val active = viewModel.activeTasks.first { it.isNotEmpty() }
        val completed = viewModel.completedTasks.first()

        assertEquals(1, active.size)
        assertFalse(active[0].isCompleted)
        assertTrue(completed.isEmpty())
    }

    @Test
    fun clearCompletedTasks_shouldRemoveAllCompletedOnly() = runBlocking {
        fakeDao.insertTask(Task(id = 1, title = "Done 1", isImportant = true, isUrgent = true, isCompleted = true))
        fakeDao.insertTask(Task(id = 2, title = "Done 2", isImportant = false, isUrgent = false, isCompleted = true))
        fakeDao.insertTask(Task(id = 3, title = "Active 1", isImportant = true, isUrgent = false, isCompleted = false))

        viewModel.clearCompletedTasks()

        val completed = viewModel.completedTasks.first()
        val active = viewModel.activeTasks.first()

        assertTrue(completed.isEmpty())
        assertEquals(1, active.size)
        assertEquals("Active 1", active[0].title)
    }

    @Test
    fun deleteTask_shouldRemoveTaskCompletely() = runBlocking {
        val task = Task(id = 1, title = "Throw trash", isImportant = false, isUrgent = true)
        fakeDao.insertTask(task)

        viewModel.deleteTask(task)

        val active = viewModel.activeTasks.first()
        assertTrue(active.isEmpty())
    }
}
