package com.example.quanlynhansu.services;

import com.example.quanlynhansu.models.entity.CalculateScoreEntity;
import com.example.quanlynhansu.models.entity.EmployeeEntity;
import com.example.quanlynhansu.repos.CalculateScoreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayrollService {

    @Autowired
    private CalculateScoreRepo calculateScoreRepo;

    public void saveCalculateScore(Long plusScore,
                                   Long minusScore,
                                   String reason,
                                   EmployeeEntity employee){
        CalculateScoreEntity calculateScoreEntity = new CalculateScoreEntity();
        calculateScoreEntity.setPlusScore(plusScore);
        calculateScoreEntity.setMinusScore(minusScore);
        calculateScoreEntity.setReason(reason);
        calculateScoreEntity.setEmployee(employee);
        calculateScoreRepo.save(calculateScoreEntity);
    }

}
