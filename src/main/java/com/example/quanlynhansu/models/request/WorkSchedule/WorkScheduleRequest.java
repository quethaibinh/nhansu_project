package com.example.quanlynhansu.models.request.WorkSchedule;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class WorkScheduleRequest {

    @JsonFormat(pattern = "yyyy-MM-dd") // nếu không có hsonnnFormat thì mặc định của nó nhận vào cũng là yyyy-MM-dd
    private LocalDate workDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime; // nếu không có JsonFormat thì mặc định của localTime sẽ là "HH:mm" nếu ss = 00 hoặc là "HH:mm:ss"

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private String location;
    private String note;

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
