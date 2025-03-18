package com.example.quanlynhansu.services;

import com.example.quanlynhansu.models.request.workHistory.WorkHistoryRequest;
import com.example.quanlynhansu.models.response.WorkHistoryResponse;

import java.text.ParseException;
import java.util.List;

public interface WorkHistoryService {

    List<WorkHistoryResponse> findAllByEmployeeId();
    void addOrUpdateWorkHistory(WorkHistoryRequest workHistoryRequest) throws ParseException;

}
