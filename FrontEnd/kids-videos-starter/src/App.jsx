import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import Signup from './pages/SignUp';
import Detail from './pages/Detail';
import ProtectedRoute from './components/ProtectedRoute';
import Navbar from './components/Navbar';
import AdminRoute from './components/AdminRoute';
import UploadVideo from './pages/UploadVideo';

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route element={<ProtectedRoute />}>
          <Route path="/" element={<Home />} />
          <Route path="/detail/:id" element={<Detail />} />
        </Route>
        <Route element={<AdminRoute />}>
          <Route path="/upload" element={<UploadVideo />} />
        </Route>
    
      </Routes>
    </Router>
  );
}

export default App;