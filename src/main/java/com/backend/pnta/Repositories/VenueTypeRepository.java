package com.backend.pnta.Repositories;

import com.backend.pnta.Models.Venues.Venue.VenueType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenueTypeRepository extends JpaRepository<VenueType, Long> {
    List<VenueType> findByVenue_VenueId(Long venueId);
    @Transactional
    void deleteByVenue_VenueId(Long venueId);
}
