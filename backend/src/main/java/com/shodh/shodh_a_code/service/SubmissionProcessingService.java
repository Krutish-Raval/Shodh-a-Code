package com.shodh.shodh_a_code.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.shodh.shodh_a_code.model.Submission;
import com.shodh.shodh_a_code.model.TestCase;
import com.shodh.shodh_a_code.repository.SubmissionRepository;
import com.shodh.shodh_a_code.repository.TestCaseRepository;

@Service
public class SubmissionProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(SubmissionProcessingService.class);

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private LocalExecutionService localExecutionService;

    @Async
    public void processSubmission(String submissionId) {
        logger.info("Processing submission: {}", submissionId);

        try {
            Submission submission = submissionRepository.findById(submissionId)
                    .orElseThrow(() -> new RuntimeException("Submission not found: " + submissionId));

            // Update status to RUNNING
            submission.setStatus(Submission.SubmissionStatus.RUNNING);
            submissionRepository.save(submission);

            // Get test cases for the problem
            List<TestCase> testCases = testCaseRepository.findByProblemId(submission.getProblemId());

            if (testCases.isEmpty()) {
                submission.setStatus(Submission.SubmissionStatus.RUNTIME_ERROR);
                submission.setErrorMessage("No test cases found for this problem");
                submission.setProcessedAt(LocalDateTime.now());
                submissionRepository.save(submission);
                return;
            }

            // Execute the code
            Submission.SubmissionStatus result = localExecutionService.executeCode(submission, testCases);

            // Update submission with results
            submission.setStatus(result);
            submission.setProcessedAt(LocalDateTime.now());

            // Set result message based on status
            switch (result) {
                case ACCEPTED:
                    submission.setResult("Accepted");
                    break;
                case WRONG_ANSWER:
                    submission.setResult("Wrong Answer");
                    break;
                case TIME_LIMIT_EXCEEDED:
                    submission.setResult("Time Limit Exceeded");
                    break;
                case MEMORY_LIMIT_EXCEEDED:
                    submission.setResult("Memory Limit Exceeded");
                    break;
                case RUNTIME_ERROR:
                    submission.setResult("Runtime Error");
                    break;
                case COMPILATION_ERROR:
                    submission.setResult("Compilation Error");
                    break;
                default:
                    submission.setResult("Unknown Error");
            }

            submissionRepository.save(submission);

            logger.info("Completed processing submission: {} with result: {}", submissionId, result);

        } catch (Exception e) {
            logger.error("Error processing submission: {}", submissionId, e);

            // Update submission with error status
            try {
                Submission submission = submissionRepository.findById(submissionId).orElse(null);
                if (submission != null) {
                    submission.setStatus(Submission.SubmissionStatus.RUNTIME_ERROR);
                    submission.setErrorMessage("Internal server error during processing");
                    submission.setProcessedAt(LocalDateTime.now());
                    submissionRepository.save(submission);
                }
            } catch (Exception ex) {
                logger.error("Failed to update submission status after error: {}", submissionId, ex);
            }
        }
    }
}
