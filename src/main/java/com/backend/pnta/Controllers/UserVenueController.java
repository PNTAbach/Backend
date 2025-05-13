package com.backend.pnta.Controllers;

import com.backend.pnta.Services.UserVenue.UserVenueService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/manageUserVenue")
@RequiredArgsConstructor
public class UserVenueController {
    private final UserVenueService userVenueService;

    @Autowired
    private HttpServletRequest request;

    @PutMapping("/setVenueManager/{targetUserId}/{venueId}")
    public ResponseEntity<?> setVenueManager(@PathVariable Long targetUserId, @PathVariable Long venueId) {
        return handleSetVenueRole(targetUserId, venueId, "manager");
    }

    @PutMapping("/setVenueStaff/{userId}/{venueId}")
    public ResponseEntity<?> setVenueStaff(
            @PathVariable Long userId,
            @PathVariable Long venueId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        token = token.substring("Bearer ".length());
        userVenueService.setVenueStaff(token, userId, venueId);
        return ResponseEntity.ok("User successfully set as venue staff.");
    }
    @GetMapping("/getVenueWorkers/{venueId}")
    public ResponseEntity<?> getVenueWorkers(@PathVariable Long venueId) {
        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (token == null) {
                // Handle missing or invalid token
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
            }


            // Call service method to get venue workers
            return ResponseEntity.ok(userVenueService.getVenueWorkers( venueId));
        } catch (RuntimeException e) {
            // Log the exception
            e.printStackTrace(); // Or use logger.error(e.getMessage()) to log the error
            // Return error response
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace(); // Or use logger.error(e.getMessage()) to log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request.");
        }
    }

    @DeleteMapping("/removeVenueUser/{targetUserId}/{venueId}")
    public ResponseEntity<?> deleteVenueWorkers(@PathVariable Long targetUserId, @PathVariable Long venueId) {
        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (token == null) {
                // Handle missing or invalid token
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
            }
            userVenueService.deleteVenueWorkers(targetUserId, venueId);
            return ResponseEntity.ok("User "+targetUserId+" removed from your venue!");
        } catch (RuntimeException e) {
            // Log the exception
            e.printStackTrace(); // Or use logger.error(e.getMessage()) to log the error
            // Return error response
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace(); // Or use logger.error(e.getMessage()) to log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request.");
        }
    }
    @PostMapping("/addVenueWorker/{targetUserId}/{venueId}")
    public ResponseEntity<?> addVenueWorker(@PathVariable Long targetUserId, @PathVariable Long venueId) {
        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (token == null) {
                // Handle missing or invalid token
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
            }
            userVenueService.addVenueWorker(targetUserId, venueId);
            return ResponseEntity.ok("User "+targetUserId+" added to your venue!");
        } catch (RuntimeException e) {
            // Log the exception
            e.printStackTrace(); // Or use logger.error(e.getMessage()) to log the error
            // Return error response
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace(); // Or use logger.error(e.getMessage()) to log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request.");
        }
    }
    private ResponseEntity<?> handleSetVenueRole(Long targetUserId, Long venueId, String role) {
        // Retrieve the token from the Authorization header
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null) {
            // Handle missing or invalid token
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        // Remove "Bearer " prefix from the token
        token = token.substring("Bearer ".length());

        try {
            // Proceed with your logic to set the venue role
            if (role.equals("manager")) {
                userVenueService.setVenueManager(token, targetUserId, venueId);
            } else {
                userVenueService.setVenueStaff(token, targetUserId, venueId);
            }
            return ResponseEntity.ok("User successfully set as venue " + role + ".");
        } catch (RuntimeException e) {
            // Return error message if an exception occurs
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}