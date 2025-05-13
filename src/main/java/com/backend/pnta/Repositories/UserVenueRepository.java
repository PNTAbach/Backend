package com.backend.pnta.Repositories;

import com.backend.pnta.Models.UserVenue.UserPAndRoleVenueDTO;
import com.backend.pnta.Models.UserVenue.UserVenue;
import com.backend.pnta.Models.UserVenue.VenueUserRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserVenueRepository extends JpaRepository<UserVenue, Long> {
    Optional<UserVenue> findByUserP_UserIdAndVenue_VenueId(Long userId, Long venueId);
    Optional<UserVenue> findByUserP_UserIdAndVenue_VenueIdAndRole(Long userId, Long venueId, VenueUserRole role);
    @Transactional
    void deleteByVenue_VenueId(Long venueId);
    @Query("SELECT uv.userP.userId FROM UserVenue uv WHERE uv.venue.venueId = :venueId")
    List<Long> getAllUsersIdsWhereVenueId(Long venueId);
    @Query("SELECT uv.venue.venueId FROM UserVenue uv WHERE uv.userP.userId = :userId")
    Optional<Long> findVenueIdByUserId(Long userId);

    @Query("SELECT NEW com.backend.pnta.Models.UserVenue.UserPAndRoleVenueDTO(uv.userP, uv.role) FROM UserVenue uv WHERE uv.venue.venueId = :venueId")
    List<UserPAndRoleVenueDTO> getAllUsersFromVenue(Long venueId);
    @Modifying
    @Query("DELETE FROM UserVenue uv WHERE uv.userP.userId = :targetUserId AND uv.venue.venueId = :venueId")
    void deleteUserFromVenue(@Param("targetUserId") Long targetUserId, @Param("venueId") Long venueId);
}
