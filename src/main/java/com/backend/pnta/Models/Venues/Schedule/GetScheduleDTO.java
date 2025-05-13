package com.backend.pnta.Models.Venues.Schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetScheduleDTO {
    private Long scheduleId;
    private Long venueId;
    private int weekDay;
    private String happyHour;
    private String openingTime;
    private String closingTime;
}
