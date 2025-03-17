package com.backend.pnta.Services.Helpers;

import com.backend.pnta.Configuration.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthHelper {

    private final JwtService jwtService;
    //this will verify if the token as the necessry role
    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
        }
        return false;
    }

    // This method will extract the user ID from the JWT token
    public Long getUserIdFromToken(String token) {
        // Check if the token is not null or empty
        if (token != null && !token.isEmpty()) {
            // Extract the user ID from the token claims
            Integer userIdInt = jwtService.extractClaim(token, claims -> claims.get("userId", Integer.class));

            // If user ID is not null, convert it to Long and return
            if (userIdInt != null) {
                return userIdInt.longValue();
            } else {
                throw new RuntimeException("User ID not found in token claims");
            }
        } else {
            throw new IllegalArgumentException("Token is null or empty");
        }
    }

}
