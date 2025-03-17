package com.backend.pnta.Proxy.User;

import com.backend.pnta.Exceptions.InsufficientPermissionsException;
import com.backend.pnta.Models.User.UserUpdateDTO;
import com.backend.pnta.Proxy.Authorization.AuthorizationService;
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
public class UserAuthorizationAspectU {
    private final AuthorizationService authorizationService;
    private static final String PATH_TO_SERVICE="execution(* com.example.pnta.Services.User.UserService." ;
    private static final String ADD_UPDATE_VENUE_POINTCUT = PATH_TO_SERVICE+"updateUser(..))";

    @Around(ADD_UPDATE_VENUE_POINTCUT)
    public Object authorize(ProceedingJoinPoint joinPoint) throws Throwable {
        // Token shit
        String token = authorizationService.extractToken();
        Long userId = authorizationService.getUserIdFromToken(token);

        //get user id from the request body
        UserUpdateDTO userInput = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof UserUpdateDTO) {
                userInput = (UserUpdateDTO) arg;
                break;
            }
        }

        //check if user input is null or user id is null
        if (userInput == null || userInput.getUserId() == null) {
            throw new IllegalArgumentException("User id from the request body is null");
        }

        //authorization check
        if (userId != null && (userId.equals(userInput.getUserId()) || authorizationService.isUserAdmin(userId))) {
            // Proceed with the method execution if authorized
            return joinPoint.proceed();
        } else {
            throw new InsufficientPermissionsException("User is not authorized to perform this action");
        }
    }
}

