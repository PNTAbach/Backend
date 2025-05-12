package com.backend.pnta.Controllers;

import com.backend.pnta.Models.Event.EventCreateDTO;
import com.backend.pnta.Models.Event.EventDTO;
import com.backend.pnta.Models.Event.EventUpdateDTO;
import com.backend.pnta.Services.Event.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc (addFilters = false)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    private EventDTO getMockEventDTO() {
        EventDTO dto = new EventDTO();
        dto.setEventId(1L);
        dto.setVenueId(22L);
        dto.setName("College Bash");
        dto.setDescription("Fun night");
        dto.setEventDate(LocalDate.of(2025, 5, 6));
        dto.setStartTime(LocalTime.of(17, 0));
        dto.setEndTime(LocalTime.of(22, 0));
        return dto;
    }

    @Test
    void createEvent_shouldReturnCreated() throws Exception {
        EventCreateDTO input = new EventCreateDTO();
        input.setVenueId(22L);
        input.setName("Event");
        input.setDescription("Desc");
        input.setEventDate("2025-05-06");
        input.setStartTime("17:00");
        input.setEndTime("22:00");
        Mockito.when(eventService.createEvent(any())).thenReturn(new com.backend.pnta.Models.Event.Event());

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk());
    }

    @Test
    void updateEvent_shouldReturnUpdated() throws Exception {
        EventCreateDTO input = new EventCreateDTO();
        input.setVenueId(22L);
        input.setName("Event1");
        input.setDescription("Desc1");
        input.setEventDate("2025-05-06");
        input.setStartTime("18:00");
        input.setEndTime("23:00");
        Mockito.when(eventService.updateEvent(eq(1L), any())).thenReturn(new com.backend.pnta.Models.Event.Event());

        mockMvc.perform(put("/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk());
    }

    @Test
    void getAllEvents_shouldReturnList() throws Exception {
        Mockito.when(eventService.getAllEvents()).thenReturn(List.of(getMockEventDTO()));

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void getEventById_shouldReturnEvent() throws Exception {
        Mockito.when(eventService.getEventById(1L)).thenReturn(getMockEventDTO());

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(1L));
    }

    @Test
    void getEventById_shouldReturnNotFound() throws Exception {
        Mockito.when(eventService.getEventById(1L)).thenReturn(null);

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getEventsByVenueId_shouldReturnList() throws Exception {
        Mockito.when(eventService.getEventsByVenueId(22L)).thenReturn(List.of(getMockEventDTO()));

        mockMvc.perform(get("/events/venue/22"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void deleteEvent_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/events/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Event deleted"));
    }
}
