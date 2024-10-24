package com.example.admin.auth.controller;

import com.example.admin.common.response.StatusResult;
import com.example.admin.member.dto.SignInDto;
import com.example.admin.member.service.MemberService;
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
    public StatusResult signin(@RequestBody @Valid SignInDto signInDto, HttpServletResponse response) {
        memberService.signIn(signInDto, response);

        return new StatusResult(true);
    }

    @PostMapping("/logout")
    public StatusResult logOut(HttpServletRequest request, HttpServletResponse response) {
        memberService.logOut(request, response);

        StatusResult statusResult = new StatusResult();
        statusResult.setSuccess(true);

        return new StatusResult(true);
    }
}
