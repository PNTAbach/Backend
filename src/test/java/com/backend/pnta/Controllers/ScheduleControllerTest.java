package com.backend.pnta.Controllers;

import com.backend.pnta.Models.Venues.Schedule.GetScheduleDTO;
import com.backend.pnta.Models.Venues.Schedule.ScheduleDTO;
import com.backend.pnta.Models.Venues.Schedule.UpdateScheduleDTO;
import com.backend.pnta.Services.Venues.Schedule.ScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testuser", roles = {"USER"})
public class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addSchedule_shouldReturnCreated() throws Exception {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        GetScheduleDTO response = new GetScheduleDTO();
        Mockito.when(scheduleService.saveSchedule(any(), anyLong())).thenReturn(response);

        mockMvc.perform(post("/schedules")
                        .param("venueId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateSchedule_shouldReturnOk() throws Exception {
        UpdateScheduleDTO updateDTO = new UpdateScheduleDTO();
        GetScheduleDTO response = new GetScheduleDTO();
        Mockito.when(scheduleService.updateSchedule(any(), eq(1L))).thenReturn(response);

        mockMvc.perform(put("/schedules/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteSchedule_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/schedules/1"))
                .andExpect(status().isOk());
        Mockito.verify(scheduleService).deleteSchedule(1L);
    }

    @Test
    void getSchedulesByVenueId_shouldReturnOk() throws Exception {
        List<GetScheduleDTO> list = List.of(new GetScheduleDTO());
        Mockito.when(scheduleService.getScheduleByVenue(1L)).thenReturn(list);

        mockMvc.perform(get("/schedules/venue/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getSchedulesByVenueId_shouldReturnNotFound() throws Exception {
        Mockito.when(scheduleService.getScheduleByVenue(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/schedules/venue/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getScheduleById_shouldReturnOk() throws Exception {
        GetScheduleDTO schedule = new GetScheduleDTO();
        Mockito.when(scheduleService.getScheduleById(1L)).thenReturn(schedule);

        mockMvc.perform(get("/schedules/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getScheduleById_shouldReturnNotFound() throws Exception {
        Mockito.when(scheduleService.getScheduleById(1L)).thenReturn(null);

        mockMvc.perform(get("/schedules/1"))
                .andExpect(status().isNotFound());
    }
}
