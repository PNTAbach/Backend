package com.backend.pnta.Proxy.Venue;

import com.backend.pnta.Proxy.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@EnableAspectJAutoProxy
public class VenueAuthorizationAspectC {
    //this call is called on every request for the corresponding @Around
    private final AuthorizationService authorizationService;
    //curently proxy for  new venue type and update venue
    private static final String ADD_UPDATE_VENUE_POINTCUT = "execution(* com.example.pnta.Services.Venues.Venue.VenueService.saveVenue(..))";

    @Around(ADD_UPDATE_VENUE_POINTCUT)
    public Object authorize(ProceedingJoinPoint joinPoint) throws Throwable {
        // Token shit
        String token = authorizationService.extractToken();
        // get user ID from the token using AuthHelper
        Long userId = authorizationService.getUserIdFromToken(token);

        // Perform authorization check using AuthorizationService
        // For example, check if the user has permission to edit the venue
        if (userId != null && authorizationService.isUserAdmin(userId)) {
            // Proceed with the method execution if authorized
            return joinPoint.proceed();
        } else {
            throw new RuntimeException("User is not authorized to perform this action");
        }
    }
}
