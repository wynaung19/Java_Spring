package com.waiyannaung.sku.model.domain;

import lombok.*; // 어노테이션 자동 생성
import jakarta.persistence.*; // 기존 javax 후속 버전

@Getter // setter는 없음(무분별한 변경 x)
@Entity // 아래 객체와 DB 테이블을 매핑. JPA가 관리
@Table(name = "board") // 테이블 이름을 지정. 없는 경우 클래스이름으로 설정
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부 생성자 접근 방지
public class Board {
    @Id // 기본 키
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 1씩 증가
    @Column(name = "id", updatable = false) // 수정 x
    private Long id;
    @Column(name = "title", nullable = false) // null x
    private String title = "";
    @Column(name = "content", nullable = false)
    private String content = "";
    @Column(name = "user", nullable = false) // 이름
    private String user = "";
    @Column(name = "newdate", nullable = false) // 날짜
    private String newdate = "";
    @Column(name = "count", nullable = false) // 조회수
    private String count = "";
    @Column(name = "likec", nullable = false) // 좋아요
    private String likec = "";
    @Column(name = "password", nullable = false) // 게시글 비밀번호 (DB에서 NOT NULL)
    private String password = ""; // 기본값 빈 문자열
    @Column(name = "address", nullable = true) // 주소 (선택사항)
    private String address = ""; // 기본값 빈 문자열
    @Column(name = "age", nullable = false) // 나이 (DB에서 NOT NULL이므로 기본값 제공)
    private String age = "0"; // 기본값 "0"
    @Column(name = "email", nullable = false) // 이메일 (DB에서 NOT NULL이므로 기본값 제공)
    private String email = ""; // 기본값 빈 문자열
    @Column(name = "mobile", nullable = false) // 연락처 (DB에서 NOT NULL이므로 기본값 제공)
    private String mobile = ""; // 기본값 빈 문자열
    @Column(name = "name", nullable = false) // 이름 (DB에서 NOT NULL이므로 기본값 제공)
    private String name = ""; // 기본값 빈 문자열

    @Builder // 생성자에 빌더 패턴 적용(불변성)
    public Board(String title, String content, String user, String newdate, String count, String likec, String password, String address, String age, String email, String mobile, String name) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.newdate = newdate;
        this.count = count;
        this.likec = likec;
        this.password = password != null ? password : ""; // 기본값 처리
        this.address = address != null ? address : ""; // 기본값 처리
        this.age = age != null ? age : "0"; // 기본값 처리
        this.email = email != null ? email : ""; // 기본값 처리
        this.mobile = mobile != null ? mobile : ""; // 기본값 처리
        this.name = name != null ? name : ""; // 기본값 처리
    }

    public void update(String title, String content, String user, String newdate, String count, String likec, String password, String address, String age, String email, String mobile, String name) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.newdate = newdate;
        this.count = count;
        this.likec = likec;
        this.password = password != null ? password : ""; // 기본값 처리
        this.address = address != null ? address : ""; // 기본값 처리
        this.age = age != null ? age : "0"; // 기본값 처리
        this.email = email != null ? email : ""; // 기본값 처리
        this.mobile = mobile != null ? mobile : ""; // 기본값 처리
        this.name = name != null ? name : ""; // 기본값 처리
    }
}