package core.backendstudyToyService.domain.member.service.memberserviceimpl;

import core.backendstudyToyService.domain.member.dto.MemberDTO;
import core.backendstudyToyService.domain.member.entitiy.Member;
import core.backendstudyToyService.domain.member.service.MemberService;
import core.backendstudyToyService.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveMember(MemberDTO dto) {
        if (isUsernameAvailable(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 가입된 유저");
        } else {
            Member member = new Member();
            member.setUsername(dto.getUsername());
            member.setPassword(passwordEncoder.encode(dto.getPassword()));
            member.setRole("user");
            memberRepository.save(member);
        }
    }


    @Override
    public Optional<Member> findMemberById(Long memberId) {
        return Optional.empty();
    }

    @Override
    public List<Member> findAllMembers() {
        return null;
    }

    @Override
    public void deleteMember(String username) {
        Optional<Member> memberOptional = memberRepository.findByUsername(username);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            memberRepository.delete(member);
        }
    }



    public MemberDTO login(MemberDTO dto) {
       Optional<Member> enter = memberRepository.findByUsername(dto.getUsername());
        if (enter.isPresent()) {
            Member member = enter.get();
            if (dto.getPassword().equals(member.getPassword())) {
                return dto;
            }else {
                return null;
            }
        }
        return null;
    }


    @Override
    public boolean isUsernameAvailable(String username) {
        // 해당 username을 가진 회원이 존재하는지 조회
        Optional<Member> existingMember = memberRepository.findByUsername(username);
        return existingMember.isPresent();
    }

}




