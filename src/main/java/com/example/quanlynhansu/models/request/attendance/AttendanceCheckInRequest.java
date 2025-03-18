package com.example.quanlynhansu.models.request.attendance;

import java.util.Date;

public class AttendanceCheckInRequest {

    private Date checkIn;
    private Long employeeId;

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
