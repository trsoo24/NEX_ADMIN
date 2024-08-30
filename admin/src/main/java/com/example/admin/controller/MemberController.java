package com.example.admin.controller;

import com.example.admin.domain.dto.member.MemberInfo;
import com.example.admin.domain.dto.member.SignInDto;
import com.example.admin.domain.dto.member.SignUpDto;
import com.example.admin.domain.dto.member.UpdateMemberRequestDto;
import com.example.admin.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/info")
    public ResponseEntity<MemberInfo> getMemberInfo(@RequestParam("memberId") @Valid Integer memberId) {
        return ResponseEntity.ok(memberService.getMemberInfo(memberId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MemberInfo>> getAllMemberInfo() {
        return ResponseEntity.ok(memberService.findAllMember());
    }

    @PutMapping("/modify")
    public void updateMemberInfo(@RequestBody @Valid UpdateMemberRequestDto updateMemberRequestDto) {
        memberService.updateMemberInfo(updateMemberRequestDto);
    }

    @DeleteMapping("/drop")
    public void deleteMember(@RequestParam("memberId") @Valid Integer memberId) {
        memberService.deleteMember(memberId);
    }
}
