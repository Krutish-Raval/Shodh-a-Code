package com.shodh.shodh_a_code.service;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.shodh.shodh_a_code.model.Contest;
import com.shodh.shodh_a_code.model.Problem;
import com.shodh.shodh_a_code.model.TestCase;
import com.shodh.shodh_a_code.model.User;
import com.shodh.shodh_a_code.repository.ContestRepository;
import com.shodh.shodh_a_code.repository.ProblemRepository;
import com.shodh.shodh_a_code.repository.TestCaseRepository;
import com.shodh.shodh_a_code.repository.UserRepository;
import com.shodh.shodh_a_code.util.MongoConnectionTest;

@Service
public class DataInitializationService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializationService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private MongoConnectionTest mongoConnectionTest;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Initializing sample data...");

        // Test MongoDB Atlas connection
        if (!mongoConnectionTest.testConnection()) {
            logger.error("Failed to connect to MongoDB Atlas. Please check your connection settings.");
            return;
        }

        mongoConnectionTest.logConnectionInfo();

        // Create sample users
        createSampleUsers();

        // Create sample contest
        createSampleContest();

        logger.info("Sample data initialization completed!");
    }

    private void createSampleUsers() {
        if (userRepository.count() == 0) {
            User user1 = new User("john_doe", "john@example.com", "password123");
            user1.setFirstName("John");
            user1.setLastName("Doe");
            userRepository.save(user1);

            User user2 = new User("jane_smith", "jane@example.com", "password123");
            user2.setFirstName("Jane");
            user2.setLastName("Smith");
            userRepository.save(user2);

            User user3 = new User("alice_wonder", "alice@example.com", "password123");
            user3.setFirstName("Alice");
            user3.setLastName("Wonder");
            userRepository.save(user3);

            logger.info("Created 3 sample users");
        }
    }

    private void createSampleContest() {
        if (contestRepository.count() == 0) {
            // Create contest
            Contest contest = new Contest(
                    "Spring Boot Coding Contest 2024",
                    "A competitive programming contest featuring algorithmic challenges",
                    LocalDateTime.now().minusHours(1),
                    LocalDateTime.now().plusDays(7));
            contest.setStatus(Contest.ContestStatus.RUNNING);
            contest = contestRepository.save(contest);

            // Create problems
            Problem problem1 = createProblem1(contest.getId());
            Problem problem2 = createProblem2(contest.getId());
            Problem problem3 = createProblem3(contest.getId());

            // Update contest with problem IDs
            contest.setProblemIds(Arrays.asList(problem1.getId(), problem2.getId(), problem3.getId()));
            contestRepository.save(contest);

            logger.info("Created sample contest with 3 problems");
        }
    }

    private Problem createProblem1(String contestId) {
        Problem problem = new Problem(
                "Two Sum",
                "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.\n\n"
                        +
                        "You may assume that each input would have exactly one solution, and you may not use the same element twice.\n\n"
                        +
                        "Example:\n" +
                        "Input: nums = [2,7,11,15], target = 9\n" +
                        "Output: [0,1]\n" +
                        "Explanation: Because nums[0] + nums[1] == 9, we return [0, 1].",
                "EASY");
        problem.setContestId(contestId);
        problem.setTimeLimit(2000);
        problem.setMemoryLimit(128);
        problem = problemRepository.save(problem);

        // Create test cases
        TestCase testCase1 = new TestCase(problem.getId(), "2 7 11 15\n9", "0 1");
        testCaseRepository.save(testCase1);

        TestCase testCase2 = new TestCase(problem.getId(), "3 2 4\n6", "1 2");
        testCaseRepository.save(testCase2);

        TestCase testCase3 = new TestCase(problem.getId(), "3 3\n6", "0 1");
        testCaseRepository.save(testCase3);

        return problem;
    }

    private Problem createProblem2(String contestId) {
        Problem problem = new Problem(
                "Palindrome Number",
                "Given an integer x, return true if x is a palindrome integer.\n\n" +
                        "An integer is a palindrome when it reads the same backward as forward.\n\n" +
                        "Example:\n" +
                        "Input: x = 121\n" +
                        "Output: true\n" +
                        "Explanation: 121 reads as 121 from left to right and from right to left.",
                "EASY");
        problem.setContestId(contestId);
        problem.setTimeLimit(2000);
        problem.setMemoryLimit(128);
        problem = problemRepository.save(problem);

        // Create test cases
        TestCase testCase1 = new TestCase(problem.getId(), "121", "true");
        testCaseRepository.save(testCase1);

        TestCase testCase2 = new TestCase(problem.getId(), "-121", "false");
        testCaseRepository.save(testCase2);

        TestCase testCase3 = new TestCase(problem.getId(), "10", "false");
        testCaseRepository.save(testCase3);

        return problem;
    }

    private Problem createProblem3(String contestId) {
        Problem problem = new Problem(
                "Binary Tree Inorder Traversal",
                "Given the root of a binary tree, return the inorder traversal of its nodes' values.\n\n" +
                        "Example:\n" +
                        "Input: root = [1,null,2,3]\n" +
                        "Output: [1,3,2]",
                "MEDIUM");
        problem.setContestId(contestId);
        problem.setTimeLimit(3000);
        problem.setMemoryLimit(256);
        problem = problemRepository.save(problem);

        // Create test cases
        TestCase testCase1 = new TestCase(problem.getId(), "1 null 2 3", "1 3 2");
        testCaseRepository.save(testCase1);

        TestCase testCase2 = new TestCase(problem.getId(), "1 2", "2 1");
        testCaseRepository.save(testCase2);

        TestCase testCase3 = new TestCase(problem.getId(), "1 null 2", "1 2");
        testCaseRepository.save(testCase3);

        return problem;
    }
}
