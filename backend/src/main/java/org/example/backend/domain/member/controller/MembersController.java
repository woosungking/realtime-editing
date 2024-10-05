package org.example.backend.domain.member.controller;

import org.example.backend.domain.member.dto.MemberRequest;
import org.example.backend.domain.member.service.MembersService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class MembersController {
    private final MembersService memberService;

    public MembersController(MembersService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/member/login")
    public void login(@RequestBody MemberRequest memberRequest){
        memberService.login(memberRequest);
    }

    @PostMapping("/member/signup")
    public void signUp(@RequestBody MemberRequest memberRequest){
        memberService.createMember(memberRequest);
    }

}
