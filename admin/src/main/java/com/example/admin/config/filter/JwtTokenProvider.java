package com.example.admin.config.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.admin.exception.MemberException;
import com.example.admin.repository.mapper.member.MemberMapper;
import com.example.admin.repository.mapper.member.RefreshTokenMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.admin.exception.enums.MemberErrorCode.INVALID_TOKEN;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${access.token}")
    private String ACCESS_TOKEN;
    @Value("${refresh.token}")
    private String REFRESH_TOKEN;
    @Value("${username}")
    private String USERNAME;
    @Value("${role}")
    private String ROLE;
    @Value("${access.time}")
    private Long ACCESS_TOKEN_EXPIRED_TIME;
    @Value("${refresh.time}")
    private Long REFRESH_TOKEN_EXPIRED_TIME;
    private final CookieUtil cookieUtil;
    private final RefreshTokenMapper refreshTokenMapper;
    private final MemberMapper memberMapper;

    public String generateAccessToken(String username, String role, HttpServletResponse response) {
        Date date = new Date();

        String accessToken = JWT.create()
                .withSubject(ACCESS_TOKEN)
                .withExpiresAt(new Date(date.getTime() + ACCESS_TOKEN_EXPIRED_TIME))
                .withClaim(USERNAME, username)
                .withClaim(ROLE, role)
                .sign(Algorithm.HMAC256(secretKey));

        cookieUtil.setAccessTokenCookie(response, accessToken);

        return accessToken;
    }

    public void generateRefreshToken(String username, String role, HttpServletResponse response) {
        Date date = new Date();

        String refreshToken = JWT.create()
                .withSubject(REFRESH_TOKEN)
                .withExpiresAt(new Date(date.getTime() + REFRESH_TOKEN_EXPIRED_TIME))
                .withClaim(USERNAME, username)
                .withClaim(ROLE, role)
                .sign(Algorithm.HMAC256(secretKey));

        Map<String, String> mapperMap = new HashMap<>();
        mapperMap.put("username", username);
        mapperMap.put("refreshToken", refreshToken);
        refreshTokenMapper.insertRefreshToken(mapperMap);

        cookieUtil.setRefreshTokenCookie(response, refreshToken);
    }

    public String getUsernameByToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secretKey))
                    .build()
                    .verify(token)
                    .getClaim(USERNAME)
                    .asString();
        } catch (Exception e) {
            throw new MemberException(INVALID_TOKEN);
        }
    }

    public String getRoleByToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secretKey))
                    .build()
                    .verify(token)
                    .getClaim(ROLE)
                    .asString();
        } catch (Exception e) {
            throw new MemberException(INVALID_TOKEN);
        }
    }

    public String getAccessTokenFromRequest(HttpServletRequest request) { // Request Header 토큰 추출
        String accessToken = cookieUtil.getAccessToken(request).getValue();

        if (accessToken == null) {
            throw new MemberException(INVALID_TOKEN);
        }
        return accessToken;
    }

    public Authentication getAuthenticationByAccessToken(String access_token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(access_token).getBody();
        String username = claims.getSubject();
        String role = claims.get("roles").toString();

        if (memberMapper.existsUsername(username)) {
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
            return new UsernamePasswordAuthenticationToken(username, "", authorities);
        }
        return null;
    }

    public boolean isValidToken(String token) {
        try {
            return toDecodedJwtToken(token).getExpiresAt().after(new Date());
        } catch (TokenExpiredException e) {
            return false; // 토큰이 만료된 경우
        } catch (Exception e) {
            return false; // 다른 예외도 우선 false 처리
        }
    }

    public String RegenerateToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshTokenFromRequest(request);
        String username = getUsernameByToken(refreshToken);

        if (isValidToken(refreshToken) && checkValidUser(username, refreshToken)) {
            String role = getRoleByToken(refreshToken);
            String accessToken = generateAccessToken(username, role, response);

            if (isValidRefreshToken(refreshToken) && checkValidUser(username, refreshToken)) {
                // 요청 시에 Refresh Token 도 체크
                generateRefreshToken(username, role, response);
            }
            return accessToken;
        } else {
            return null;
        }
    }

    private DecodedJWT toDecodedJwtToken(String encodedToken) {
        return JWT.require(Algorithm.HMAC256(secretKey)).build().verify(encodedToken);
    }

    private String getRefreshTokenFromRequest(HttpServletRequest request) {
        String refreshToken = cookieUtil.getRefreshToken(request).getValue();

        if (refreshToken == null) {
            throw new MemberException(INVALID_TOKEN);
        }
        return refreshToken;
    }

    private boolean isValidRefreshToken(String refreshToken) {
        Date expirationTime = toDecodedJwtToken(refreshToken).getExpiresAt();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 30);
        Date after30Minute = calendar.getTime();

        return expirationTime != null && expirationTime.before(after30Minute);
    }

    private boolean checkValidUser(String username, String refreshToken) {
        String tokenInDB = refreshTokenMapper.selectRefreshTokenByUsername(username);

        return tokenInDB != null && tokenInDB.equals(refreshToken);
    }

    public void logOut(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = getAccessTokenFromRequest(request);
        String refreshToken = getRefreshTokenFromRequest(request);
        String username = getUsernameByToken(accessToken);

        if (username.equals(getUsernameByToken(refreshToken)) && checkValidUser(username, refreshToken)) {
            refreshTokenMapper.deleteRefreshTokenById(username);
            cookieUtil.deleteCookie(response);
        }
    }
}
