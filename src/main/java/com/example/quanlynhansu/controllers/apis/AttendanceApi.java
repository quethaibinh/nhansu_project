package com.example.quanlynhansu.controllers.apis;

import com.example.quanlynhansu.models.request.attendance.AttendanceRequest;
import com.example.quanlynhansu.services.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/attendance")
public class AttendanceApi {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/add")
    public void addAttendance(@RequestBody AttendanceRequest attendanceRequest) throws ParseException {
        attendanceService.addCheckInCheckOut(attendanceRequest);
    }

}
