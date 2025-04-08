package com.example.quanlynhansu.controllers;

import com.example.quanlynhansu.services.WorkScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/work_schedule")
public class WorkScheduleControler {

    @Autowired
    private WorkScheduleService workScheduleService;

    @GetMapping("/display")
    public ResponseEntity<?> display(){
        return workScheduleService.displayScheduleOfCurrentWeek();
    }

    @GetMapping("/display_month/{monthYear}")
    public ResponseEntity<?> displayMonth(@PathVariable String monthYear){
        return workScheduleService.displayScheduleOfMonth(monthYear);
    }

    @GetMapping("/current_week")
    public ResponseEntity<?> currentWeek(){
        return workScheduleService.displayDayAndMonthOfCurrentWeek();
    }

}
