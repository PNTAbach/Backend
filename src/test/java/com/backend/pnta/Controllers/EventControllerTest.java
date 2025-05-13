package com.backend.pnta.Controllers;

import com.backend.pnta.Models.Event.Event;
import com.backend.pnta.Models.Event.EventCreateDTO;
import com.backend.pnta.Models.Event.EventDTO;
import com.backend.pnta.Models.Event.EventUpdateDTO;
import com.backend.pnta.Services.Event.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventService eventService;

    private EventCreateDTO createDTO;
    private EventUpdateDTO updateDTO;
    private Event event;
    private EventDTO eventDTO;

    @BeforeEach
    void setup() {
        createDTO = new EventCreateDTO();
        createDTO.setVenueId(1L);
        createDTO.setName("Concert");
        createDTO.setDescription("Live music");
        createDTO.setEventDate("2025-05-15");
        createDTO.setStartTime("18:00");
        createDTO.setEndTime("22:00");

        updateDTO = new EventUpdateDTO();
        updateDTO.setName("Updated Concert");
        updateDTO.setDescription("Updated Desc");
        updateDTO.setEventDate("2025-05-16");
        updateDTO.setStartTime("19:00");
        updateDTO.setEndTime("23:00");

        event = new Event();
        event.setEventId(1L);
        event.setVenueId(1L);
        event.setName("Concert");
        event.setDescription("Live music");
        event.setEventDate(LocalDate.of(2025, 5, 15));
        event.setStartTime(LocalTime.of(18, 0));
        event.setEndTime(LocalTime.of(22, 0));

        eventDTO = new EventDTO();
        eventDTO.setEventId(1L);
        eventDTO.setVenueId(1L);
        eventDTO.setName("Concert");
        eventDTO.setDescription("Live music");
        eventDTO.setEventDate(LocalDate.of(2025, 5, 15));
        eventDTO.setStartTime(LocalTime.of(18, 0));
        eventDTO.setEndTime(LocalTime.of(22, 0));
    }

    @Test
    void testCreateEvent() {
        when(eventService.createEvent(createDTO)).thenReturn(event);

        ResponseEntity<EventDTO> response = eventController.createEvent(createDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateEvent() {
        when(eventService.updateEvent(1L, updateDTO)).thenReturn(event);

        ResponseEntity<EventDTO> response = eventController.updateEvent(1L, updateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllEvents() {
        when(eventService.getAllEvents()).thenReturn(List.of(eventDTO));

        ResponseEntity<List<EventDTO>> response = eventController.getAllEvents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(eventDTO, response.getBody().get(0));
    }

    @Test
    void testGetEventById_found() {
        when(eventService.getEventById(1L)).thenReturn(eventDTO);

        ResponseEntity<EventDTO> response = eventController.getEventById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eventDTO, response.getBody());
    }

    @Test
    void testGetEventById_notFound() {
        when(eventService.getEventById(999L)).thenReturn(null);

        ResponseEntity<EventDTO> response = eventController.getEventById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteEvent() {
        doNothing().when(eventService).deleteEvent(1L);

        ResponseEntity<?> response = eventController.deleteEvent(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Event deleted", response.getBody());
    }
}
