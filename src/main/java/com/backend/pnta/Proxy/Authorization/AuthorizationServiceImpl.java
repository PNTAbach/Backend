package com.backend.pnta.Proxy.Authorization;


import com.backend.pnta.Services.Helpers.AuthHelper;
import com.backend.pnta.Services.Helpers.UserHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {
    private final AuthHelper authHelper;
    private final UserHelper userHelper;
    private final HttpServletRequest request;

    @Override
    public Long getUserIdFromToken(String token) {
        return authHelper.getUserIdFromToken(token);
    }
    public String extractToken() {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid token");
        }
        return token.substring("Bearer ".length());
    }

    @Override
    public boolean isUserAdmin(Long userId) {
        return userHelper.isUserAdmin(userId);
    }


}
