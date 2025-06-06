package com.backend.pnta.Models.UserVenueLike;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVenueLikeDTO {
    private Long userVenueLikeId;
    private Long userId;
    private Long venueId;
    private UserVenueLikeType type;
}
