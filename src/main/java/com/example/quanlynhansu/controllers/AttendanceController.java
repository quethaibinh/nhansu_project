package com.example.quanlynhansu.controllers;

import com.example.quanlynhansu.models.response.AttendanceResponse;
import com.example.quanlynhansu.services.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/display/{id}") // admin
    public AttendanceResponse displayAttendanceByAdmin(@PathVariable Long id){ // id = null => current user, != null => admin, manager
        return attendanceService.displayAttendance(id);
    }

    @GetMapping("/display") // current user
    public AttendanceResponse displayMyAttendance(){
        return attendanceService.displayAttendance(null);
    }

}
