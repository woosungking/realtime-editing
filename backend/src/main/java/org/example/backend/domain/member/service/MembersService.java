package org.example.backend.domain.member.service;

import org.example.backend.domain.member.dto.MemberRequest;
import org.example.backend.domain.member.entity.Members;
import org.example.backend.domain.member.repository.MembersRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MembersService {
    private final MembersRepository memberRepository;

    private final AuthenticationManager authenticationManager;

    private final MembersDetailService membersDetailService;
    //그냥 여담으로 해당 인터페이스의 구현체로 authentivateProvider가 있는데 거기에 .authenticate()의 구현체가 있음.

    private final PasswordEncoder passwordEncoder;

    public MembersService(MembersRepository memberRepository, AuthenticationManager authenticationManager, MembersDetailService membersDetailService, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.authenticationManager = authenticationManager;

        this.membersDetailService = membersDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void createMember(MemberRequest memberRequest) {
        String email = memberRequest.getEmail();
        String password = memberRequest.getPassword();
        String encodedPassword = passwordEncoder.encode(password); // 저장도 인코딩을 해쉬로 해야 로그 인 시 비교할때 오류가 안나옴
        Members member = new Members.Builder().setEmail(email).setPassword(encodedPassword).build();

        memberRepository.save(member);

    }

    public void login(MemberRequest memberRequest) {
        String email = memberRequest.getEmail();
        String password = memberRequest.getPassword();
        try {
            // AuthenticationManager를 통해 인증 시도
            UserDetails userDetails = membersDetailService.loadUserByUsername(email);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("잘못된 비밀번호입니다.");
            }
            // 인증 정보 생성 및 SecurityContext에 설정
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDetails.getUsername(), password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            System.out.println(e);
        }


    }
}