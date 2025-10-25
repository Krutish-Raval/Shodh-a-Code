package com.shodh.shodh_a_code.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.shodh.shodh_a_code.model.Problem;

@Repository
public interface ProblemRepository extends MongoRepository<Problem, String> {
    List<Problem> findByContestId(String contestId);

    List<Problem> findByDifficulty(String difficulty);

    List<Problem> findByContestIdAndDifficulty(String contestId, String difficulty);
}
