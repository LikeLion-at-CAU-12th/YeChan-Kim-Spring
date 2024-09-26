package com.example.likelionspring.controller;

import com.example.likelionspring.domain.Member;
import com.example.likelionspring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/members")
    public Page<Member> getMembersByPage(@RequestParam int page, @RequestParam int size) {
        return memberService.getMembersByPage(page, size);
    }

    @GetMapping("/members/age")
    public Page<Member> getMembersByAgeGreater(@RequestParam int age, @RequestParam int page, @RequestParam int size) {
        return memberService.getMembersByAgeGreater(age, page, size);
    }

    @GetMapping("/members/username")
    public Page<Member> getMembersByUsernamePrefix(@RequestParam String prefix, @RequestParam int page, @RequestParam int size) {
        return memberService.getMembersByUsernamePrefix(prefix, page, size);
    }
}
