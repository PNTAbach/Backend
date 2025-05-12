package com.backend.pnta.Controllers;

import com.backend.pnta.Models.UserVenue.AllUserVenueDTO;
import com.backend.pnta.Services.UserVenue.UserVenueService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username = "testuser", roles = {"ADMIN"})

class UserVenueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserVenueService userVenueService;

    private final String token = "Bearer mock-token";

    @Test
    void addVenueWorker_shouldReturnOk() throws Exception {
        doNothing().when(userVenueService).addVenueWorker(1L, 2L);

        mockMvc.perform(post("/manageUserVenue/addVenueWorker/1/2")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(content().string("User 1 added to your venue!"));
    }

    @Test
    void deleteVenueWorker_shouldReturnOk() throws Exception {
        doNothing().when(userVenueService).deleteVenueWorkers(1L, 2L);

        mockMvc.perform(delete("/manageUserVenue/removeVenueUser/1/2")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(content().string("User 1 removed from your venue!"));
    }

    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    @Test
    void getVenueWorkers_shouldReturnOk() throws Exception {

        List<AllUserVenueDTO> mockedList = List.of(new AllUserVenueDTO());
        Mockito.when(userVenueService.getVenueWorkers(2L)).thenReturn(mockedList);

        mockMvc.perform(get("/manageUserVenue/getVenueWorkers/2")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }

    @Test
    void setVenueManager_shouldReturnOk() throws Exception {
        doNothing().when(userVenueService).setVenueManager("mock-token", 1L, 2L);

        mockMvc.perform(put("/manageUserVenue/setVenueManager/1/2")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully set as venue manager."));
    }

    @Test
    void setVenueStaff_shouldReturnOk() throws Exception {
        doNothing().when(userVenueService).setVenueStaff("mock-token", 1L, 2L);

        mockMvc.perform(put("/manageUserVenue/setVenueStaff/1/2")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully set as venue staff."));
    }

    @Test
    void addVenueWorker_shouldReturnUnauthorized_whenTokenMissing() throws Exception {
        mockMvc.perform(post("/manageUserVenue/addVenueWorker/1/2"))
                .andExpect(status().isUnauthorized());
    }
}
