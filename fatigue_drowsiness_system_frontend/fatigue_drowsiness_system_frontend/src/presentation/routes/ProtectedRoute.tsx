import React from "react";
import {Navigate, Outlet} from "react-router-dom";
import {useAuth} from "@/shared/context/AuthContext";

const ProtectedRoute = ({allowedRole}: { allowedRole: string }) => {
    const {token, role, loading} = useAuth();
    console.log("ProtectedRoute - token:", token, "role:", role);

    if (loading) return null;

    if (!token || role !== allowedRole) {
        return <Navigate to="/login" replace/>;
    }

    return <Outlet/>;
};

export default ProtectedRoute;
