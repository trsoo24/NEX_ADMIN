package com.example.admin.service.member;

import com.example.admin.config.filter.JwtTokenProvider;
import com.example.admin.domain.dto.member.MemberInfo;
import com.example.admin.domain.dto.member.SignInDto;
import com.example.admin.domain.dto.member.SignUpDto;
import com.example.admin.domain.dto.member.UpdateMemberRequestDto;
import com.example.admin.domain.entity.member.Member;
import com.example.admin.repository.mapper.member.MemberMapper;
import com.example.admin.service.reference.MemberReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;
    private final MemberReference memberReference;
    private final JwtTokenProvider jwtTokenProvider;

    public void signUp(SignUpDto signUpDto) {
        memberReference.isExistMemberName(signUpDto.getMemberName());
        memberReference.isExistMemberCtn(signUpDto.getCtn());
        memberReference.isExistMemberEmail(signUpDto.getEmail());

        memberMapper.createMember(signUpDto);
    }

    public MemberInfo signIn(SignInDto signInDto) {
        MemberInfo memberInfo = memberMapper.signIn(signInDto);
        updateLastConnectTime(memberInfo.getMemberId());

        return memberInfo;
    }

    public void updateLastConnectTime(Integer memberId) {
        //TODO 마지막 접속 시간 최신화
        memberMapper.updateLastConnectedTime(memberId);
    }

    public MemberInfo getMemberInfo(Integer memberId) {
        return memberReference.findMember(memberId);
    }

    public void updateMemberInfo(UpdateMemberRequestDto updateMemberRequestDto) {
        //유효한 사용자인지 체크
        memberReference.findMember(updateMemberRequestDto.getMemberId());

        memberMapper.updateMemberInfo(updateMemberRequestDto);
    }

    public void deleteMember(Integer memberId) {
        //TODO 접속한 유저가 요청한 Request 에서 memberId 와 RequestParam 의 Id 가 일치한지 확인할 것
        memberMapper.deleteMember(memberId);
    }

    public List<MemberInfo> findAllMember() {
        return memberMapper.findAllMember();
    }

    public Member findMemberByMemberName(String memberName) {
        return memberMapper.findMemberByMemberName(memberName);
    }
}
