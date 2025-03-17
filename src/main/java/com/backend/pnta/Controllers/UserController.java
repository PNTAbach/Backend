package com.backend.pnta.Controllers;


import com.backend.pnta.Exceptions.InsufficientPermissionsException;
import com.backend.pnta.Models.User.UserDto;
import com.backend.pnta.Models.User.UserSignUpDTO;
import com.backend.pnta.Models.User.UserUpdateDTO;
import com.backend.pnta.Services.Helpers.AuthHelper;
import com.backend.pnta.Services.User.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthHelper authHelper;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get All users", description = "This will return a list of all the users in the system")
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers() {
        try {
            // Check if the authenticated user has the required role
            if (authHelper.hasRole("ROLE_ADMIN")) {
                List<UserDto> users = userService.getAllUsers();
                return ResponseEntity.ok(users);
            }
            // Return Forbidden response if the user doesn't have the required role
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Operation(summary = "Get user by id", description = "This will return the user by the given id")
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            UserDto user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @Operation(summary = "Create a user", description = "This will return the created user")
    @PostMapping("/users")
    public ResponseEntity<Object> addUser(@RequestBody UserSignUpDTO userInput) {
        try {
            UserDto user = userService.saveUser(userInput);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Operation(summary = "Update user", description = "This will update the user. The user id goes in the body")
    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDTO userInput) {
        try {
            UserDto user = userService.updateUser(userInput);
            return ResponseEntity.ok(user);
        } catch (InsufficientPermissionsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> removeUser(@PathVariable Long userId) {
        try {
            userService.removeUser(userId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}