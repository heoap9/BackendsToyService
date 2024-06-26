package core.backendstudyToyService.domain.board.service;

import core.backendstudyToyService.domain.board.dto.PostDTO;
import core.backendstudyToyService.domain.board.dto.PostDetailsDTO;
import core.backendstudyToyService.domain.board.entity.Post;
import core.backendstudyToyService.domain.member.entitiy.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PostService {
    public PostDetailsDTO getPostDetails(Long postId);

    List<Post> findAllPosts();

    void addLikePost(Long postId, Long userId);

    void addCommentPost(Long postId, Long userId, String content);

    void insertPost(CustomUserDetails userDetails, PostDTO postDTO, List<MultipartFile> images);
}