package com.example.admin.config.filter;

import com.example.admin.exception.MemberException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.admin.exception.enums.MemberErrorCode.INVALID_TOKEN;

@RequiredArgsConstructor
public class CustomFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().equals("/**")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtTokenProvider.getAccessTokenFromRequest(request);
        try {
            if (accessToken != null && jwtTokenProvider.isValidToken(accessToken)) {
                Authentication auth = jwtTokenProvider.getAuthenticationByAccessToken(accessToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                accessToken = jwtTokenProvider.RegenerateToken(request, response);

                if (jwtTokenProvider.isValidToken(accessToken)) {
                    Authentication auth = jwtTokenProvider.getAuthenticationByAccessToken(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (MemberException e) {
            SecurityContextHolder.clearContext();
            throw new MemberException(INVALID_TOKEN);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if (request.getRequestURI().equals("/**")) {
            return true;
        }

        if (request.getRequestURI().equals("/login")) {
            return true;
        }
        return false;
    }
}
