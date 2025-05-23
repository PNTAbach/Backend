package com.backend.pnta.Services.UserVenueLike;

import com.backend.pnta.Models.User.UserP;
import com.backend.pnta.Models.UserVenueLike.UserVenueLike;
import com.backend.pnta.Models.UserVenueLike.UserVenueLikeCreateDTO;
import com.backend.pnta.Models.UserVenueLike.UserVenueLikeDTO;
import com.backend.pnta.Models.UserVenueLike.UserVenueLikeType;
import com.backend.pnta.Models.Venues.Venue.Venue;
import com.backend.pnta.Repositories.UserRepository;
import com.backend.pnta.Repositories.UserVenueLikeRepository;
import com.backend.pnta.Repositories.VenueRepository;
import com.backend.pnta.Services.Helpers.AuthHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserVenueLikeServiceImpl implements UserVenueLikeService {
    private final UserVenueLikeRepository userVenueLikeRepository;
    private final AuthHelper authHelper;
    private final UserRepository userRepository;
    private final VenueRepository venueRepository;

    @Override
    public void addLikeToVenue(UserVenueLikeCreateDTO userVenueLikeCreateDTO, String token) {
        //check the like type
        if (userVenueLikeCreateDTO.getType() == null || !EnumSet.allOf(UserVenueLikeType.class).contains(userVenueLikeCreateDTO.getType())) {
            throw new IllegalArgumentException("Invalid like type");
        }
        long userId =authHelper.getUserIdFromToken(token);
        UserP userP = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Venue venue = venueRepository.findById(userVenueLikeCreateDTO.getVenueId())
                .orElseThrow(() -> new IllegalArgumentException("Venue not found"));


        boolean alreadyLiked = userVenueLikeRepository.existsByUserPAndVenue(userP, venue);
        if (alreadyLiked) {
            throw new IllegalStateException("User has already liked this venue");
        }

        // Create and save the new like
        UserVenueLike userVenueLike = new UserVenueLike();
        userVenueLike.setUserP(userP);
        userVenueLike.setVenue(venue);
        userVenueLike.setType(userVenueLikeCreateDTO.getType());
        userVenueLikeRepository.save(userVenueLike);
    }


    @Override
    public void removeLikeFromVenue(Long venueId, String token) {
        long userId = authHelper.getUserIdFromToken(token);
        UserP userP = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("Venue not found"));

        // Find the user's like for the specified venue
        UserVenueLike userVenueLike = userVenueLikeRepository.findByUserAndVenue(userP, venue)
                .orElseThrow(() -> new IllegalArgumentException("Like not found"));

        // Delete the like from the repository
        userVenueLikeRepository.delete(userVenueLike);
    }


    @Override
    public List<UserVenueLikeDTO> getLikesFromVenue(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("Venue not found"));
        List<UserVenueLike> likes = userVenueLikeRepository.findByVenue(venue);
        return toDtoList(likes);
    }

    @Override
    public Integer getNumberOfLikesFromVenue(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("Venue not found"));
        return userVenueLikeRepository.countByVenue(venue);
    }
    private UserVenueLikeDTO toDto(UserVenueLike userVenueLike) {
        return UserVenueLikeDTO.builder()
                .userVenueLikeId(userVenueLike.getUserVenueLikeId())
                .userId(userVenueLike.getUserP().getUserId())
                .venueId(userVenueLike.getVenue().getVenueId())
                .type(userVenueLike.getType())
                .build();
    }

    private List<UserVenueLikeDTO> toDtoList(List<UserVenueLike> userVenueLikes) {
        return userVenueLikes.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
