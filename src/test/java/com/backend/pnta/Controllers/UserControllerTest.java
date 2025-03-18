package com.backend.pnta.Controllers;

import com.backend.pnta.Exceptions.InsufficientPermissionsException;
import com.backend.pnta.Models.User.Role;
import com.backend.pnta.Models.User.UserDto;
import com.backend.pnta.Models.User.UserSignUpDTO;
import com.backend.pnta.Models.User.UserUpdateDTO;
import com.backend.pnta.Services.Helpers.AuthHelper;
import com.backend.pnta.Services.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private AuthHelper authHelper;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService, authHelper);
    }

    private UserDto getMockUser() {
        return new UserDto(1L,
                "john@example.com",
                "John",
                "Doe",
                "1234567890",
                "+1",
                "profile.jpg",
                new Date(),
                Role.ROLE_USER);
    }

    @Test
    void testGetUsers_Success_AdminRole() {
        // Given
        when(authHelper.hasRole("ROLE_ADMIN")).thenReturn(true);
        List<UserDto> mockUsers = Arrays.asList(getMockUser());
        when(userService.getAllUsers()).thenReturn(mockUsers);

        // When
        ResponseEntity<List<UserDto>> response = userController.getUsers();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetUsers_Forbidden() {
        // Given
        when(authHelper.hasRole("ROLE_ADMIN")).thenReturn(false);

        // When
        ResponseEntity<List<UserDto>> response = userController.getUsers();

        // Then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testGetUserById_Success() {
        // Given
        Long userId = 1L;
        UserDto mockUser = getMockUser();
        when(userService.getUserById(userId)).thenReturn(mockUser);

        // When
        ResponseEntity<?> response = userController.getUserById(userId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    void testGetUserById_Failure() {
        // Given
        Long userId = 99L;
        when(userService.getUserById(userId)).thenThrow(new IllegalArgumentException("User not found"));

        // When
        ResponseEntity<?> response = userController.getUserById(userId);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void testAddUser_Success() {
        // Given
        UserSignUpDTO newUser = new UserSignUpDTO("john@example.com", "password", "John", "Doe", "1234567890", "+1", "profile.jpg", new Date());
        UserDto createdUser = getMockUser();
        when(userService.saveUser(newUser)).thenReturn(createdUser);

        // When
        ResponseEntity<Object> response = userController.addUser(newUser);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdUser, response.getBody());
    }

    @Test
    void testAddUser_Failure() {
        // Given
        UserSignUpDTO newUser = new UserSignUpDTO("john@example.com", "password", "John", "Doe", "1234567890", "+1", "profile.jpg", new Date());
        when(userService.saveUser(newUser)).thenThrow(new IllegalArgumentException("Email already taken"));

        // When
        ResponseEntity<Object> response = userController.addUser(newUser);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Email already taken", response.getBody());
    }

    @Test
    void testUpdateUser_Success() {
        // Given
        UserUpdateDTO updateUser = new UserUpdateDTO(1L, "john@example.com", "John", "Doe", "1234567890", "+1", "profile.jpg", new Date());
        UserDto updatedUser = getMockUser();
        when(userService.updateUser(updateUser)).thenReturn(updatedUser);

        // When
        ResponseEntity<?> response = userController.updateUser(updateUser);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    void testUpdateUser_InsufficientPermissions() {
        // Given
        UserUpdateDTO updateUser = new UserUpdateDTO(1L, "john@example.com", "John", "Doe", "1234567890", "+1", "profile.jpg", new Date());
        when(userService.updateUser(updateUser)).thenThrow(new InsufficientPermissionsException("Not allowed"));

        // When
        ResponseEntity<?> response = userController.updateUser(updateUser);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Not allowed", response.getBody());
    }

    @Test
    void testUpdateUser_BadRequest() {
        // Given
        UserUpdateDTO updateUser = new UserUpdateDTO(1L, "john@example.com", "John", "Doe", "1234567890", "+1", "profile.jpg", new Date());
        when(userService.updateUser(updateUser)).thenThrow(new IllegalArgumentException("Invalid data"));

        // When
        ResponseEntity<?> response = userController.updateUser(updateUser);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid data", response.getBody());
    }

    @Test
    void testRemoveUser_Success() {
        // Given
        Long userId = 1L;
        doNothing().when(userService).removeUser(userId);

        // When
        ResponseEntity<?> response = userController.removeUser(userId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRemoveUser_Failure() {
        // Given
        Long userId = 99L;
        doThrow(new IllegalArgumentException("User not found")).when(userService).removeUser(userId);

        // When
        ResponseEntity<?> response = userController.removeUser(userId);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }
}
