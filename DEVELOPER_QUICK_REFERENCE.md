# 🔧 개발자 빠른 참조 가이드

## 📌 빠른 시작

### 세션 처리 - 핵심 변경사항

#### Before (❌ 문제)

```java
// 로그인할 때마다 기존 세션 제거
HttpSession session = request2.getSession(false);
if (session != null) {
    session.invalidate(); // 모든 사용자 강제 로그아웃!
}
```

#### After (✅ 개선)

```java
// 새 세션만 생성 (기존 세션 유지)
HttpSession newSession = request2.getSession(true);
// 기존 session invalidate 제거됨
```

---

## 📁 파일 업로드 - 핵심 코드

### 1. FileUploadService 사용법

```java
@Autowired
private FileUploadService fileUploadService;

// 다중 파일 업로드
try {
    List<String> filenames = fileUploadService.uploadFiles(files, userEmail);
    // filenames: ["report.pdf", "image_20231213_143022_123.jpg"]
} catch (IllegalArgumentException e) {
    // 파일 검증 실패 (크기, 타입)
    model.addAttribute("errorMessage", e.getMessage());
} catch (IOException e) {
    // 파일 저장 실패
    model.addAttribute("errorMessage", "파일 저장 실패");
}
```

### 2. 컨트롤러에서 사용

```java
@PostMapping("/upload-files")
public String upload(
    @RequestParam("file1") MultipartFile file1,
    @RequestParam(value = "file2", required = false) MultipartFile file2,
    @RequestParam("userEmail") String userEmail,
    Model model) {

    try {
        MultipartFile[] files = new MultipartFile[] { file1, file2 };
        List<String> uploaded = fileUploadService.uploadFiles(files, userEmail);

        model.addAttribute("success", true);
        model.addAttribute("files", uploaded);
        return "upload_end";
    } catch (Exception e) {
        model.addAttribute("error", e.getMessage());
        return "error_page/file_upload_error";
    }
}
```

### 3. HTML 폼

```html
<!-- 기본 폼 -->
<form action="/upload-files" method="post" enctype="multipart/form-data">
  <input type="email" name="userEmail" required />
  <input type="file" name="file1" required />
  <input type="file" name="file2" />
  <button type="submit">업로드</button>
</form>

<!-- 완전한 예제는 file_upload_form.html 참조 -->
```

---

## 🔍 파일 검증 규칙

```java
// 파일 크기
10MB 이상 → ❌ 거부

// 파일 타입 (허용 목록)
✅ 허용: txt, pdf, doc, docx, xls, xlsx, ppt, pptx, jpg, jpeg, png, gif, zip, rar
❌ 거부: exe, bat, sh, java, class, sql, 기타

// 파일명
✅ document.pdf
✅ my_file_2024.docx
❌ ../../../etc/passwd (경로 조작)
❌ file<>name.txt (특수문자)
```

---

## 📂 디렉토리 구조

```
./src/main/resources/static/upload/
└── [사용자이메일]/
    ├── document.pdf
    ├── report_20231213_143022_123.pdf    (중복 시)
    ├── image.jpg
    └── ...
```

예시:

```
upload/
├── alice_example_com/
│   ├── report.pdf
│   └── photo.jpg
└── bob_test_com/
    ├── presentation.pptx
    └── data_20231213_143022_123.xlsx
```

---

## 🧪 테스트 코드 예제

### 단위 테스트

```java
@SpringBootTest
class FileUploadServiceTest {

    @Autowired
    private FileUploadService fileUploadService;

    @Test
    void testUploadValidFile() throws IOException {
        // 1. 유효한 파일 준비
        MockMultipartFile file = new MockMultipartFile(
            "file1", "document.pdf", "application/pdf", "content".getBytes()
        );

        // 2. 업로드
        List<String> result = fileUploadService.uploadFiles(
            new MultipartFile[]{file}, "test@example.com"
        );

        // 3. 검증
        assertEquals(1, result.size());
        assertEquals("document.pdf", result.get(0));
    }

    @Test
    void testUploadTooLargeFile() {
        // 크기 초과 파일 테스트
        MockMultipartFile file = new MockMultipartFile(
            "file1", "large.pdf", "application/pdf",
            new byte[11 * 1024 * 1024] // 11MB
        );

        assertThrows(IllegalArgumentException.class, () -> {
            fileUploadService.uploadFiles(new MultipartFile[]{file}, "test@example.com");
        });
    }
}
```

### 통합 테스트

```java
@SpringBootTest
@AutoConfigureMockMvc
class FileControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testMultipleFileUpload() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile(
            "file1", "doc1.pdf", "application/pdf", "content1".getBytes()
        );
        MockMultipartFile file2 = new MockMultipartFile(
            "file2", "doc2.docx", "application/msword", "content2".getBytes()
        );

        mockMvc.perform(multipart("/upload-files")
                .file(file1)
                .file(file2)
                .param("userEmail", "test@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/upload_end"));
    }
}
```

---

## 🐛 디버깅 팁

### 1. 파일 업로드 디버깅

