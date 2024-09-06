package com.example.admin.config.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.admin.domain.entity.member.Member;
import com.example.admin.exception.MemberException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.admin.exception.enums.MemberErrorCode.INVALID_TOKEN;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${access.token}")
    private String ACCESS_TOKEN;
    @Value("${authorize.key}")
    private String AUTHORITIES_KEY;
    @Value("${bearer.type}")
    private String BEARER_TYPE;
    @Value("${username}")
    private String USERNAME;
    @Value("${role}")
    private String ROLE;
    @Value("${access.time}")
    private Long ACCESS_TOKEN_EXPIRED_TIME;
    private final CookieUtil cookieUtil;

    public String generateAccessToken(Member member) { // accessToken 생성
        Date date = new Date();

        return JWT.create()
                .withSubject(ACCESS_TOKEN)
                .withExpiresAt(new Date(date.getTime() + ACCESS_TOKEN_EXPIRED_TIME))
                .withClaim(USERNAME, member.getUsername())
                .withClaim(ROLE, member.getRole().getType())
                .sign(Algorithm.HMAC256(secretKey));
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

    public Optional<String> getAccessTokenFromRequest(HttpServletRequest request) { // Request Header 토큰 추출
        return Optional.ofNullable(request.getHeader(AUTHORITIES_KEY))
                .filter(accessToken -> accessToken.startsWith(BEARER_TYPE))
                .map(accessToken -> accessToken.replace(BEARER_TYPE, ""));
    }

    public Authentication getAuthenticationByAccessToken(String access_token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(access_token).getBody();
        String username = claims.getSubject();
        List<GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(username, "", authorities);
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

    private DecodedJWT toDecodedJwtToken(String encodedToken) {
        return JWT.require(Algorithm.HMAC256(secretKey)).build().verify(encodedToken);
    }
}
