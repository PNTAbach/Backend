package com.backend.pnta.Models.Venues.Venue;

import jakarta.persistence.*;

@Entity
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long venueId;
    @Column(nullable = false)
    private String name;
    @Column
    private String icon;
    @Column
    private String openHours;
    @Column
    private String closeHours;
    @Column
    private String description;
    @Enumerated(EnumType.STRING)
    private PriceRating priceRating;
    @Column
    private double rating;
    @Column
    private Long managerId;
    public Venue() {
    }

    public Venue(Long venueId, String name, String icon, String openHours, String description, PriceRating priceRating, double rating, Long managerId) {
        this.venueId = venueId;
        this.name = name;
        this.icon = icon;
        this.openHours = openHours;
        this.description = description;
        this.priceRating = priceRating;
        this.rating = rating;
        this.managerId = managerId;
    }
    //getters and setters

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getCloseHours() {
        return closeHours;
    }

    public void setCloseHours(String closeHours) {
        this.closeHours = closeHours;
    }

    public Long getVenueId() {
        return venueId;
    }
    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public PriceRating getPriceRating() {
        return priceRating;
    }
    public void setPriceRating(PriceRating priceRating) {
        this.priceRating = priceRating;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        //the rating to the nearest 0.5 increment
        double roundedRating = Math.round(rating * 2) / 2.0;

        if (roundedRating < 0 || roundedRating > 5) {
            throw new IllegalArgumentException("Rating must be between 0.0 and 5.0");
        }

        this.rating = roundedRating;
    }
    public Long getManagerId() {return managerId;}
    public void setManagerId(Long managerId) {this.managerId = managerId;}
}
