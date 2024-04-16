package core.backendstudyToyService.domain.board.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PostDTO {

    private Long id;
    private String title;
    private String content;
    private String imagePath;

}
