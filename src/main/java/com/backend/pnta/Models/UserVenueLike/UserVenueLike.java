package com.backend.pnta.Models.UserVenueLike;

import com.backend.pnta.Models.User.UserP;
import com.backend.pnta.Models.Venues.Venue.Venue;
import jakarta.persistence.*;

@Entity
public class UserVenueLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userVenueLikeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private UserP userP;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venueId", referencedColumnName = "venueId")
    private Venue venue;
    @Enumerated(EnumType.STRING)
    private UserVenueLikeType type;

    public UserVenueLike() {
    }

    public UserVenueLike(Long userVenueLikeId, UserP userP, Venue venue, UserVenueLikeType type) {
        this.userVenueLikeId = userVenueLikeId;
        this.userP = userP;
        this.venue = venue;
        this.type = type;
    }

    public Long getUserVenueLikeId() {
        return this.userVenueLikeId;
    }

    public void setUserVenueLikeId(Long userVenueLikeId) {
        this.userVenueLikeId = userVenueLikeId;
    }

    public UserP getUserP() {
        return userP;
    }

    public void setUserP(UserP userP) {
        this.userP = userP;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public UserVenueLikeType getType() {
        return type;
    }

    public void setType(UserVenueLikeType type) {
        this.type = type;
    }
}
