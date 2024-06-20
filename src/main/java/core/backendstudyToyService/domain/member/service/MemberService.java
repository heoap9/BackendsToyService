package core.backendstudyToyService.domain.member.service;


import core.backendstudyToyService.domain.member.dto.MemberDTO;
import core.backendstudyToyService.domain.member.entitiy.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    void saveMember(MemberDTO MemberDTO);

    Optional<Member> findMemberById(Long memberId);
    List<Member> findAllMembers();
    void deleteMember(String username);
    MemberDTO login(MemberDTO MemberDTO);

    boolean isUsernameAvailable(String username);
}
