package com.backend.pnta.Controllers;

import com.backend.pnta.Models.Event.EventCreateDTO;
import com.backend.pnta.Models.Event.EventDTO;
import com.backend.pnta.Models.Event.EventUpdateDTO;
import com.backend.pnta.Services.Event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventCreateDTO dto) {
        return ResponseEntity.ok(mapToDTO(eventService.createEvent(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @RequestBody EventUpdateDTO dto) {
        return ResponseEntity.ok(mapToDTO(eventService.updateEvent(id, dto)));
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        EventDTO event = eventService.getEventById(id);
        if (event == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(event);
    }

    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<EventDTO>> getEventsByVenueId(@PathVariable Long venueId) {
        return ResponseEntity.ok(eventService.getEventsByVenueId(venueId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok("Event deleted");
    }

    private EventDTO mapToDTO(com.backend.pnta.Models.Event.Event event) {
        EventDTO dto = new EventDTO();
        dto.setEventId(event.getEventId());
        dto.setVenueId(event.getVenueId());
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        return dto;
    }
}
