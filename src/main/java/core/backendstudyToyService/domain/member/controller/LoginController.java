package core.backendstudyToyService.domain.member.controller;

import core.backendstudyToyService.domain.member.dto.MemberDTO;
import core.backendstudyToyService.domain.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class LoginController {

    private final MemberService memberService;

    public LoginController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping("/login")
    public String showLoginForm() {
        return "/login";
    }

    @PostMapping("/login/login-proc")
    public String login(MemberDTO dto) {
        System.out.println(dto.getUsername());

        return "/posts";// postContorller 참조
    }
}
