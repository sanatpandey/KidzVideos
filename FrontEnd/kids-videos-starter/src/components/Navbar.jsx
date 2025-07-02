import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';

const Navbar = () => {
  const[userName, setUserName] = useState(null);
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const name = localStorage.getItem("userName");
    setUserName(name);
  }, [location]);

  const handleLogout = () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userName');
    setUserName(null);
    navigate('/login');
  }

  const isLoginPage = location.pathname === '/login';

  return (
    <nav className="bg-gradient-to-r from-pink-200 via-pink-300 to-pink-400 p-4 flex justify-between items-center shadow-md sticky top-0 z-50">
      <Link to="/" className="text-2xl font-extrabold text-white drop-shadow-md">KidsFlix</Link>
      {userName ? (
        <div className="flex gap-3 items-center">
          <span className="text-white text-sm">Hi, {userName} 👋</span>
          <button onClick={handleLogout} className="text-sm bg-white text-pink-500 px-3 py-1 rounded hover:bg-pink-100">Logout</button>
        </div>
      ) : (
        <Link to="/signup" className="text-sm text-white hover:underline">
          {isLoginPage ? 'Sign Up' : 'Login'}
        </Link>
      )}
    </nav>
  );
};

export default Navbar;