package com.backend.pnta.Models.Venues.Venue;

import jakarta.persistence.*;

@Entity
public class VenueType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long venueTypeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venueId", referencedColumnName = "venueId")
    private Venue venue;
    @Column
    private String type;

    public VenueType() {
    }

    public VenueType(Long venueTypeId, Venue venue, String type) {
        this.venueTypeId = venueTypeId;
        this.venue = venue;
        this.type = type;
    }
    //GET SET
    public Long getVenueTypeId() {
        return venueTypeId;
    }

    public void setVenueTypeId(Long venueTypeId) {
        this.venueTypeId = venueTypeId;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
