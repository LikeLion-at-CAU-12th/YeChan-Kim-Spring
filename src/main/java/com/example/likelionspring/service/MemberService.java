package com.example.likelionspring.service;

import com.example.likelionspring.domain.Member;
import com.example.likelionspring.dto.request.JoinRequest;
import com.example.likelionspring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Page<Member> getMembersByPage(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
        return memberRepository.findAll(pageable);
    }

    public void printMembersByPage(int page, int size){
        Page<Member> memberPage = getMembersByPage(page, size);
        List<Member> members = memberPage.getContent();

        for (Member member: members){
            System.out.println("ID: " + member.getId() + ", Username: " + member.getUsername());
        }
    }

    public Page<Member> getMembersByAgeGreaterthan20(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
        int fixedAge = 20;
        return memberRepository.findByAgeGreaterThanEqual(fixedAge, pageable);
    }

    public Page<Member> getMembersByUsernamePrefix(String prefix, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
        return memberRepository.findByUsernameStartingWith(prefix, pageable);
    }

    public void join(JoinRequest joinRequest) {
        if (memberRepository.existsByUsername(joinRequest.getUsername())) {
            return; // 나중에는 예외 처리
        }

        System.out.println("Username" + joinRequest.getUsername());
        Member member = Member.builder()
                .username(joinRequest.getUsername())
                .email(joinRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(joinRequest.getPassword()))
                .build();

        memberRepository.save(member);
    }
}
