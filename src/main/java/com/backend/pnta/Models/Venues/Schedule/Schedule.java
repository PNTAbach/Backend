package com.backend.pnta.Models.Venues.Schedule;

import com.backend.pnta.Models.Venues.Venue.Venue;
import jakarta.persistence.*;

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venueId", referencedColumnName = "venueId")
    private Venue venue;
    @Column
    private int weekDay;
    @Column
    private String happyHour;
    @Column
    private String openingTime;
    @Column
    private String closingTime;

    public Schedule() {
    }
    public Schedule(Long scheduleId, Venue venue, int weekDay, String happyHour, String openingTime, String closingTime) {
        this.scheduleId = scheduleId;
        this.venue = venue;
        this.weekDay = weekDay;
        this.happyHour = happyHour;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public String getHappyHour() {
        return happyHour;
    }

    public void setHappyHour(String happyHour) {
        this.happyHour = happyHour;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }
}
