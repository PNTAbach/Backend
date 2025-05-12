package com.backend.pnta.Models.Venues.Venue;

import com.backend.pnta.Models.Venues.Schedule.ScheduleFilterDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VenueFilterDTO {
    private String name;
    private String city;
    private String country;
    private ScheduleFilterDTO scheduleFilterDTO;
    private PriceRating priceRating;
    private Double minRating;
    private Double maxRating;
    private List<String> types;
}
