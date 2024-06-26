package core.backendstudyToyService.domain.board.dto;

import core.backendstudyToyService.domain.board.entity.PostImage;
import core.backendstudyToyService.domain.member.entitiy.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDTO {

    private Long id;
    private String memberId;
    private String title;
    private String content;
    private LocalDateTime uploadDate;
    private LocalDateTime modifiedDate;
    private char isDeleted;
    private List<MultipartFile> images = new ArrayList<>();

}
