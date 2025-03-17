package com.backend.pnta.Proxy.Authorization;

public interface AuthorizationService {
    Long getUserIdFromToken(String token);
    String extractToken();
    boolean isUserAdmin(Long userId);

}
