package com.waiyannaung.sku.model.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*; // 어노테이션 자동 생성
import com.waiyannaung.sku.model.domain.Board;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Data // getter, setter, toString, equals 등 자동 생성
public class AddArticleRequest {
    @NotBlank
    @Size(max = 200)
    private String title;
    @NotBlank
    @Size(max = 5000)
    private String content;
    private String user;
    private String newdate;
    private String count;
    private String likec;
    private String password; // 게시글 비밀번호 (DB 컬럼 대응)
    private String address; // 주소 필드 추가
    private String age; // 나이 필드 추가 (DB 컬럼 대응)
    private String email; // 이메일 필드 추가 (DB 컬럼 대응)
    private String mobile; // 연락처 필드 추가 (DB 컬럼 대응)
    private String name; // 이름 필드 추가 (DB 컬럼 대응)

    public Board toEntity() { // Article 객체 생성
        String resolvedDate = resolveDate();
        return Board.builder()
                .title(title)
                .content(content)
                .user(user)
            .newdate(resolvedDate)
                .count(count)
                .likec(likec)
                .password(password != null ? password : "") // 비밀번호 포함 (기본값 빈 문자열)
                .address(address) // 주소 포함
                .age(age != null ? age : "0") // 나이 포함 (기본값 0)
                .email(email != null ? email : "") // 이메일 포함 (기본값 빈 문자열)
                .mobile(mobile != null ? mobile : "") // 연락처 포함 (기본값 빈 문자열)
                .name(name != null ? name : "") // 이름 포함 (기본값 빈 문자열)
                .build();
    }

    private String resolveDate() {
        if (newdate != null && !newdate.isBlank() && !"오늘날짜".equals(newdate)) {
            return newdate;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.now().format(formatter);
    }
}