package com.backend.pnta.Proxy;

public interface AuthorizationService {
    Long getUserIdFromToken(String token);
    String extractToken();
    boolean isUserAdmin(Long userId);
    boolean isUserAssociatedWithVenue(Long userId,Long venueId);
    boolean isAdminOfVenue(Long userId, Long venueId);

}
