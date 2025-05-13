package com.backend.pnta.Services.UserVenueLike;

import com.backend.pnta.Models.UserVenueLike.UserVenueLikeCreateDTO;
import com.backend.pnta.Models.UserVenueLike.UserVenueLikeDTO;

import java.util.List;

public interface UserVenueLikeService {
    void addLikeToVenue(UserVenueLikeCreateDTO userVenueLikeCreateDTO, String token);
    void removeLikeFromVenue(Long venueId,String token);
    List<UserVenueLikeDTO> getLikesFromVenue(Long venueId);
    Integer getNumberOfLikesFromVenue(Long venueId);

}
