package com.backend.pnta.Controllers;

import com.backend.pnta.Models.User.Role;
import com.backend.pnta.Services.Statistics.StatisticsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WithMockUser(username = "testuser", roles = {"ADMIN"})
@AutoConfigureMockMvc(addFilters = false)

public class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsService statisticsService;

    @Test
    void getTotalUsers_shouldReturnOk() throws Exception {
        Mockito.when(statisticsService.getTotalUsers()).thenReturn(100L);

        mockMvc.perform(get("/statistics/totalUsers"))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));
    }

    @Test
    void getTotalLocations_shouldReturnOk() throws Exception {
        Mockito.when(statisticsService.getTotalLocations()).thenReturn(50L);

        mockMvc.perform(get("/statistics/totalLocations"))
                .andExpect(status().isOk())
                .andExpect(content().string("50"));
    }

    @Test
    void getTotalVenues_shouldReturnOk() throws Exception {
        Mockito.when(statisticsService.getTotalVenues()).thenReturn(25L);

        mockMvc.perform(get("/statistics/totalVenues"))
                .andExpect(status().isOk())
                .andExpect(content().string("25"));
    }

    @Test
    void getAverageVenueRating_shouldReturnOk() throws Exception {
        Mockito.when(statisticsService.getAverageVenueRating()).thenReturn(4.5);

        mockMvc.perform(get("/statistics/averageVenueRating"))
                .andExpect(status().isOk())
                .andExpect(content().string("4.5"));
    }

    @Test
    void getUsersCountByRole_shouldReturnOk() throws Exception {
        Mockito.when(statisticsService.getUsersCountByRole()).thenReturn(
                Map.of(Role.ROLE_ADMIN, 5L, Role.ROLE_USER, 95L)
        );

        mockMvc.perform(get("/statistics/usersCountByRole"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ROLE_ADMIN").value(5))
                .andExpect(jsonPath("$.ROLE_USER").value(95));
    }

}
