# Agent Operating Guide

Repository này là dự án Android mobile app viết bằng Kotlin. Tài liệu này giữ kỷ luật cộng tác giữa người và agent trước khi đi vào implementation.

Không scaffold bừa source folder, module, script, CI, hay test mới nếu chưa xác định rõ nó phục vụ task hiện tại hoặc phù hợp với cấu trúc Kotlin/Android đang có.

## Source Of Truth

Đọc theo thứ tự sau:

1. `README.md` để biết trạng thái tổng quan của repository.
2. `docs/ARCHITECTURE.md` trước khi đề xuất hướng implementation.
3. `docs/FEATURE_INTAKE.md` trước khi biến prompt thành công việc.
4. Prompt hoặc spec do người dùng cung cấp, nếu có.
5. `docs/stories/` để xem story packet và backlog.
6. `docs/TEST_MATRIX.md` để biết trạng thái bằng chứng và khoảng trống kiểm thử.
7. `docs/decisions/` để hiểu lý do của các quyết định quan trọng.
8. `app/build.gradle.kts` và `settings.gradle.kts` để biết cấu trúc module và dependency.

## Android/Kotlin Repository Context

Mã ứng dụng nằm trong `app/src/`. Cấu trúc package chính thường theo mô hình feature-based hoặc layer-based:

- `app/src/main/java/` — source chính
- `app/src/main/res/` — resource (layout, drawable, string, navigation graph...)
- `app/src/test/` — unit test
- `app/src/androidTest/` — instrumented/integration test

Không sửa trực tiếp code được generate bởi build tools (ví dụ `BuildConfig`, class trong `build/`). Khi cần patch behavior của library bên ngoài, ưu tiên wrapper hoặc abstraction layer thay vì fork/modify library.

## Task Loop

Với mỗi task:

1. Phân loại request bằng `docs/FEATURE_INTAKE.md` nếu tồn tại, hoặc tự phân loại theo ba mức: `tiny`, `normal`, `high-risk`.
2. Xác định input là spec mới, lát cắt spec, change request, maintenance, hay refactor.
3. Xác định các package, feature module, screen, ViewModel, Repository, hoặc UseCase bị ảnh hưởng.
4. Kiểm tra `docs/TEST_MATRIX.md` để biết bằng chứng hiện có và khoảng trống.
5. Chỉ làm việc trong lane đã chọn: `tiny`, `normal`, hoặc `high-risk`.
6. Trước khi kết thúc, tự hỏi:
   - Product behavior có thay đổi không?
   - Validation/test expectation có thay đổi không?
   - Architecture rules có thay đổi không?
   - Có lộ ra failure pattern lặp lại không?
   - Agent tiếp theo có cần hướng dẫn rõ hơn không?
   - Có cần chạy `gradlew build`, `lint`, `test`, hay sync Gradle không?
   - Có bỏ sót navigation graph, DI module, manifest entry, hay resource nào không?
7. Cập nhật trực tiếp các file docs thường quy, hoặc thêm đề xuất vào `docs/HARNESS_BACKLOG.md` nếu thay đổi mang tính cấu trúc.

## Harness Change Policy

Agent có thể cập nhật trực tiếp:

- Trạng thái story và evidence.
- Các dòng trong `docs/TEST_MATRIX.md`.
- Link từ story packet tới product docs.
- Validation notes và reports.
- Các làm rõ nhỏ gắn trực tiếp với task hiện tại.
- Mã nguồn Kotlin, layout XML, resource, DI module, navigation graph khi đó là phần thực thi bắt buộc của task.

Agent nên hỏi xác nhận từ người trước khi:

- Đổi hướng kiến trúc (ví dụ chuyển từ MVI sang MVVM, đổi DI framework).
- Gỡ bỏ validation requirement.
- Thay đổi source-of-truth hierarchy.
- Thay đổi rule phân loại rủi ro.
- Xóa logic cũ mà chưa xác minh hết điểm dùng.
- Thay đổi build script, dependency, hoặc target SDK theo cách có thể phá vỡ compatibility.
- Thực hiện thao tác phá hủy dữ liệu.

## Done Definition

Một task chỉ được xem là hoàn thành khi:

