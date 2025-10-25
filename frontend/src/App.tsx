import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import ContestPage from './components/ContestPage';
import JoinPage from './components/JoinPage';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<JoinPage />} />
          <Route path="/contest" element={<ContestPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
