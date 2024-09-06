package com.example.admin.config.filter;

import com.example.admin.exception.MemberException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.admin.exception.enums.MemberErrorCode.INVALID_TOKEN;

@Component
@RequiredArgsConstructor
public class CustomFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = String.valueOf(jwtTokenProvider.getAccessTokenFromRequest(request));
        try {
            if (token != null && jwtTokenProvider.isValidToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthenticationByAccessToken(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (MemberException e) {
            SecurityContextHolder.clearContext();
            throw new MemberException(INVALID_TOKEN);
        }
        filterChain.doFilter(request, response);
    }
}
