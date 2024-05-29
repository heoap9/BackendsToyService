package core.backendstudyToyService.domain.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostDetailsDTO {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private int likeCount;
    private List<String> imageUrls;
    private List<CommentDTO> comments;

    // 기존 생성자 및 getter, setter 메소드

    public PostDetailsDTO(Long id, String title, String content, String authorName, int likeCount, List<String> imageUrls, List<CommentDTO> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.likeCount = likeCount;
        this.imageUrls = imageUrls;
        this.comments = comments;
    }
}
