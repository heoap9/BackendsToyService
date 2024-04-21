package core.backendstudyToyService.domain.member.controller;

import core.backendstudyToyService.domain.member.dto.loginDTO;
import core.backendstudyToyService.domain.member.entitiy.Member;
import core.backendstudyToyService.domain.member.repository.MemberRepository;
import core.backendstudyToyService.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class MemberController {

    private final MemberService memberService;
    private final HttpSession httpSession;

    @Autowired
    private MemberRepository memberRepository;

    // test를 위한 임시 홈화면 입니다
    @GetMapping("home")
    public String testhome(Member member, Model model) {
        model.addAttribute("username", member.getUsername());
        return "home";
    }

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
    @Transactional
    public String signUp(loginDTO dto) {

        try {
            memberService.saveMember(dto);
        } catch (Exception e) {

            System.out.println("회원가입 중 오류 발생: " + e.getMessage());
            return "redirect:/signup?error=회원가입 중 오류가 발생하였습니다";
        }
        return "redirect:/login"; // 회원가입 후 로그인 페이지로 리다이렉트
    }

    @RequestMapping("/signup/checkUsername")
    public boolean checkUsernameExists(HttpServletRequest req, HttpServletResponse res) {
        return memberService.isUsernameAvailable((String) req.getParameter("username"));

    }
}
