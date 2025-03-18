package com.example.quanlynhansu.controllers.apis;

import com.example.quanlynhansu.models.response.WorkHistoryResponse;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/work_history")
public class WorkHistoryApi {

    @PutMapping("/termination_of_contract") // api chấm dứt hợp đồng trước thời hạn.
    public WorkHistoryResponse terminationOfContract(){
        return null;
    }

}
