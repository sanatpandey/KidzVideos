import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Signup = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [ageGroup, setAgeGroup] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSignup = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const response = await axios.post('/api/user/register', {
        name,
        email,
        password,
        ageGroup
      }, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
      alert('Signup successful! Please login.');
      navigate('/login');
    } catch (err) {
      console.error("Signup failed", err);
      setError('Signup failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gradient-to-br from-blue-100 via-green-100 to-yellow-100">
      <form onSubmit={handleSignup} className="bg-white p-10 rounded-3xl shadow-2xl w-96 border border-green-200">
        <h2 className="text-3xl font-extrabold mb-6 text-green-600 text-center">Create Your Account 🎉</h2>

        <input
          type="text"
          placeholder="Full Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
          className="p-2 border border-gray-300 rounded w-full"
          required
        />
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="p-2 border border-gray-300 rounded w-full mt-3"
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="p-2 border border-gray-300 rounded w-full mt-3"
          required
        />
        <select
          className="p-2 border border-gray-300 rounded w-full mt-3"
          value={ageGroup}
          onChange={(e) => setAgeGroup(e.target.value)}
          required
        >
          <option value="">Select Age Group</option>
          <option value="lt15">Less than 15</option>
          <option value="lt10">Less than 10</option>
          <option value="lt5">Less than 5</option>
        </select>

        {error && <p className="text-red-500 text-sm mt-2">{error}</p>}

        <button
          type="submit"
          className="w-full bg-green-500 hover:bg-green-600 text-white font-semibold py-2 px-4 rounded mt-6"
          disabled={loading}
        >
          {loading ? 'Signing up...' : 'Sign Up'}
        </button>
      </form>
    </div>
  );
};

export default Signup;