package com.example.quanlynhansu.controllers;

import com.example.quanlynhansu.models.response.WorkHistoryResponse;
import com.example.quanlynhansu.services.WorkHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/work_history")
public class WorkHistoryController {

    @Autowired
    private WorkHistoryService workHistoryService;

    @GetMapping("/select")
    public List<WorkHistoryResponse> findAllByEmployeeId(){
        return workHistoryService.findAllByEmployeeId();
    }

}
