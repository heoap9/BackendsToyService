package core.backendstudyToyService.domain.member.repository;

import core.backendstudyToyService.domain.member.entitiy.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
}
