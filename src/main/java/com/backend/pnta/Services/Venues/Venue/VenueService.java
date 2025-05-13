package com.backend.pnta.Services.Venues.Venue;

import com.backend.pnta.Models.Venues.Venue.*;

import java.util.List;

public interface VenueService {
    AllVenueInfo getAllVenueInfoById(Long venueId);
    AllVenueInfo getVenueByToken(String token);
    List<AllVenueInfo> getAllVenues();
    VenueType addVenueType(Long venueId, VenueTypeDTO venueType);
    void deleteVenueById(Long venueId);
    CreateVenueDTO saveVenue(CreateVenueDTO createVenueDTO);
    CreateVenueDTO updateVenue(Long venueId, CreateVenueDTO createVenueDTO);
    Long getIdOfVenueOwner(Long venueId);
    List<AllVenueInfo> getAllVenuesByFilter(VenueFilterDTO filterDTO);


}