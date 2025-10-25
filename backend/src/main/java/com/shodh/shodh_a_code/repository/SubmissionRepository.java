package com.shodh.shodh_a_code.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.shodh.shodh_a_code.model.Submission;

@Repository
public interface SubmissionRepository extends MongoRepository<Submission, String> {
    List<Submission> findByUserId(String userId);

    List<Submission> findByProblemId(String problemId);

    List<Submission> findByContestId(String contestId);

    List<Submission> findByUserIdAndContestId(String userId, String contestId);

    List<Submission> findByUserIdAndProblemId(String userId, String problemId);

    List<Submission> findByStatus(Submission.SubmissionStatus status);

    List<Submission> findByUserIdAndContestIdOrderBySubmittedAtDesc(String userId, String contestId);
}
