package com.example.project_BE.project_BE.repository;

import com.example.project_BE.project_BE.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    @Query(value = "SELECT * FROM admin WHERE username = :username", nativeQuery = true)
    Optional<Admin> findByUsername(String username);

    @Query(value = "SELECT * FROM admin WHERE email = :email", nativeQuery = true)
    Optional<Admin> findByEmail(String email);
}
