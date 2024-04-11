package core.backendstudyToyService.domain.member.service;


import core.backendstudyToyService.domain.member.entitiy.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Member saveMember(Member member);
    Optional<Member> findMemberById(Long memberId);
    List<Member> findAllMembers();
    void deleteMember(Long memberId);

    Optional<Member> findMemberByUsernameAndPassword(String username, String password);
}
