package com.shodh.shodh_a_code.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shodh.shodh_a_code.dto.ContestDto;
import com.shodh.shodh_a_code.dto.LeaderboardEntry;
import com.shodh.shodh_a_code.dto.ProblemDto;
import com.shodh.shodh_a_code.model.Contest;
import com.shodh.shodh_a_code.model.Problem;
import com.shodh.shodh_a_code.model.Submission;
import com.shodh.shodh_a_code.model.User;
import com.shodh.shodh_a_code.repository.ContestRepository;
import com.shodh.shodh_a_code.repository.ProblemRepository;
import com.shodh.shodh_a_code.repository.SubmissionRepository;
import com.shodh.shodh_a_code.repository.UserRepository;

@Service
public class ContestService {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    public ContestDto getContestById(String contestId) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Contest not found: " + contestId));

        return convertToDto(contest);
    }

    public List<ProblemDto> getContestProblems(String contestId) {
        List<Problem> problems = problemRepository.findByContestId(contestId);
        return problems.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<LeaderboardEntry> getContestLeaderboard(String contestId) {
        // Get all submissions for this contest
        List<Submission> submissions = submissionRepository.findByContestId(contestId);

        // Group by user and calculate scores
        Map<String, List<Submission>> userSubmissions = submissions.stream()
                .collect(Collectors.groupingBy(Submission::getUserId));

        List<LeaderboardEntry> leaderboard = new ArrayList<>();

        for (Map.Entry<String, List<Submission>> entry : userSubmissions.entrySet()) {
            String userId = entry.getKey();
            List<Submission> userSubs = entry.getValue();

            // Get user info
            User user = userRepository.findById(userId).orElse(null);
            if (user == null)
                continue;

            // Calculate metrics
            int totalSubmissions = userSubs.size();
            int acceptedSubmissions = (int) userSubs.stream()
                    .filter(sub -> sub.getStatus() == Submission.SubmissionStatus.ACCEPTED)
                    .count();

            // Calculate score based on accepted problems
            Set<String> acceptedProblems = userSubs.stream()
                    .filter(sub -> sub.getStatus() == Submission.SubmissionStatus.ACCEPTED)
                    .map(Submission::getProblemId)
                    .collect(Collectors.toSet());

            int totalScore = acceptedProblems.size() * 100; // 100 points per problem

            // Calculate total time (sum of execution times for accepted submissions)
            long totalTime = userSubs.stream()
                    .filter(sub -> sub.getStatus() == Submission.SubmissionStatus.ACCEPTED)
                    .mapToLong(sub -> sub.getExecutionTime() != null ? sub.getExecutionTime() : 0)
                    .sum();

            leaderboard.add(new LeaderboardEntry(
                    userId,
                    user.getUsername(),
                    totalSubmissions,
                    acceptedSubmissions,
                    totalScore,
                    totalTime));
        }

        // Sort by score (descending), then by time (ascending)
        leaderboard.sort((a, b) -> {
            int scoreCompare = Integer.compare(b.getTotalScore(), a.getTotalScore());
            if (scoreCompare != 0)
                return scoreCompare;
            return Long.compare(a.getTotalTime(), b.getTotalTime());
        });

        return leaderboard;
    }

    private ContestDto convertToDto(Contest contest) {
        ContestDto dto = new ContestDto();
        dto.setId(contest.getId());
        dto.setTitle(contest.getTitle());
        dto.setDescription(contest.getDescription());
        dto.setStartTime(contest.getStartTime());
        dto.setEndTime(contest.getEndTime());
        dto.setStatus(contest.getStatus().toString());
        dto.setProblemIds(contest.getProblemIds());
        return dto;
    }

    private ProblemDto convertToDto(Problem problem) {
        ProblemDto dto = new ProblemDto();
        dto.setId(problem.getId());
        dto.setTitle(problem.getTitle());
        dto.setDescription(problem.getDescription());
        dto.setDifficulty(problem.getDifficulty());
        dto.setTimeLimit(problem.getTimeLimit());
        dto.setMemoryLimit(problem.getMemoryLimit());
        dto.setContestId(problem.getContestId());
        return dto;
    }
}
