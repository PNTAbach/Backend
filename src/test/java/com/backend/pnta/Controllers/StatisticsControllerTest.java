package com.backend.pnta.Controllers;

import com.backend.pnta.Models.User.Role;
import com.backend.pnta.Services.Statistics.StatisticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsControllerTest {

    @InjectMocks
    private StatisticsController statisticsController;

    @Mock
    private StatisticsService statisticsService;

    @Test
    void getTotalUsers_shouldReturnOk() {
        when(statisticsService.getTotalUsers()).thenReturn(100L);

        ResponseEntity<Long> response = statisticsController.getTotalUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(100L, response.getBody());
    }

    @Test
    void getTotalLocations_shouldReturnOk() {
        when(statisticsService.getTotalLocations()).thenReturn(50L);

        ResponseEntity<Long> response = statisticsController.getTotalLocations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(50L, response.getBody());
    }

    @Test
    void getTotalVenues_shouldReturnOk() {
        when(statisticsService.getTotalVenues()).thenReturn(25L);

        ResponseEntity<Long> response = statisticsController.getTotalVenues();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(25L, response.getBody());
    }

    @Test
    void getAverageVenueRating_shouldReturnOk() {
        when(statisticsService.getAverageVenueRating()).thenReturn(4.5);

        ResponseEntity<Double> response = statisticsController.getAverageVenueRating();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4.5, response.getBody());
    }

    @Test
    void getUsersCountByRole_shouldReturnOk() {
        Map<Role, Long> expected = Map.of(Role.ROLE_ADMIN, 5L, Role.ROLE_USER, 95L);
        when(statisticsService.getUsersCountByRole()).thenReturn(expected);

        ResponseEntity<?> response = statisticsController.getUsersCountByRole();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void getMostCommonCity_shouldReturnOk() {
        Map.Entry<String, Long> expected = Map.entry("New York", 10L);
        when(statisticsService.getMostCommonCity()).thenReturn(expected);

        ResponseEntity<Map.Entry<String, Long>> response = statisticsController.getMostCommonCity();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void getTopRatedVenues_shouldReturnOk() {
        List<Map.Entry<Long, Double>> expected = List.of(
                Map.entry(1L, 4.9),
                Map.entry(2L, 4.8)
        );
        when(statisticsService.getTopRatedVenues(2)).thenReturn(expected);

        ResponseEntity<List<Map.Entry<Long, Double>>> response = statisticsController.getTopRatedVenues(2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }
}
