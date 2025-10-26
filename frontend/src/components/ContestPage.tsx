import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import type { Contest, Problem, Submission } from '../services/api';
import { contestApi, submissionApi } from '../services/api';
import Leaderboard from './Leaderboard';
import ProblemView from './ProblemView';
import SimpleCodeEditor from './SimpleCodeEditor';

const ContestPage: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { contestId, username } = location.state || {};

  const [contest, setContest] = useState<Contest | null>(null);
  const [selectedProblem, setSelectedProblem] = useState<Problem | null>(null);
  const [code, setCode] = useState('');
  const [language, setLanguage] = useState<'JAVA' | 'PYTHON' | 'CPP' | 'C'>('JAVA');
  const [submitting, setSubmitting] = useState(false);
  const [submissionStatus, setSubmissionStatus] = useState<string>('');
  const [submissionResult, setSubmissionResult] = useState<Submission | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!contestId || !username) {
      navigate('/');
      return;
    }

    fetchContest();
  }, [contestId, username, navigate]);

  const fetchContest = async () => {
    try {
      const contestData = await contestApi.getContest(contestId);
      // Fetch problems separately
      const problems = await contestApi.getContestProblems(contestId);
      contestData.problems = problems;
      
      setContest(contestData);
      if (problems.length > 0) {
        setSelectedProblem(problems[0]);
      }
      setError('');
    } catch (err) {
      setError('Failed to load contest');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async () => {
    if (!selectedProblem || !code.trim()) {
      alert('Please select a problem and write some code');
      return;
    }

    setSubmitting(true);
    setSubmissionStatus('Submitting...');

    try {
      const response = await submissionApi.submitCode({
        userId: username,
        problemId: selectedProblem.id,
        language,
        code,
      });

      const submissionId = response.submissionId;
      setSubmissionStatus('Running...');

      // Poll for submission status
      const pollSubmission = async () => {
        try {
          const submission = await submissionApi.getSubmission(submissionId);
          setSubmissionResult(submission);
          setSubmissionStatus(submission.status);

          if (submission.status === 'PENDING' || submission.status === 'RUNNING') {
            setTimeout(pollSubmission, 2000); // Poll every 2 seconds
          } else {
            setSubmitting(false);
          }
        } catch (err) {
          setSubmissionStatus('Error checking submission');
          setSubmitting(false);
        }
      };

      pollSubmission();
    } catch (err) {
      setSubmissionStatus('Failed to submit');
      setSubmitting(false);
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'ACCEPTED':
        return 'text-green-600 bg-green-100';
      case 'WRONG_ANSWER':
        return 'text-red-600 bg-red-100';
      case 'TIME_LIMIT_EXCEEDED':
        return 'text-yellow-600 bg-yellow-100';
      case 'RUNTIME_ERROR':
        return 'text-red-600 bg-red-100';
      case 'COMPILATION_ERROR':
        return 'text-red-600 bg-red-100';
      case 'RUNNING':
        return 'text-blue-600 bg-blue-100';
      case 'PENDING':
        return 'text-gray-600 bg-gray-100';
      default:
        return 'text-gray-600 bg-gray-100';
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50 flex items-center justify-center">
        <div className="text-center">
          <div className="relative">
            <div className="w-20 h-20 bg-gradient-to-r from-purple-600 to-blue-600 rounded-full flex items-center justify-center mx-auto mb-6 animate-pulse">
              <svg className="w-10 h-10 text-white animate-spin" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
              </svg>
            </div>
            <div className="absolute inset-0 w-20 h-20 bg-gradient-to-r from-purple-600 to-blue-600 rounded-full mx-auto animate-ping opacity-20"></div>
          </div>
          <h2 className="text-2xl font-bold text-gray-800 mb-2">Loading Contest</h2>
          <p className="text-gray-600">Preparing your coding environment...</p>
        </div>
      </div>
    );
  }

  if (error || !contest) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50 flex items-center justify-center">
        <div className="text-center max-w-md mx-auto p-8">
          <div className="w-20 h-20 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-6">
            <svg className="w-10 h-10 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <h2 className="text-2xl font-bold text-gray-800 mb-4">Oops! Something went wrong</h2>
          <p className="text-red-600 mb-6">{error || 'Contest not found'}</p>
          <button
            onClick={() => navigate('/')}
            className="bg-gradient-to-r from-purple-600 to-blue-600 text-white px-6 py-3 rounded-xl font-semibold hover:from-purple-700 hover:to-blue-700 transition-all duration-200 transform hover:scale-105 shadow-lg"
          >
            Back to Join Page
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50">
      {/* Header */}
      <div className="bg-white/80 backdrop-blur-lg shadow-lg border-b border-gray-200/50 sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center space-x-4">
              <div className="flex items-center space-x-3">
                <div className="w-10 h-10 bg-gradient-to-r from-purple-600 to-blue-600 rounded-lg flex items-center justify-center">
                  <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 20l4-16m4 4l4 4-4 4M6 16l-4-4 4-4" />
                  </svg>
                </div>
                <div>
                  <h1 className="text-xl font-bold text-gray-800">{contest.title}</h1>
                  <p className="text-sm text-gray-500">Live Coding Contest</p>
                </div>
              </div>
              <span className={`px-3 py-1 rounded-full text-sm font-medium ${
                contest.status === 'RUNNING' ? 'bg-green-100 text-green-800 animate-pulse' :
                contest.status === 'UPCOMING' ? 'bg-yellow-100 text-yellow-800' :
                'bg-gray-100 text-gray-800'
              }`}>
                {contest.status === 'RUNNING' && (
                  <span className="w-2 h-2 bg-green-500 rounded-full inline-block mr-2 animate-ping"></span>
                )}
                {contest.status}
              </span>
            </div>
            <div className="flex items-center space-x-4">
              <div className="flex items-center space-x-2 bg-gray-100 rounded-lg px-3 py-2">
                <div className="w-8 h-8 bg-gradient-to-r from-purple-500 to-pink-500 rounded-full flex items-center justify-center">
                  <span className="text-white text-sm font-semibold">{username.charAt(0).toUpperCase()}</span>
                </div>
                <span className="text-gray-700 font-medium">{username}</span>
              </div>
              <button
                onClick={() => navigate('/')}
                className="text-gray-600 hover:text-gray-800 px-3 py-2 rounded-lg hover:bg-gray-100 transition-colors duration-200"
              >
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        <div className="grid grid-cols-1 lg:grid-cols-4 gap-6 min-h-[calc(100vh-8rem)]">
          {/* Problems List */}
          <div className="lg:col-span-1">
            <div className="bg-white/80 backdrop-blur-lg rounded-2xl shadow-xl border border-gray-200/50 p-6 h-full overflow-y-auto flex flex-col">
              <div className="flex items-center justify-between mb-4">
                <h3 className="text-xl font-bold text-gray-800">Problems</h3>
                <div className="text-xs text-gray-500 bg-gray-100 px-2 py-1 rounded-full">
                  {contest.problems.length}
                </div>
              </div>
              <div className="flex-1 space-y-2 overflow-y-auto pr-1">
                {contest.problems.map((problem) => (
                  <button
                    key={problem.id}
                    onClick={() => setSelectedProblem(problem)}
                    className={`w-full text-left p-3 rounded-lg border transition-all duration-200 ${
                      selectedProblem?.id === problem.id
                        ? 'border-purple-500 bg-gradient-to-r from-purple-50 to-blue-50'
                        : 'border-gray-200 hover:border-purple-300 bg-white'
                    }`}
                  >
                    <div className="flex items-start justify-between gap-2 mb-1">
                      <div className="font-semibold text-sm text-gray-800 line-clamp-1">{problem.title}</div>
                      <div className={`px-2 py-0.5 rounded text-xs font-medium whitespace-nowrap ${
                        problem.difficulty === 'EASY' ? 'bg-green-100 text-green-800' :
                        problem.difficulty === 'MEDIUM' ? 'bg-yellow-100 text-yellow-800' :
                        'bg-red-100 text-red-800'
                      }`}>
                        {problem.difficulty}
                      </div>
                    </div>
                    <div className="flex items-center gap-3 text-xs text-gray-500">
                      <div className="flex items-center">
                        <svg className="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                        </svg>
                        {problem.timeLimit}s
                      </div>
                      <div className="flex items-center">
                        <svg className="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 7v10c0 2.21 3.582 4 8 4s8-1.79 8-4V7M4 7c0 2.21 3.582 4 8 4s8-1.79 8-4M4 7c0-2.21 3.582-4 8-4s8 1.79 8 4" />
                        </svg>
                        {problem.memoryLimit}MB
                      </div>
                    </div>
                  </button>
                ))}
              </div>
            </div>
          </div>

          {/* Main Content */}
          <div className="lg:col-span-2">
            <div className="grid grid-cols-1 h-full gap-6">
              {/* Problem View */}
              <div className="h-1/2">
                {selectedProblem ? (
                  <ProblemView problem={selectedProblem} />
                ) : (
                  <div className="bg-white rounded-lg shadow-md p-6 h-full flex items-center justify-center">
                    <p className="text-gray-500">Select a problem to view details</p>
                  </div>
                )}
              </div>

              {/* Code Editor */}
              <div className="h-1/2 flex flex-col">
                <div className="bg-white/80 backdrop-blur-lg rounded-2xl shadow-xl border border-gray-200/50 p-4 flex flex-col flex-1">
                  <div className="flex items-center justify-between mb-3 flex-shrink-0">
                    <h3 className="text-lg font-bold text-gray-800 flex items-center gap-2">
                      <div className="w-7 h-7 bg-gradient-to-r from-green-500 to-blue-500 rounded flex items-center justify-center">
                        <svg className="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 20l4-16m4 4l4 4-4 4M6 16l-4-4 4-4" />
                        </svg>
                      </div>
                      Code Editor
                    </h3>
                    <div className="flex items-center gap-2">
                      <div className="relative">
                        <select
                          value={language}
                          onChange={(e) => setLanguage(e.target.value as 'JAVA' | 'PYTHON' | 'CPP' | 'C')}
                          className="appearance-none bg-white border border-gray-300 rounded-lg px-3 py-1.5 pr-7 text-sm font-medium text-gray-700 focus:border-purple-500 focus:outline-none"
                        >
                          <option value="JAVA">☕ Java</option>
                          <option value="PYTHON">🐍 Python</option>
                          <option value="CPP">⚡ C++</option>
                          <option value="C">🔧 C</option>
                        </select>
                        <div className="absolute inset-y-0 right-0 flex items-center px-1 pointer-events-none">
                          <svg className="w-3 h-3 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                          </svg>
                        </div>
                      </div>
                      <button
                        onClick={handleSubmit}
                        disabled={submitting || !selectedProblem}
                        className="bg-gradient-to-r from-purple-600 to-blue-600 text-white px-4 py-1.5 rounded-lg text-sm font-semibold hover:from-purple-700 hover:to-blue-700 disabled:opacity-50 disabled:cursor-not-allowed transition-all flex items-center gap-2"
                      >
                        {submitting ? (
                          <>
                            <svg className="animate-spin h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                              <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                              <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                            </svg>
                            Submitting...
                          </>
                        ) : (
                          <>
                            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 10l7-7m0 0l7 7m-7-7v18" />
                            </svg>
                            Submit
                          </>
                        )}
                      </button>
                    </div>
                  </div>
                  
                  <div className="flex-1 min-h-0">
                    <SimpleCodeEditor
                      code={code}
                      language={language}
                      onChange={setCode}
                    />
                  </div>

                  {/* Submission Status */}
                  {(submissionStatus || submissionResult) && (
                    <div className="mt-4 p-4 bg-gray-50 rounded-lg border border-gray-200 flex-shrink-0">
                      <div className="flex items-center justify-between mb-3">
                        <div className="flex items-center gap-3">
                          <span className="text-sm font-semibold text-gray-700">Status:</span>
                          <span className={`px-2.5 py-1 rounded-md text-xs font-medium ${getStatusColor(submissionStatus)}`}>
                            {submissionStatus}
                          </span>
                          {(submissionStatus === 'RUNNING' || submissionStatus === 'PENDING') && (
                            <div className="flex gap-1">
                              <div className="w-1.5 h-1.5 bg-blue-500 rounded-full animate-bounce"></div>
                              <div className="w-1.5 h-1.5 bg-blue-500 rounded-full animate-bounce delay-100"></div>
                              <div className="w-1.5 h-1.5 bg-blue-500 rounded-full animate-bounce delay-200"></div>
                            </div>
                          )}
                        </div>
                        {submissionResult && (
                          <div className="flex items-center gap-4 text-xs text-gray-600">
                            {submissionResult.executionTime && (
                              <div className="flex items-center gap-1">
                                <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                                </svg>
                                <span>{submissionResult.executionTime}ms</span>
                              </div>
                            )}
                            {submissionResult.memoryUsed && (
                              <div className="flex items-center gap-1">
                                <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 7v10c0 2.21 3.582 4 8 4s8-1.79 8-4V7M4 7c0 2.21 3.582 4 8 4s8-1.79 8-4M4 7c0-2.21 3.582-4 8-4s8 1.79 8 4" />
                                </svg>
                                <span>{submissionResult.memoryUsed}KB</span>
                              </div>
                            )}
                          </div>
                        )}
                      </div>
                      {submissionResult?.result && (
                        <div className="mt-3 pt-3 border-t border-gray-300">
                          <span className="text-xs font-semibold text-gray-600 mb-2 block">Output:</span>
                          <pre className="bg-white p-3 rounded text-xs overflow-x-auto border border-gray-200 font-mono text-gray-800">
                            {submissionResult.result}
                          </pre>
                        </div>
                      )}
                    </div>
                  )}
                </div>
              </div>
            </div>
          </div>

          {/* Leaderboard */}
          <div className="lg:col-span-1">
            <Leaderboard contestId={contestId} currentUser={username} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default ContestPage;
