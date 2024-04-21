package core.backendstudyToyService.domain.member.service;

import core.backendstudyToyService.domain.member.entitiy.Member;
import core.backendstudyToyService.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class MemberSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MemberSecurityService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not found user" + username));

        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .roles("USER")
                .build();
    }
}
