package com.example.admin.controller;

import com.example.admin.domain.dto.member.MemberInfo;
import com.example.admin.domain.dto.member.SignInDto;
import com.example.admin.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<MemberInfo> signin(@RequestBody @Valid SignInDto signInDto) {
        return ResponseEntity.ok(memberService.signIn(signInDto));
    }
}
