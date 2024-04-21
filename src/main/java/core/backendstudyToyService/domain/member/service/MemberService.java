package core.backendstudyToyService.domain.member.service;


import core.backendstudyToyService.domain.member.dto.loginDTO;
import core.backendstudyToyService.domain.member.entitiy.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    void saveMember(loginDTO loginDTO);

    Optional<Member> findMemberById(Long memberId);
    List<Member> findAllMembers();
    void deleteMember(Long memberId);
    loginDTO login(loginDTO loginDTO);

    boolean isUsernameAvailable(String username);
}
