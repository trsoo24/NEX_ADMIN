package com.example.admin.repository.mapper.member;


import com.example.admin.domain.dto.member.MemberInfo;
import com.example.admin.domain.dto.member.SignUpDto;
import com.example.admin.domain.dto.member.UpdateMemberRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    boolean existsMemberName(String memberName);
    boolean existsCtn(String ctn);
    boolean existsEmail(String email);
    void createMember(SignUpDto signUpDto);
    void updateLastConnectedTime(Integer memberId);
    MemberInfo findMemberByMemberId(Integer memberId);
    void updateMemberInfo(UpdateMemberRequestDto updateMemberRequestDto);
    void deleteMember(Integer memberId);
    List<MemberInfo> findAllMember();
}
