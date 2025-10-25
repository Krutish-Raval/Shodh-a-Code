import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Types
export interface Contest {
  id: string;
  name: string;
  description: string;
  startTime: string;
  endTime: string;
  status: 'UPCOMING' | 'RUNNING' | 'FINISHED';
  problems: Problem[];
}

export interface Problem {
  id: string;
  title: string;
  description: string;
  difficulty: 'EASY' | 'MEDIUM' | 'HARD';
  timeLimit: number;
  memoryLimit: number;
  testCases: TestCase[];
}

export interface TestCase {
  id: string;
  input: string;
  expectedOutput: string;
  isHidden: boolean;
}

export interface Submission {
  id: string;
  userId: string;
  problemId: string;
  language: 'JAVA' | 'PYTHON' | 'CPP' | 'C';
  code: string;
  status: 'PENDING' | 'RUNNING' | 'ACCEPTED' | 'WRONG_ANSWER' | 'TIME_LIMIT_EXCEEDED' | 'RUNTIME_ERROR' | 'COMPILATION_ERROR';
  executionTime?: number;
  memoryUsed?: number;
  submittedAt: string;
  result?: string;
}

export interface LeaderboardEntry {
  userId: string;
  totalScore: number;
  problemsSolved: number;
  totalTime: number;
  submissions: Array<{
    problemId: string;
    status: string;
    submissionTime: string;
  }>;
}

export interface SubmissionRequest {
  userId: string;
  problemId: string;
  language: 'JAVA' | 'PYTHON' | 'CPP' | 'C';
  code: string;
}

// API Functions
export const contestApi = {
  // Get contest details
  getContest: async (contestId: string): Promise<Contest> => {
    const response = await api.get(`/contests/${contestId}`);
    return response.data;
  },

  // Get leaderboard
  getLeaderboard: async (contestId: string): Promise<LeaderboardEntry[]> => {
    const response = await api.get(`/contests/${contestId}/leaderboard`);
    return response.data;
  },
};

export const submissionApi = {
  // Submit code
  submitCode: async (submission: SubmissionRequest): Promise<{ submissionId: string }> => {
    const response = await api.post('/submissions', submission);
    return response.data;
  },

  // Get submission status
  getSubmission: async (submissionId: string): Promise<Submission> => {
    const response = await api.get(`/submissions/${submissionId}`);
    return response.data;
  },
};

export default api;
