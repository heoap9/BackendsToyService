package core.backendstudyToyService.domain.board.dto;

import core.backendstudyToyService.domain.board.entity.PostImage;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDTO {

    private Long id;
    private String title;
    private String content;
    private java.sql.Date uploadDate;
    private java.sql.Date modifiedDate;
    private char isDeleted;
    private List<PostImage> images;

}
