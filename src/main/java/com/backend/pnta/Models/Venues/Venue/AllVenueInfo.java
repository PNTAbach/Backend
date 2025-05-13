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
public class AllVenueInfo {
    //entity
    private Long venueId;
    //entity
    private String name;
    //entity
    private String icon;
    //entity
    private String description;
    //entity
    private PriceRating priceRating;
    //entity
    private double rating;
    //entity
    private Long managerId;
    //non-entity
    private List<LocationDTO> locations;
    private List<String> types;
    private List<ScheduleDTO> schedules;
}
