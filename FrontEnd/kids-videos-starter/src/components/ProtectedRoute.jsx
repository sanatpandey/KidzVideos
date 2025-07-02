import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

const isAuthenticated = true;

const ProtectedRoute = () => {
  return isAuthenticated ? <Outlet /> : <Navigate to="/login" />;
};

export default ProtectedRoute;