package com.example.admin.controller;

import com.example.admin.domain.dto.member.SignInDto;
import com.example.admin.service.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {
    private final MemberService memberService;

    @PostMapping("/login")
    public void signin(@RequestBody @Valid SignInDto signInDto, HttpServletResponse response) {
        memberService.signIn(signInDto, response);
    }

    @PostMapping("/logout")
    public void logOut(HttpServletRequest request, HttpServletResponse response) {
        memberService.logOut(request, response);
    }
}
