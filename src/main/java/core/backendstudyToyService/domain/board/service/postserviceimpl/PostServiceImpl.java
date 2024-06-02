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
import core.backendstudyToyService.domain.member.entitiy.CustomUserDetails;
import core.backendstudyToyService.domain.member.entitiy.Member;
import core.backendstudyToyService.domain.member.repository.MemberRepository;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
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
    private final MinioClient minioClient;

    /**
     * 버킷 이름 주입
     * */
    @Value("${minio.bucket-name}")
    private String minioBucketName;

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

        String authorName = post.getMember().getUsername();
        int likeCount = post.getLikeCount();
        List<CommentDTO> comments = post.getReplyList().stream()
                .map(reply -> new CommentDTO(reply.getMember().getUsername(), reply.getContent()))
                .collect(Collectors.toList());
        List<String> imageUrls = post.getImages().stream()
                .map(PostImage::getFilePath)
                .collect(Collectors.toList());

        return new PostDetailsDTO(post.getId(), post.getTitle(), post.getContent(), authorName, likeCount, imageUrls, comments);
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
                .orElseThrow(() -> new EntityNotFoundException("찾는 게시글이 없습니다 " + postId + " 번호"));
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("찾는 회원이 없습니다 " + userId));

        Optional<Like> existingLike = likeRepository.findByPostIdAndMemberId(postId, userId);
        if (existingLike.isPresent()) {
            // 이미 좋아요가 등록되어 있음
            return;
        }

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

    @Override
    public void insertPost(CustomUserDetails userDetails, PostDTO postDTO, List<MultipartFile> images) {

        // 로그인된 멤버의 ID로 Member 객체 조회
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 게시글 설정
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setMember(member);
        post.setContent(postDTO.getContent());
        post.setUploadDate(LocalDateTime.now());
        Post savedPost = postRepository.save(post);

        // 이미지가 있을 때만 처리
        if (!images.isEmpty()) {
            System.out.println("[PostServiceImpl-insertPost]이미지 있을때 조건문 통과");
            for(MultipartFile image: images){
                String imageUrl = uploadImageToMinio(image); // 이미지 S3에 업로드
                if (imageUrl != null) { // 이미지 업로드 성공한 경우에만 이미지와 게시글 연결
                    saveImageToPost(savedPost, imageUrl);
                }
            }
        }
    }



    private String uploadImageToMinio(MultipartFile image) {
        try {
            if (image.isEmpty()) {
                throw new IllegalArgumentException("파일이 없습니다.");
            }

            if (!isImageFile(image)) {
                throw new IllegalArgumentException("이미지 파일이 아닙니다.");
            }

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioBucketName)
                            .object(image.getOriginalFilename())
                            .stream(image.getInputStream(), image.getSize(), -1)
                            .build()
            );

            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioBucketName)
                            .object(image.getOriginalFilename())
                            .expiry(1, TimeUnit.HOURS)  // url 만료시간 설정
                            .build()
            );

        } catch (MinioException | IOException e) {
            logger.error("Failed to save image: " + e.getMessage(), e);
            return null;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isImageFile(MultipartFile image) {
        String contentType = image.getContentType();
        return contentType != null && contentType.startsWith("image");
    }


    //이미지와 게시글 연결
    private void saveImageToPost(Post post, String imagePath) {

        PostImage postImage = imageRepository.findByImagePath(imagePath);
        if (postImage == null) {
            // postImage가 null일 때의 처리 로직
            // 예: 새 PostImage 객체를 생성하거나, 에러 메시지를 로깅
            postImage = new PostImage();
            postImage.setImagePath(imagePath);
            // 필요하다면 다른 초기화 코드
        }
        postImage.setPost(post);
        imageRepository.save(postImage);
    }
}
