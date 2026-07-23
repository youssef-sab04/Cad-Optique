
import React from 'react'
import { useSelector } from 'react-redux'
import { Navigate, Outlet, useLocation } from 'react-router-dom';

const PrivateRoute = ({ allowedRoles  , publicPage=false}) => {
    const { user } = useSelector((state) => state.auth);

   console.log("user" , user)

    if (!user) return <Navigate to="/login" />;

    const hasAccess = allowedRoles.some(role => user.roles?.includes(role));
    
    console.log("has" , hasAccess)


    return hasAccess ? <Outlet /> : <Navigate to="/" />;
};

export default PrivateRoute;

