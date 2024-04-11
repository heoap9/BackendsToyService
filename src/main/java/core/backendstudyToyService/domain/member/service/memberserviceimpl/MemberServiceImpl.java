package core.backendstudyToyService.domain.member.service.memberserviceimpl;

import core.backendstudyToyService.domain.member.entitiy.Member;
import core.backendstudyToyService.domain.member.service.MemberService;
import core.backendstudyToyService.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Member> findMemberById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    @Override
    public Optional<Member> findMemberByUsernameAndPassword(String username, String password){
        return memberRepository.findById(Long.valueOf(username));
    }

}
