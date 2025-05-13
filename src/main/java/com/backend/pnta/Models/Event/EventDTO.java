package com.backend.pnta.Models.Event;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class EventDTO {
    private Long eventId;
    private Long venueId;
    private String name;
    private String description;
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime createdAt;
}
