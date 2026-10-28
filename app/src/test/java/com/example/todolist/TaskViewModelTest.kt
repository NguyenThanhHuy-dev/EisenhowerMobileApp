package com.example.todolist

import com.example.todolist.data.Task
import com.example.todolist.data.TaskDao
import com.example.todolist.data.TaskRepository
import com.example.todolist.ui.viewmodels.TaskViewModel
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

        val q1 = viewModel.quadrant1Tasks.first { it.isNotEmpty() }
        val q2 = viewModel.quadrant2Tasks.first { it.isNotEmpty() }
        val q3 = viewModel.quadrant3Tasks.first { it.isNotEmpty() }
        val q4 = viewModel.quadrant4Tasks.first { it.isNotEmpty() }

        assertEquals("Q1", q1[0].title)
        assertEquals("Q2", q2[0].title)
        assertEquals("Q3", q3[0].title)
        assertEquals("Q4", q4[0].title)
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
