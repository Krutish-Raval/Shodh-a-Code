package com.shodh.shodh_a_code.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shodh.shodh_a_code.dto.SubmissionDto;
import com.shodh.shodh_a_code.dto.SubmissionRequest;
import com.shodh.shodh_a_code.model.Submission;
import com.shodh.shodh_a_code.repository.SubmissionRepository;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private SubmissionProcessingService submissionProcessingService;

    public String submitCode(SubmissionRequest request) {
        // Create new submission
        Submission submission = new Submission(
                request.getUserId(),
                request.getProblemId(),
                request.getContestId(),
                request.getCode(),
                request.getLanguage());

        // Save submission
        submission = submissionRepository.save(submission);

        // Process asynchronously
        submissionProcessingService.processSubmission(submission.getId());

        return submission.getId();
    }

    public SubmissionDto getSubmissionById(String submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found: " + submissionId));

        return convertToDto(submission);
    }

    public List<SubmissionDto> getUserSubmissions(String userId) {
        List<Submission> submissions = submissionRepository.findByUserId(userId);
        return submissions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<SubmissionDto> getContestSubmissions(String contestId) {
        List<Submission> submissions = submissionRepository.findByContestId(contestId);
        return submissions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private SubmissionDto convertToDto(Submission submission) {
        SubmissionDto dto = new SubmissionDto();
        dto.setId(submission.getId());
        dto.setUserId(submission.getUserId());
        dto.setProblemId(submission.getProblemId());
        dto.setContestId(submission.getContestId());
        dto.setCode(submission.getCode());
        dto.setLanguage(submission.getLanguage());
        dto.setStatus(submission.getStatus().toString());
        dto.setResult(submission.getResult());
        dto.setErrorMessage(submission.getErrorMessage());
        dto.setExecutionTime(submission.getExecutionTime());
        dto.setMemoryUsed(submission.getMemoryUsed());
        dto.setSubmittedAt(submission.getSubmittedAt());
        dto.setProcessedAt(submission.getProcessedAt());
        return dto;
    }
}
