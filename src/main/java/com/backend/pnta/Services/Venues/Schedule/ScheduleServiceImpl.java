package com.backend.pnta.Services.Venues.Schedule;

import com.backend.pnta.Models.Venues.Schedule.GetScheduleDTO;
import com.backend.pnta.Models.Venues.Schedule.Schedule;
import com.backend.pnta.Models.Venues.Schedule.ScheduleDTO;
import com.backend.pnta.Models.Venues.Schedule.UpdateScheduleDTO;
import com.backend.pnta.Models.Venues.Venue.Venue;
import com.backend.pnta.Repositories.ScheduleRepository;

import com.backend.pnta.Repositories.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final VenueRepository venueRepository;

    @Override
    public GetScheduleDTO saveSchedule(ScheduleDTO scheduleDTO, Long venueId) {
        validateWeekDay(scheduleDTO.getWeekDay());
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found"));

        if (scheduleRepository.existsByVenueAndWeekDay(venue, scheduleDTO.getWeekDay())) {
            throw new RuntimeException("Schedule for this weekday already exists for this venue");
        }

        Schedule schedule = new Schedule();
        schedule.setVenue(venue);
        schedule.setWeekDay(scheduleDTO.getWeekDay());
        schedule.setHappyHour(scheduleDTO.getHappyHour());
        schedule.setOpeningTime(scheduleDTO.getOpeningTime());
        schedule.setClosingTime(scheduleDTO.getClosingTime());
        scheduleRepository.save(schedule);
        return mapToGetScheduleDTO(schedule);
    }

    @Override
    public GetScheduleDTO updateSchedule(UpdateScheduleDTO scheduleDTO, Long scheduleId) {
        validateWeekDay(scheduleDTO.getWeekDay());
        Venue venue = venueRepository.findById(scheduleDTO.getVenueId())
                .orElseThrow(() -> new RuntimeException("Venue not found"));

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (schedule.getWeekDay() != scheduleDTO.getWeekDay() &&
                scheduleRepository.existsByVenueAndWeekDay(venue, scheduleDTO.getWeekDay())) {
            throw new RuntimeException("Schedule for this weekday already exists for this venue");
        }

        schedule.setVenue(venue);
        schedule.setWeekDay(scheduleDTO.getWeekDay());
        schedule.setHappyHour(scheduleDTO.getHappyHour());
        schedule.setOpeningTime(scheduleDTO.getOpeningTime());
        schedule.setClosingTime(scheduleDTO.getClosingTime());
        scheduleRepository.save(schedule);
        return mapToGetScheduleDTO(schedule);
    }

    @Override
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        scheduleRepository.delete(schedule);
    }

    @Override
    public List<GetScheduleDTO> getScheduleByVenue(Long venueId) {
        venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found"));
        List<Schedule> schedules = scheduleRepository.findByVenue_VenueId(venueId);
        return schedules.stream().map(this::mapToGetScheduleDTO).collect(Collectors.toList());
    }

    @Override
    public GetScheduleDTO getScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        return mapToGetScheduleDTO(schedule);
    }

    private GetScheduleDTO mapToGetScheduleDTO(Schedule schedule) {
        return GetScheduleDTO.builder()
                .weekDay(schedule.getWeekDay())
                .happyHour(schedule.getHappyHour())
                .openingTime(schedule.getOpeningTime())
                .closingTime(schedule.getClosingTime())
                .venueId(schedule.getVenue().getVenueId())
                .scheduleId(schedule.getScheduleId())
                .build();
    }
    private void validateWeekDay(int weekDay) {
        if (weekDay < 0 || weekDay > 6) {
            throw new IllegalArgumentException("Weekday must be between 0 and 6");
        }
    }
}