package com.backend.pnta.Services.Venues.Schedule;

import com.backend.pnta.Models.Venues.Schedule.GetScheduleDTO;
import com.backend.pnta.Models.Venues.Schedule.ScheduleDTO;
import com.backend.pnta.Models.Venues.Schedule.UpdateScheduleDTO;

import java.util.List;

public interface ScheduleService {
    GetScheduleDTO saveSchedule(ScheduleDTO scheduleDTO, Long venueId);
    GetScheduleDTO updateSchedule(UpdateScheduleDTO scheduleDTO, Long scheduleId);
    void deleteSchedule(Long scheduleId);
    List<GetScheduleDTO> getScheduleByVenue(Long venueId);
    GetScheduleDTO getScheduleById(Long scheduleId);
}