import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import gateway from '../utils/axiosGateway';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  //const [ageGroup, setAgeGroup] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    console.log("Login started", { email, password });
    try {
      const response = await gateway.post('/login', {
        email,
        password
      }, {
        headers: {
          'Content-Type': 'application/json'
        },
        withCredentials: true
      });

      const { accessToken, userName, role} = response.data;
      localStorage.setItem('authToken', accessToken);
      localStorage.setItem('userName', userName);
      localStorage.setItem('userRole', role);
      console.log("Login successful", userName);
      console.log("Role", role);
      if (role === 'admin') {
        navigate('/upload');
      } else {
        navigate('/');
      }
    } catch (err) {
      console.error("Login failed", err);
      setError('Invalid credentials or network error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gradient-to-br from-yellow-100 via-pink-100 to-purple-100">
      <form onSubmit={handleLogin} className="bg-white p-10 rounded-3xl shadow-2xl w-96 border border-pink-200">
        <h2 className="text-3xl font-extrabold mb-6 text-pink-600 text-center">Welcome Back! 👋</h2>

        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="input"
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="input mt-3"
          required
        />
        

        {error && <p className="text-red-500 text-sm mt-2">{error}</p>}

        <button
          type="submit"
          className="btn-primary mt-6 flex justify-center items-center"
          disabled={loading}
        >
          {loading ? 'Logging in...' : 'Login'}
        </button>
      </form>
    </div>
  );
};

export default Login;