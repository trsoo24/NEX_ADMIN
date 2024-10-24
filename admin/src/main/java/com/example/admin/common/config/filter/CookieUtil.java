package com.example.admin.common.config.filter;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieUtil {
    public Cookie getAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public Cookie getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh_token")) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public void setAccessTokenCookie(HttpServletResponse response, String accessToken) {
        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 60); // 30 분

        response.addCookie(cookie);
    }

    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 1 일

        response.addCookie(cookie);
    }

    public void deleteCookie(HttpServletResponse response) {
        // 로그아웃일 때 access, refresh 모두 삭제
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        Cookie refrehsTokenCookie = new Cookie("refreshToken", null);

        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);

        refrehsTokenCookie.setHttpOnly(true);
        refrehsTokenCookie.setPath("/");
        refrehsTokenCookie.setMaxAge(0);

        response.addCookie(accessTokenCookie);
        response.addCookie(refrehsTokenCookie);
    }
}
