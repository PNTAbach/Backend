package com.backend.pnta.Models.Event;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;
    @Column
    private Long venueId;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private LocalDate eventDate;
    @Column
    private LocalTime startTime;
    @Column
    private LocalTime endTime;
    @Column
    private String createdAt;

    public Event() {

    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Long getEventId() {
        return eventId;
    }
    public Long getVenueId() {
        return venueId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }
    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }


}
