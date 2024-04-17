package core.backendstudyToyService.domain.member.service.memberserviceimpl;

import core.backendstudyToyService.domain.member.entitiy.Member;
import core.backendstudyToyService.domain.member.service.MemberService;
import core.backendstudyToyService.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    public Member saveMember(Member member) {
        if (DuplicateMember(member)){
            return null;
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setUsername(member.getUsername());

        this.memberRepository.save(member);
        return member;
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
    public void deleteMember(Long memberId) {

    }

    // 중복 검사
    public boolean DuplicateMember(Member member) {
        Optional<Member> existingMember = memberRepository.findByUsername(member.getUsername());

        return existingMember.isPresent();
    }


    public Member login(Member loginMember) {
        Optional<Member> memberName = memberRepository.findByUsername(loginMember.getUsername());
        if (memberName.isPresent()) {
            Member member = memberName.get();
            if (loginMember.getPassword().equals(member.getPassword())) {
                Member successLogin = loginMember;
                return successLogin;
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
        // 존재하지 않으면 사용 가능(true), 존재하면 이미 사용 중(false)
        return !existingMember.isPresent();
    }

}




