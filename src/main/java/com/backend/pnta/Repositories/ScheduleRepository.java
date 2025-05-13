package com.backend.pnta.Repositories;

import com.backend.pnta.Models.Venues.Schedule.Schedule;
import com.backend.pnta.Models.Venues.Venue.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    @Query("SELECT s FROM Schedule s WHERE s.venue.venueId = :venueId")
    List<Schedule> findByVenue_VenueId(Long venueId);

    @Query("SELECT s FROM Schedule s WHERE s.venue = :venue")
    List<Schedule> findByVenue(Venue venue);
    boolean existsByVenueAndWeekDay(Venue venue, int weekDay);
}
