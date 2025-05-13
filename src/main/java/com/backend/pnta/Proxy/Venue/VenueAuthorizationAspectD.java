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
public class VenueAuthorizationAspectD {
    private final AuthorizationService authorizationService;
    @Around("execution(* com.backend.pnta.Services.Venues.Venue.VenueService.deleteVenueById(..))")
    public Object authorizeDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        // Token shit
        String token = authorizationService.extractToken();
        // Extract venue ID from the method arguments
        Long venueId = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof Long) {
                venueId = (Long) arg;
                break;
            }
        }
        Long userId = authorizationService.getUserIdFromToken(token);

        // Check if the user is the admin of the venue
        if (userId != null && venueId != null && (authorizationService.isUserAdmin(userId)||authorizationService.isAdminOfVenue(userId,venueId))) {
            // Proceed with the method execution if authorized
            return joinPoint.proceed();
        } else {
            throw new RuntimeException("Only venue admin is authorized to delete this venue!");
        }
    }
}
