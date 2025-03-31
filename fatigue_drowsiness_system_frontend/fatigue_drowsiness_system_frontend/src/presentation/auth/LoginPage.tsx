import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {useAuth} from "@/shared/context/AuthContext";
import api from "@/infrastructure/api";

const LoginPage = () => {
    const {login, token, role} = useAuth();
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorMsg, setErrorMsg] = useState("");
    const [loginCompleted, setLoginCompleted] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const res = await api.post("/auth/login", {email, password});
            const {token, role, id} = res.data;
            login(token, role, id);
            setLoginCompleted(true);
        } catch {
            setErrorMsg("Invalid credentials.");
        }
    };

    useEffect(() => {
        if (loginCompleted && token && role) {
            navigate(role === "admin" ? "/admin" : "/driver");
        }
    }, [loginCompleted, token, role, navigate]);

    return (
        <div
            className="min-h-screen bg-cover bg-center flex items-center justify-center px-4"
            style={{backgroundImage: "url('/bg-fatigue.jpg')"}}
        >
            <div
                className="bg-black bg-opacity-60 backdrop-blur-sm p-8 rounded-lg max-w-md w-full text-white shadow-2xl border border-gray-600">
                <h1 className="text-3xl font-bold text-center mb-2">FleetGuard Login</h1>
                <p className="text-sm text-center text-gray-300 mb-6">Fatigue & Drowsiness Monitoring System</p>

                {errorMsg && (
                    <div className="bg-red-500 text-white p-2 rounded mb-4 text-center text-sm">
                        {errorMsg}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-4">
                    <input
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        className="w-full bg-transparent border-b border-gray-400 text-white placeholder-gray-400 py-2 focus:outline-none focus:border-blue-500"
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        className="w-full bg-transparent border-b border-gray-400 text-white placeholder-gray-400 py-2 focus:outline-none focus:border-blue-500"
                    />
                    <button
                        type="submit"
                        className="w-full bg-pink-600 hover:bg-pink-700 text-white py-2 rounded font-semibold transition"
                    >
                        Submit
                    </button>
                </form>

                <p className="text-xs text-center text-gray-500 mt-6">
                    &copy; 2025 FleetGuard. All rights reserved.
                </p>
            </div>
        </div>
    );
};

export default LoginPage;
