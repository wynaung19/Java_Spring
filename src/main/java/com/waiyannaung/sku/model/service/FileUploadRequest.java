package com.waiyannaung.sku.model.service;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * 파일 업로드 요청 데이터 모델
 * - 사용자 이메일
 * - 최대 2개의 파일 업로드 지원
 */
@Data
public class FileUploadRequest {

    // 사용자 이메일 (사용자별 디렉토리 생성 및 식별용)
    private String userEmail;

    // 첫 번째 업로드 파일
    private MultipartFile file1;

    // 두 번째 업로드 파일 (선택사항)
    private MultipartFile file2;

    /**
     * 업로드된 파일 배열 반환
     * 
     * @return 비어있지 않은 파일만 포함한 배열
     */
    public MultipartFile[] getFiles() {
        if (file2 != null && !file2.isEmpty()) {
            return new MultipartFile[] { file1, file2 };
        }
        if (file1 != null && !file1.isEmpty()) {
            return new MultipartFile[] { file1 };
        }
        return new MultipartFile[] {};
    }
}
