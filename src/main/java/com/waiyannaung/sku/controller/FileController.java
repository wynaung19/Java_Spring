package com.waiyannaung.sku.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.waiyannaung.sku.model.service.FileUploadService;

/**
 * 파일 업로드를 처리하는 컨트롤러
 * - 이메일 콘텐츠 업로드 (텍스트 파일)
 * - 다중 파일 업로드 (최대 2개, 자동 이름 변경, 에러 처리)
 */
@Controller
public class FileController {

    @Value("${spring.servlet.multipart.location}") // properties 등록된 설정(경로) 주입
    private String uploadFolder;

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 이메일 콘텐츠 업로드 (기존 기능)
     * 
     * @param email              사용자 이메일
     * @param subject            메일 제목
     * @param message            메일 메시지
     * @param redirectAttributes 리다이렉트 속성
     * @return 성공/실패 페이지
     */
    @PostMapping("/upload-email")
    public String uploadEmail( // 이메일, 제목, 메시지를 전달받음
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            RedirectAttributes redirectAttributes) {
        try {
            Path uploadPath = Paths.get(uploadFolder).toAbsolutePath();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String sanitizedEmail = email.replaceAll("[^a-zA-Z0-9]", "_");
            Path filePath = uploadPath.resolve(sanitizedEmail + ".txt"); // 업로드 폴더에 .txt 이름 설정
            System.out.println("File path: " + filePath); // 디버깅용 출력
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
                writer.write("메일 제목: " + subject); // 쓰기
                writer.newLine(); // 줄 바꿈
                writer.write("요청 메시지:");
                writer.newLine();
                writer.write(message);
            }
            redirectAttributes.addFlashAttribute("message", "메일 내용이 성공적으로 업로드되었습니다!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "업로드 중 오류가 발생했습니다.");
            return "/error_page/article_error"; // 오류 처리 페이지로 연결
        }
        return "upload_end"; // .html 파일 연동
    }

    /**
     * 다중 파일 업로드 처리 (최대 2개)
     * 기능:
     * - 최대 2개 파일 업로드 지원
     * - 파일명 중복 시 타임스탐프 추가하여 자동 이름 변경
     * - 파일 크기 및 타입 검증
     * - 상세한 에러 처리
     * 
     * @param file1              첫 번째 파일
     * @param file2              두 번째 파일 (선택사항)
     * @param userEmail          사용자 이메일 (사용자별 폴더 생성용)
     * @param model              모델 (뷰에 데이터 전달)
     * @param redirectAttributes 리다이렉트 속성
     * @return 성공 시 upload_end, 실패 시 에러 페이지
     */
    @PostMapping("/upload-files")
    public String uploadMultipleFiles(
            @RequestParam(value = "file1", required = false) MultipartFile file1,
            @RequestParam(value = "file2", required = false) MultipartFile file2,
            @RequestParam("userEmail") String userEmail,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            // 파일 배열 생성
            MultipartFile[] files = new MultipartFile[] { file1, file2 };

            // 파일 유효성 검증
            if ((file1 == null || file1.isEmpty()) && (file2 == null || file2.isEmpty())) {
                // 파일이 없는 경우
                model.addAttribute("errorType", "NO_FILE");
                model.addAttribute("errorMessage", "업로드할 파일을 선택해주세요.");
                return "/error_page/file_upload_error";
            }

            // FileUploadService를 사용하여 파일 업로드 처리
            List<String> uploadedFilenames = fileUploadService.uploadFiles(files, userEmail);

            // 성공 메시지 설정
            String message = uploadedFilenames.size() + "개의 파일이 성공적으로 업로드되었습니다.\n";
            message += "파일명: " + String.join(", ", uploadedFilenames);
            redirectAttributes.addFlashAttribute("message", message);
            redirectAttributes.addFlashAttribute("uploadedFiles", uploadedFilenames);

            System.out.println("파일 업로드 성공: " + userEmail + " - " + uploadedFilenames);
            return "redirect:/upload_end";

        } catch (IllegalArgumentException e) {
            // 파일 검증 실패 (파일 크기 초과, 허용되지 않는 파일 타입)
            System.err.println("파일 검증 실패: " + e.getMessage());
            model.addAttribute("errorType", "VALIDATION_ERROR");
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("errorDetail", "파일 크기나 타입을 확인해주세요.");
            return "/error_page/file_upload_error";

        } catch (IOException e) {
            // 파일 저장 실패
            System.err.println("파일 저장 실패: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorType", "UPLOAD_ERROR");
            model.addAttribute("errorMessage", "파일 업로드 중 오류가 발생했습니다.");
            model.addAttribute("errorDetail", e.getMessage());
            return "/error_page/file_upload_error";

        } catch (Exception e) {
            // 예상 외 오류
            System.err.println("예상 외 오류 발생: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorType", "UNKNOWN_ERROR");
            model.addAttribute("errorMessage", "알 수 없는 오류가 발생했습니다.");
            model.addAttribute("errorDetail", e.getMessage());
            return "/error_page/file_upload_error";
        }
    }
}
