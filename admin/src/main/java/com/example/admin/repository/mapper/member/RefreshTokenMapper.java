package com.example.admin.repository.mapper.member;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface RefreshTokenMapper {
    void insertRefreshToken(Map<String, String> map);
    String selectRefreshTokenByUsername(String username);
    void deleteRefreshTokenById(String username);
}
