package com.example.likelionspring.repository;


import com.example.likelionspring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    public void testMember(){
        Member member = Member.builder()
                .username("memberA")
                .age(23)
                .email("yeahcold@naver.com")
                .build();

        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).orElse(null);

        Assertions.assertThat(findMember).isNotNull();
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember).isEqualTo(member);
    }

    @Test
    @Transactional
    public void testFindAll(){
        Member member1 = Member.builder()
                .username("member1")
                .age(23)
                .email("yeahcold@naver.com")
                .build();

        Member member2 = Member.builder()
                .username("member2")
                .age(23)
                .email("yeahcold@naver.com")
                .build();

        Member member3 = Member.builder()
                .username("member3")
                .age(23)
                .email("yeahcold@naver.com")
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<Member> members = memberRepository.findAll();

        for (Member member: members){
            System.out.println(member.getId() + " " + member.getAge() + " " + member.getUsername());
        }

        Assertions.assertThat(members).hasSize(3);
    }

    @Test
    @Transactional
    public void testFindByUsername() {
        Member member = Member.builder()
                .username("memberA")
                .age(23)
                .email("yeahcold@naver.com")
                .build();

        memberRepository.save(member);

        List<Member> byUsername = memberRepository.findByUsername(member.getUsername());

        for (Member member1:byUsername){
            System.out.println(member1.getId());
        }

        Assertions.assertThat(byUsername).isNotEmpty();
    }
}