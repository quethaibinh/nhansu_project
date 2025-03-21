package com.example.quanlynhansu.models.request.attendance;

import java.util.Date;

public class AttendanceRequest {

    private String time; // 'hh:mm yyyy/MM/dd'
    private Long employeeId;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
