package com.example.quanlynhansu.repos;

import com.example.quanlynhansu.models.entity.EmployeeEntity;
import com.example.quanlynhansu.models.entity.WorkScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface WorkScheduleRepo extends JpaRepository<WorkScheduleEntity, Long> {

    List<WorkScheduleEntity> findByEmployeeAndWorkDateBetween(EmployeeEntity employeeCurrent, LocalDate mondayOfWeek, LocalDate sundayOfWeek);

}
