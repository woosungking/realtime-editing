package org.example.backend.domain.member.service;

import org.example.backend.domain.member.entity.CustomMembersDetail;
import org.example.backend.domain.member.entity.Members;
import org.example.backend.domain.member.repository.MembersRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
public class MembersDetailService implements UserDetailsService {
    private final MembersRepository membersRepository;
    private final PasswordEncoder passwordEncoder;

    public MembersDetailService(MembersRepository membersRepository, PasswordEncoder passwordEncoder) {
        this.membersRepository = membersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 이메일과 비밀번호로 회원 정보를 조회
        Optional<Members> optionalMembers = membersRepository.findByEmail(email);
        if (!optionalMembers.isPresent()) {
            System.out.println("멤버 없음 ㅋ");
            throw new UsernameNotFoundException("ㅋㅋ");
        }
        System.out.println("인증 1단계 통과");
        Members members = optionalMembers.get();
        System.out.println(members.getPassword());
        // CustomUserDetails 객체로 변환하여 반환
        return new CustomMembersDetail(members);
    }
}
