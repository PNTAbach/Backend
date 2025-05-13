package com.backend.pnta.Controllers;

import com.backend.pnta.Models.UserVenueLike.UserVenueLikeCreateDTO;
import com.backend.pnta.Models.UserVenueLike.UserVenueLikeDTO;
import com.backend.pnta.Services.UserVenueLike.UserVenueLikeService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserVenueLikeControllerTest {

    @Mock
    private UserVenueLikeService userVenueLikeService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserVenueLikeController userVenueLikeController;

    private final String bearerToken = "Bearer mock-token";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userVenueLikeController, "request", request);
        lenient().when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(bearerToken);    }


    @Test
    void addLikeToVenue_shouldReturnCreated() {
        // Arrange
        UserVenueLikeCreateDTO dto = new UserVenueLikeCreateDTO();
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer mock-token");

        doNothing().when(userVenueLikeService).addLikeToVenue(dto, "mock-token");

        // Act
        ResponseEntity<?> response = userVenueLikeController.addLikeToVenue(dto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Like added successfully", response.getBody());
    }

    @Test
    void removeLikeFromVenue_shouldReturnOk() {
        doNothing().when(userVenueLikeService).removeLikeFromVenue(1L, "mock-token");

        ResponseEntity<?> response = userVenueLikeController.removeLikeFromVenue(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Like removed successfully", response.getBody());
    }

    @Test
    void getLikesFromVenue_shouldReturnOk() {
        List<UserVenueLikeDTO> likes = List.of(new UserVenueLikeDTO());
        when(userVenueLikeService.getLikesFromVenue(1L)).thenReturn(likes);

        ResponseEntity<?> response = userVenueLikeController.getLikesFromVenue(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(likes, response.getBody());
    }

    @Test
    void getLikesFromVenue_shouldReturnNotFound() {
        when(userVenueLikeService.getLikesFromVenue(1L)).thenReturn(List.of());

        ResponseEntity<?> response = userVenueLikeController.getLikesFromVenue(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No likes found for the specified venue", response.getBody());
    }

    @Test
    void getNumberOfLikesFromVenue_shouldReturnOk() {
        when(userVenueLikeService.getNumberOfLikesFromVenue(1L)).thenReturn(7);

        ResponseEntity<?> response = userVenueLikeController.getNumberOfLikesFromVenue(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(7, response.getBody());
    }

}
