package com.example.quanlynhansu.controllers.apis;

import com.example.quanlynhansu.models.request.WorkSchedule.WorkScheduleRequest;
import com.example.quanlynhansu.services.WorkScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/work_schedule")
public class WorkScheduleApi {

    @Autowired
    private WorkScheduleService workScheduleService;


    @PostMapping("/create")
    public ResponseEntity<?> createSchedule(@RequestBody List<WorkScheduleRequest> workScheduleRequest){
        return workScheduleService.createAndUpdateWorkSchedule(workScheduleRequest);
    }


}
