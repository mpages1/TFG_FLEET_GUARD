import React from "react";
import {useAuth} from "@/shared/context/AuthContext";
import {useNavigate} from "react-router-dom";
import {FileText, LogOut, UserPlus, Users} from "lucide-react";

const ActionCard = ({icon: Icon, title, onClick}: { icon: any; title: string; onClick: () => void }) => (
    <div
        className="bg-gray-800 p-6 rounded-lg shadow-lg cursor-pointer hover:bg-gray-700 transition"
        onClick={onClick}
    >
        <div className="flex items-center space-x-4">
            <Icon className="text-pink-500 w-6 h-6"/>
            <h2 className="text-xl font-semibold">{title}</h2>
        </div>
    </div>
);

const AdminDashboard = () => {
    const {logout, role} = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    return (
        <div className="min-h-screen bg-gray-950 text-white px-6 py-10">
            <div className="max-w-6xl mx-auto space-y-8">
                <div className="flex items-center justify-between">
                    <div>
                        <h1 className="text-3xl font-bold">Admin Panel - FleetGuard</h1>
                        <p className="text-gray-400 text-sm mt-1">Gestió i supervisió de conductors en temps real</p>
                    </div>

                    <button
                        onClick={handleLogout}
                        className="bg-red-600 hover:bg-red-700 flex items-center gap-2 text-white py-2 px-4 rounded transition"
                    >
                        <LogOut className="w-4 h-4"/>
                        Logout
                    </button>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                    <ActionCard
                        icon={UserPlus}
                        title="Registrar nou conductor"
                        onClick={() => navigate("/admin/register-driver")}
                    />
                    <ActionCard
                        icon={FileText}
                        title="Visualitzar informes"
                        onClick={() => navigate("/admin/reports")}
                    />
                    <ActionCard
                        icon={Users}
                        title="Gestionar usuaris"
                        onClick={() => navigate("/admin/users")}
                    />
                </div>

                <div className="mt-10 text-gray-400 text-sm text-center">
                    {/*&copy; 2025 FleetGuard | Monitoring Platform*/}
                </div>
            </div>
        </div>
    );
};

export default AdminDashboard;
