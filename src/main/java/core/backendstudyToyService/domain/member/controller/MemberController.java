package core.backendstudyToyService.domain.member.controller;

import core.backendstudyToyService.domain.member.entitiy.Member;
import core.backendstudyToyService.domain.member.repository.MemberRepository;
import core.backendstudyToyService.domain.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MemberController {

    private final MemberService memberService;
    private final HttpSession httpSession;

    @Autowired
    private MemberRepository memberRepository;


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
    public String signUp(Member member) {

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
        return "/home";
    }

    @PostMapping("/checkUsername")
    public boolean checkUsernameExists(@RequestBody String username) {
        if (username == null || username.trim().isEmpty()) {
            // 유저네임이 null이거나 공백인 경우
            return false; // 실패
        }

        return memberService.isUsernameAvailable(username);
    }

    @GetMapping("home")
    public String testhome(Member member, Model model){
        model.addAttribute("username", member.getUsername());
        return "home";
    }
}
