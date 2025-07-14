import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import Signup from './pages/SignUp';
import ProtectedRoute from './components/ProtectedRoute';
import Navbar from './components/Navbar';
import AdminRoute from './components/AdminRoute';
import UploadVideo from './pages/UploadVideo';
import Watch from './pages/Watch';
import Detail from './pages/Detail';

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route element={<ProtectedRoute />}>
          <Route path="/" element={<Home />} />
        </Route>
        <Route element={<AdminRoute />}>
          <Route path="/upload" element={<UploadVideo />} />
        </Route>
        <Route path="/detail/:id" element={<Detail />} />
        <Route path="/watch/:id" element={<Watch />} />
    
      </Routes>
    </Router>
  );
}

export default App;