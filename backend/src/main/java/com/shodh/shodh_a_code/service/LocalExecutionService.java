package com.shodh.shodh_a_code.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shodh.shodh_a_code.model.Submission;
import com.shodh.shodh_a_code.model.TestCase;

@Service
public class LocalExecutionService {

    private static final Logger logger = LoggerFactory.getLogger(LocalExecutionService.class);

    @Value("${docker.execution.timeout:5000}")
    private long timeoutMs;

    public Submission.SubmissionStatus executeCode(Submission submission, List<TestCase> testCases) {
        logger.info("Starting LOCAL execution for submission: {}", submission.getId());
        logger.info("Code to execute: {}", submission.getCode());
        logger.info("Language: {}", submission.getLanguage());
        logger.info("Number of test cases: {}", testCases.size());

        try {
            // Create temporary directory for code files
            Path tempDir = Files.createTempDirectory("code_execution_" + submission.getId());
            logger.info("Created temp directory: {}", tempDir);

            try {
                // Write code to file based on language
                String fileName = getFileName(submission.getLanguage());
                Path codeFile = tempDir.resolve(fileName);
                Files.write(codeFile, submission.getCode().getBytes());
                logger.info("Written code to file: {}", codeFile);

                // Execute against each test case
                for (int i = 0; i < testCases.size(); i++) {
                    TestCase testCase = testCases.get(i);
                    logger.info("Executing test case {}: Input='{}', Expected='{}'",
                            i + 1, testCase.getInput(), testCase.getExpectedOutput());

                    Submission.SubmissionStatus result = executeTestCase(submission, testCase, tempDir, fileName);
                    logger.info("Test case {} result: {}", i + 1, result);

                    if (result != Submission.SubmissionStatus.ACCEPTED) {
                        return result;
                    }
                }

                return Submission.SubmissionStatus.ACCEPTED;

            } finally {
                // Clean up temporary directory with retry on Windows
                deleteDirectory(tempDir);
            }

        } catch (Exception e) {
            logger.error("Error executing code for submission: {}", submission.getId(), e);
            return Submission.SubmissionStatus.RUNTIME_ERROR;
        }
    }

    private Submission.SubmissionStatus executeTestCase(Submission submission, TestCase testCase, Path tempDir,
            String fileName) {
        try {
            // Step 1: Compile the code (for Java and C/C++)
            if (submission.getLanguage().toLowerCase().equals("java") ||
                    submission.getLanguage().toLowerCase().equals("cpp") ||
                    submission.getLanguage().toLowerCase().equals("c++") ||
                    submission.getLanguage().toLowerCase().equals("c")) {

                Submission.SubmissionStatus compileResult = compileCode(submission, tempDir, fileName);
                if (compileResult != Submission.SubmissionStatus.ACCEPTED) {
                    return compileResult;
                }
            }

            // Step 2: Execute the code
            return executeCompiledCode(submission, testCase, tempDir, fileName);

        } catch (Exception e) {
            logger.error("Error executing test case for submission: {}", submission.getId(), e);
            return Submission.SubmissionStatus.RUNTIME_ERROR;
        }
    }

    private Submission.SubmissionStatus compileCode(Submission submission, Path tempDir, String fileName) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            List<String> command = getLocalExecutionCommand(submission.getLanguage(), fileName);

            processBuilder.command(command);
            processBuilder.directory(tempDir.toFile());

            logger.debug("Compiling with command: {}", String.join(" ", command));

            Process process = processBuilder.start();
            boolean finished = process.waitFor(10000, TimeUnit.MILLISECONDS); // 10 second compile timeout

            if (!finished) {
                process.destroyForcibly();
                logger.warn("Compilation timed out for submission: {}", submission.getId());
                return Submission.SubmissionStatus.RUNTIME_ERROR;
            }

            int exitCode = process.exitValue();
            String error = readStream(process.getErrorStream());

            logger.info("Compilation exit code: {}, Error: '{}'", exitCode, error);

            if (exitCode != 0) {
                logger.error("Compilation failed. Exit code: {}, Error: {}", exitCode, error);
                return Submission.SubmissionStatus.COMPILATION_ERROR;
            }

