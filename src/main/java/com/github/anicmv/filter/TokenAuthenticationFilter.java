package com.github.anicmv.filter;

import com.github.anicmv.config.DouBanConfig;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;


/**
 * @author anicmv
 * @date 2025/2/23 22:29
 * @description token验证分流器
 */
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private DouBanConfig douBanConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (validateToken(douBanConfig.getAccessToken(), token)) {
                Authentication auth = getAuthentication(douBanConfig.getRole());
                SecurityContextHolder.getContext().setAuthentication(auth);
                filterChain.doFilter(request, response);
            }
        }

    }


    private boolean validateToken(String tokens, String verificationToken) {
        return Arrays.asList(tokens.split(",")).contains(verificationToken);
    }


    private Authentication getAuthentication(String role) {
        return new UsernamePasswordAuthenticationToken(
                "admin",
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
}