package com.example.quanlynhansu.controllers;


import com.example.quanlynhansu.services.PayrollService;
import jakarta.mail.MessagingException;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/payroll")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @PostMapping("/summary")
    public void summary() throws MessagingException {
        payrollService.calculateScore();
    }

    @GetMapping("/payroll_last_month")
    public ResponseEntity<?> payrollOfLastMonth(){
        return payrollService.payrollOfLastMonth();
    }

    @GetMapping("/history_calculate_score/{startTime}/{endTime}")
    public ResponseEntity<?> history(@PathVariable LocalDate startTime,
                                     @PathVariable LocalDate endTime){
        return payrollService.historyCalculateScore(startTime, endTime);
    }

}
