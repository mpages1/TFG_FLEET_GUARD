import React from "react";
import {Navigate, Route, Routes} from "react-router-dom";
import LoginPage from "@/presentation/auth/LoginPage";
import AdminDashboard from "@/presentation/admin/AdminDashboard";
import RegisterDriverForm from "@/presentation/admin/RegisterDriverForm";
import DriverList from "@/presentation/admin/DriverList";
import ProtectedRoute from "@/presentation/routes/ProtectedRoute";
import {useAuth} from "@/shared/context/AuthContext";
import DriverDashboard from "@/presentation/drivers/DriverDashboard";
import AdminReportsPage from "@/presentation/admin/AdminReportsPage";
import AdminUsersPage from "@/presentation/admin/AdminUsersPage";


export const AppRoutes = () => {
    const {loading} = useAuth();

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center text-white">
                Loading...
            </div>
        );
    }

    return (

        <Routes>
            <Route path="/login" element={<LoginPage/>}/>

            {/*admin*/}
            <Route element={<ProtectedRoute allowedRole="admin"/>}>
                <Route path="/admin" element={<AdminDashboard/>}/>
                <Route path="/admin/register-driver" element={<RegisterDriverForm/>}/>
                <Route path="/admin/drivers" element={<DriverList/>}/>
                <Route path="/admin/users" element={<AdminUsersPage/>}/>
                <Route path="/admin/reports" element={<AdminReportsPage/>}/>
            </Route>

            {/* Driver */}
            <Route element={<ProtectedRoute allowedRole="driver"/>}>
                <Route path="/driver" element={<DriverDashboard/>}/>
            </Route>


            {/* Redirect for default */}
            <Route path="*" element={<Navigate to="/login"/>}/>
        </Routes>

    );
};
