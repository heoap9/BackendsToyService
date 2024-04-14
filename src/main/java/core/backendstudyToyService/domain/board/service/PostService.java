package core.backendstudyToyService.domain.board.service;

import core.backendstudyToyService.domain.board.dto.PostDetailsDTO;
import core.backendstudyToyService.domain.board.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    public PostDetailsDTO getPostDetails(Long postId);
}
