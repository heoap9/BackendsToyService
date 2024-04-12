package core.backendstudyToyService.domain.board.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostController {
// 하이
    @GetMapping("/postlist")
    public String showpostlist(){
        return "post";
    }
}
