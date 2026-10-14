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

