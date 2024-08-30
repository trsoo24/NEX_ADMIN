package com.example.admin.config.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.admin.exception.MemberException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

import static com.example.admin.exception.enums.MemberErrorCode.INVALID_TOKEN;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret.key}")
    private String secretKey;
    private static final String ACCESS_TOKEN = "AccessToken";
    private static final String AUTHORITIES_KEY = "Authorization";
    private static final String BEARER_TYPE = "Bearer ";
    private static final String MEMBER_NAME = "MEMBER_NAME";
    private final Long accessTokenValidTime = 1000L * 60 * 60 * 6;
    private final UserDetailsService userDetailsService;

    public String generateAccessToken(String memberName) { // accessToken 생성
        Date date = new Date();

        return JWT.create()
                .withSubject(ACCESS_TOKEN)
                .withExpiresAt(new Date(date.getTime() + accessTokenValidTime))
                .withClaim(MEMBER_NAME, memberName)
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String getPayloadEmail(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secretKey))
                    .build()
                    .verify(token)
                    .getClaim(MEMBER_NAME)
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
        String userPrincipal = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(access_token).getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(userPrincipal);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
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
