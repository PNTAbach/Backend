package com.backend.pnta.Repositories;

import com.backend.pnta.Models.Venues.Venue.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface VenueRepository extends JpaRepository<Venue, Long>, JpaSpecificationExecutor<Venue> {
    Optional<Venue> findByManagerId(Long userId);
}
