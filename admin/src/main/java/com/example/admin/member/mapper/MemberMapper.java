package com.example.admin.member.mapper;


import com.example.admin.member.dto.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper {
    boolean existsUsername(String username);
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
