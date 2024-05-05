package core.backendstudyToyService.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
public class CommentDTO {
    private String authorName;
    private String content;
}
