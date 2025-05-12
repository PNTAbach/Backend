package com.backend.pnta.Models.UserVenue;

import com.backend.pnta.Models.User.UserP;
import com.backend.pnta.Models.Venues.Venue.Venue;
import jakarta.persistence.*;

@Entity
public class UserVenue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userVenueId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private UserP userP;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venueId", referencedColumnName = "venueId")
    private Venue venue;
    @Enumerated(EnumType.STRING)
    private VenueUserRole role;

    public UserVenue() {
    }

    public UserVenue(Long userVenueId, UserP userP, Venue venue, VenueUserRole role) {
        this.userVenueId = userVenueId;
        this.userP = userP;
        this.venue = venue;
        this.role = role;
    }

    public Long getUserVenueId() {
        return userVenueId;
    }

    public void setUserVenueId(Long userVenueId) {
        this.userVenueId = userVenueId;
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

    public VenueUserRole getRole() {
        return role;
    }

    public void setRole(VenueUserRole role) {
        this.role = role;
    }
}
