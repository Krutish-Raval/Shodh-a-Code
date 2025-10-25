package com.shodh.shodh_a_code.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shodh.shodh_a_code.dto.SubmissionDto;
import com.shodh.shodh_a_code.dto.SubmissionRequest;
import com.shodh.shodh_a_code.model.TestCase;
import com.shodh.shodh_a_code.repository.TestCaseRepository;
import com.shodh.shodh_a_code.service.SubmissionService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private TestCaseRepository testCaseRepository;

    @PostMapping("/submissions")
    public ResponseEntity<Map<String, String>> submitCode(@RequestBody SubmissionRequest request) {
        try {
            String submissionId = submissionService.submitCode(request);
            return ResponseEntity.ok(Map.of("submissionId", submissionId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/submissions/{submissionId}")
    public ResponseEntity<SubmissionDto> getSubmission(@PathVariable String submissionId) {
        try {
            SubmissionDto submission = submissionService.getSubmissionById(submissionId);
            return ResponseEntity.ok(submission);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/submissions/user/{userId}")
    public ResponseEntity<List<SubmissionDto>> getUserSubmissions(@PathVariable String userId) {
        try {
            List<SubmissionDto> submissions = submissionService.getUserSubmissions(userId);
            return ResponseEntity.ok(submissions);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/submissions/contest/{contestId}")
    public ResponseEntity<List<SubmissionDto>> getContestSubmissions(@PathVariable String contestId) {
        try {
            List<SubmissionDto> submissions = submissionService.getContestSubmissions(contestId);
            return ResponseEntity.ok(submissions);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Debug endpoint to check test cases for a problem
    @GetMapping("/debug/problem/{problemId}/testcases")
    public ResponseEntity<List<TestCase>> getProblemTestCases(@PathVariable String problemId) {
        try {
            List<TestCase> testCases = testCaseRepository.findByProblemId(problemId);
            return ResponseEntity.ok(testCases);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}