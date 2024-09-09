package com.example.admin.controller;

import com.example.admin.common.response.DataResult;
import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
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
    public PageResult<MemberInfo> getAllMemberInfo(@RequestParam("page") @Valid Integer page,
                                                   @RequestParam("pageSize") @Valid Integer pageSize) {
        Page<MemberInfo> memberInfoPage = memberService.findAllMember(page, pageSize);

        return new PageResult<>(true, memberInfoPage);
    }

    @GetMapping("/authentication")
    public StatusResult authenticationMember(HttpServletRequest request, @RequestParam("password") @Valid String password) {
        memberService.checkPassword(request, password);

        return new StatusResult(true);
    }

    @GetMapping("/info")
    public DataResult<MemberInfo> getMemberInfo(HttpServletRequest request) {
        MemberInfo memberInfo = memberService.findMemberByRequest(request);

        return new DataResult<>(true, memberInfo);
    }

    @PutMapping
    public StatusResult updateMemberInfo(HttpServletRequest request, @RequestBody @Valid UpdateMemberRequestDto updateMemberRequestDto) {
        memberService.updateMemberInfo(request, updateMemberRequestDto);

        return new StatusResult(true);
    }

    @DeleteMapping
    public StatusResult deleteMember(HttpServletRequest request, @RequestBody @Valid DeleteMemberDto ids) {
        memberService.deleteMember(request, ids);

        return new StatusResult(true);
    }

    @PostMapping
    public StatusResult signup(@RequestBody @Valid SignUpDto signUpDto) {
        memberService.generateMember(signUpDto);

        return new StatusResult(true);
    }
}
