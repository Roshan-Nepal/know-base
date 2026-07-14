package com.roshan.know_base.common.constant;

public final class SecurityConstants {
    private SecurityConstants(){}
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";


    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_USER_ID = "userId";


    public static final String[] PUBLIC_URLS = {
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
            "/api/v1/auth/register",
            "/actuator/health",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
}
