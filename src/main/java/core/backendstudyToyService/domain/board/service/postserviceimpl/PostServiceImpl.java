package core.backendstudyToyService.domain.board.service.postserviceimpl;

import core.backendstudyToyService.domain.board.dto.CommentDTO;
import core.backendstudyToyService.domain.board.dto.PostDTO;
import core.backendstudyToyService.domain.board.dto.PostDetailsDTO;
import core.backendstudyToyService.domain.board.entity.Like;
import core.backendstudyToyService.domain.board.entity.Post;
import core.backendstudyToyService.domain.board.entity.PostImage;
import core.backendstudyToyService.domain.board.entity.Reply;
import core.backendstudyToyService.domain.board.exeption.EntityNotFoundException;
import core.backendstudyToyService.domain.board.repository.ImageRepository;
import core.backendstudyToyService.domain.board.repository.LikeRepository;
import core.backendstudyToyService.domain.board.repository.PostRepository;
import core.backendstudyToyService.domain.board.repository.ReplyRepository;
import core.backendstudyToyService.domain.board.service.PostService;
import core.backendstudyToyService.domain.member.entitiy.Member;
import core.backendstudyToyService.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
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
    public List<Post> findAllPosts() { // 페이징을 기능구현 보류
        return postRepository.findAll();
    }

    @Override
    public PostDetailsDTO getPostDetails(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("찾는 게시글이 없습니다: " + postId + "번호"));

        String authorName = post.getMember().getUsername(); // 게시글 작성자의 이름
        int likeCount = post.getLikeCount(); // 좋아요 수
        List<CommentDTO> comments = post.getReplyList().stream()
                .map(reply -> new CommentDTO(reply.getMember().getUsername(), reply.getContent())) // 댓글작성자와 내용을 한번에 표시해서 반환함
                .collect(Collectors.toList());

        return new PostDetailsDTO(post.getId(), post.getTitle(), post.getContent(), authorName, likeCount, comments);
    }

    /**
     * 게시글좋아요를 반영할때 유저의 정보를 같이 넣어 등록합니다
     *
     * @param postId 게시글 번호입니다
     * @param userId 좋아요를 등록할 유저입니다
     */
    @Override
    public void addLikePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("찾는 게시글이 없습니다" + postId + "번호"));
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("찾는 회원이 없습니다" + userId));
        Like like = new Like();
        like.setPost(post);
        like.setMember(member);
        likeRepository.save(like);
    }

    /**
     * 게시글에 댓글을 등록합니다 유저의 정보를 같이 넣어 등록하며
     * 댓글 내용을 포함하여 등록합니다
     *
     * @param postId  게시글번호입니다
     * @param userId  유저 정보입니다
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

    private String imagePath = "";

    @Override
    public void insertPost(@AuthenticationPrincipal UserDetails userDetails, PostDTO postDTO, List<MultipartFile> images) {
        // 멤버 아이디
        String memberId = userDetails.getUsername();
        System.out.println("[PostServiceImpl] 로그인된 멤버 아이디 확인: " + memberId);

        // 게시글 텍스트 설정 및 저장
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setUploadDate(LocalDateTime.now());

        // 게시글 저장(텍스트, 업로드 날짜 등 추가정보 포함)
        Post savedPost = postRepository.save(post);

        // 이미지 저장
        if (images != null && !images.isEmpty()) {
            int maxImages = Math.min(images.size(), 3); // 이미지 최대 3개
            for (int i = 0; i < maxImages; i++) {
                MultipartFile image = images.get(i);
                // 파일 경로 생성 및 저장
                imagePath = createImagePath(image);
                System.out.println("[PostServiceImpl]이미지 저장경로 확인: " + imagePath);
                // 이미지와 게시글 연결
                saveImageToPost(savedPost, imagePath);
            }
        }
    }


    private String createImagePath(MultipartFile image) {
        try {
            if (image.isEmpty()) {
                throw new IllegalArgumentException("파일이 없습니다.");
            }

            if (!isImageFile(image)) {
                throw new IllegalArgumentException("이미지 파일이 아닙니다.");
            }

            // 프로젝트 폴더 내 images폴더에 이미지 저장
            String uploadDirectory = "src/main/resources/images";
            Path uploadPath = Paths.get(imagePath);

            // 경로 없으면 생성
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // UUID 생성
            String uuid = UUID.randomUUID().toString();

            // 저장할 파일명 생성
            String imageName = uuid + "_" + image.getOriginalFilename();

            // 저장할 파일 경로 설정
            Path path = uploadPath.resolve(imageName);

            // 파일 저장
            Files.copy(image.getInputStream(), path);

            return "images/" + imageName;
        } catch (IOException e) {
            // 예외처리
            logger.error("Failed to save image: " + e.getMessage(), e);

            return null;
        }
    }

    private boolean isImageFile(MultipartFile image) {
        String contentType = image.getContentType();
        return contentType != null && contentType.startsWith("image");
    }


    //이미지와 게시글 연결
    private void saveImageToPost(Post post, String imagePath) {

        PostImage postImage = imageRepository.findByImagePath(imagePath);

        postImage.setPost(post);

        // 이미지 저장
        imageRepository.save(postImage);
    }
}
