package com.shodh.shodh_a_code.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shodh.shodh_a_code.dto.ContestDto;
import com.shodh.shodh_a_code.dto.LeaderboardEntry;
import com.shodh.shodh_a_code.dto.ProblemDto;
import com.shodh.shodh_a_code.service.ContestService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ContestController {

    @Autowired
    private ContestService contestService;

    @GetMapping("/contests/{contestId}")
    public ResponseEntity<ContestDto> getContest(@PathVariable String contestId) {
        try {
            ContestDto contest = contestService.getContestById(contestId);
            return ResponseEntity.ok(contest);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/contests/{contestId}/problems")
    public ResponseEntity<List<ProblemDto>> getContestProblems(@PathVariable String contestId) {
        try {
            List<ProblemDto> problems = contestService.getContestProblems(contestId);
            return ResponseEntity.ok(problems);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/contests/{contestId}/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getContestLeaderboard(@PathVariable String contestId) {
        try {
            List<LeaderboardEntry> leaderboard = contestService.getContestLeaderboard(contestId);
            return ResponseEntity.ok(leaderboard);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
