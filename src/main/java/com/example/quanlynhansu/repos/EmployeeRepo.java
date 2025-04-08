package com.example.quanlynhansu.repos;

import com.example.quanlynhansu.models.entity.ContractEntity;
import com.example.quanlynhansu.models.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<EmployeeEntity, Long> {

    List<EmployeeEntity> findAll();
    EmployeeEntity findOneByEmail(String username);
    EmployeeEntity findOneById(Long id);
    List<EmployeeEntity> findByPositionNotIn(List<String> listPosition);

}
