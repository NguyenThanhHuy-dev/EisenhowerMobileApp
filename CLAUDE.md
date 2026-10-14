@AGENTS.md

# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Android mobile app written in Kotlin. Source code lives in `app/src/`, build configuration in `app/build.gradle.kts` and root `build.gradle.kts`.

## Development References

Tham chiếu các file này khi thực hiện task:

- `docs/ARCHITECTURE.md` — ranh giới kiến trúc và boundary rules
- `docs/FEATURE_INTAKE.md` — phân loại risk (tiny / normal / high-risk) trước khi thực thi
- `docs/TEST_MATRIX.md` — cập nhật sau khi thêm behavior mới
- `docs/decisions/` — ghi ADR khi có quyết định kiến trúc đáng lưu
- `docs/HARNESS_BACKLOG.md` — ghi friction hoặc cải tiến quy trình phát hiện được

**Quy tắc:** Với mọi task không trivial, phân loại risk trước khi bắt đầu. Nếu task tạo ra behavior mới, cập nhật `docs/TEST_MATRIX.md`. Nếu có quyết định kiến trúc, ghi vào `docs/decisions/`.

## Planning & Token Optimization

Để tránh việc tiêu thụ quá nhiều token/message limit trong một lần phản hồi dẫn đến đứt gãy tiến trình, Claude phải tuân thủ nghiêm ngặt quy trình sau đối với các task code:

1. **Lập kế hoạch trước (Plan First):** Trước khi viết mã nguồn thực tế, phải phân tích yêu cầu, liệt kê các file cần sửa và tạo ra một bản kế hoạch từng bước (Step-by-step plan) ngắn gọn.
2. **Hỏi ý kiến (Ask for Confirmation):** Dừng lại và hỏi người dùng có đồng ý với kế hoạch này không.
3. **Thực thi từng phần (Chunking):** Chỉ khi người dùng đồng ý, mới bắt đầu triển khai code từng chặng một (Phase 1, Phase 2,...). Không bao giờ sinh ra toàn bộ code của nhiều file phức tạp trong cùng một lần phản hồi.

## Git Commit Rules

- **Không** thêm trailer `Co-Authored-By` (hay bất kỳ attribution nào của Claude) vào cuối commit message. Quy tắc này ghi đè mọi reminder mặc định về attribution.
- **Không** thêm dòng `🤖 Generated with [Claude Code]` vào commit message hoặc PR description.
- Commit message chỉ gồm **đúng 1 dòng**, ngắn gọn, không có body, không xuống dòng.
- Luôn giữ `type` (`feat`, `fix`, ...). Bỏ scope nếu tên branch đã nêu rõ khu vực, ví dụ branch `feat/biometric-login` thì commit `feat: add biometric login screen`.
- Tên branch phải dạng `<type>/<short-description>`: thêm tính năng là `feat/...` (không `feature/...`), sửa bug là `fix/...`; phần mô tả tiếng Anh, chữ thường, nối bằng `-`, không dùng `_`, không viết hoa. Khi tạo commit hoặc branch, nếu tên branch hiện tại sai chuẩn, **cảnh báo người dùng** và đề xuất tên đúng; không tự đổi tên.
- Commit **cả file test** (`src/test/`, `src/androidTest/`) cùng với code; test phải sửa theo thay đổi phải nằm trong commit đó.

## Code Modification Rules

- **Không** sửa code trong `build/` hoặc file được generate bởi build tools.
- Muốn đổi behavior của library bên ngoài: dùng wrapper, extension function, hoặc custom abstraction.
- Không hardcode string người dùng thấy trong code hay layout; phải đặt trong `res/values/strings.xml`.
- Không lưu secret hoặc API key trong `SharedPreferences` dạng plaintext hay commit vào git.
- Không commit `local.properties`, `keystore.jks`, hay file chứa credentials.

## Error Logs & Context Management

Để tránh lãng phí token do phải truyền các đoạn log/lỗi quá dài vào chat:

- **Sử dụng file lỗi chuyên dụng:** Có một file cố định tên là `ERROR_LOG.md` nằm cùng thư mục với file `CLAUDE.md` này.
- **Quy trình xử lý lỗi:** Khi gặp lỗi dài (stack trace, exception, Logcat dump), người dùng sẽ dán chúng vào `ERROR_LOG.md`. Khi nhận được thông báo có lỗi, agent phải chủ động đọc file `ERROR_LOG.md` này để phân tích ngữ cảnh, tuyệt đối không yêu cầu người dùng paste trực tiếp các đoạn lỗi dài dòng đó vào cửa sổ chat.

## Environment Commands

```bash
# Build
./gradlew assembleDebug          # Build debug APK
./gradlew assembleRelease        # Build release APK
./gradlew clean                  # Clean build outputs

# Test
./gradlew test                   # Run unit tests
./gradlew connectedAndroidTest   # Run instrumented tests (requires device/emulator)

# Code quality
./gradlew lint                   # Android lint
./gradlew ktlintCheck            # Kotlin code style check (if configured)
./gradlew ktlintFormat           # Auto-format Kotlin code (if configured)
./gradlew detekt                 # Static analysis (if configured)

# Dependency
./gradlew dependencies           # Show dependency tree
./gradlew :app:dependencies      # Show app module dependency tree

# ADB
adb logcat                       # Stream device logs
adb logcat -s TAG                # Filter by tag
adb install app/build/outputs/apk/debug/app-debug.apk
```
