package core.backendstudyToyService.domain.board.repository;

import core.backendstudyToyService.domain.board.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
//    // 특정 게시글에 달린 댓글 조회
//    List<Reply> findByPostId(Long postId);
//
//    // 특정 회원이 작성한 댓글 조회
//    List<Reply> findByMemberId(Long memberId);
}
