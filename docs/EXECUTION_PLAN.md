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

### Giai đoạn 2: Nâng Cấp UX/UI, Empty State, Chế Độ Xem Kép (Grid vs Tab) Cho Pixel [HOÀN THÀNH]
1. **Chế độ xem kép (View Modes):**
   - Toggle giữa **Lưới 2x2 (Grid)** và **4 Tab vuốt ngang (HorizontalPager)** tối ưu cho Pixel 1-3 màn hình nhỏ, có ghi nhớ lựa chọn của người dùng trong SharedPreferences. Đã hoàn thành.
2. **TaskCard & Empty State:**
   - Làm đẹp TaskCard với thanh màu chỉ báo mức độ ưu tiên (accent strip), Material ripple effect khi bấm, viền bo tròn 12dp và hiệu ứng gạch ngang text khi hoàn thành. Đã hoàn thành.
   - Thêm Empty State thân thiện kèm icon và thông điệp cho từng góc khi chưa có việc. Đã hoàn thành.
3. **An toàn khi xóa (Undo Snackbar):**
   - Tích hợp `SnackbarHost` hiển thị "Đã xóa công việc" kèm nút "Hoàn tác" (Undo) trong 4 giây. Đã hoàn thành.

### Giai đoạn 3: Hạn Chót (Due Date) & Nhắc Việc (Reminder) [CHƯA BẮT ĐẦU]
1. Thêm trường `dueDate` vào `Task` entity (Room migration v1 -> v2).
2. Date/Time Picker trong màn hình thêm và sửa task.
3. Lên lịch thông báo với AlarmManager / WorkManager.


### Giai đoạn 5: Dọn code, cấu trúc, UX, hiệu suất (2026-10-28) [HOÀN THÀNH, chưa push]
Nhánh `refactor/cleanup-and-ux`. Mỗi bước một commit, unit test và build debug/release đều qua.
1. **An toàn dữ liệu:** bỏ `fallbackToDestructiveMigration`, bật `exportSchema`, truy vấn có `ORDER BY` (hạn chót, rồi ngày tạo), xóa `getAllTasks` không dùng.
2. **Cấu trúc package:** `core/{database,theme,audio,prefs,ui}`, `feature/tasks/{data,reminder,ui,ui/components}`, `navigation/`.
3. **Gom code lặp:** `QuadrantType` (enum 4 góc), `QuadrantStyle`, `UserPrefs`; `TaskViewModel.quadrants` thay 4 StateFlow; `EisenhowerScreen` 431 -> khoảng 290 dòng.
4. **UX:** giao diện theo hệ thống (bỏ nền tối cố định, bỏ màu chữ cứng trong Typography), vuốt trái xóa (có Undo), vuốt phải hoàn thành, nút `+` thêm nhanh ở từng góc (điền sẵn Important/Urgent), tương phản chữ.
5. **Hiệu suất:** `TaskCard` bỏ `IntrinsicSize.Min`, định dạng ngày dùng `remember`, bỏ lớp phủ toàn màn hình, `collectAsStateWithLifecycle`, bật R8 + shrinkResources cho release, `animateItem()` cho danh sách.

**Việc còn lại:** nối `TaskReminderManager.scheduleReminder` vào luồng thêm/sửa task và xin quyền thông báo (hiện chưa được gọi ở đâu); thêm chọn hạn chót ở `AddTaskScreen`; tính năng "Cần mua" (tạm hoãn); đo hiệu năng trên máy thật.

---

## 4. Kế hoạch Xác minh (Verification)
- Chạy unit tests: `./gradlew test` pass 100%.
- Kiểm tra build: `./gradlew assembleDebug` thành công không có warning/error mới.
- Kiểm tra lint: `./gradlew lint` đạt tiêu chuẩn.
- Xác nhận không có string nào hardcode trực tiếp trong UI.
