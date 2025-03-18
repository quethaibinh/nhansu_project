package com.example.quanlynhansu.models.request.attendance;

import java.util.Date;

public class AttendanceCheckOutRequest {

    private Date checkOut;
    private Long employeeID;

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public Long getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Long employeeID) {
        this.employeeID = employeeID;
    }
}
