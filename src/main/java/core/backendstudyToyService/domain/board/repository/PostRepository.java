package core.backendstudyToyService.domain.board.repository;

import core.backendstudyToyService.domain.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 특정 회원이 작성한 게시글 조회
    List<Post> findByMemberId(Long memberId);
}
