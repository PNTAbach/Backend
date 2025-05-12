package com.backend.pnta.Models.Venues.Venue;

import com.backend.pnta.Models.Venues.Location.LocationDTO;
import com.backend.pnta.Models.Venues.Schedule.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class CreateVenueDTO {
    private String name;
    private String icon;
    private String description;
    private PriceRating priceRating;
    private Long managerId;
    private double rating;
    private List<Schedule> schedules;

    // List of locations
    private List<LocationDTO> locations;

    // Venue type
    // List of venue types
    private List<String> types;
}
