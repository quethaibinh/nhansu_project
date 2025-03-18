package com.example.quanlynhansu.repos;

import com.example.quanlynhansu.models.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepo extends JpaRepository<AccountEntity, Long> {
    AccountEntity findByUsername(String username);
}
