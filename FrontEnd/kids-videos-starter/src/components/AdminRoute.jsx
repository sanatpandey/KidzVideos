import React from "react";
import { Navigate } from "react-router-dom";

const AdminRoute = ({children}) => {
    const token = localStorage.getItem('authToken');
    const userRole = localStorage.getItem('userRole');

    if(!token || userRole !== 'admin'){
        return <Navigate to="/login" replace />
    }

    return children;
};

export default AdminRoute;