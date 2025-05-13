package com.backend.pnta.Controllers;

import com.backend.pnta.Models.Venues.Schedule.GetScheduleDTO;
import com.backend.pnta.Models.Venues.Schedule.ScheduleDTO;
import com.backend.pnta.Models.Venues.Schedule.UpdateScheduleDTO;
import com.backend.pnta.Services.Venues.Schedule.ScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleControllerTest {

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    private final ScheduleDTO scheduleDTO = new ScheduleDTO();
    private final UpdateScheduleDTO updateDTO = new UpdateScheduleDTO();
    private final GetScheduleDTO responseDTO = new GetScheduleDTO();

    @Test
    void addSchedule_shouldReturnCreated() {
        when(scheduleService.saveSchedule(eq(scheduleDTO), eq(1L))).thenReturn(responseDTO);

        ResponseEntity<?> response = scheduleController.addSchedule(scheduleDTO, 1L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void updateSchedule_shouldReturnOk() {
        when(scheduleService.updateSchedule(eq(updateDTO), eq(1L))).thenReturn(responseDTO);

        ResponseEntity<?> response = scheduleController.updateSchedule(updateDTO, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void deleteSchedule_shouldReturnOk() {
        doNothing().when(scheduleService).deleteSchedule(1L);

        ResponseEntity<?> response = scheduleController.deleteSchedule(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void getSchedulesByVenueId_shouldReturnOk() {
        List<GetScheduleDTO> list = List.of(responseDTO);
        when(scheduleService.getScheduleByVenue(1L)).thenReturn(list);

        ResponseEntity<?> response = scheduleController.getSchedulesByVenueId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
    }

    @Test
    void getSchedulesByVenueId_shouldReturnNotFound() {
        when(scheduleService.getScheduleByVenue(1L)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = scheduleController.getSchedulesByVenueId(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void getScheduleById_shouldReturnOk() {
        when(scheduleService.getScheduleById(1L)).thenReturn(responseDTO);

        ResponseEntity<?> response = scheduleController.getScheduleById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getScheduleById_shouldReturnNotFound() {
        when(scheduleService.getScheduleById(1L)).thenReturn(null);

        ResponseEntity<?> response = scheduleController.getScheduleById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }
}
