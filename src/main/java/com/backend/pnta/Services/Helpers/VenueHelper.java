package com.backend.pnta.Services.Helpers;

import com.backend.pnta.Models.Venues.Location.Location;
import com.backend.pnta.Models.Venues.Location.LocationDTO;
import com.backend.pnta.Models.Venues.Schedule.Schedule;
import com.backend.pnta.Models.Venues.Schedule.ScheduleDTO;
import com.backend.pnta.Models.Venues.Venue.*;
import com.backend.pnta.Repositories.LocationRepository;
import com.backend.pnta.Repositories.ScheduleRepository;
import com.backend.pnta.Repositories.VenueTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VenueHelper {
    private final VenueTypeRepository venueTypeRepository;
    private final LocationRepository locationRepository;
    private final ScheduleRepository scheduleRepository;
    public Venue convertCreateVenueDtoToVenue(CreateVenueDTO venueDTO) {
        try {
            Venue venue = new Venue();
            venue.setDescription(venueDTO.getDescription());
            venue.setName(venueDTO.getName());
            venue.setIcon(venueDTO.getIcon());
            venue.setManagerId(venueDTO.getManagerId());

            // Check if venue rating is within the valid range
            double rating = venueDTO.getRating();
            validateRatingRange(rating);
            venue.setRating(rating);

            // Validate and set price rating
            venue.setPriceRating(validatePriceRating(venueDTO.getPriceRating().toString()));

            return venue;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void validateRatingRange(double rating) {
        if (rating < 0.0 || rating > 5.0) {
            throw new IllegalArgumentException("Venue rating must be between 0.0 and 5.0");
        }
    }


    public LocationDTO convertToLocationDTO(Location location) {
        return LocationDTO.builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .city(location.getCity())
                .postalCode(location.getPostalCode())
                .country(location.getCountry())
                .address(location.getAdress())
                .build();
    }
    public Location convertLocationDtoToLocation(LocationDTO locationDTO, Venue venue) {
        Location location = new Location();
        location.setVenue(venue);
        location.setLatitude(locationDTO.getLatitude());
        location.setLongitude(locationDTO.getLongitude());
        location.setCity(locationDTO.getCity());
        location.setPostalCode(locationDTO.getPostalCode());
        location.setCountry(locationDTO.getCountry());
        location.setAdress(locationDTO.getAddress());
        return location;
    }
    public VenueType convertTypeStringToVenueType(String type, Venue venue) {
        VenueType venueType = new VenueType();
        venueType.setVenue(venue);
        venueType.setType(type);
        return venueType;
    }

    public CreateVenueDTO convertVenueToCreateVenueDto(Venue venue, List<Location> locations, List<VenueType> venueTypes) {
        CreateVenueDTO createVenueDTO = new CreateVenueDTO();
        createVenueDTO.setName(venue.getName());
        createVenueDTO.setIcon(venue.getIcon());
        createVenueDTO.setDescription(venue.getDescription());
        createVenueDTO.setManagerId(venue.getManagerId());
        createVenueDTO.setPriceRating(venue.getPriceRating());
        createVenueDTO.setRating(venue.getRating());

        // Populate list of LocationDTO objects
        List<LocationDTO> locationDTOs = new ArrayList<>();
        for (Location location : locations) {
            LocationDTO locationDTO = LocationDTO.builder()
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .city(location.getCity())
                    .postalCode(location.getPostalCode())
                    .country(location.getCountry())
                    .address(location.getAdress())
                    .build();
            locationDTOs.add(locationDTO);
        }
        createVenueDTO.setLocations(locationDTOs);

        // Populate list of venue types
        List<String> types = new ArrayList<>();
        for (VenueType venueType : venueTypes) {
            types.add(venueType.getType());
        }
        createVenueDTO.setTypes(types);

        return createVenueDTO;
    }

    private PriceRating validatePriceRating(String priceRating) {
        try {
            return PriceRating.valueOf(priceRating);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid PriceRating value: " + priceRating);
        }
    }

    public Venue updateVenueDetails(Venue venue, CreateVenueDTO createVenueDTO) {
        if (venue == null || createVenueDTO == null) {
            throw new IllegalArgumentException("Venue or createVenueDTO cannot be null");
        }

        if (createVenueDTO.getName() != null) {
            venue.setName(createVenueDTO.getName());
        }
        if (createVenueDTO.getDescription() != null) {
            venue.setDescription(createVenueDTO.getDescription());
        }
        if (createVenueDTO.getManagerId() != null) {
            venue.setManagerId(createVenueDTO.getManagerId());
        }
        if (createVenueDTO.getPriceRating() != null) {
            venue.setPriceRating(createVenueDTO.getPriceRating());
        }
        if (createVenueDTO.getRating() != 0.0) {
            venue.setRating(createVenueDTO.getRating());
        }
        if (createVenueDTO.getIcon() != null) {
            venue.setIcon(createVenueDTO.getIcon());
        }
        return venue;
    }
    public List<Location> createNewLocations(Venue updatedVenue, List<LocationDTO> locationDTOs) {
        List<Location> newLocations = new ArrayList<>();
        for (LocationDTO locationDTO : locationDTOs) {
            Location location = new Location();
            location.setVenue(updatedVenue);
            location.setAdress(locationDTO.getAddress());
            location.setCountry(locationDTO.getCountry());
            location.setCity(locationDTO.getCity());
            location.setPostalCode(locationDTO.getPostalCode());
            location.setLatitude(locationDTO.getLatitude());
            location.setLongitude(locationDTO.getLongitude());
            newLocations.add(location);
        }
        return newLocations;
    }

    public List<VenueType> createNewVenueTypes(Venue updatedVenue, List<String> venueTypeStrings) {
        List<VenueType> newVenueTypes = new ArrayList<>();

        for (String venueTypeString : venueTypeStrings) {
            VenueType venueType = new VenueType();
            venueType.setVenue(updatedVenue);
            venueType.setType(venueTypeString);
            newVenueTypes.add(venueType);
        }

        return newVenueTypes;
    }

    public ScheduleDTO convertToScheduleDTO(Schedule schedule) {
        return ScheduleDTO.builder()
                .closingTime(schedule.getClosingTime())
                .openingTime(schedule.getOpeningTime())
                .happyHour(schedule.getHappyHour())
                .weekDay(schedule.getWeekDay())
                .build();
    }

    public AllVenueInfo mapVenueToAllVenueInfo(Venue venue) {
        //location dto
        List<Location> locations = locationRepository.findByVenue_VenueId(venue.getVenueId());
        List<LocationDTO> locationDTOS=new ArrayList<>();
        for (Location location: locations
        ) {
            locationDTOS.add(convertToLocationDTO(location));
        }
        //scheduledto
        List<Schedule> schedules = scheduleRepository.findByVenue_VenueId(venue.getVenueId());
        List<ScheduleDTO> scheduleDTOS=new ArrayList<>();
        for (Schedule schedule: schedules
        ) {
            scheduleDTOS.add(convertToScheduleDTO(schedule));
        }
        //string
        List<VenueType> venueTypes=venueTypeRepository.findByVenue_VenueId(venue.getVenueId());
        List<String> types = new ArrayList<>();
        for (VenueType venueType: venueTypes
        ) {
            types.add(venueType.getType());
        }

        AllVenueInfo allVenueInfo = new AllVenueInfo();
        allVenueInfo.setVenueId(venue.getVenueId());
        allVenueInfo.setDescription(venue.getDescription());
        allVenueInfo.setName(venue.getName());
        allVenueInfo.setIcon(venue.getIcon());
        allVenueInfo.setLocations(locationDTOS);
        allVenueInfo.setSchedules(scheduleDTOS);
        allVenueInfo.setTypes(types);
        allVenueInfo.setRating(venue.getRating());
        allVenueInfo.setPriceRating(venue.getPriceRating());
        allVenueInfo.setManagerId(venue.getManagerId());
        return allVenueInfo;
    }
}