            logger.info("✅ Compilation successful");
            return Submission.SubmissionStatus.ACCEPTED;

        } catch (Exception e) {
            logger.error("Error compiling code for submission: {}", submission.getId(), e);
            return Submission.SubmissionStatus.COMPILATION_ERROR;
        }
    }

    private Submission.SubmissionStatus executeCompiledCode(Submission submission, TestCase testCase, Path tempDir,
            String fileName) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            List<String> command = getExecutionCommand(submission.getLanguage(), fileName);

            processBuilder.command(command);
            processBuilder.directory(tempDir.toFile());

            logger.debug("Executing with command: {}", String.join(" ", command));

            Process process = processBuilder.start();

            // Write input to process
            try (OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream())) {
                writer.write(testCase.getInput());
                writer.flush();
                logger.debug("Written input to process: {}", testCase.getInput());
            }

            // Wait for process to complete with timeout
            boolean finished = process.waitFor(timeoutMs, TimeUnit.MILLISECONDS);

            if (!finished) {
                process.destroyForcibly();
                logger.warn("Process timed out for submission: {}", submission.getId());
                return Submission.SubmissionStatus.TIME_LIMIT_EXCEEDED;
            }

            int exitCode = process.exitValue();

            // Read output
            String output = readStream(process.getInputStream());
            String error = readStream(process.getErrorStream());

            logger.info("Execution exit code: {}, Output: '{}', Error: '{}'", exitCode, output, error);

            if (exitCode != 0) {
                if (error.contains("OutOfMemoryError") || error.contains("MemoryError")) {
                    logger.error("Memory limit exceeded. Error: {}", error);
                    return Submission.SubmissionStatus.MEMORY_LIMIT_EXCEEDED;
                }
                logger.error("Runtime error. Exit code: {}, Error: {}", exitCode, error);
                return Submission.SubmissionStatus.RUNTIME_ERROR;
            }

            // Compare output with expected output
            String normalizedOutput = normalizeOutput(output);
            String normalizedExpected = normalizeOutput(testCase.getExpectedOutput());

            logger.info("Comparing outputs - Actual: '{}', Expected: '{}'", normalizedOutput, normalizedExpected);

            if (normalizedOutput.equals(normalizedExpected)) {
                logger.info("✅ Output matches expected result");
                return Submission.SubmissionStatus.ACCEPTED;
            } else {
                logger.warn("❌ Output does not match expected result");
                return Submission.SubmissionStatus.WRONG_ANSWER;
            }

        } catch (Exception e) {
            logger.error("Error executing compiled code for submission: {}", submission.getId(), e);
            return Submission.SubmissionStatus.RUNTIME_ERROR;
        }
    }

    private String getFileName(String language) {
        return switch (language.toLowerCase()) {
            case "java" -> "Solution.java";
            case "python" -> "solution.py";
            case "cpp", "c++" -> "solution.cpp";
            case "c" -> "solution.c";
            default -> throw new IllegalArgumentException("Unsupported language: " + language);
        };
    }

    private List<String> getLocalExecutionCommand(String language, String fileName) {
        return switch (language.toLowerCase()) {
            case "java" -> List.of("javac", fileName);
            case "python" -> List.of("python", fileName);
            case "cpp", "c++" -> List.of("g++", "-o", "solution", fileName);
            case "c" -> List.of("gcc", "-o", "solution", fileName);
            default -> throw new IllegalArgumentException("Unsupported language: " + language);
        };
    }

    private List<String> getExecutionCommand(String language, String fileName) {
        String os = System.getProperty("os.name").toLowerCase();
        boolean isWindows = os.contains("windows");

        return switch (language.toLowerCase()) {
            case "java" -> List.of("java", "Solution");
            case "python" -> List.of("python", fileName);
            case "cpp", "c++" -> isWindows ? List.of("solution.exe") : List.of("./solution");
            case "c" -> isWindows ? List.of("solution.exe") : List.of("./solution");
            default -> throw new IllegalArgumentException("Unsupported language: " + language);
        };
    }

    private String readStream(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            return result.toString().trim();
        }
    }

    private String normalizeOutput(String output) {
        return output.replaceAll("\\r\\n", "\n")
                .replaceAll("\\r", "\n")
                .trim();
    }

    private void deleteDirectory(Path directory) {
        try {
            // On Windows, files may still be locked by the process
            // Add a small delay to allow file handles to be released
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("windows")) {
                try {
                    Thread.sleep(100); // Small delay for Windows
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            Files.walk(directory)
                    .sorted((a, b) -> b.compareTo(a)) // Delete files before directories
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            logger.warn("Failed to delete file: {}", path);
                        }
                    });
        } catch (IOException e) {
            logger.warn("Failed to delete directory: {}", directory);
        }
    }
}