- Thay đổi được yêu cầu đã xong hoặc blocker đã được ghi rõ.
- Docs, stories, test matrix entries, và các file liên quan vẫn còn nhất quán.
- Build (`./gradlew assembleDebug`) và lint không có lỗi mới.
- Validation commands đã được chạy khi chúng tồn tại và khả thi.
- Phản hồi cuối nêu rõ đã thay đổi gì và phần nào chưa được thử hoặc chưa được làm.

## Working Commands

Điểm vào chuẩn là Gradle wrapper:

- `./gradlew assembleDebug`: build debug APK.
- `./gradlew assembleRelease`: build release APK.
- `./gradlew test`: chạy unit test.
- `./gradlew connectedAndroidTest`: chạy instrumented test trên device/emulator.
- `./gradlew lint`: chạy Android lint.
- `./gradlew ktlintCheck`: kiểm tra Kotlin code style (nếu có plugin ktlint).
- `./gradlew ktlintFormat`: auto-format theo code style.
- `./gradlew detekt`: chạy static analysis (nếu có plugin Detekt).
- `./gradlew dependencies`: xem dependency tree.
- `adb logcat`: theo dõi log trên device.

## Editing Conventions

Tuân theo `.editorconfig` nếu có: thường 4 dấu cách cho Kotlin; UTF-8; LF; newline cuối file.

