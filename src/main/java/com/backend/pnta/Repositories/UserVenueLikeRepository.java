package com.backend.pnta.Repositories;

import com.backend.pnta.Models.User.UserP;
import com.backend.pnta.Models.UserVenueLike.UserVenueLike;
import com.backend.pnta.Models.Venues.Venue.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserVenueLikeRepository extends JpaRepository<UserVenueLike, Long> {
    List<UserVenueLike> findByVenue(Venue venue);
    Integer countByVenue(Venue venue);
    boolean existsByUserPAndVenue(UserP userP, Venue venue);
    @Query("SELECT uvl FROM UserVenueLike uvl WHERE uvl.venue = :venue AND uvl.userP = :userP")
    Optional<UserVenueLike> findByUserAndVenue(UserP userP, Venue venue);

}
