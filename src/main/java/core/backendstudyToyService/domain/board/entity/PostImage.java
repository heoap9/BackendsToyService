package core.backendstudyToyService.domain.board.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Entity
@Table(name = "post_files")
@Getter
@Setter
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="file_path")
    private String filePath;

    @Column(name="file_name")
    private String fileName;

    @Column(name="uuid")
    private String uuid;

    @Column(name="file_type")
    private String fileType;

    @Column(name="file_size")
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public PostImage(Long id, String filePath, String fileName, String uuid, String fileType, Long fileSize, Post post) {
        this.id = id;
        this.filePath = filePath;
        this.fileName = fileName;
        this.uuid = uuid;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.post = post;

    }

    public void setImagePath(String imagePath) {
        this.filePath = imagePath;
    }

    public String getImagePath(String dbStoredPath) {
        if (dbStoredPath == null || dbStoredPath.isEmpty()) {
            throw new IllegalArgumentException("데이터베이스에 저장된 이미지 경로가 유효하지 않습니다.");
        }
        return "/images/" + dbStoredPath;  // 웹 루트에 맞는 절대 경로로 변환
    }

}
