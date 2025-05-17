package com.backend.pnta.Controllers;

import com.backend.pnta.Models.Venues.Venue.*;
import com.backend.pnta.Services.Venues.Venue.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/venue")
@RequiredArgsConstructor
public class VenueController {
    private final VenueService venueService;
    private final HttpServletRequest request;

    @PostMapping("/filter")
    public ResponseEntity<List<AllVenueInfo>> getAllVenuesByFilter(@RequestBody VenueFilterDTO filterDTO) {
        try {
            List<AllVenueInfo> filteredVenues = venueService.getAllVenuesByFilter(filterDTO);
            return ResponseEntity.ok(filteredVenues);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(summary = "Get Venue Info by Token", description = "Endpoint to get information about a venue associated with the user token.")
    @GetMapping("/byToken")
    public ResponseEntity<?> getVenueByToken() {
        try {
            // Retrieve the token from the request header
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (token == null || !token.startsWith("Bearer ")) {
                System.out.println("Token is missing or does not start with Bearer.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
            }

            // Remove "Bearer " prefix from the token
            token = token.substring("Bearer ".length());
            System.out.println("Received token: " + token);

            // Call the service method to get venue info by token
            AllVenueInfo venueInfo = venueService.getVenueByToken(token);

            // Log the returned venue info
            System.out.println("Fetched venue info: " + venueInfo);

            // Return the venue info
            return ResponseEntity.ok(venueInfo);
        } catch (RuntimeException e) {
            System.out.println("Runtime exception occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(summary = "Get Venue Info", description = "Endpoint to get all information about a venue by its ID.")
    @GetMapping("/{venueId}")
    public ResponseEntity<?> getVenueInfo(@PathVariable Long venueId) {
        try {
            AllVenueInfo venueInfo = venueService.getAllVenueInfoById(venueId);
            if (venueInfo != null) {
                return ResponseEntity.ok(venueInfo);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Operation(summary = "Add Venue Type", description = "Endpoint to add a new venue type to a venue.")
    @PostMapping("/{venueId}/type")
    public ResponseEntity<?> addVenueType(@PathVariable Long venueId, @RequestBody VenueTypeDTO venueType) {
        try {
            VenueType addedVenueType = venueService.addVenueType(venueId, venueType);
            return ResponseEntity.status(HttpStatus.CREATED).body("Added type "+addedVenueType.getType()+" to venue "+venueId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @Operation(summary = "Get All Venues", description = "Endpoint to get all venues.")
    @GetMapping
    public ResponseEntity<List<AllVenueInfo>> getAllVenues() {
        try {
            List<AllVenueInfo> venues = venueService.getAllVenues();
            if (!venues.isEmpty()) {
                return ResponseEntity.ok(venues);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Add Venue", description = "Endpoint to add a new venue.")
    @PostMapping
    public ResponseEntity<Object> addVenue(@RequestBody CreateVenueDTO createVenueDTO) {
        try {
            CreateVenueDTO venue = venueService.saveVenue(createVenueDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(venue);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @Operation(summary = "Remove Venue", description = "Endpoint to remove a venue by its ID.")
    @DeleteMapping("/{venueId}")
    public ResponseEntity<?> removeVenue(@PathVariable Long venueId) {
        // Authorization aspect will intercept this method call
        try {
            venueService.deleteVenueById(venueId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException  e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @Operation(summary = "Update Venue", description = "Endpoint to update a venue.")
    @PutMapping("/{venueId}")
    public ResponseEntity<?> updateVenue(@PathVariable Long venueId, @RequestBody CreateVenueDTO createVenueDTO) {
        try {
            CreateVenueDTO updatedVenue = venueService.updateVenue(venueId, createVenueDTO);
            return ResponseEntity.ok(updatedVenue);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}