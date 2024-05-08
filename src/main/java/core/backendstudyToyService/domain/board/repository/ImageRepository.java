package core.backendstudyToyService.domain.board.repository;

import core.backendstudyToyService.domain.board.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<PostImage, Long> {
    @Query("SELECT pi FROM PostImage pi WHERE pi.filePath = :imagePath")
    PostImage findByImagePath(String imagePath);
}
