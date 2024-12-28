package com.example.project_BE.project_BE.repository;

import com.example.project_BE.project_BE.model.Buket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuketRepository extends JpaRepository<Buket, Long> {
    List<Buket> findByAdminId(Long idAdmin);
}
