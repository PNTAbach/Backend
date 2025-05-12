package com.backend.pnta.Models.Event;

import lombok.Data;

@Data
public class EventUpdateDTO {
    private String name;
    private String description;
    private String eventDate;
    private String startTime;
    private String endTime;
}
