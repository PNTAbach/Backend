package com.backend.pnta.Proxy.UserVenue;

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
public class UserVenueAuthorizationAspectP {
    //this call is called on every request for the corresponding @Around
    private final AuthorizationService authorizationService;
    //curently proxy for  new venue type and update venue
    private static final String ADD_UPDATE_VENUE_POINTCUT = "execution(* com.example.pnta.Services.UserVenue.UserVenueService.addVenueWorker(..))";

    @Around(ADD_UPDATE_VENUE_POINTCUT)
    public Object authorize(ProceedingJoinPoint joinPoint) throws Throwable {
        // Token shit
        String token = authorizationService.extractToken();

        // get venue ID from the path variable
        Long venueId = null;
        int argCount = 0;

        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof Long) {
                argCount++;
                if (argCount == 2) {
                    venueId = (Long) arg;
                    break;
                }
            }
        }

        // get user ID from the token using AuthHelper
        Long userId = authorizationService.getUserIdFromToken(token);

        // Perform authorization check using AuthorizationService
        // For example, check if the user has permission to edit the venue
        if (userId != null && venueId != null && (authorizationService.isUserAdmin(userId)||authorizationService.isAdminOfVenue(userId,venueId))) {

            return joinPoint.proceed();
        } else {
            throw new RuntimeException("User is not authorized to perform this action");
        }
    }
}
