package com.backend.pnta.Repositories;

import com.backend.pnta.Models.Venues.Location.Location;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByVenue_VenueId(Long venueId);
    @Transactional
    void deleteByVenue_VenueId(Long venueId);
}
