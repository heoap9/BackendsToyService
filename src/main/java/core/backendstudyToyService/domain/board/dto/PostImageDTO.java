package core.backendstudyToyService.domain.board.dto;

import core.backendstudyToyService.domain.board.entity.PostImage;
import lombok.*;

@NoArgsConstructor
@Data
@ToString
public class PostImageDTO {

    private String filePath;

    private String fileName;

    private String fileUuid;

    private String fileType;

    private Long fileSize;

    private Long postId;

    public PostImage toEntity() {
        return PostImage.builder()
                .filePath(filePath)
                .fileName(fileName)
                .fileUuid(fileUuid)
                .fileType(fileType)
                .fileSize(fileSize)
                .build();
    }

}
