package com.example.quanlynhansu.models.response;

import com.example.quanlynhansu.utils.PairClass;

import java.util.List;
import java.util.Map;

public class AttendanceResponse {

    private Map<String, List<PairClass<String, String>>> attendanceOfDay; // Map<ngày, List<giờ, status>>

    public Map<String, List<PairClass<String, String>>> getAttendanceOfDay() {
        return attendanceOfDay;
    }

    public void setAttendanceOfDay(Map<String, List<PairClass<String, String>>> attendanceOfDay) {
        this.attendanceOfDay = attendanceOfDay;
    }
}
