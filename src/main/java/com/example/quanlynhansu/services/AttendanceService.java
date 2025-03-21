package com.example.quanlynhansu.services;

import com.example.quanlynhansu.models.request.attendance.AttendanceRequest;
import com.example.quanlynhansu.models.response.AttendanceResponse;

import java.text.ParseException;

public interface AttendanceService {

    void addCheckInCheckOut(AttendanceRequest attendanceRequest) throws ParseException;
    AttendanceResponse displayAttendance(Long employeeId);

}
