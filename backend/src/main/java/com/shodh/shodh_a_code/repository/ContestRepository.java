package com.shodh.shodh_a_code.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.shodh.shodh_a_code.model.Contest;

@Repository
public interface ContestRepository extends MongoRepository<Contest, String> {
    List<Contest> findByStatus(Contest.ContestStatus status);

    List<Contest> findByStatusOrderByStartTimeAsc(Contest.ContestStatus status);
}
