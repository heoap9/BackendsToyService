package core.backendstudyToyService.domain.member.controller;

import core.backendstudyToyService.domain.member.dto.MemberDTO;
import core.backendstudyToyService.domain.member.entitiy.Member;
import core.backendstudyToyService.domain.member.service.MemberService;
import core.backendstudyToyService.domain.member.service.memberserviceimpl.MemberServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.Collections;


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
        return "/posts";
    }


}