Tuân theo chuẩn Kotlin coding conventions (https://kotlinlang.org/docs/coding-conventions.html):
- `PascalCase` cho class và object.
- `camelCase` cho function và property.
- `SCREAMING_SNAKE_CASE` cho constant.
- File name khớp với tên class chính.

Cấu trúc thường gặp trong một feature:

```
feature/
├── data/           # Repository implementation, data source, DTO, mapper
├── domain/         # UseCase, Entity, Repository interface
└── presentation/   # ViewModel, Fragment/Activity, State, UiModel
```

### String Resource Rule

Mọi chuỗi hiển thị cho người dùng phải đặt trong `res/values/strings.xml`, không hardcode string trực tiếp trong code hoặc layout. Bản dịch tiếng Việt (nếu có) đặt trong `res/values-vi/strings.xml`.

### Browser/Device Test Rule

Khi task có thay đổi UI hoặc luồng người dùng, sau khi test bằng unit test xong, agent phải hỏi người dùng có muốn test thêm trên thiết bị thật hoặc emulator không. Chỉ test khi người dùng đồng ý.

## Commit And Handover Notes

### Commit Message Format

Commit message chỉ gồm **đúng 1 dòng** (subject), không body, không footer, không trailer. Sử dụng cấu trúc:

```text
<type>[optional scope]: <description>
```

Ví dụ:

```text
feat(auth): add biometric login support
fix(cart): correct total price calculation on discount
refactor(network): extract base repository interface
docs(readme): update build instructions
```

### Commit Types

Chọn `type` theo mục đích thực tế của thay đổi:

* `feat`: thêm capability hoặc hành vi mới cho sản phẩm.
* `fix`: sửa bug hoặc hành vi sai. Không dùng `bug:` cho commit mới.
* `refactor`: thay đổi cấu trúc code nhưng không chủ đích thêm feature hoặc sửa hành vi.
* `perf`: cải thiện hiệu năng.
* `docs`: chỉ thay đổi tài liệu.
* `test`: thêm hoặc sửa test.
* `build`: thay đổi build script, Gradle, dependency hoặc tooling.
* `ci`: thay đổi pipeline CI/CD.
* `chore`: công việc bảo trì không thuộc các nhóm trên.
* `style`: chỉ dùng cho thay đổi formatting/whitespace không làm thay đổi logic. Nếu thay đổi UI thực sự sửa bug hoặc thêm hành vi mới, dùng `fix` hoặc `feat`.

### Scope

Scope là tùy chọn nhưng nên dùng khi nó giúp xác định nhanh khu vực bị tác động. Scope phải ngắn, lowercase, ví dụ:

```text
auth
cart
checkout
profile
home
product
search
network
db
navigation
```

Bỏ scope khi tên branch đã nêu rõ khu vực. Ví dụ branch `feat/biometric-login` thì dùng `feat: add biometric login support`, không dùng `feat(auth): ...`. Vẫn luôn giữ `type`.

### Description Rules

Description phải:

* Viết bằng tiếng Anh.
* Ngắn gọn nhưng nói rõ thay đổi thực tế.
* Bắt đầu bằng động từ hành động như `add`, `fix`, `extract`, `remove`, `update`, `prevent`, `support`.
* Không dùng mô tả chung chung như `update code`, `fix bug`, `changes`.
* Không kết thúc bằng dấu chấm.
* Giữ toàn bộ dòng commit trong tối đa 72 ký tự.

### Branch Naming

Tên branch phải theo dạng `<type>/<short-description>`:

* `<type>` dùng cùng bộ type với commit: thêm tính năng là `feat/...` (không `feature/...`), sửa bug là `fix/...`.
* `<short-description>` viết bằng tiếng Anh, chữ thường, các từ nối bằng dấu gạch ngang `-`.
* Không dùng dấu gạch dưới `_`, khoảng trắng, hay viết hoa chữ cái đầu.

Tốt:

```text
feat/biometric-login
fix/cart-total-discount
refactor/network-layer
```

Không tốt:

```text
feature/biometric_login
Fix/CartTotal
biometric-login
```

Khi tạo commit hoặc branch, kiểm tra tên branch hiện tại. Nếu không theo chuẩn trên, **cảnh báo người dùng** và đề xuất tên đúng. Không tự đổi tên branch.

### Atomic Commits

Mỗi commit nên đại diện cho một logical change có thể hiểu và review độc lập.

Trước khi đề xuất hoặc tạo commit, kiểm tra:

```bash
git status
git diff
git diff --staged
```

Không commit:

* Secret, token, password hoặc API key.
* File `local.properties`, `keystore` hoặc bất kỳ file credentials nào.
* File cache, build artifact trong `build/`, `.gradle/`.
* Debug code hoặc temporary logging không còn cần thiết.

### Commit Creation Rules For Agents

Khi người dùng yêu cầu tạo commit:

1. Kiểm tra `git status`.
2. Review diff trước khi stage.
3. Xác định logical change và chọn đúng `type`.
4. Chọn scope khi scope thực sự giúp commit dễ hiểu hơn.
5. Chỉ stage file liên quan.
6. Chạy validation phù hợp với phạm vi thay đổi khi khả thi.
7. Tạo commit message theo các rule ở trên.
8. Sau commit, kiểm tra lại `git status`.

Trước khi kết thúc một phase (hoặc báo hoàn thành task), agent phải **hỏi người dùng có muốn commit không** và chờ trả lời rõ ràng. Không commit nếu người dùng chưa đồng ý.

Không tự ý:

* Commit thay đổi nếu người dùng chưa đồng ý.
* Amend commit đã tồn tại.
* Rebase lịch sử.
* Force push.
* Push branch lên remote.

### Android Commit Validation

Trước commit, chọn validation phù hợp với phạm vi thay đổi:

```bash
./gradlew assembleDebug          # đảm bảo build không vỡ
./gradlew lint                   # kiểm tra lint
./gradlew test                   # chạy unit test
./gradlew ktlintCheck            # kiểm tra code style (nếu có)
./gradlew detekt                 # static analysis (nếu có)
```

Nếu thay đổi cần một trong các bước sau sau khi deploy, phải ghi rõ khi handover:

```text
Gradle sync
ProGuard rule update
Manifest permission update
Room migration
API key rotation
```

### Handover

Khi bàn giao hoặc chuẩn bị PR, nêu ngắn gọn:

* Feature module, package hoặc khu vực bị ảnh hưởng.
* Behavior đã thay đổi.
* Validation đã chạy và kết quả.
* Validation chưa chạy hoặc chưa thể chạy.
* Có cần Gradle sync, Room migration, ProGuard update, hay permission change không.
* Có migration, schema change hoặc API change hay không.

## Prohibited Practices

Các nguyên tắc dưới đây là guardrail bắt buộc khi làm việc với Kotlin/Android codebase.

### Core And Dependency Safety

1. Không sửa trực tiếp code trong thư mục `build/` hoặc bất kỳ file nào do build tool generate.

2. Không fork hoặc modify source library bên thứ ba trực tiếp.

   Nếu cần thay đổi behavior của library, ưu tiên wrapping, extension function, hoặc custom implementation riêng.

3. Không chỉnh `build.gradle.kts` hay dependency nếu task không thực sự yêu cầu.

### Architecture And Dependency Injection

4. Không gọi DI container trực tiếp bằng cách hard-reference (ví dụ `ServiceLocator.get()` hay global singleton) trong business code nếu đã có DI framework (Hilt/Koin).

   Dependency phải được inject qua constructor hoặc framework injection.

5. Không đặt business logic lớn trong Activity hay Fragment.

   Activity/Fragment nên chủ yếu:
   * observe ViewModel state;
   * delegate user action về ViewModel;
   * xử lý navigation và UI lifecycle.

   Business rules, data transformation, network call phải đặt trong ViewModel, UseCase hoặc Repository.

6. Không đặt network call hoặc database query trực tiếp trong ViewModel (nếu có UseCase layer).

   Ưu tiên UseCase làm orchestration layer giữa ViewModel và Repository.

### Database And Persistence

7. Không query Room database trực tiếp từ UI thread.

   Mọi database operation phải chạy trên `Dispatchers.IO` hoặc qua `Flow`/`suspend fun`.

8. Không thực hiện raw SQL UPDATE/DELETE trên Room mà không xem xét:
   * entity và relation bị ảnh hưởng;
   * migration cần thiết;
   * cache/state bị stale sau operation.

### UI And Resources

9. Không hardcode string người dùng thấy trực tiếp trong code hay layout XML.

   Mọi string phải đặt trong `res/values/strings.xml`.

10. Không hardcode color, dimension, hoặc style trực tiếp trong layout.

    Dùng `res/values/colors.xml`, `res/values/dimens.xml`, và theme/style.

11. Không dùng `View.GONE`/`View.VISIBLE` logic phức tạp trong Activity/Fragment nếu có thể dùng data binding hoặc ViewState từ ViewModel.

### Coroutines And Concurrency

12. Không launch coroutine `GlobalScope` trong production code.

    Dùng `viewModelScope`, `lifecycleScope`, hoặc scope được inject qua DI.

13. Không block main thread bằng `runBlocking` trong production code.

### Security And Sensitive Data

14. Không lưu secret, token, API key vào `SharedPreferences` dạng plaintext hoặc commit vào git.

    Dùng `EncryptedSharedPreferences` hoặc Android Keystore cho dữ liệu nhạy cảm.

15. Không commit `local.properties`, `keystore.jks`, hay bất kỳ file chứa credentials.

### Validation Before Merge

16. Không merge hoặc tuyên bố code sẵn sàng merge khi chưa thực hiện validation phù hợp:

    * `./gradlew assembleDebug` — build pass;
    * `./gradlew lint` — không có lỗi mới;
    * `./gradlew test` — unit test pass;
    * Kiểm tra thủ công trên device/emulator nếu có thay đổi UI.

17. Khi thêm hoặc sửa logic có hành vi (ViewModel, UseCase, Repository, Mapper, Service), agent phải viết hoặc cập nhật unit test trong `src/test/` của module đó, cùng cấu trúc package với class được test.

    Unit test phải:
    * phủ từng nhánh nghiệp vụ chính: thành công, lỗi/ngoại lệ, giá trị biên, dữ liệu rỗng;
    * mock mọi phụ thuộc ngoài (network, database, analytics) bằng Mockito, MockK, hoặc fake;
    * với bug đã sửa, có test hồi quy mô tả đúng tình huống gây lỗi;
    * kiểm tra hành vi quan sát được, không chỉ "không ném lỗi";
    * chạy được bằng `./gradlew test` và pass trước khi kết thúc phase.

## Structured Workflow Router

### Lazy Documentation Loading

Không preload toàn bộ `docs/`.

Chỉ load tài liệu đúng với phase hiện tại:

#### Sửa code / feature / change request

Đọc khi cần:

- `docs/ARCHITECTURE.md`
- `docs/FEATURE_INTAKE.md`

Nếu task liên quan navigation, DI, hoặc database:
- Kiểm tra `app/src/main/res/navigation/`, `di/`, hoặc Room database class trước khi viết code mới.

#### Fix bug

Không refactor ngoài phạm vi chỉ để sửa bug nếu chưa chứng minh cần thiết.

#### Commit / pre-commit

Chỉ khi người dùng yêu cầu commit thì mới chạy validation và chuẩn bị commit message.

Không preload commit rules khi chỉ đang phân tích hoặc sửa code.

### Token Discipline

- Không đọc tất cả docs ở đầu session.
- Không load commit/handover rules trước phase commit/handover.
- Không tạo plan mới chỉ để quy trình trông đầy đủ.
- Không đọc toàn bộ file lớn khi grep/line range đủ đáp ứng yêu cầu.
