package core.backendstudyToyService.domain.board.repository;

import core.backendstudyToyService.domain.board.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<PostImage, Long> {
}
