package com.example.admin.service.reference;


import com.example.admin.domain.dto.member.MemberInfo;
import com.example.admin.exception.MemberException;
import com.example.admin.repository.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.admin.exception.enums.MemberErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberReference {
    private final MemberMapper memberMapper;

    public void isExistMemberName(String memberName) {
        if (memberMapper.existsMemberName(memberName)) {
            throw new MemberException(DUPLICATED_NAME);
        }
    }

    public void isExistMemberEmail(String email) {
        if (memberMapper.existsEmail(email)) {
            throw new MemberException(DUPLICATED_EMAIL);
        }
    }

    public void isExistMemberCtn(String ctn) {
        if (memberMapper.existsCtn(ctn)) {
            throw new MemberException(DUPLICATED_CTN);
        }
    }

    public MemberInfo findMember(Integer memberId) {
        MemberInfo memberInfo = memberMapper.findMemberByMemberId(memberId);

        if (memberInfo == null) {
            throw new MemberException(MEMBER_NOT_FOUND);
        }

        return memberInfo;
    }
}
