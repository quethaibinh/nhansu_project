package com.example.quanlynhansu.utils;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Component
public class CheckTime {

    // check thời gian đã hết hạn hợp đồng chưa
    public boolean isContractExpired(Date expiryDate) {
        LocalDate expiryLocalDate = expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return expiryLocalDate.isBefore(LocalDate.now());
    }

    // check đã qua ngày chấm công mới chưa
    public boolean isAttendanceOfNewDay(String oldDate, String newDate) {
        try {
            // Chuyển String thành LocalDate
            LocalDate attendanceOldDate = LocalDate.parse(oldDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            LocalDate attendanceNewDate = LocalDate.parse(newDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));

            // So sánh với ngày hiện tại
            return attendanceOldDate.isBefore(attendanceNewDate);
        } catch (DateTimeParseException e) {
            System.err.println("Lỗi parse ngày: " + e.getMessage());
            return false; // Nếu có lỗi parse, coi như ngày không hợp lệ
        }
    }


}
