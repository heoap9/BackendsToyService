package core.backendstudyToyService.domain.board.controller;


import core.backendstudyToyService.domain.board.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import core.backendstudyToyService.domain.common.ResponseDTO;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/postlist")
    public String showpostlist(){
        return "post";
    }

    @GetMapping("/writeForm")
    public String writeForm() {
        return "postdetails";
    }

    @PostMapping("/uploadPost")
    public ResponseEntity<ResponseDTO> uploadPost(@RequestParam("title") String title,
                                                  @RequestParam("content") String content,
                                                  @RequestParam(value = "image", required = false)MultipartFile image) {
        try {
            postService.uploadPost(title, content, image);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(), "게시물 업로드 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "게시물 업로드 실패: " + e.getMessage()));
        }
    }

}
