package com.backend.pnta.Models.Venues.Venue;

import com.backend.pnta.Models.Venues.Location.LocationDTO;
import com.backend.pnta.Models.Venues.Schedule.ScheduleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VenueDTO {
    private String name;
    private String icon;
    private String openHours;

    private String closeHours;
    private String description;
    private PriceRating priceRating;
    private double rating;
    private Long managerId;
    private List<ScheduleDTO> schedules;
    private List<LocationDTO> locations;
    private List<String> types;
}