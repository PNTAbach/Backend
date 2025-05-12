package com.backend.pnta.Services.Helpers;


import com.backend.pnta.Models.User.Role;
import com.backend.pnta.Models.User.UserDto;
import com.backend.pnta.Models.User.UserP;
import com.backend.pnta.Models.UserVenue.AllUserVenueDTO;
import com.backend.pnta.Models.UserVenue.UserPAndRoleVenueDTO;
import com.backend.pnta.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@RequiredArgsConstructor
public class UserHelper {
    private final UserRepository userRepository;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PWD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%*]).{8,24}$";
    private static final Pattern pattern = Pattern.compile(PWD_REGEX);
    public boolean isUserAdmin(Long userId) {
        try {
            Optional<Role> userRoleOptional = userRepository.getRole(userId);
            return userRoleOptional.isPresent() && userRoleOptional.get() == Role.ROLE_ADMIN;
        } catch (Exception e) {

            throw new RuntimeException("User with id "+userId+" does not exist!");
        }
    }
    public boolean doesUserExist(Long id) {
        return userRepository.findById(id).isPresent();
    }

    public boolean findByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean doesEmailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean isValidPassword(String password) {
        return pattern.matcher(password).matches();
    }

    public boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    public boolean verifyValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean verifyPasswordComplexity(String password) {
        Pattern pattern = Pattern.compile(PWD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    public UserDto convertToDto(UserP userEntity) {
        return UserDto.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .fName(userEntity.getfName())
                .lName(userEntity.getlName())
                .phoneNumber(userEntity.getPhoneNumber())
                .phoneCountryCode(userEntity.getPhoneCountryCode())
                .image(userEntity.getImage())
                .birthDate(userEntity.getBirthDate())
                .role(userEntity.getRole())
                .build();
    }

    public AllUserVenueDTO convertToAllUserVenueDto(UserPAndRoleVenueDTO userPAndRoleVenueDTO) {
        return AllUserVenueDTO.builder()
                .userId(userPAndRoleVenueDTO.getUserP().getUserId())
                .email(userPAndRoleVenueDTO.getUserP().getEmail())
                .fName(userPAndRoleVenueDTO.getUserP().getfName())
                .lName(userPAndRoleVenueDTO.getUserP().getlName())
                .phoneNumber(userPAndRoleVenueDTO.getUserP().getPhoneNumber())
                .phoneCountryCode(userPAndRoleVenueDTO.getUserP().getPhoneCountryCode())
                .image(userPAndRoleVenueDTO.getUserP().getImage())
                .birthDate(userPAndRoleVenueDTO.getUserP().getBirthDate())
                .role(userPAndRoleVenueDTO.getUserP().getRole())
                .venueRole(userPAndRoleVenueDTO.getVenueRole())
                .build();
    }

    public UserDto convertToUserDto(UserP userP) {
        return UserDto.builder()
                .userId(userP.getUserId())
                .email(userP.getEmail())
                .fName(userP.getfName())
                .lName(userP.getlName())
                .phoneNumber(userP.getPhoneNumber())
                .phoneCountryCode(userP.getPhoneCountryCode())
                .image(userP.getImage())
                .birthDate(userP.getBirthDate())
                .role(userP.getRole())
                .build();
    }
}