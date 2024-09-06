package com.example.admin.service.reference;


import com.example.admin.domain.dto.member.type.Role;
import com.example.admin.domain.entity.member.Member;
import com.example.admin.exception.MemberException;
import com.example.admin.repository.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.admin.exception.enums.MemberErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberReference {
    private final MemberMapper memberMapper;

    public void isExistUsername(String username) {
        if (memberMapper.existsUsername(username)) {
            throw new MemberException(DUPLICATED_NAME);
        }
    }

    public void isExistMemberEmail(String email) {
        if (memberMapper.existsEmail(email)) {
            throw new MemberException(DUPLICATED_EMAIL);
        }
    }

    public void findMember(Integer memberId) {
        if (memberMapper.findMemberByMemberId(memberId) == null) {
            throw new MemberException(MEMBER_NOT_FOUND);
        }
    }

    public boolean checkMemberRole(String requestMemberRole, String target) {
        if(requestMemberRole.equals(target)) {
            // 동일 권한은 불가능
            return false;
        }

        if (requestMemberRole.equals(Role.ROLE_ADMIN.getRole())) {
            // 요청 사용자가 SUPER_ADMIN 일 경우
            return true;
        } else if (requestMemberRole.equals(Role.ROLE_MANAGER.getRole()) && target.equals(Role.ROLE_USER.getRole())) {
            // 요청 사용자가 MANAGER 이고, Target 이 USER 일 경우
            return true;
        }
        return false;
    }
}
