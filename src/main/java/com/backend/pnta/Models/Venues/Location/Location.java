package com.backend.pnta.Models.Venues.Location;

import com.backend.pnta.Models.Venues.Venue.Venue;
import jakarta.persistence.*;

@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venueId", referencedColumnName = "venueId")
    private Venue venue;
    @Column
    private Double latitude;
    @Column
    private Double longitude;
    @Column
    private String city;
    @Column
    private String postalCode;
    @Column
    private String country;
    @Column
    private String adress;

    public Location() {
    }
    public Location(Long locationId, Venue venue, Double latitude, Double longitude, String city, String postalCode, String country, String adress) {
        this.locationId = locationId;
        this.venue = venue;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.adress = adress;
    }
    //get set

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
