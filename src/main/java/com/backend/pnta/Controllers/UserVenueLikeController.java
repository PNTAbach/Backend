package com.backend.pnta.Controllers;

import com.backend.pnta.Models.UserVenueLike.UserVenueLikeCreateDTO;
import com.backend.pnta.Models.UserVenueLike.UserVenueLikeDTO;
import com.backend.pnta.Services.UserVenueLike.UserVenueLikeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user-venue-like")
@RequiredArgsConstructor
public class UserVenueLikeController {
    private final UserVenueLikeService userVenueLikeService;
    @Autowired
    private HttpServletRequest request;

    @Operation(summary = "Add Like to Venue", description = "Endpoint to add a like to a venue.")
    @PostMapping("/like")
    public ResponseEntity<?> addLikeToVenue(@RequestBody UserVenueLikeCreateDTO userVenueLikeCreateDTO) {
        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
            }
            token = token.substring("Bearer ".length());
            userVenueLikeService.addLikeToVenue(userVenueLikeCreateDTO, token);
            return ResponseEntity.status(HttpStatus.CREATED).body("Like added successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the like");
        }
    }

    @Operation(summary = "Remove Like from Venue", description = "Endpoint to remove a like from a venue by its ID.")
    @DeleteMapping("/like/{venueId}") // <-- Include the path variable for userVenueLikeId
    public ResponseEntity<?> removeLikeFromVenue(@PathVariable Long venueId) { // <-- Add path variable for userVenueLikeId
        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
            }
            token = token.substring("Bearer ".length());
            userVenueLikeService.removeLikeFromVenue(venueId, token); // <-- Pass userVenueLikeId
            return ResponseEntity.ok("Like removed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while removing the like");
        }
    }


    @Operation(summary = "Get Likes from Venue", description = "Endpoint to get all likes from a venue by its ID.")
    @GetMapping("/likes/{venueId}")
    public ResponseEntity<?> getLikesFromVenue(@PathVariable Long venueId) {
        try {
            List<UserVenueLikeDTO> likes = userVenueLikeService.getLikesFromVenue(venueId);
            if (!likes.isEmpty()) {
                return ResponseEntity.ok(likes);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No likes found for the specified venue");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching likes");
        }
    }

    @Operation(summary = "Get Number of Likes from Venue", description = "Endpoint to get the number of likes from a venue by its ID.")
    @GetMapping("/likes/count/{venueId}")
    public ResponseEntity<?> getNumberOfLikesFromVenue(@PathVariable Long venueId) {
        try {
            Integer count = userVenueLikeService.getNumberOfLikesFromVenue(venueId);
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while counting likes");
        }
    }
}
