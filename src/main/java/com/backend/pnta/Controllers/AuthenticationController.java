package com.backend.pnta.Controllers;


import com.backend.pnta.Exceptions.InsufficientPermissionsException;
import com.backend.pnta.Exceptions.UserAlreadyAdminException;
import com.backend.pnta.Models.User.AuthenticationRequest;
import com.backend.pnta.Models.User.AuthenticationResponse;
import com.backend.pnta.Models.User.UserDto;
import com.backend.pnta.Models.User.UserSignUpDTO;
import com.backend.pnta.Services.Authentication.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @Autowired
    private HttpServletRequest request;
    @Operation(summary = "Login", description = "This will return a token if the user is authenticate")
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = authenticationService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Register", description = "This will return a token and userId if the user is registered")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserSignUpDTO user) {
        try {
            AuthenticationResponse response = authenticationService.registerUser(user);
            return ResponseEntity.ok(response);
        }  catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Get User ID from Token", description = "This will return the user ID extracted from the provided token")
    @GetMapping("/user-id")
    public ResponseEntity<?> getUserIdFromToken() {
        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (token == null || !token.startsWith("Bearer ")) {
                // Handle missing or invalid token
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
            }
            Long userId = authenticationService.getUserIdByToken(token.substring("Bearer ".length()));
            return ResponseEntity.ok(userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
    @Operation(summary = "Get All User from Token", description = "This will return the user extracted from the provided token")
    @GetMapping("/user")
    public ResponseEntity<?> getUserFromToken() {
        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (token == null || !token.startsWith("Bearer ")) {
                // Handle missing or invalid token
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
            }
            UserDto userId = authenticationService.getUserByToken(token.substring("Bearer ".length()));
            return ResponseEntity.ok(userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @Operation(summary = "Give User Admin", description = "This will give admin privileges to a user, olny admin users can do this!!")
    @PostMapping("/give-admin/{userId}")
    public ResponseEntity<?> giveUserAdmin(@PathVariable Long userId) {
        try {
            authenticationService.giveUserAdmin(userId);
            return ResponseEntity.ok("User granted admin privileges successfully");
        } catch (InsufficientPermissionsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (UserAlreadyAdminException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}