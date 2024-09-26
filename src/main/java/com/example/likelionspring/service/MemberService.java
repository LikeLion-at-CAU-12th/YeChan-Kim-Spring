package com.example.likelionspring.service;

import com.example.likelionspring.domain.Member;
import com.example.likelionspring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

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
}
