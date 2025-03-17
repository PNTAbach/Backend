package com.backend.pnta.Models.User;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long userId;
    private String email;
    private String fName;
    private String lName;
    private String phoneNumber;
    private String phoneCountryCode;
    private String image;
    private Date birthDate;
    private Role role;
}
