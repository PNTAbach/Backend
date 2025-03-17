package com.backend.pnta.Models.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {

    public static UserDto toDto(UserP userEntity) {
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

    public static List<UserDto> toDtoList(List<UserP> userEntities) {
        return userEntities.stream()
                .map(UserConverter::toDto)
                .collect(Collectors.toList());
    }

}
