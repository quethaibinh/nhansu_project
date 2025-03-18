package com.example.quanlynhansu.repos;

import com.example.quanlynhansu.models.entity.WorkHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkHistoryRepo extends JpaRepository<WorkHistoryEntity, Long> {

    List<WorkHistoryEntity> findAllByEmployee_Id(Long employeeId);
    WorkHistoryEntity findOneById(Long id);
    WorkHistoryEntity findOneByContract_Id(Long id);

}
