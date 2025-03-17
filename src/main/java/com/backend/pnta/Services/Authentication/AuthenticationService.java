package com.backend.pnta.Services.Authentication;

import com.backend.pnta.Models.User.AuthenticationRequest;
import com.backend.pnta.Models.User.AuthenticationResponse;
import com.backend.pnta.Models.User.UserDto;
import com.backend.pnta.Models.User.UserSignUpDTO;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse registerUser(UserSignUpDTO user);
    Long getUserIdByToken(String token);
    void giveUserAdmin(Long userId);

    UserDto getUserByToken(String substring);
}
