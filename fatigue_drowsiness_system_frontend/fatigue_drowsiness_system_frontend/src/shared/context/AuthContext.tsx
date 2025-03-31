import React, {createContext, useContext, useEffect, useState} from "react";

interface AuthContextType {
    token: string | null;
    role: string | null;
    userId: number | null;
    login: (token: string, role: string, id: number) => void;
    logout: () => void;
    loading: boolean;
}

const AuthContext = createContext<AuthContextType>({} as AuthContextType);

export const AuthProvider = ({children}: { children: React.ReactNode }) => {
    const [token, setToken] = useState<string | null>(null);
    const [role, setRole] = useState<string | null>(null);
    const [userId, setUserId] = useState<number | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const storedToken = localStorage.getItem("token");
        const storedRole = localStorage.getItem("role");
        const storedId = localStorage.getItem("id");
        if (storedId) setUserId(Number(storedId));
        if (storedToken) setToken(storedToken);
        if (storedRole) setRole(storedRole);
        setLoading(false);
    }, []);

    const login = (token: string, role: string, id: number) => {
        localStorage.setItem("token", token);
        localStorage.setItem("role", role);
        localStorage.setItem("id", id.toString());
        setToken(token);
        setRole(role);
        setUserId(id);
    };

    const logout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("role");
        setToken(null);
        setRole(null);
    };

    return (
        <AuthContext.Provider value={{token, role, userId, login, logout, loading}}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
