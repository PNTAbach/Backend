package com.backend.pnta.Services.Helpers;

import com.backend.pnta.Models.User.UserP;
import com.backend.pnta.Models.UserVenue.UserVenue;
import com.backend.pnta.Models.UserVenue.UserVenueDTO;
import com.backend.pnta.Models.UserVenue.VenueUserRole;
import com.backend.pnta.Models.Venues.Venue.Venue;
import com.backend.pnta.Repositories.UserRepository;
import com.backend.pnta.Repositories.UserVenueRepository;
import com.backend.pnta.Repositories.VenueRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserVenueHelper {
    private final UserVenueRepository userVenueRepository;
    private final VenueRepository venueRepository;
    private final UserRepository userRepository;
    private final AuthHelper authHelper;

    @Transactional
    public boolean isUserVenueRole(Long userId, Long venueId, VenueUserRole role) {
        return userVenueRepository.findByUserP_UserIdAndVenue_VenueIdAndRole(userId, venueId, role).isPresent();
    }

    @Transactional
    public void verifyAdminPermissions(String token, Long venueId) {
        Long userIdFromToken = authHelper.getUserIdFromToken(token);
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new EntityNotFoundException("Venue not found with id: " + venueId));

        if (!userIdFromToken.equals(venue.getManagerId())) {
            throw new RuntimeException("User does not have admin permissions for this venue");
        }
    }

    @Transactional
    public void setUserRole(Long userId, Long venueId, VenueUserRole role) {
        // Check if the user is already assigned to the venue with a different role
        UserVenue existingUserVenue = userVenueRepository.findByUserP_UserIdAndVenue_VenueId(userId, venueId)
                .orElse(null);
        if (existingUserVenue != null) {
            existingUserVenue.setRole(role);
            userVenueRepository.save(existingUserVenue);
        } else {
            // Create a new UserVenue entity and assign the role
            UserP user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
            Venue venue = venueRepository.findById(venueId)
                    .orElseThrow(() -> new EntityNotFoundException("Venue not found with id: " + venueId));

            UserVenue newUserVenue = new UserVenue();
            newUserVenue.setUserP(user);
            newUserVenue.setVenue(venue);
            newUserVenue.setRole(role);
            userVenueRepository.save(newUserVenue);
        }
    }

    public UserVenue convertToDto(UserVenueDTO userVenueDTO) {
        UserVenue userVenue = new UserVenue();
        Optional<UserP> userP = userRepository.findById(userVenueDTO.getUserId());
        Optional<Venue> venue = venueRepository.findById(userVenueDTO.getVenueId());

        if (userP.isPresent() && venue.isPresent()) {
            userVenue.setRole(userVenueDTO.getRole());
            userVenue.setUserP(userP.get());
            userVenue.setVenue(venue.get());
        } else {
            // Handle the case where either UserP or Venue is not found
            throw new RuntimeException("User or Venue not found");
        }

        return userVenue;
    }
}