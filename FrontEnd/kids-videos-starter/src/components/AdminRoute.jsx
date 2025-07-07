import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

const AdminRoute = () => {
  const token = localStorage.getItem('authToken');
  const role = localStorage.getItem('userRole');

  console.log("🛡️ AdminRoute Check:", { token, role });

  if (!token || role !== 'admin') {
    return <Navigate to="/login" />;
  }

  return <Outlet />;
};

export default AdminRoute;
