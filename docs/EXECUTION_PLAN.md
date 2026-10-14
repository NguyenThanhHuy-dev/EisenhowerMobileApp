# Kế hoạch Thực thi (Execution Plan) - ToDoList Eisenhower Matrix

## 1. Tổng quan Task (Task Overview)
Cải tiến toàn diện ứng dụng Eisenhower ToDoList về mặt: Logic nghiệp vụ, Tối ưu hiệu năng cho máy Pixel màn hình nhỏ, Kiến trúc dữ liệu Room, Chuẩn hóa Giao diện (UI/UX) và Hệ thống chuỗi đa ngôn ngữ (Localization).

---

## 2. Các file tác động chính (Key Files)
- `app/src/main/java/com/example/todolist/data/Task.kt`
- `app/src/main/java/com/example/todolist/data/TaskDao.kt`
- `app/src/main/java/com/example/todolist/data/TaskRepository.kt`
- `app/src/main/java/com/example/todolist/ui/viewmodels/TaskViewModel.kt`
- `app/src/main/java/com/example/todolist/ui/theme/Theme.kt`
- `app/src/main/java/com/example/todolist/ui/theme/Color.kt`
- `app/src/main/java/com/example/todolist/ui/tasks/FinishedTaskScreen.kt`
- `app/src/main/java/com/example/todolist/ui/tasks/EisenhowerScreen.kt`
- `app/src/main/java/com/example/todolist/ui/tasks/AddTaskScreen.kt`
- `app/src/main/java/com/example/todolist/ui/tasks/TaskDetailScreen.kt`
- `app/src/main/java/com/example/todolist/ui/components/TaskCard.kt`
- `app/src/main/java/com/example/todolist/ui/components/Quadrant.kt`
- `app/src/main/res/values/strings.xml`
- `app/src/main/res/values-vi/strings.xml`
- `app/src/test/java/com/example/todolist/TaskViewModelTest.kt`

---

## 3. Các giai đoạn thực hiện (Phases)

### Giai đoạn 1: Fix Logic Dang Dở, Tối Ưu Room Flow, Chuẩn Hóa Theme & Strings [HOÀN THÀNH]
1. **Sửa Logic `FinishedTasksScreen`:**
   - Cho phép uncheck để phục hồi task (`isCompleted = false`). Đã hoàn thành.
   - Thêm nút icon "Xóa tất cả việc đã hoàn thành" kèm confirm dialog và empty state. Đã hoàn thành.
2. **Tối ưu Room Query & ViewModel:**
   - Gộp các Flow query riêng rẽ của 4 góc thành 1 query `getActiveTasks()`. Đã hoàn thành.
   - Phân loại 4 góc bằng StateFlow trên bộ nhớ trong `TaskViewModel`. Đã hoàn thành.
   - Hỗ trợ injectable scope cho Unit test. Đã hoàn thành.
3. **Chuẩn hóa Theme & Color:**
   - Sửa lỗi tương phản trong `Theme.kt` (Dark theme và Light theme chuẩn Material 3). Đã hoàn thành.
4. **Localization & Resource Strings:**
   - Trích xuất toàn bộ chuỗi hardcode vào `strings.xml` và `res/values-vi/strings.xml`. Đã hoàn thành.
5. **Unit Tests:**
   - Viết bộ unit test `TaskViewModelTest.kt` kiểm tra toàn bộ 6 nhánh hành vi (thêm, cập nhật, phục hồi, xóa, xóa sạch, phân loại 4 góc). 100% test pass. Đã hoàn thành.

### Giai đoạn 2: Nâng Cấp UX/UI, Empty State, Chế Độ Xem Kép (Grid vs Tab) Cho Pixel [CHƯA BẮT ĐẦU]
1. **Chế độ xem kép (View Modes):**
   - Toggle giữa **Lưới 2x2 (Grid)** và **4 Tab vuốt ngang (HorizontalPager)** tối ưu cho Pixel 1-3 màn hình nhỏ.
2. **TaskCard & Empty State:**
   - Làm đẹp TaskCard với thanh màu chỉ báo mức độ ưu tiên (accent strip) và hiệu ứng gạch ngang text khi hoàn thành.
   - Thêm Empty State thân thiện cho từng góc khi chưa có việc.
3. **An toàn khi xóa (Undo Snackbar):**
   - Bấm xóa có Snackbar "Đã xóa công việc" kèm nút "Hoàn tác" (Undo) 4 giây.

### Giai đoạn 3: Hạn Chót (Due Date) & Nhắc Việc (Reminder) [CHƯA BẮT ĐẦU]
1. Thêm trường `dueDate` vào `Task` entity (Room migration v1 -> v2).
2. Date/Time Picker trong màn hình thêm và sửa task.
3. Lên lịch thông báo với AlarmManager / WorkManager.

---

## 4. Kế hoạch Xác minh (Verification)
- Chạy unit tests: `./gradlew test` pass 100%.
- Kiểm tra build: `./gradlew assembleDebug` thành công không có warning/error mới.
- Kiểm tra lint: `./gradlew lint` đạt tiêu chuẩn.
- Xác nhận không có string nào hardcode trực tiếp trong UI.
