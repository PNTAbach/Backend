package com.backend.pnta.Services.Authentication;


import com.backend.pnta.Configuration.JwtService;
import com.backend.pnta.Exceptions.InsufficientPermissionsException;
import com.backend.pnta.Exceptions.UserAlreadyAdminException;
import com.backend.pnta.Models.User.*;
import com.backend.pnta.Repositories.UserRepository;
import com.backend.pnta.Services.Helpers.AuthHelper;
import com.backend.pnta.Services.Helpers.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserHelper userHelper;
    private final AuthHelper authHelper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword())
            );
            var user = userRepository.findByEmail(request.getEmail());
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).build();
        } catch (AuthenticationException ex) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
    @Override
    public AuthenticationResponse registerUser(UserSignUpDTO user) {
        if(userHelper.doesEmailExist(user.getEmail())){
            throw new IllegalArgumentException("Email is already registered");
        }
        if (!userHelper.verifyValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email address");
        }
        if (!userHelper.verifyPasswordComplexity(user.getPassword())) {
            throw new IllegalArgumentException("Password does not meet complexity requirements");
        }
        // create a new user and save it to the db
        UserP newUser = new UserP();
        newUser.setEmail(user.getEmail());
        newUser.setlName(user.getLName());
        newUser.setfName(user.getFName());
        newUser.setBirthDate(user.getBirthDate());
        newUser.setImage(user.getImage());
        newUser.setPassword(user.getPassword());
        newUser.setPhoneCountryCode(user.getPhoneCountryCode());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setRole(Role.ROLE_USER);
        //encrypt the password before saving it to the DB
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(newUser);
        var jwt = jwtService.generateToken(newUser);
        return AuthenticationResponse.builder().token(jwt).build();
    }

    public Long getUserIdByToken(String token) {
        try {
            return authHelper.getUserIdFromToken(token);
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            throw new RuntimeException("Invalid token");
        }
    }

    @Override
    public void giveUserAdmin(Long userId) {
        boolean isAdmin = authHelper.hasRole("ROLE_ADMIN");
        if (!isAdmin) {
            throw new InsufficientPermissionsException("User does not have permission to perform this operation.");
        }

        Optional<UserP> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        UserP user = userOptional.get();
        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new UserAlreadyAdminException("User is already an admin.");
        }

        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);
    }

    @Override
    public UserDto getUserByToken(String token) {
        try {
            Optional<UserP> user = userRepository.findById(authHelper.getUserIdFromToken(token));

            if (user.isPresent()) {
                return userHelper.convertToUserDto(user.get());
            } else {
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user by token", e);
        }
    }



}
