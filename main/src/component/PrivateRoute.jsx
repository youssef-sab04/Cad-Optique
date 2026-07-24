import React from 'react'
import { useSelector } from 'react-redux'
import { Navigate, Outlet } from 'react-router-dom';

const PrivateRoute = ({ allowedRoles = [], publicPage = false }) => {
  const { user } = useSelector((state) => state.auth);

  if (publicPage) {
    if (!user) return <Outlet />;
    const redirect = user.roles?.includes("ROLE_ADMIN")
      ? "/admin-dashboard/dash"
      : "/responsable-dashboard/dash";
    return <Navigate to={redirect} replace />;
  }

  if (!user) return <Navigate to="/login" replace />;

  const hasAccess = allowedRoles.some(role => user.roles?.includes(role));
  return hasAccess ? <Outlet /> : <Navigate to="/" replace />;
};

export default PrivateRoute;