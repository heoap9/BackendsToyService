package core.backendstudyToyService.domain.board.service;

import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    void uploadPost(String title, String content, MultipartFile image);
}
