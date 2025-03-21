package com.example.quanlynhansu.repos;

import com.example.quanlynhansu.models.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Repository
public interface AttendanceRepo extends JpaRepository<AttendanceEntity, Long> {

    AttendanceEntity findTopByEmployeeIdOrderByTimeStampDesc(Long employeeId);
    List<AttendanceEntity> findAllByEmployee_Id(Long employeeId);

}
