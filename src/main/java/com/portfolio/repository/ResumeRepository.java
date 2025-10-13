package com.portfolio.repository;

import com.portfolio.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Resume findTopByOrderByUpdatedAtDesc();
}
