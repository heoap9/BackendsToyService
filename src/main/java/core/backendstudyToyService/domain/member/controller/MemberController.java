package core.backendstudyToyService.domain.member.controller;

import core.backendstudyToyService.domain.member.entitiy.Member;
import core.backendstudyToyService.domain.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("member", new Member());
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(Member member) {
        memberService.saveMember(member);
        return "redirect:/login"; // 회원가입 후 로그인 페이지로 리다이렉트
    }
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(String username, String password, Model model) {
        //로그인 후 메인페이지로 진입하는 페이지를 만들어주어야 합니다
        return "/login";
    }


}
