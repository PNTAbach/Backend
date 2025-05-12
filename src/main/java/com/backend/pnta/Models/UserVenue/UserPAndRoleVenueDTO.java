package com.backend.pnta.Models.UserVenue;

import com.backend.pnta.Models.User.UserP;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPAndRoleVenueDTO {
    private UserP userP;
    private VenueUserRole venueRole;
}
