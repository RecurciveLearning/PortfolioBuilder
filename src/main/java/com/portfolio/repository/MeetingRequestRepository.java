package com.portfolio.repository;

import com.portfolio.entity.MeetingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MeetingRequestRepository extends JpaRepository<MeetingRequest, Long> {
    List<MeetingRequest> findByStatus(String status);
}
