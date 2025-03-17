package com.backend.pnta.Services.User;



import com.backend.pnta.Models.User.UserDto;
import com.backend.pnta.Models.User.UserP;
import com.backend.pnta.Models.User.UserSignUpDTO;
import com.backend.pnta.Models.User.UserUpdateDTO;

import java.util.List;


public interface UserService {
    UserDto getUserById(Long userId) ;
    List<UserDto> getAllUsers();
    UserP getUserByEmail(String email);
    UserDto updateUser(UserUpdateDTO updatedUser);
    UserDto saveUser(UserSignUpDTO user);
    void removeUser(Long userId);
}
