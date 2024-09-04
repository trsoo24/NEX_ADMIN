package com.example.admin.service.member;

import com.example.admin.domain.dto.member.*;
import com.example.admin.domain.entity.member.Member;
import com.example.admin.domain.entity.member.enums.Role;
import com.example.admin.exception.MemberException;
import com.example.admin.repository.mapper.member.MemberMapper;
import com.example.admin.service.FunctionUtil;
import com.example.admin.service.reference.MemberReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.admin.exception.enums.MemberErrorCode.UNACCEPTABLE_ROLE;
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;
    private final MemberReference memberReference;
    private final FunctionUtil functionUtil;

    public void signUp(SignUpDto signUpDto) {
        memberReference.isExistMemberName(signUpDto.getUsername());
        memberReference.isExistMemberEmail(signUpDto.getEmail());

        Map<String, Object> signUpMap = checkService(signUpDto.getServices());
        signUpMap.put("role", fromString(signUpDto.getRole()).getType());
        signUpMap.put("team", signUpDto.getTeam());
        signUpMap.put("username", signUpDto.getUsername());
        signUpMap.put("password", signUpDto.getPassword());
        signUpMap.put("email", signUpDto.getEmail());
        signUpMap.put("name", signUpDto.getName());

        memberMapper.createMember(signUpMap);
    }

    public MemberInfo signIn(SignInDto signInDto) {
        Map<String, String> signInMap = new HashMap<>();
        signInMap.put("username", signInDto.getUsername());
        signInMap.put("password", signInDto.getPassword());

        Member member = memberMapper.signIn(signInMap);
        updateLastConnectTime(member.getMemberId());

        MemberInfo memberInfo = new MemberInfo();
        memberInfo.toMemberInfo(member);

        return memberInfo;
    }

    private void updateLastConnectTime(Integer memberId) {
        //TODO 마지막 접속 시간 최신화
        memberMapper.updateLastConnectedTime(memberId);
    }

    public void updateMemberInfo(UpdateMemberRequestDto updateMemberRequestDto) {
        //유효한 사용자인지 체크
        memberReference.findMember(updateMemberRequestDto.getMemberId());

        Map<String, Object> requestMap = checkService(updateMemberRequestDto.getServices());
        requestMap.put("password", updateMemberRequestDto.getPassword());
        requestMap.put("email", updateMemberRequestDto.getEmail());
        requestMap.put("memberId", updateMemberRequestDto.getMemberId());
        memberMapper.updateMemberInfo(requestMap);
    }

    public void deleteMember(DeleteMemberDto ids) {
        //TODO 접속한 유저가 요청한 Request 에서 memberId 와 RequestParam 의 Id 가 일치한지 확인할 것
        for (Integer id : ids.getIds()) {
            memberMapper.deleteMember(id);
        }
    }

    public Page<MemberInfo> findAllMember(Integer page, Integer pageSize) {
        List<Member> memberList = memberMapper.findAllMember();
        List<MemberInfo> memberInfoList = new ArrayList<>();

        for (Member member : memberList) {
            MemberInfo memberInfo = new MemberInfo();
            memberInfoList.add(memberInfo.toMemberInfo(member));
        }

        return functionUtil.toPage(memberInfoList, page, pageSize);
    }

    public MemberInfo findMemberByMemberName(String username) {
        MemberInfo memberInfo = new MemberInfo();

        return memberInfo.toMemberInfo(memberMapper.findMemberByUsername(username));
    }

    private Map<String, Object> checkService(List<String> services) {
        Map<String, Object> map = new HashMap<>();
        for (String service : services) {
            switch (service) {
                case "ADCB":
                    map.put("adcb",(Integer) map.getOrDefault("adcb", 0) + 1);
                    break;
                case "GDCB":
                    map.put("gdcb", (Integer) map.getOrDefault("gdcb", 0) + 1);
                    break;
                case "MDCB":
                    map.put("mdcb", (Integer) map.getOrDefault("mdcb", 0) + 1);
                    break;
                case "MSDCB":
                    map.put("msdcb", (Integer) map.getOrDefault("msdcb", 0) + 1);
                    break;
                case "NDCB":
                    map.put("ndcb", (Integer) map.getOrDefault("ndcb", 0) + 1);
                    break;
                case "PDCB":
                    map.put("pdcb", (Integer) map.getOrDefault("pdcb", 0) + 1);
                    break;
                case "SDCB":
                    map.put("sdcb", (Integer) map.getOrDefault("sdcb", 0) + 1);
                    break;
            }
        }
        return map;
    }

    private Role fromString(String requestRole) {
        for (Role role : Role.values()) {
            if (role.getType().equalsIgnoreCase(requestRole)) {
                return role;
            }
        }
        throw new MemberException(UNACCEPTABLE_ROLE);
    }
}
