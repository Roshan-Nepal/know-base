package com.roshan.know_base.auth.service;

import com.roshan.know_base.auth.entity.CustomUserDetails;
import com.roshan.know_base.common.enums.ErrorCode;
import com.roshan.know_base.common.exception.AuthException;
import com.roshan.know_base.common.security.CurrentUserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class SpringSecurityCurrentUserProvider implements CurrentUserProvider {
    /**
     * Extracts the current authenticated user's ID
     * @return UUID of authenticated user
     * @throws AuthException if the user is unauthenticated or has an invalid principle
     */

    @Override
    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            throw new AuthException("User is not authenticated", ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }
        Object principal =  authentication.getPrincipal();
        if(principal instanceof CustomUserDetails customUserDetails){
            return customUserDetails.getId();
        }
        throw new AuthException("Current user principal is not an instance of CustomUserDetails.", ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Extracts the current authenticated user's username
     * @return the username of authenticated user
     * @throws AuthException if the user is anonymous or unauthenticated
     */
    @Override
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || isAnonymous(authentication)) {
            throw new AuthException("User is not authenticated", ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        return authentication.getName();
    }

    /**
     * Helper method to explicitly catch Spring Security's default anonymous user
     */
    private boolean isAnonymous(Authentication authentication) {
        return "anonymousUser".equals(authentication.getPrincipal());
    }
}
