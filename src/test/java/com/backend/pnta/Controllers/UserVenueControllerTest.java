package com.backend.pnta.Controllers;

import com.backend.pnta.Models.UserVenue.AllUserVenueDTO;
import com.backend.pnta.Services.UserVenue.UserVenueService;
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

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserVenueControllerTest {

    @InjectMocks
    private UserVenueController userVenueController;

    @Mock
    private UserVenueService userVenueService;

    @Mock
    private HttpServletRequest request;

    private final String token = "Bearer mock-token";

    @BeforeEach
    void injectRequest() throws Exception {
        Field requestField = UserVenueController.class.getDeclaredField("request");
        requestField.setAccessible(true);
        requestField.set(userVenueController, request);
    }

    @Test
    void addVenueWorker_shouldReturnOk() {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(token);
        doNothing().when(userVenueService).addVenueWorker(1L, 2L);

        ResponseEntity<?> response = userVenueController.addVenueWorker(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User 1 added to your venue!", response.getBody());
    }

    @Test
    void deleteVenueWorker_shouldReturnOk() {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(token);
        doNothing().when(userVenueService).deleteVenueWorkers(1L, 2L);

        ResponseEntity<?> response = userVenueController.deleteVenueWorkers(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User 1 removed from your venue!", response.getBody());
    }

    @Test
    void getVenueWorkers_shouldReturnOk() {
        List<AllUserVenueDTO> mockList = List.of(new AllUserVenueDTO());
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(token);
        when(userVenueService.getVenueWorkers(2L)).thenReturn(mockList);

        ResponseEntity<?> response = userVenueController.getVenueWorkers(2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockList, response.getBody());
    }

    @Test
    void setVenueManager_shouldReturnOk() {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(token);
        doNothing().when(userVenueService).setVenueManager("mock-token", 1L, 2L);

        ResponseEntity<?> response = userVenueController.setVenueManager(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User successfully set as venue manager.", response.getBody());
    }

    @Test
    void setVenueStaff_shouldReturnOk() {
        doNothing().when(userVenueService).setVenueStaff("mock-token", 1L, 2L);

        ResponseEntity<?> response = userVenueController.setVenueStaff(1L, 2L, "Bearer mock-token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User successfully set as venue staff.", response.getBody());
    }

    @Test
    void addVenueWorker_shouldReturnUnauthorized_whenTokenMissing() {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        ResponseEntity<?> response = userVenueController.addVenueWorker(1L, 2L);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Missing or invalid token", response.getBody());
    }

    @Test
    void deleteVenueWorker_shouldReturnUnauthorized_whenTokenMissing() {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        ResponseEntity<?> response = userVenueController.deleteVenueWorkers(1L, 2L);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Missing or invalid token", response.getBody());
    }
}
