package com.example.quanlynhansu.repos;

import com.example.quanlynhansu.models.entity.EmployeeEntity;
import com.example.quanlynhansu.models.entity.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface NoteRepo extends JpaRepository<NoteEntity, Long> {

    NoteEntity findByEmployeeAndWorkDateAndCa(EmployeeEntity employee, LocalDate workDate, String ca);

}
