package com.waiyannaung.sku.model.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 업로드를 처리하는 서비스 클래스
 * - 최대 2개 파일 업로드 지원
 * - 파일명 중복 시 자동 이름 변경 (타임스탐프 또는 UUID 추가)
 * - 파일 크기 및 타입 검증
 * - 상세한 에러 처리
 */
@Service
public class FileUploadService {

    @Value("${spring.servlet.multipart.location}")
    private String uploadFolder;

    // 최대 파일 크기 (바이트 단위)
    // app.file.upload.max-size에서 읽거나, 기본값으로 10MB 사용
    // 주의: spring.servlet.multipart.max-file-size는 "10MB" 형식이므로 직접 사용 불가
    @Value("${app.file.upload.max-size:10485760}") // 기본값: 10MB (10485760 bytes)
    private long maxFileSize;

    // 허용하는 파일 확장자 (안전한 파일 타입만)
    private static final List<String> ALLOWED_EXTENSIONS = List.of(
            "txt", "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
            "jpg", "jpeg", "png", "gif", "zip", "rar");

    /**
     * 단일 파일 업로드 처리
     * 
     * @param file      업로드할 파일
     * @param userEmail 사용자 이메일 (사용자별 폴더 생성용)
     * @return 저장된 파일 경로
     * @throws IllegalArgumentException 파일 검증 실패 시
     * @throws IOException              파일 저장 실패 시
     */
    public String uploadFile(MultipartFile file, String userEmail) throws IllegalArgumentException, IOException {
        // 파일 검증
        validateFile(file);

        // 업로드 디렉토리 생성
        String sanitizedEmail = userEmail.replaceAll("[^a-zA-Z0-9._-]", "_");
        Path userUploadPath = Paths.get(uploadFolder, sanitizedEmail).toAbsolutePath();
        if (!Files.exists(userUploadPath)) {
            Files.createDirectories(userUploadPath);
        }

        // 파일명 처리 (중복 시 자동 이름 변경)
        String originalFilename = file.getOriginalFilename();
        String finalFilename = handleDuplicateFilename(originalFilename, userUploadPath);

        // 파일 저장
        Path filePath = userUploadPath.resolve(finalFilename);
        file.transferTo(filePath.toFile());

        return finalFilename; // 저장된 파일명 반환
    }

    /**
     * 다중 파일 업로드 처리 (최대 2개)
     * 
     * @param files     업로드할 파일 배열
     * @param userEmail 사용자 이메일
     * @return 저장된 파일명 리스트
     * @throws IllegalArgumentException 파일 개수 또는 검증 실패 시
     * @throws IOException              파일 저장 실패 시
     */
    public List<String> uploadFiles(MultipartFile[] files, String userEmail)
            throws IllegalArgumentException, IOException {
        // 파일 개수 검증 (최대 2개)
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("업로드할 파일을 선택해주세요.");
        }
        if (files.length > 2) {
            throw new IllegalArgumentException("최대 2개의 파일만 업로드할 수 있습니다.");
        }

        List<String> uploadedFilenames = new ArrayList<>();

        // 업로드 디렉토리 생성
        String sanitizedEmail = userEmail.replaceAll("[^a-zA-Z0-9._-]", "_");
        Path userUploadPath = Paths.get(uploadFolder, sanitizedEmail).toAbsolutePath();
        if (!Files.exists(userUploadPath)) {
            Files.createDirectories(userUploadPath);
        }

        // 각 파일 업로드 처리
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue; // 빈 파일 스킵
            }

            try {
                // 파일 검증
                validateFile(file);

                // 파일명 처리 (중복 시 자동 이름 변경)
                String originalFilename = file.getOriginalFilename();
                String finalFilename = handleDuplicateFilename(originalFilename, userUploadPath);

                // 파일 저장
                Path filePath = userUploadPath.resolve(finalFilename);
                file.transferTo(filePath.toFile());

                uploadedFilenames.add(finalFilename);
            } catch (IllegalArgumentException e) {
                // 개별 파일 검증 실패 시 계속 진행
                System.err.println("파일 검증 실패: " + file.getOriginalFilename() + " - " + e.getMessage());
            }
        }

        if (uploadedFilenames.isEmpty()) {
            throw new IOException("업로드된 파일이 없습니다.");
        }

        return uploadedFilenames; // 저장된 파일명 리스트 반환
    }

    /**
     * 파일 검증 (크기, 확장자)
     * 
     * @param file 검증할 파일
     * @throws IllegalArgumentException 검증 실패 시
     */
    private void validateFile(MultipartFile file) throws IllegalArgumentException {
        // 파일 크기 검증
        if (file.getSize() > maxFileSize) {
            long maxSizeMB = maxFileSize / (1024 * 1024);
            throw new IllegalArgumentException("파일 크기가 " + maxSizeMB + "MB를 초과했습니다.");
        }

        // 파일명 검증
        String filename = file.getOriginalFilename();
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("파일명이 없습니다.");
        }

        // 파일 확장자 검증
        String extension = getFileExtension(filename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("허용되지 않는 파일 타입입니다. (" + extension + ")");
        }
    }

    /**
     * 파일명 중복 처리 - 타임스탐프 또는 UUID 추가로 새로운 이름 생성
     * 
     * @param originalFilename 원본 파일명
     * @param directoryPath    디렉토리 경로
     * @return 중복되지 않는 파일명
     */
    private String handleDuplicateFilename(String originalFilename, Path directoryPath) {
        Path filePath = directoryPath.resolve(originalFilename);

        // 파일이 존재하지 않으면 원본 파일명 반환
        if (!Files.exists(filePath)) {
            return originalFilename;
        }

        // 파일명 분리
        String nameWithoutExtension = getFilenameWithoutExtension(originalFilename);
        String extension = getFileExtension(originalFilename);

        // 타임스탐프 기반 새로운 파일명 생성
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");
        String timestamp = now.format(formatter);

        String newFilename = nameWithoutExtension + "_" + timestamp + "." + extension;
        Path newFilePath = directoryPath.resolve(newFilename);

        // 극히 드문 경우 UUID 추가
        int attempts = 0;
        while (Files.exists(newFilePath) && attempts < 10) {
            String uuid = UUID.randomUUID().toString().substring(0, 8);
            newFilename = nameWithoutExtension + "_" + timestamp + "_" + uuid + "." + extension;
            newFilePath = directoryPath.resolve(newFilename);
            attempts++;
        }

        return newFilename;
    }

    /**
     * 파일명에서 확장자 추출
     * 
     * @param filename 파일명
     * @return 확장자 (점 제외)
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        return "";
    }

    /**
     * 파일명에서 확장자를 제외한 부분 추출
     * 
     * @param filename 파일명
     * @return 확장자를 제외한 파일명
     */
    private String getFilenameWithoutExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return filename.substring(0, lastDotIndex);
        }
        return filename;
    }
}
