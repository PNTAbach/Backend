package com.backend.pnta.Services.Venues.Venue;

import com.backend.pnta.Models.Venues.Location.Location;
import com.backend.pnta.Models.Venues.Location.LocationDTO;
import com.backend.pnta.Models.Venues.Schedule.Schedule;
import com.backend.pnta.Models.Venues.Venue.*;
import com.backend.pnta.Repositories.*;
import com.backend.pnta.Services.Helpers.AuthHelper;
import com.backend.pnta.Services.Helpers.VenueHelper;
import com.backend.pnta.Services.UserVenue.UserVenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {
    private final VenueRepository venueRepository;
    private final LocationRepository locationRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserVenueRepository userVenueRepository;
    private final VenueTypeRepository venueTypeRepository;
    private final VenueHelper venueHelper;
    private final AuthHelper authHelper;
    private final UserVenueService userVenueService;

    @Override
    public AllVenueInfo getAllVenueInfoById(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found with id: " + venueId));

        return venueHelper.mapVenueToAllVenueInfo(venue);
    }

    @Override
    public AllVenueInfo getVenueByToken(String token) {
        Long userId = authHelper.getUserIdFromToken(token);
        Optional<Venue> venueOptional = venueRepository.findByManagerId(userId);
        if (venueOptional.isPresent()) {
            // If the venue is found, retrieve its ID and return the venue information
            System.out.println("testt->>>>."+venueOptional.get().getVenueId());
            Long venueId = venueOptional.get().getVenueId();
            return getAllVenueInfoById(venueId);
        } else {
            // If the user is not associated with any venue as a manager,
            // try to retrieve the venue where the user is associated in any role
            Long userVenueId = userVenueService.getUserVenue(userId);
            if (userVenueId != null) {
                // If the user is associated with a venue in any role, return the venue information
                return getAllVenueInfoById(userVenueId);
            } else {
                // If the user is not associated with any venue, throw an exception
                throw new RuntimeException("Venue not found for the manager ID: " + userId);
            }
        }
    }


    @Override
    public List<AllVenueInfo> getAllVenues() {
        // Retrieve all venues from the repository
        List<Venue> venues = venueRepository.findAll();

        // Initialize a list to store AllVenueInfo objects
        List<AllVenueInfo> allVenuesInfo = new ArrayList<>();

        // Iterate through each venue and create AllVenueInfo objects
        for (Venue venue : venues) {
            // Retrieve information related to the venue
            List<Location> locations = locationRepository.findByVenue_VenueId(venue.getVenueId());
            List<Schedule> schedules = scheduleRepository.findByVenue_VenueId(venue.getVenueId());
            List<VenueType> venueTypes = venueTypeRepository.findByVenue_VenueId(venue.getVenueId());

            // Create AllVenueInfo object and add it to the list
            AllVenueInfo venueInfo = AllVenueInfo.builder()
                    .name(venue.getName())
                    .schedules(schedules.stream().map(venueHelper::convertToScheduleDTO).collect(Collectors.toList()))
                    .venueId(venue.getVenueId())
                    .icon(venue.getIcon())
                    .description(venue.getDescription())
                    .priceRating(venue.getPriceRating())
                    .managerId(venue.getManagerId())
                    .rating(venue.getRating())
                    .locations(locations.stream().map(venueHelper::convertToLocationDTO).collect(Collectors.toList()))
                    .types(venueTypes.stream().map(VenueType::getType).collect(Collectors.toList()))
                    .build();

            allVenuesInfo.add(venueInfo);
        }

        return allVenuesInfo;
    }

    @Override
    public VenueType addVenueType(Long venueId, VenueTypeDTO venueType) {
        // Retrieve the venue based on the provided venueId
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found with id: " + venueId));

        // Create a new VenueType entity
        VenueType newVenueType = new VenueType();
        newVenueType.setVenue(venue);
        newVenueType.setType(venueType.getType());

        // Save the new VenueType entity
        return venueTypeRepository.save(newVenueType);
    }
    @Override
    public void deleteVenueById(Long venueId) {
        //this will delete all locations, venue types
        try {
            // Delete corresponding venue types
            venueTypeRepository.deleteByVenue_VenueId(venueId);
            // Delete corresponding locations
            locationRepository.deleteByVenue_VenueId(venueId);
            //delte uservenue relations
            userVenueRepository.deleteByVenue_VenueId(venueId);

            // Delete the venue
            venueRepository.deleteById(venueId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public CreateVenueDTO updateVenue(Long venueId, CreateVenueDTO createVenueDTO) {
        try {
            Venue existingVenue = venueRepository.findById(venueId)
                    .orElseThrow(() -> new RuntimeException("Venue not found"));

            //update venue details
            existingVenue = venueHelper.updateVenueDetails(existingVenue, createVenueDTO);

            //delete existing locations
            List<Location> existingLocations = locationRepository.findByVenue_VenueId(venueId);
            locationRepository.deleteAll(existingLocations);

            //delete existing venue types
            List<VenueType> existingVenueTypes = venueTypeRepository.findByVenue_VenueId(venueId);
            venueTypeRepository.deleteAll(existingVenueTypes);

            //save updated venue
            Venue updatedVenue = venueRepository.save(existingVenue);

            //add new locations
            List<Location> newLocations = venueHelper.createNewLocations(updatedVenue, createVenueDTO.getLocations());
            locationRepository.saveAll(newLocations);

            // Add new venue types
            List<VenueType> newVenueTypes = venueHelper.createNewVenueTypes(updatedVenue,createVenueDTO.getTypes());
            venueTypeRepository.saveAll(newVenueTypes);

            //create a new dto to return it to client
            return venueHelper.convertVenueToCreateVenueDto(updatedVenue, newLocations, newVenueTypes);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid enum value provided in VenueDTO: " + e.getMessage());
        }
    }

    @Override
    public Long getIdOfVenueOwner(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found with id: " + venueId));

        Long managerId = venue.getManagerId();
        if (managerId == null) {
            throw new RuntimeException("Venue does not have a manager assigned");
        }

        return managerId;
    }

    @Override
    public List<AllVenueInfo> getAllVenuesByFilter(VenueFilterDTO filterDTO) {
        Specification<Venue> spec = Specification.where(null);

        if (filterDTO.getName() != null && !filterDTO.getName().isEmpty()) {
            spec = spec.and(VenueSpecifications.nameContains(filterDTO.getName()));
        }
        if (filterDTO.getCity() != null && !filterDTO.getCity().isEmpty()) {
            spec = spec.and(VenueSpecifications.cityEquals(filterDTO.getCity()));
        }
        if (filterDTO.getCountry() != null && !filterDTO.getCountry().isEmpty()) {
            spec = spec.and(VenueSpecifications.countryEquals(filterDTO.getCountry()));
        }
        if (filterDTO.getPriceRating() != null) {
            spec = spec.and(VenueSpecifications.priceRatingEquals(filterDTO.getPriceRating()));
        }
        if (filterDTO.getMinRating() != null) {
            spec = spec.and(VenueSpecifications.ratingGreaterThanOrEqualTo(filterDTO.getMinRating()));
        }
        if (filterDTO.getMaxRating() != null) {
            spec = spec.and(VenueSpecifications.ratingLessThanOrEqualTo(filterDTO.getMaxRating()));
        }
        if (filterDTO.getTypes() != null && !filterDTO.getTypes().isEmpty() && filterDTO.getTypes().get(0)!="") {
            spec = spec.and(VenueSpecifications.typesIn(filterDTO.getTypes()));
        }
        if (filterDTO.getScheduleFilterDTO() != null) {
            if (filterDTO.getScheduleFilterDTO().getWeekDay() != -1) {
                spec = spec.and(VenueSpecifications.scheduleWeekDayEquals(filterDTO.getScheduleFilterDTO().getWeekDay()));
            }
            if (filterDTO.getScheduleFilterDTO().getTime() != null && !filterDTO.getScheduleFilterDTO().getTime().isEmpty()) {
                spec = spec.and(VenueSpecifications.scheduleIsOpen(filterDTO.getScheduleFilterDTO().getTime()));
            }

        }
        List<Venue> venues = venueRepository.findAll(spec);
        List<AllVenueInfo> venuesReturn = new ArrayList<>();
        for (Venue venue : venues) {
            venuesReturn.add(venueHelper.mapVenueToAllVenueInfo(venue));
        }
        return venuesReturn;
    }


    @Override
    public CreateVenueDTO saveVenue(CreateVenueDTO createVenueDTO) {
        try {
            // Convert CreateVenueDTO to Venue entity
            Venue venue = venueRepository.save(venueHelper.convertCreateVenueDtoToVenue(createVenueDTO));
            // Convert each LocationDTO to Location entity and save them
            List<Location> locations = new ArrayList<>();
            for (LocationDTO locationDTO : createVenueDTO.getLocations()) {
                Location location = locationRepository.save(venueHelper.convertLocationDtoToLocation(locationDTO, venue));
                locations.add(location);
            }

            // Convert each type string to VenueType entity and save them
            List<VenueType> venueTypes = new ArrayList<>();
            for (String type : createVenueDTO.getTypes()) {
                VenueType venueType = venueTypeRepository.save(venueHelper.convertTypeStringToVenueType(type, venue));
                venueTypes.add(venueType);
            }

            // Retrieve the saved venue, locations, and venue types from the database
            venue = venueRepository.findById(venue.getVenueId())
                    .orElseThrow(() -> new RuntimeException("Venue not found"));

            // Return the updated CreateVenueDTO
            return venueHelper.convertVenueToCreateVenueDto(venue, locations, venueTypes);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cant create venue: " + e.getMessage());
        }
    }
}
