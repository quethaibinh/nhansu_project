package com.example.quanlynhansu.repos;

import com.example.quanlynhansu.models.entity.CalculateScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculateScoreRepo extends JpaRepository<CalculateScoreEntity, Long> {



}
