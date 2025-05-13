package com.backend.pnta.Models.UserVenue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVenueDTO {
    private Long userId;
    private Long venueId;
    private VenueUserRole role;
}
