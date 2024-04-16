package core.backendstudyToyService.domain.member.controller;

import core.backendstudyToyService.domain.member.entitiy.Member;
import core.backendstudyToyService.domain.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MemberController {

    private final MemberService memberService;
    private final HttpSession httpSession;


    @Autowired
    public MemberController(MemberService memberService, HttpSession httpSession) {
        this.memberService = memberService;
        this.httpSession = httpSession;

    }

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("member", new Member());
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(Member member, BindingResult bindingResult) {

        try {
            memberService.saveMember(member);
        } catch (Exception e) {

            System.out.println("회원가입 중 오류 발생: " + e.getMessage());
            return "redirect:/signup?error=회원가입 중 오류가 발생하였습니다";
        }

        return "redirect:/login"; // 회원가입 후 로그인 페이지로 리다이렉트
    }


    @GetMapping("/login")
    public String showLoginForm() {
        return "/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam Member member) {
        //로그인 후 메인페이지로 진입하는 페이지를 만들어주어야 합니다
        Member EnterMember = memberService.login(member);

        if (EnterMember == null) {
            System.out.println("false");
        }
        return "/home";
    }

    @GetMapping("/checkUsername")
    public boolean checkUsername(@RequestParam String username) {
        return !memberService.isUsernameAvailable(username);
    }

}
