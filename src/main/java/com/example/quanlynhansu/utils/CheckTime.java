package com.example.quanlynhansu.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class CheckTime {

    // check thời gian đã hết hạn hợp đồng chưa
    public static boolean isContractExpired(Date expiryDate) {
        LocalDate expiryLocalDate = expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return expiryLocalDate.isBefore(LocalDate.now());
    }

}
