package core.backendstudyToyService.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * 게시글의 디테일한 정보를 저장하기 위한 계층으로 사용됩니다
 */
@Getter@Setter
@AllArgsConstructor
public class PostDetailsDTO {
    private Long postId;
    private String title;
    private String content;
    private String authorName;
    private int likeCount;
    private List<String> imageUrls;
    private List<CommentDTO> comments;
}
