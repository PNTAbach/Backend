package com.backend.pnta.Repositories;

import com.backend.pnta.Models.Event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByVenueId(Long venueId);
}
