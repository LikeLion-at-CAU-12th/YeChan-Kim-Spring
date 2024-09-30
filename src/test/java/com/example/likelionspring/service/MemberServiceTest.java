package com.example.likelionspring.service;

import com.example.likelionspring.domain.Member;
import com.example.likelionspring.repository.MemberRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    // 테스트 실행 전, Member 100개 생성하는 코드입니다.
    @BeforeAll
    public static void setUp(@Autowired MemberRepository memberRepository) {
        memberRepository.deleteAll();

        Random random = new Random();

        IntStream.range(0, 100).forEach(i -> {
            String username = "user" + random.nextInt(10000);
            String email = "user" + random.nextInt(10000) + "@example.com";
            int age = random.nextInt(60 - 18 + 1) + 18;
            Member member = Member.builder()
                    .username(username)
                    .email(email)
                    .age(age)
                    .build();
            memberRepository.save(member);
        });
    }

    @Test
    public void testPrintMembersByPage() {
        // 이 부분은 숫자 바꿔가면서 출력해보세요 !
        memberService.printMembersByPage(0, 10);
    }

    @Test
    public void testGetMembersByAgeGreaterthan20(){
        Page<Member> memberPage = memberService.getMembersByAgeGreaterthan20(0, 5);
        assertFalse(memberPage.isEmpty(), "20세 이상의 멤버는 존재하지 않습니다.");

        memberPage.getContent().forEach(member -> {
            assertTrue(member.getAge() >= 20, "모든 멤버는 20세 이상이어야 합니다.");
        });

        System.out.println("20세 이상인 멤버 : ");
        memberPage.getContent().forEach(member -> {
            System.out.println("ID: " + member.getId() + ", Username: " + member.getUsername() + ", Age: " + member.getAge());
        });
    }

    @Test
    public void testGetMembersByUsernamePrefix() {
        Member spec = Member.builder()
                .username("test123")
                .age(23)
                .email("yeahcold@naver.com")
                .build();

        memberRepository.save(spec);

        Page<Member> memberPage = memberService.getMembersByUsernamePrefix("test", 0, 10);
        assertFalse(memberPage.isEmpty(), "'test'로 시작하는 username이 존재하지 않습니다");

        memberPage.getContent().forEach(member -> {
            assertTrue(member.getUsername().startsWith("test"));
        });

        System.out.println("'test'로 시작하는 username : ");
        memberPage.getContent().forEach(member -> {
            System.out.println("ID: " + member.getId() + ", Username: " + member.getUsername() + ", Age : " + member.getAge());
        });
    }
}