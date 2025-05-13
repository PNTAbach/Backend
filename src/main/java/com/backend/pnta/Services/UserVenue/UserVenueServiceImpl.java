package com.backend.pnta.Services.UserVenue;

import com.backend.pnta.Models.UserVenue.*;
import com.backend.pnta.Repositories.UserVenueRepository;
import com.backend.pnta.Services.Helpers.UserHelper;
import com.backend.pnta.Services.Helpers.UserVenueHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserVenueServiceImpl implements UserVenueService{
    private final UserVenueHelper userVenueHelper;
    private final UserVenueRepository userVenueRepository;
    private final UserHelper userHelper;

    @Override
    public void setVenueManager(String token, Long targetUserId, Long venueId) {
        setUserRoleWithPermissionCheck(token, targetUserId, venueId, VenueUserRole.VENUE_MANAGER_ROLE);
    }

    @Override
    public void setVenueStaff(String token, Long targetUserId, Long venueId) {
        setUserRoleWithPermissionCheck(token, targetUserId, venueId, VenueUserRole.VENUE_STAFF_ROLE);
    }
    @Override
    public Long getUserVenue(Long userId) {
        //findVenueIdByUserId to get the venueId for the given user
        Optional<Long> venueIdOptional = userVenueRepository.findVenueIdByUserId(userId);

        if (venueIdOptional.isPresent()) {
            return venueIdOptional.get();
        } else {
            throw new RuntimeException("User is not associated with any venue");
        }
    }
    @Override
    public VenueUserRole getRoleOfUserInVenue(Long userId, Long venueId) {
        // Query the database to find the user's role in the specified venue
        Optional<UserVenue> userVenueOptional = userVenueRepository.findByUserP_UserIdAndVenue_VenueId(userId, venueId);

        if (userVenueOptional.isPresent()) {
            UserVenue userVenue = userVenueOptional.get();
            VenueUserRole role = userVenue.getRole();

            if (role != null) {
                return role;
            } else {
                throw new RuntimeException("Role not defined for user in this venue");
            }
        } else {
            throw new RuntimeException("User is not associated with this venue");
        }
    }

    @Override
    public boolean isUserpartOfVenue(Long userId, Long venueId) {
        // Query the database to find if the user is associated with the venue
        Optional<UserVenue> userVenue = userVenueRepository.findByUserP_UserIdAndVenue_VenueId(userId, venueId);

        if (userVenue.isPresent()) {
            return true; // User is associated with the venue
        } else {
            return false;
        }
    }
    @Override
    public List<AllUserVenueDTO> getVenueWorkers(Long venueId) {
        try {
            List<UserPAndRoleVenueDTO> userEntities = userVenueRepository.getAllUsersFromVenue(venueId);

            // Convert user entities to user DTOs using userHelper.convertToDto method
            List<AllUserVenueDTO> userDtos = userEntities.stream()
                    .map(userHelper::convertToAllUserVenueDto)
                    .collect(Collectors.toList());

            return userDtos;
        } catch (Exception e) {
            // Handle exceptions, such as invalid token or lack of admin permissions
            e.printStackTrace(); // Example: Print stack trace
            return Collections.emptyList(); // Return an empty list or handle error response accordingly
        }
    }

    @Transactional
    public void deleteVenueWorkers(Long targetUserId, Long venueId){
        try {
            if (!userVenueRepository.findVenueIdByUserId(targetUserId).isPresent()) {
                throw new RuntimeException("User does not exist in the venue");
            }
            userVenueRepository.deleteUserFromVenue(targetUserId, venueId);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to delete venue workers", e);
        }
    }

    @Override
    public void addVenueWorker(Long targetUserId, Long venueId) {
        try {
            if (userVenueRepository.findVenueIdByUserId(targetUserId).isPresent()) {
                throw new RuntimeException("User is already associated with a venue");
            }

            UserVenueDTO userVenueDTO = new UserVenueDTO(targetUserId, venueId, VenueUserRole.VENUE_STAFF_ROLE);
            UserVenue userVenue = userVenueHelper.convertToDto(userVenueDTO);
            System.out.println("User ID: " + userVenue.getUserP().getUserId() + " Venue ID: " + userVenue.getVenue().getVenueId());
            userVenueRepository.save(userVenue);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to add venue worker", e);
        }
    }


    private void setUserRoleWithPermissionCheck(String token, Long targetUserId, Long venueId, VenueUserRole role) {
        userVenueHelper.verifyAdminPermissions(token, venueId); // Verify if the requester has admin permissions for the venue
        // Check if the user already has the specified role for the venue
        if (userVenueHelper.isUserVenueRole(targetUserId, venueId, role)) {
            throw new RuntimeException("User is already a " + role.name() + " for this venue");
        }
        userVenueHelper.setUserRole(targetUserId, venueId, role);
    }


}
