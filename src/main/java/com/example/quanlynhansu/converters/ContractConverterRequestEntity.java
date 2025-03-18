package com.example.quanlynhansu.converters;

import com.example.quanlynhansu.models.entity.ContractEntity;
import com.example.quanlynhansu.models.entity.EmployeeEntity;
import com.example.quanlynhansu.models.request.contract.ContractRequest;
import com.example.quanlynhansu.models.response.ContractResponse;
import com.example.quanlynhansu.repos.EmployeeRepo;
import com.example.quanlynhansu.repos.WorkHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class ContractConverterRequestEntity {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private WorkHistoryRepo workHistoryRepo;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public ContractEntity requestToEntity(ContractRequest contractRequest) throws ParseException {

        List<ContractEntity> list = new ArrayList<>();
        ContractEntity contractEntity = new ContractEntity();
        contractEntity.setId(contractRequest.getId());
        contractEntity.setContractType(contractRequest.getContractType());
        contractEntity.setDepartment(contractRequest.getDepartment());
        contractEntity.setDocumentUrl(contractRequest.getDocumentUrl());
        contractEntity.setSalary(contractRequest.getSalary());
        contractEntity.setStartDate(sdf.parse(contractRequest.getStartDate()));
        contractEntity.setEndDate(sdf.parse(contractRequest.getEndDate()));
        if(contractEntity.getId() != null) contractEntity.setWorkHistory(workHistoryRepo.findOneByContract_Id(contractRequest.getId()));

        EmployeeEntity employeeEntity = employeeRepo.findOneById(contractRequest.getEmployeeId());
        if(employeeEntity != null){
            contractEntity.setEmployee(employeeEntity);
            list = employeeEntity.getContracts();
            if (contractEntity.getId() != null) { // Xóa hợp đồng cũ trong TH cập nhật hợp đồng.
                list.removeIf(contract -> contract.getId().equals(contractEntity.getId()));
            }
            list.add(contractEntity);
            employeeEntity.setContracts(list);
            return contractEntity;
        }

        return null;
    }

    public ContractResponse entityToResponse(ContractEntity contractEntity){

        ContractResponse contractResponse = new ContractResponse();
        contractResponse.setId(contractEntity.getId());
        contractResponse.setContractType(contractEntity.getContractType());
        contractResponse.setDepartment(contractEntity.getDepartment());
        contractResponse.setSalary(contractEntity.getSalary());
        contractResponse.setDocumentUrl(contractEntity.getDocumentUrl());
        contractResponse.setStartDate(sdf.format(contractEntity.getStartDate()));
        contractResponse.setEndDate(sdf.format(contractEntity.getEndDate()));
        return contractResponse;

    }

}
