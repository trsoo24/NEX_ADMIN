package com.example.admin.repository.mapper.member;


import com.example.admin.domain.dto.member.SignInDto;
import com.example.admin.domain.dto.member.UpdateMemberRequestDto;
import com.example.admin.domain.entity.member.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper {
    boolean existsMemberName(String memberName);
    boolean existsEmail(String email);
    void createMember(Map<String, Object> map);
    void updateLastConnectedTime(Integer memberId);
    Member findMemberByMemberId(Integer memberId);
    void updateMemberInfo(Map<String, Object> map);
    void deleteMember(Integer memberId);
    List<Member> findAllMember();
    Member findMemberByUsername(String username);
    Member signIn(Map<String, String> map);
}
