package core.backendstudyToyService.domain.board.service.postserviceimpl;

import core.backendstudyToyService.domain.board.dto.CommentDTO;
import core.backendstudyToyService.domain.board.dto.PostDetailsDTO;
import core.backendstudyToyService.domain.board.entity.Post;
import core.backendstudyToyService.domain.board.exeption.EntityNotFoundException;
import core.backendstudyToyService.domain.board.repository.PostRepository;
import core.backendstudyToyService.domain.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    public PostDetailsDTO getPostDetails(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("찾는 게시글이 없습니다: " + postId +"번호"));

        String authorName = post.getMember().getUsername(); // 게시글 작성자의 이름
        int likeCount = post.getLikeCount(); // 좋아요 수
        List<CommentDTO> comments = post.getReplyList().stream()
                .map(reply -> new CommentDTO(reply.getMember().getUsername(), reply.getContent())) // 댓글작성자와 내용을 한번에 표시해서 반환함
                .collect(Collectors.toList());

        return new PostDetailsDTO(post.getId(), post.getTitle(), post.getContent(), authorName, likeCount, comments);
    }



}
