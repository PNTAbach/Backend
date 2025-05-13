package com.backend.pnta.Services.UserVenue;

import com.backend.pnta.Models.UserVenue.AllUserVenueDTO;
import com.backend.pnta.Models.UserVenue.VenueUserRole;

import java.util.List;

public interface UserVenueService {
    void setVenueManager(String token, Long targetUserId, Long venueId);
    void setVenueStaff(String token,Long targetUserId, Long venueId);
    Long getUserVenue(Long userId);
    VenueUserRole getRoleOfUserInVenue(Long userId, Long venueId);
    boolean isUserpartOfVenue(Long userId,Long venueId);
    List<AllUserVenueDTO> getVenueWorkers(Long venueId);

    void deleteVenueWorkers(Long targetUserId, Long venueId);

    void addVenueWorker(Long targetUserId, Long venueId);
}