```java
// FileUploadService에 디버그 로그 추가
System.out.println("파일명: " + file.getOriginalFilename());
System.out.println("파일 크기: " + file.getSize() + " bytes");
System.out.println("파일 타입: " + file.getContentType());
System.out.println("저장 경로: " + filePath.toString());
```

### 2. 세션 디버깅

```java
// MemberController에서 세션 정보 확인
HttpSession session = request2.getSession(false);
if (session != null) {
    System.out.println("Session ID: " + session.getId());
    System.out.println("User Email: " + session.getAttribute("email"));
    System.out.println("User ID: " + session.getAttribute("userId"));
    System.out.println("Session Timeout: " + session.getMaxInactiveInterval());
}
```

### 3. 에러 추적

```java
// 컨트롤러에서 상세 에러 출력
catch (IOException e) {
    System.err.println("IOException: " + e.getMessage());
    e.printStackTrace(); // 스택 트레이스 출력
    logger.error("File upload failed", e);
}
```

---

## 📝 설정 커스터마이징

### application.properties

```properties
# 파일 업로드 설정 변경 예제

# 1. 업로드 디렉토리 변경
spring.servlet.multipart.location=/var/uploads

# 2. 파일 크기 제한 변경 (예: 50MB)
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=100MB

# 3. 세션 타임아웃 변경 (예: 30분)
server.servlet.session.timeout=1800s

# 4. 세션 쿠키 이름 변경
server.servlet.session.cookie.name=MY_SESSION_ID
```

### FileUploadService 커스터마이징

```java
// 허용 파일 타입 추가
private static final List<String> ALLOWED_EXTENSIONS = List.of(
    "txt", "pdf", "doc", "docx", "xls", "xlsx",
    "mp3", "mp4", "mov" // 미디어 파일 추가
);

// 최대 파일 크기 변경
@Value("${spring.servlet.multipart.max-file-size:50MB}")
private long maxFileSize; // 기본 50MB
```

---

## 🚀 배포 체크리스트

- [ ] application.properties에서 업로드 디렉토리 확인
- [ ] 업로드 디렉토리에 쓰기 권한 확인
- [ ] 파일 업로드 크기 제한 설정 확인
- [ ] HTTPS 활성화 (세션 보안)
- [ ] 로그 레벨 설정 (프로덕션: WARN)
- [ ] 에러 페이지 커스터마이징
- [ ] 바이러스 스캔 도구 검토

---

## 📋 일반적인 문제 해결

| 문제                   | 원인                           | 해결                       |
| ---------------------- | ------------------------------ | -------------------------- |
| 413 Payload Too Large  | 파일 크기 초과                 | `max-file-size` 증가       |
| 400 Bad Request        | 허용되지 않는 파일 타입        | `ALLOWED_EXTENSIONS` 추가  |
| 세션 즉시 종료         | 로그인 시 invalidate           | MemberController 수정 확인 |
| 파일 저장 실패         | 디렉토리 권한 없음             | 디렉토리 권한 확인         |
| 중복 파일명 처리 안 됨 | handleDuplicateFilename 미작동 | 파일 시스템 확인           |

---

## 💡 모범 사례

### 1. 파일 업로드

```java
// ❌ 나쁜 예
file.transferTo(new File(userUploadPath + "/" + file.getOriginalFilename()));

// ✅ 좋은 예
String safeFilename = handleDuplicateFilename(file.getOriginalFilename(), path);
validateFile(file);
Path filePath = userUploadPath.resolve(safeFilename);
file.transferTo(filePath.toFile());
```

### 2. 에러 처리

```java
// ❌ 나쁜 예
try {
    uploadFile(file);
} catch (Exception e) {
    // 무시
}

// ✅ 좋은 예
try {
    uploadFile(file);
} catch (IllegalArgumentException e) {
    logger.warn("File validation failed: {}", e.getMessage());
    model.addAttribute("error", e.getMessage());
} catch (IOException e) {
    logger.error("File upload failed", e);
    model.addAttribute("error", "Upload failed");
}
```

### 3. 세션 처리

```java
// ❌ 나쁜 예
session.invalidate();
session = request.getSession(true);

// ✅ 좋은 예
HttpSession newSession = request.getSession(true);
// 기존 invalidate 제거
```

---

## 📞 기술 지원

문제가 발생하면:

1. **로그 확인** → `/logs/` 또는 console 출력
2. **에러 메시지 읽기** → 구체적인 원인 파악
3. **문서 확인** → [FILE_UPLOAD_AND_SESSION_IMPROVEMENTS.md](FILE_UPLOAD_AND_SESSION_IMPROVEMENTS.md)
4. **테스트** → 단위 테스트로 격리된 문제 재현

---

## 📚 추가 자료

- [Spring MultipartFile Documentation](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/multipart/MultipartFile.html)
- [Spring Session Management](https://docs.spring.io/spring-session/docs/current/reference/html5/)
- [Servlet API - HttpSession](https://javaee.github.io/servlet-spec/downloads/servlet-4.0/servlet-4_0_FINAL.pdf)

---

**마지막 업데이트:** 2024년 12월 13일
