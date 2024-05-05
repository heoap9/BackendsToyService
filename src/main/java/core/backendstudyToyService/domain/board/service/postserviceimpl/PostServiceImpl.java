package core.backendstudyToyService.domain.board.service.postserviceimpl;

import core.backendstudyToyService.domain.board.dto.CommentDTO;
import core.backendstudyToyService.domain.board.dto.PostDTO;
import core.backendstudyToyService.domain.board.dto.PostDetailsDTO;
import core.backendstudyToyService.domain.board.entity.Like;
import core.backendstudyToyService.domain.board.entity.Post;
import core.backendstudyToyService.domain.board.entity.Reply;
import core.backendstudyToyService.domain.board.exeption.EntityNotFoundException;
import core.backendstudyToyService.domain.board.repository.LikeRepository;
import core.backendstudyToyService.domain.board.repository.PostRepository;
import core.backendstudyToyService.domain.board.repository.ReplyRepository;
import core.backendstudyToyService.domain.board.service.PostService;
import core.backendstudyToyService.domain.board.service.PostService2;
import core.backendstudyToyService.domain.member.entitiy.Member;
import core.backendstudyToyService.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final ReplyRepository replyRepository;

    /**
     * 게시글의 작성자,제목,글쓴내용을 모두 반영하여 반환합니다
     *
     * @param postId 게시글 번호를 받습니다
     * @return 게시글의 자세한 정보를 모두 반환합니다
     */

    @Override
    public List<Post> findAllPosts(){ // 페이징을 기능구현 보류
        return postRepository.findAll();
    }

    @Override
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

    /**
     * 게시글좋아요를 반영할때 유저의 정보를 같이 넣어 등록합니다
     * @param postId 게시글 번호입니다
     * @param userId 좋아요를 등록할 유저입니다
     */
    @Override
    public void addLikePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new EntityNotFoundException("찾는 게시글이 없습니다"+postId+"번호"));
        Member member = memberRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("찾는 회원이 없습니다"+userId));
        Like like = new Like();
        like.setPost(post);
        like.setMember(member);
        likeRepository.save(like);
    }

    /**
     * 게시글에 댓글을 등록합니다 유저의 정보를 같이 넣어 등록하며
     * 댓글 내용을 포함하여 등록합니다
     * @param postId 게시글번호입니다
     * @param userId 유저 정보입니다
     * @param content 댓글내용입니다
     */
    @Override
    public void addCommentPost(Long postId, Long userId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found for id: " + postId));
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for id: " + userId));
        Reply reply = new Reply();
        reply.setPost(post);
        reply.setMember(member);
        reply.setContent(content);
        replyRepository.save(reply);
    }

    @Override
    public void insertPost(PostDTO postDTO, List<MultipartFile> images) {

    }


}
