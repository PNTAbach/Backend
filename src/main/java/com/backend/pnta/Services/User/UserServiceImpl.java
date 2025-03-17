package com.backend.pnta.Services.User;


import com.backend.pnta.Models.User.*;
import com.backend.pnta.Repositories.UserRepository;
import com.backend.pnta.Services.Helpers.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserHelper userHelper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDto getUserById(Long userId) {
        if(!userHelper.doesUserExist(userId)){
            throw new IllegalArgumentException("User with id "+userId+" does not exist!");
        }
        return UserConverter.toDto(userRepository.getById(userId));
    }
    @Override
    public UserDto saveUser(UserSignUpDTO user) {

        UserP userP = new UserP(user);
        if(userHelper.doesEmailExist(user.getEmail())){
            throw new IllegalArgumentException("Email is already registered");
        }
        if (!userHelper.verifyValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email address");
        }
        if (!userHelper.verifyPasswordComplexity(user.getPassword())) {
            throw new IllegalArgumentException("Password does not meet complexity requirements");
        }
        userP.setRole(Role.ROLE_USER);
        userP.setPassword(passwordEncoder.encode(user.getPassword()));
        return UserConverter.toDto(userRepository.save(userP));
    }
    @Override
    public List<UserDto> getAllUsers() {
        List<UserP> userEntities = userRepository.findAll();
        return UserConverter.toDtoList(userEntities);
    }
    @Override
    public UserP getUserByEmail(String email) {
        if(!userHelper.findByEmail(email)){
            throw new IllegalArgumentException("User with email "+email+" does not exist!");
        }
        return userRepository.findByEmail(email);
    }
    @Override
    public void removeUser(Long userId) {
        if(!userHelper.doesUserExist(userId)){
            throw new IllegalArgumentException("User with id "+userId+" does not exist!");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto updateUser(UserUpdateDTO updatedUser) {
        UserP existingUser = userRepository.findById(updatedUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User with id " + updatedUser.getUserId() + " does not exist!"));
        // Validate the email
        if (!userHelper.isValidEmail(updatedUser.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setfName(updatedUser.getFName());
        existingUser.setlName(updatedUser.getLName());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setPhoneCountryCode(updatedUser.getPhoneCountryCode());
        existingUser.setImage(updatedUser.getImage());
        existingUser.setBirthDate(updatedUser.getBirthDate());
        return UserConverter.toDto(userRepository.save(existingUser));
    }
}