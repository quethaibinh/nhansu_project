package com.example.quanlynhansu.repos;

import com.example.quanlynhansu.models.entity.EmployeeEntity;
import com.example.quanlynhansu.models.entity.PayrollEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollRepo extends JpaRepository<PayrollEntity, Long> {

    Boolean existsByEmployeeAndMonth(EmployeeEntity user, String month);
    PayrollEntity findByMonthAndEmployee(String month, EmployeeEntity employee);

}
