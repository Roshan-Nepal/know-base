package com.roshan.know_base.infrastructure.security;

import com.roshan.know_base.common.constant.SecurityConstants;
import com.roshan.know_base.common.security.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final PathMatcher pathMatcher;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException{
        return Arrays.stream(SecurityConstants.PUBLIC_URLS)
                .anyMatch(pattern -> pathMatcher.match(pattern, request.getServletPath()));
    }
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(SecurityConstants.AUTH_HEADER);
        final String jwt;
        final String email;

        if(authHeader == null || !authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)){
            filterChain.doFilter(request,response);
            return;
        }

        jwt = authHeader.substring(SecurityConstants.TOKEN_PREFIX.length());
        try {
            {
                email = jwtUtil.extractEmail(jwt);
                if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

                    if(jwtUtil.isTokenValid(jwt)){

                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                        // Attach incoming request details (like remote IP and Session ID)
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        MDC.put("userEmail", email);
                    }
                }
            }
        }
        catch (JwtException | IllegalArgumentException e){
            log.error("Failed to set user authentication in security context: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        } finally {
            try{
                filterChain.doFilter(request,response);
            } finally {
                MDC.remove("userEmail");
            }
        }
    }
}
