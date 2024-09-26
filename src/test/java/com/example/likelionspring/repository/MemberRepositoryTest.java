package com.example.likelionspring.repository;

import com.example.likelionspring.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @Transactional
    @Rollback(value = false)
    void save1() {
        Member member = Member.builder()
                .username("memberA")
                .age(23)
                .email("hello@naver.com")
                .build();

        Long savedId = memberRepository.save(member);


        Member findMember = memberRepository.findOne(savedId);

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember).isEqualTo(member);
    }

    @Test
    void findOne() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findByUsername() {
    }
}