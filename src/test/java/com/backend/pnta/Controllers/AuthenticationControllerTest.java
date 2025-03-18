package com.backend.pnta.Controllers;

import com.backend.pnta.Exceptions.InsufficientPermissionsException;
import com.backend.pnta.Exceptions.UserAlreadyAdminException;
import com.backend.pnta.Models.User.*;
import com.backend.pnta.Services.Authentication.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        authenticationController = new AuthenticationController(authenticationService);
    }

    @Test
    void testAuthenticateUser_Success() {
        // Given
        AuthenticationRequest authRequest = new AuthenticationRequest("testuser@example.com", "Test@1234");
        AuthenticationResponse authResponse = new AuthenticationResponse("mocked-token");

        when(authenticationService.authenticate(authRequest)).thenReturn(authResponse);

        // When
        ResponseEntity<?> response = authenticationController.authenticateUser(authRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    void testAuthenticateUser_Failure() {
        // Given
        AuthenticationRequest authRequest = new AuthenticationRequest("user@example.com", "wrongpassword");
        when(authenticationService.authenticate(authRequest)).thenThrow(new IllegalArgumentException("Invalid credentials"));

        // When
        ResponseEntity<?> response = authenticationController.authenticateUser(authRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    private UserSignUpDTO getUserSignUpDTO() {
        UserSignUpDTO userSignUpDTO = new UserSignUpDTO();
        userSignUpDTO.setEmail("testuser@example.com");
        userSignUpDTO.setPassword("Test@1234");
        userSignUpDTO.setFName("John");
        userSignUpDTO.setLName("Doe");
        userSignUpDTO.setPhoneNumber("1234567890");
        userSignUpDTO.setPhoneCountryCode("+1");
        userSignUpDTO.setImage("profile.jpg");
        userSignUpDTO.setBirthDate(new Date());
        return userSignUpDTO;
    }

    @Test
    void testRegisterUser_Success() {
        // Given
        UserSignUpDTO userSignUpDTO = getUserSignUpDTO();
        AuthenticationResponse authResponse = new AuthenticationResponse("mocked-token");

        when(authenticationService.registerUser(userSignUpDTO)).thenReturn(authResponse);

        // When
        ResponseEntity<?> response = authenticationController.registerUser(userSignUpDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    void testRegisterUser_Failure() {
        // Given
        UserSignUpDTO userSignUpDTO = getUserSignUpDTO();
        when(authenticationService.registerUser(userSignUpDTO))
                .thenThrow(new IllegalArgumentException("Email already taken"));

        // When
        ResponseEntity<?> response = authenticationController.registerUser(userSignUpDTO);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already taken", response.getBody());
    }

    @Test
    void testGiveUserAdmin_Success() {
        // Given
        Long userId = 5L;
        doNothing().when(authenticationService).giveUserAdmin(userId);

        // When
        ResponseEntity<?> response = authenticationController.giveUserAdmin(userId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User granted admin privileges successfully", response.getBody());
    }

    @Test
    void testGiveUserAdmin_InsufficientPermissions() {
        // Given
        Long userId = 5L;
        doThrow(new InsufficientPermissionsException("Only admin users can do this")).when(authenticationService).giveUserAdmin(userId);

        // When
        ResponseEntity<?> response = authenticationController.giveUserAdmin(userId);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Only admin users can do this", response.getBody());
    }

    @Test
    void testGiveUserAdmin_UserAlreadyAdmin() {
        // Given
        Long userId = 5L;
        doThrow(new UserAlreadyAdminException("User is already an admin")).when(authenticationService).giveUserAdmin(userId);

        // When
        ResponseEntity<?> response = authenticationController.giveUserAdmin(userId);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User is already an admin", response.getBody());
    }
}
