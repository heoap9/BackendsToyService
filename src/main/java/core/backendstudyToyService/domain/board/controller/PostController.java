package core.backendstudyToyService.domain.board.controller;


import core.backendstudyToyService.domain.board.dto.PostDetailsDTO;
import core.backendstudyToyService.domain.board.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor//현재 구현체가 하나이므로 생성자 주입을 사용했습니다
//별도의 서비스 구현체가 있다면 바꿔주세요
public class PostController {
//메롱
    private PostService postService;

    @GetMapping("/posts/{postId}")
    public String getPostDetails(@PathVariable Long postId, Model model){
        try {
            PostDetailsDTO postDetailsDTO = postService.getPostDetails(postId);
            model.addAttribute("postDetails",postDetailsDTO);
            return "post-details";
        }catch (RuntimeException e){
            model.addAttribute("에러!",e.getMessage());
            return "error";
        }
    }
}
