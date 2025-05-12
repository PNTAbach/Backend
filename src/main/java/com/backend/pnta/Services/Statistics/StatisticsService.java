package com.backend.pnta.Services.Statistics;

import com.backend.pnta.Models.User.Role;

import java.util.List;
import java.util.Map;

public interface StatisticsService {

    // Get total number of users
    long getTotalUsers();

    // Get total number of locations
    long getTotalLocations();


    // Get total number of venues
    long getTotalVenues();

    // Get average rating of all venues
    double getAverageVenueRating();

    // Get number of users by role
    Map<Role, Long> getUsersCountByRole();

    // Get the most common city among all venues with its frequency
    Map.Entry<String, Long> getMostCommonCity();

    // Get the top N venues with the highest ratings
    List<Map.Entry<Long, Double>> getTopRatedVenues(int n);
}