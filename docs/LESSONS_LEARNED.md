# Lessons Learned

Tài liệu này ghi lại các vấn đề không hiển nhiên, failure pattern, và bài học kinh nghiệm trong quá trình phát triển dự án. Agent phải đọc phần liên quan trước khi bắt đầu task ở cùng module/khu vực.

## Format

```
### [YYYY-MM-DD] <Module hoặc Area>
**Problem:** Mô tả một câu.
**Root Cause:** Nguyên nhân thực tế.
**Fix Applied:** Đã làm gì.
**Watch Out For:** Cần kiểm tra gì lần sau.
```

---

### [2026-10-14] Gradle & Build Tooling
**Problem:** Android Studio báo lỗi Incompatible Gradle JVM version (Gradle 8.13 không tương thích Java 25) và plugin AGP 8.13.0 không tìm thấy.
**Root Cause:** Android Studio 2026.2.1 bundle JetBrains Runtime 25 làm JVM mặc định (Gradle 8.13 chỉ hỗ trợ tối đa Java 23), và libs.versions.toml cấu hình nhầm agp = "8.13.0" (không tồn tại).
**Fix Applied:** Ghim org.gradle.java.home=/usr/lib/jvm/java-17-openjdk-amd64 trong gradle.properties và .idea/gradle.xml, hạ AGP về bản stable 8.7.3 tương thích trong libs.versions.toml.
**Watch Out For:** Khi mở project bằng Android Studio bản mới (Canary/Preview), Studio có thể dùng JBR 25 làm Gradle JVM mặc định; cần đảm bảo ghim Java 17 trong cả gradle.properties và .idea/gradle.xml.


---

### [2026-10-28] Room destructive fallback
**Problem:** `AppDatabase` dùng `fallbackToDestructiveMigration()` cùng `exportSchema = false`.
**Root Cause:** Viết sai hoặc quên migration ở lần nâng version sau sẽ xóa toàn bộ task của người dùng mà không báo lỗi.
**Fix Applied:** Bỏ fallback, bật `exportSchema`, đặt `room.schemaLocation` = `app/schemas` (file schema được commit).
**Watch Out For:** Mỗi lần tăng `version` phải viết `Migration` và commit file `schemas/.../<version>.json`. Đổi package của `AppDatabase` làm đổi tên thư mục schema.

### [2026-10-28] Màu chữ cứng trong Typography
**Problem:** Chữ tiêu đề màn "Add new task" gần như vô hình khi hệ thống ở chế độ tối.
**Root Cause:** `Type.kt` gán cứng `Color.Black` (titleLarge) và `Color.White` (headlineMedium), ghi đè theme.
**Fix Applied:** Bỏ màu trong `Typography`; màu lấy từ `MaterialTheme.colorScheme`.
**Watch Out For:** Không đặt `color` trong `TextStyle` của `Typography`; các màn dùng nền tối cố định cũng phải dùng `colorScheme`.

### [2026-10-28] Build offline và lint
**Problem:** `assembleRelease --offline` lỗi `lintVitalAnalyzeRelease`: không có `lint-gradle` trong cache.
**Root Cause:** Bước lint cần tải thư viện mà máy chưa cache, không phải lỗi code.
**Fix Applied:** Kiểm tra R8 bằng `-x lintVitalAnalyzeRelease -x lintVitalReportRelease -x lintVitalRelease`.
**Watch Out For:** Chạy lint đầy đủ khi có mạng trước khi phát hành.
