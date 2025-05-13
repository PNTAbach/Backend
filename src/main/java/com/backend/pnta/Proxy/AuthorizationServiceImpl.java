package com.backend.pnta.Proxy;


import com.backend.pnta.Services.Helpers.AuthHelper;
import com.backend.pnta.Services.Helpers.UserHelper;
import com.backend.pnta.Services.UserVenue.UserVenueService;
import com.backend.pnta.Services.Venues.Venue.VenueService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {
    private final AuthHelper authHelper;
    private final UserHelper userHelper;
    private final UserVenueService userVenueService;

    private final VenueService venueService;
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

    @Override
    public boolean isUserAssociatedWithVenue(Long userId, Long venueId) {
        //for oth admin and staff/manager
        // we can also get the role with if we want to separate actions by role userVenueService.getRoleOfUserInVenue(userId,venueId);
        if(userVenueService.isUserpartOfVenue(userId,venueId)||venueService.getIdOfVenueOwner(venueId)==userId||isUserAdmin(userId)){
            // System.out.println("TREUUUUUUUUUUUUUUUU" + userId+" "+ venueId);
            return true;
        }else{
            //System.out.println("FASLEEEEEEEEEEEEEEE" + userId+" "+ venueId);
            return false;
        }
    }
    @Override
    public boolean isAdminOfVenue(Long userId, Long venueId) {
        // for admin only, so only admin can delte for example
        if(venueService.getIdOfVenueOwner(venueId)==userId||isUserAdmin(userId)){
            return true;
        }else{
            return false;
        }
    }
}