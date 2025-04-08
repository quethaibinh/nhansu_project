package com.example.quanlynhansu.services;

import com.example.quanlynhansu.models.request.WorkSchedule.WorkScheduleRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WorkScheduleService {

    ResponseEntity<?> createAndUpdateWorkSchedule(List<WorkScheduleRequest> workScheduleRequests);
    ResponseEntity<?> displayScheduleOfCurrentWeek();
    ResponseEntity<?> displayScheduleOfMonth(String month);
    ResponseEntity<?> displayDayAndMonthOfCurrentWeek();

}
