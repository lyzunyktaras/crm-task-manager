package com.sample.crm.util;

import com.sample.crm.model.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class SecurityUtil {

    private SecurityUtil() {
        throw new UnsupportedOperationException();
    }

    public static UserPrincipal getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        UserPrincipal currentUser = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            currentUser = (UserPrincipal) authentication.getPrincipal();
        }

        return currentUser;
    }
}
