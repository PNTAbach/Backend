package com.backend.pnta.Services.Event;

import com.backend.pnta.Models.Event.Event;
import com.backend.pnta.Models.Event.EventCreateDTO;
import com.backend.pnta.Models.Event.EventDTO;
import com.backend.pnta.Models.Event.EventUpdateDTO;
import com.backend.pnta.Repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    @Override
    public Event createEvent(EventCreateDTO dto) {
        Event event = new Event();
        event.setVenueId(dto.getVenueId());
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setEventDate(LocalDate.parse(dto.getEventDate()));
        event.setStartTime(LocalTime.parse(dto.getStartTime()));
        event.setEndTime(LocalTime.parse(dto.getEndTime()));
        event.setCreatedAt(LocalDateTime.now().toString());

        return eventRepository.save(event);
    }
    @Override
    public Event updateEvent(Long id, EventUpdateDTO dto) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));

        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setEventDate(LocalDate.parse(dto.getEventDate()));
        event.setStartTime(LocalTime.parse(dto.getStartTime()));
        event.setEndTime(LocalTime.parse(dto.getEndTime()));

        return eventRepository.save(event);
    }

    @Override
    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }


    @Override
    public EventDTO getEventById(Long id) {
        return eventRepository.findById(id).map(this::mapToDto).orElse(null);
    }

    @Override
    public List<EventDTO> getEventsByVenueId(Long venueId) {
        return eventRepository.findByVenueId(venueId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private EventDTO mapToDto(Event e) {
        EventDTO dto = new EventDTO();
        dto.setEventId(e.getEventId());
        dto.setVenueId(e.getVenueId());
        dto.setName(e.getName());
        dto.setDescription(e.getDescription());
        dto.setEventDate(e.getEventDate());
        dto.setStartTime(e.getStartTime());
        dto.setEndTime(e.getEndTime());
        return dto;
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

}
