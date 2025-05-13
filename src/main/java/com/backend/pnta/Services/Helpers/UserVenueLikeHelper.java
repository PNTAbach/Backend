package com.backend.pnta.Services.Helpers;

import com.backend.pnta.Models.UserVenueLike.UserVenueLike;
import com.backend.pnta.Models.UserVenueLike.UserVenueLikeDTO;

import java.util.List;
import java.util.stream.Collectors;

public class UserVenueLikeHelper {
    public UserVenueLikeDTO toDto(UserVenueLike userVenueLike) {
        return UserVenueLikeDTO.builder()
                .userVenueLikeId(userVenueLike.getUserVenueLikeId())
                .userId(userVenueLike.getUserP().getUserId())
                .venueId(userVenueLike.getVenue().getVenueId())
                .type(userVenueLike.getType())
                .build();
    }

    public List<UserVenueLikeDTO> toDtoList(List<UserVenueLike> userVenueLikes) {
        return userVenueLikes.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
