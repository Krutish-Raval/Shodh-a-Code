# Shodh-a-Code Frontend

A React TypeScript frontend for the Shodh-a-Code contest platform with real-time features.

## Features

- **Join Page**: Simple form to enter Contest ID and Username
- **Contest Page**: Central hub with problem view, code editor, and live leaderboard
- **Real-time Submissions**: Asynchronous submission flow with status polling
- **Live Leaderboard**: Updates every 15 seconds to show current rankings
- **Code Editor**: Syntax highlighting for Java, Python, C++, and C
- **Responsive Design**: Built with Tailwind CSS for modern UI

## Tech Stack

- React 19 with TypeScript
- Tailwind CSS for styling
- React Router for navigation
- React Ace Editor for code editing
- Axios for API communication

## Getting Started

1. Install dependencies:

```bash
npm install
```

2. Start the development server:

```bash
npm run dev
```

3. Open [http://localhost:5173](http://localhost:5173) in your browser

## API Integration

The frontend communicates with the backend API running on `http://localhost:8080/api`. Make sure the backend is running before starting the frontend.

### Key API Endpoints Used:

- `GET /api/contests/{contestId}` - Get contest details
- `GET /api/contests/{contestId}/leaderboard` - Get leaderboard
- `POST /api/submissions` - Submit code
- `GET /api/submissions/{submissionId}` - Get submission status

## Real-time Features

### Submission Polling

- When a user submits code, the frontend polls the submission status every 2 seconds
- Shows live status updates: "Running", "Accepted", "Wrong Answer", etc.
- Displays execution time and memory usage when available

### Live Leaderboard

- Automatically refreshes every 15 seconds
- Highlights current user's position
- Shows problem submission statuses with color coding

## Project Structure

```
src/
├── components/
│   ├── JoinPage.tsx          # Contest join form
│   ├── ContestPage.tsx        # Main contest interface
│   ├── ProblemView.tsx       # Problem description display
│   ├── CodeEditor.tsx        # Code editor with syntax highlighting
│   └── Leaderboard.tsx       # Live leaderboard component
├── services/
│   └── api.ts               # API service functions and types
├── App.tsx                  # Main app with routing
└── index.css               # Tailwind CSS imports
```

## Usage

1. **Join Contest**: Enter a valid Contest ID and your username
2. **Select Problem**: Choose from available problems in the sidebar
3. **Write Code**: Use the code editor with syntax highlighting
4. **Submit**: Click submit to run your code against test cases
5. **Monitor**: Watch real-time status updates and leaderboard changes
