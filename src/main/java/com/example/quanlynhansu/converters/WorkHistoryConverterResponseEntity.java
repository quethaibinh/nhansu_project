package com.example.quanlynhansu.converters;

import com.example.quanlynhansu.controllers.WorkHistoryController;
import com.example.quanlynhansu.models.entity.EmployeeEntity;
import com.example.quanlynhansu.models.entity.WorkHistoryEntity;
import com.example.quanlynhansu.models.request.workHistory.WorkHistoryRequest;
import com.example.quanlynhansu.models.response.WorkHistoryResponse;
import com.example.quanlynhansu.repos.ContractRepo;
import com.example.quanlynhansu.repos.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class WorkHistoryConverterResponseEntity {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private ContractRepo contractRepo;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public WorkHistoryResponse entityToResponse(WorkHistoryEntity workHistoryEntity){

        WorkHistoryResponse response = new WorkHistoryResponse();
        response.setId(workHistoryEntity.getId());
        response.setDepartment(workHistoryEntity.getDepartment());
        response.setPosition(workHistoryEntity.getPosition());
        response.setStartDate(sdf.format(workHistoryEntity.getStartDate()));
        response.setEndDate(sdf.format(workHistoryEntity.getEndDate()));

        return response;
    }

    public WorkHistoryRequest entityToRequest(WorkHistoryEntity workHistoryEntity){

        WorkHistoryRequest workHistoryRequest = new WorkHistoryRequest();
        workHistoryRequest.setId(workHistoryEntity.getId());
        workHistoryRequest.setDepartment(workHistoryEntity.getDepartment());
        workHistoryRequest.setPosition(workHistoryEntity.getPosition());
        workHistoryRequest.setStartDate(sdf.format(workHistoryEntity.getStartDate()));
        workHistoryRequest.setEndDate(sdf.format(workHistoryEntity.getEndDate()));
        workHistoryRequest.setEmployeeId(workHistoryEntity.getEmployee().getId());

        return workHistoryRequest;
    }

    public WorkHistoryEntity requestToEntity(WorkHistoryRequest workHistoryRequest) throws ParseException {

        List<WorkHistoryEntity> list = new ArrayList<>();
        WorkHistoryEntity workHistoryEntity = new WorkHistoryEntity();
        workHistoryEntity.setId(workHistoryRequest.getId());
        workHistoryEntity.setDepartment(workHistoryRequest.getDepartment());
        workHistoryEntity.setPosition(workHistoryRequest.getPosition());
        workHistoryEntity.setStartDate(sdf.parse(workHistoryRequest.getStartDate()));
        workHistoryEntity.setEndDate(sdf.parse(workHistoryRequest.getEndDate()));
        if(workHistoryEntity.getId() != null) workHistoryEntity.setContract(contractRepo.findOneByWorkHistory_Id(workHistoryRequest.getId()));

        EmployeeEntity employeeEntity = employeeRepo.findOneById(workHistoryRequest.getEmployeeId());
        if(employeeEntity != null){
            List<WorkHistoryEntity> workHistoryList = employeeEntity.getWorkHistory();
            if (workHistoryEntity.getId() != null) { // Xóa lịch sử làm việc cũ trong TH cập nhật lịch sử.
                workHistoryList.removeIf(workHistory -> workHistory.getId().equals(workHistoryEntity.getId()));
            }
            workHistoryList.add(workHistoryEntity);
            employeeEntity.setWorkHistory(workHistoryList);
            workHistoryEntity.setEmployee(employeeEntity);
            return workHistoryEntity;
        }

        return null;
    }

}
