package core.backendstudyToyService.domain.board.repository;

import core.backendstudyToyService.domain.board.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    // 특정 게시글에 대한 좋아요 조회
    List<Like> findByPostId(Long postId);

    // 특정 회원이 누른 좋아요 조회
    List<Like> findByMemberId(Long memberId);

}
