package com.backend.pnta.Controllers;

import com.backend.pnta.Models.Venues.Schedule.GetScheduleDTO;
import com.backend.pnta.Models.Venues.Schedule.ScheduleDTO;
import com.backend.pnta.Models.Venues.Schedule.UpdateScheduleDTO;
import com.backend.pnta.Services.Venues.Schedule.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "Add Schedule", description = "Endpoint to add a new schedule.")
    @PostMapping
    public ResponseEntity<?> addSchedule(@RequestBody ScheduleDTO scheduleDTO, @RequestParam Long venueId) {
        try {
            GetScheduleDTO savedSchedule = scheduleService.saveSchedule(scheduleDTO, venueId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSchedule);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Update Schedule", description = "Endpoint to update an existing schedule.")
    @PutMapping("/{scheduleId}")
    public ResponseEntity<?> updateSchedule(@RequestBody UpdateScheduleDTO updateScheduleDTO, @PathVariable Long scheduleId) {
        try {
            GetScheduleDTO updatedSchedule = scheduleService.updateSchedule(updateScheduleDTO, scheduleId);
            return ResponseEntity.ok(updatedSchedule);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Delete Schedule", description = "Endpoint to delete a schedule by its ID.")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long scheduleId) {
        try {
            scheduleService.deleteSchedule(scheduleId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get Schedules by Venue ID", description = "Endpoint to get all schedules by venue ID.")
    @GetMapping("/venue/{venueId}")
    public ResponseEntity<?> getSchedulesByVenueId(@PathVariable Long venueId) {
        try {
            List<GetScheduleDTO> schedules = scheduleService.getScheduleByVenue(venueId);
            if (!schedules.isEmpty()) {
                return ResponseEntity.ok(schedules);
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

    @Operation(summary = "Get Schedule by ID", description = "Endpoint to get a schedule by its ID.")
    @GetMapping("/{scheduleId}")
    public ResponseEntity<?> getScheduleById(@PathVariable Long scheduleId) {
        try {
            GetScheduleDTO schedule = scheduleService.getScheduleById(scheduleId);
            if (schedule != null) {
                return ResponseEntity.ok(schedule);
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
}