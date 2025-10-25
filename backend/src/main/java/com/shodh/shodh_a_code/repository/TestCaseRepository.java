package com.shodh.shodh_a_code.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.shodh.shodh_a_code.model.TestCase;

@Repository
public interface TestCaseRepository extends MongoRepository<TestCase, String> {
    List<TestCase> findByProblemId(String problemId);

    List<TestCase> findByProblemIdAndIsHidden(String problemId, boolean isHidden);
}
