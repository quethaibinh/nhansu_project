package com.example.quanlynhansu.repos;

import com.example.quanlynhansu.models.entity.CalculateScoreEntity;
import com.example.quanlynhansu.models.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CalculateScoreRepo extends JpaRepository<CalculateScoreEntity, Long> {

    List<CalculateScoreEntity> findAllByEmployeeAndCreatedAtBetween(EmployeeEntity employee, LocalDateTime startTime, LocalDateTime endTime);

}
