package core.backendstudyToyService.domain.member.controller;

import core.backendstudyToyService.domain.member.dto.MemberDTO;
import core.backendstudyToyService.domain.member.entitiy.Member;
import core.backendstudyToyService.domain.member.repository.MemberRepository;
import core.backendstudyToyService.domain.member.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;



    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    //a
    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("member", new Member());
        return "signup";
    }

    @PostMapping("/signup")
    @Transactional
    public String signUp(MemberDTO dto) {

        try {
            memberService.saveMember(dto);
        } catch (Exception e) {

            System.out.println("회원가입 중 오류 발생: " + e.getMessage());
            return "redirect:/signup?error=회원가입 중 오류가 발생하였습니다";
        }
        return "redirect:/login"; // 회원가입 후 로그인 페이지로 리다이렉트
    }

    /* 중복 아이디 검증 로직 */
    @GetMapping("/signup/checkUsername/{username}")
    public ResponseEntity<String> checkUsernameExists(@PathVariable String username) {
        boolean isAvailable = memberService.isUsernameAvailable(username);
        if(!isAvailable) {
            return ResponseEntity.ok("사용 가능한 사용자명입니다.");
        } else {
            return ResponseEntity.ok("이미 사용 중인 사용자명입니다. 다른 이름을 시도해주세요.");
        }
    }


    /* 회원 탈퇴
    * 로그인되어 있는 객체를 가져와 사용자 이름을 DB에서 확인하고 삭제*/
    @PostMapping("/delete")
    public String deleteMember(Principal principal) {
        try {
            String username = principal.getName();
            if (!username.isEmpty()) {
                memberService.deleteMember(username);
            }
            return "redirect:/login";

        } catch (Exception e) {
            throw new NullPointerException("탈퇴에 실패하였습니다.");
        }
    }
}
