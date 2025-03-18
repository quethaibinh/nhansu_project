package com.example.quanlynhansu.repos;

import com.example.quanlynhansu.models.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepo extends JpaRepository<ContractEntity, Long> {

    List<ContractEntity> findAllByEmployee_Id(Long EmployeeId);
    ContractEntity findOneById(Long id);
    ContractEntity findOneByWorkHistory_Id(Long id);

}
