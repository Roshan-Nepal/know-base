package com.roshan.know_base.infrastructure.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter()
                .write("""
                        {
                            "detail": "The provided JWT is invalid, expired, or malformed.",
                            "instance": "%s",
                            "status": 401,
                            "title": "Unauthorized",
                            "errorCode": "AUTH_INVALID_JWT",
                            "timestamp": "%s"
                        }
                        """.formatted(request.getRequestURI(), Instant.now()));
    }
}
