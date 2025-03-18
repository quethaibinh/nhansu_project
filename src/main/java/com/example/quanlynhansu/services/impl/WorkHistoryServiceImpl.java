package com.example.quanlynhansu.services.impl;

import com.example.quanlynhansu.converters.WorkHistoryConverterResponseEntity;
import com.example.quanlynhansu.models.entity.EmployeeEntity;
import com.example.quanlynhansu.models.entity.WorkHistoryEntity;
import com.example.quanlynhansu.models.request.workHistory.WorkHistoryRequest;
import com.example.quanlynhansu.models.response.WorkHistoryResponse;
import com.example.quanlynhansu.repos.EmployeeRepo;
import com.example.quanlynhansu.repos.WorkHistoryRepo;
import com.example.quanlynhansu.services.WorkHistoryService;
import com.example.quanlynhansu.services.securityService.InfoCurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkHistoryServiceImpl implements WorkHistoryService {

    @Autowired
    private WorkHistoryRepo workHistoryRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private InfoCurrentUserService infoCurrentUserService;

    @Autowired
    private WorkHistoryConverterResponseEntity workHistoryConverterResponseEntity;


    @Override
    public List<WorkHistoryResponse> findAllByEmployeeId() {

        String username = infoCurrentUserService.getCurrentUsername();
        EmployeeEntity employeeEntity = employeeRepo.findOneByEmail(username);

        if(employeeEntity != null){
            List<WorkHistoryEntity> workHistoryEntities = workHistoryRepo.findAllByEmployee_Id(employeeEntity.getId());
            List<WorkHistoryResponse> responses = new ArrayList<>();
            for(WorkHistoryEntity workHistoryEntity : workHistoryEntities){
                responses.add(workHistoryConverterResponseEntity.entityToResponse(workHistoryEntity));
            }
            return responses;
        }

        return null;
    }

    @Override // dùng để chấm dứt hợp đồng, khơi tạo, gia hạn hợp đồng
    public void addOrUpdateWorkHistory(WorkHistoryRequest workHistoryRequest) throws ParseException {
        WorkHistoryEntity workHistoryEntity = workHistoryConverterResponseEntity.requestToEntity(workHistoryRequest);
        workHistoryRepo.save(workHistoryEntity);
    }
}
