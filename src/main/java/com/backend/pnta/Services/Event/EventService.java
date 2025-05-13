package com.backend.pnta.Services.Event;

import com.backend.pnta.Models.Event.Event;
import com.backend.pnta.Models.Event.EventCreateDTO;
import com.backend.pnta.Models.Event.EventDTO;
import com.backend.pnta.Models.Event.EventUpdateDTO;

import java.util.List;

public interface EventService {
    Event createEvent(EventCreateDTO dto);
    Event updateEvent(Long eventId, EventUpdateDTO dto);
    EventDTO getEventById(Long eventId);
    List<EventDTO> getAllEvents();
    void deleteEvent(Long eventId);
    List<EventDTO> getEventsByVenueId(Long venueId);

}
