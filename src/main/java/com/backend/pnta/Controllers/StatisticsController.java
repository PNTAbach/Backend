package com.backend.pnta.Controllers;

import com.backend.pnta.Services.Statistics.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Operation(summary = "Get Total Users", description = "Get the total number of users")
    @GetMapping("/totalUsers")
    public ResponseEntity<Long> getTotalUsers() {
        try {
            return ResponseEntity.ok(statisticsService.getTotalUsers());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Get Total Locations", description = "Get the total number of locations")
    @GetMapping("/totalLocations")
    public ResponseEntity<Long> getTotalLocations() {
        try {
            return ResponseEntity.ok(statisticsService.getTotalLocations());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Get Total Venues", description = "Get the total number of venues")
    @GetMapping("/totalVenues")
    public ResponseEntity<Long> getTotalVenues() {
        try {
            return ResponseEntity.ok(statisticsService.getTotalVenues());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @Operation(summary = "Get Average Venue Rating", description = "Get the average rating of all venues")
    @GetMapping("/averageVenueRating")
    public ResponseEntity<Double> getAverageVenueRating() {
        try {
            return ResponseEntity.ok(statisticsService.getAverageVenueRating());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Get Users Count By Role", description = "Get the count of users by role")
    @GetMapping("/usersCountByRole")
    public ResponseEntity<?> getUsersCountByRole() {
        try {
            return ResponseEntity.ok(statisticsService.getUsersCountByRole());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Get Most Common City", description = "Get the most common city among all venues with its frequency")
    @GetMapping("/mostCommonCity")
    public ResponseEntity<Map.Entry<String, Long>> getMostCommonCity() {
        try {
            return ResponseEntity.ok(statisticsService.getMostCommonCity());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Get Top Rated Venues", description = "Get the top N venues with the highest ratings")
    @GetMapping("/topRatedVenues/{n}")
    public ResponseEntity<List<Map.Entry<Long, Double>>> getTopRatedVenues(int n) {
        try {
            return ResponseEntity.ok(statisticsService.getTopRatedVenues(n));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
