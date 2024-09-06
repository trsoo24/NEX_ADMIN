package com.example.admin.controller;

import com.example.admin.domain.dto.member.DeleteMemberDto;
import com.example.admin.domain.dto.member.MemberInfo;
import com.example.admin.domain.dto.member.SignUpDto;
import com.example.admin.domain.dto.member.UpdateMemberRequestDto;
import com.example.admin.service.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public Page<MemberInfo> getAllMemberInfo(@RequestParam("page") @Valid Integer page,
                                             @RequestParam("pageSize") @Valid Integer pageSize) {
        return memberService.findAllMember(page, pageSize);
    }

    @GetMapping("/info")
    public MemberInfo getMemberInfo(HttpServletRequest request) {
        return memberService.findMemberByRequest(request);
    }

    @PutMapping("/update")
    public void updateMemberInfo(HttpServletRequest request, @RequestBody @Valid UpdateMemberRequestDto updateMemberRequestDto) {
        memberService.updateMemberInfo(request, updateMemberRequestDto);
    }

    @DeleteMapping
    public void deleteMember(HttpServletRequest request, @RequestBody @Valid DeleteMemberDto ids) {
        memberService.deleteMember(request, ids);
    }

    @PostMapping
    public void signup(@RequestBody @Valid SignUpDto signUpDto) {
        memberService.generateMember(signUpDto);
    }
}
