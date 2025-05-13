package com.backend.pnta.Models.Venues.Venue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VenueTypeDTO {
    private String type;
}
