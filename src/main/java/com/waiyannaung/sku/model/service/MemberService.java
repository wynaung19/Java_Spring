package com.waiyannaung.sku.model.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.waiyannaung.sku.model.domain.Member;
import com.waiyannaung.sku.model.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional // 트랜잭션 처리(클래스 내 모든 메소드 대상)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // 스프링 버전 5 이후, 단방향 해싱 알고리즘 지원

    private void validateDuplicateMember(AddMemberRequest request) {
        Member findMember = memberRepository.findByEmail(request.getEmail()); // 이메일 존재 유무
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다."); // 예외처리
        }
    }

    public Member saveMember(AddMemberRequest request) {
        validateDuplicateMember(request); // 이메일 체크
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword); // 암호화된 비밀번호 설정
        return memberRepository.save(request.toEntity());
    }
}
