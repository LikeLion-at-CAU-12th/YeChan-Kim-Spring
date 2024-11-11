package com.example.likelionspring.service;

import com.example.likelionspring.domain.Member;
import com.example.likelionspring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String username = oAuth2User.getAttribute("name");

        Member member = memberRepository.findByEmail(email)
                .map(existingMember -> {
                    existingMember.updateUsername(username);
                    return existingMember;
                })
                .orElseGet(() -> {
                    // 새로운 사용자 생성
                    Member newMember = Member.builder()
                            .email(email)
                            .username(username)
                            .password("") // OAuth 사용자는 비밀번호가 필요하지 않으므로 빈 문자열로 처리
                            .build();
                    memberRepository.save(newMember); // 새로운 사용자만 저장
                    return newMember;
                });

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                "sub");
    }
}

