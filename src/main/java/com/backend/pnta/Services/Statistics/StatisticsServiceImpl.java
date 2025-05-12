package com.backend.pnta.Services.Statistics;

import com.backend.pnta.Models.User.Role;
import com.backend.pnta.Models.Venues.Venue.Venue;
import com.backend.pnta.Models.Venues.Location.Location;
import com.backend.pnta.Repositories.LocationRepository;
import com.backend.pnta.Repositories.UserRepository;
import com.backend.pnta.Repositories.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final UserRepository userRepository;
    private final VenueRepository venueRepository;
    private final LocationRepository locationRepository;

    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public long getTotalLocations() {
        return locationRepository.count();
    }


    @Override
    public long getTotalVenues() {
        return venueRepository.count();
    }

    @Override
    public double getAverageVenueRating() {
        // Get all venues from the repository
        List<Venue> venues = venueRepository.findAll();

        // Calculate average rating only if there are venues
        if (!venues.isEmpty()) {
            // Filter out venues with null ratings, map to double values, and calculate average
            return venues.stream()
                    .mapToDouble(Venue::getRating)
                    .average()
                    .orElse(0.0);
        } else {
            // If no venues are present, return 0.0
            return 0.0;
        }
    }

    @Override
    public Map<Role, Long> getUsersCountByRole() {
        try {
            return userRepository.countUsersByRole().stream()
                    .collect(Collectors.toMap(
                            arr -> (Role) arr[0],
                            arr -> (Long) arr[1]
                    ));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }
    @Override
    public Map.Entry<String, Long> getMostCommonCity() {
        List<Map.Entry<String, Long>> citiesWithCounts = locationRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Location::getCity, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return citiesWithCounts.stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
    }

    @Override
    public List<Map.Entry<Long, Double>> getTopRatedVenues(int n) {
        return venueRepository.findAll()
                .stream()
                .map(v -> new AbstractMap.SimpleEntry<>(v.getVenueId(), v.getRating()))
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(n)
                .collect(Collectors.toList());
    }
}
