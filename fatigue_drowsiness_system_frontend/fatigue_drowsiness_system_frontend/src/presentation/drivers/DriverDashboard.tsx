import React, {useEffect, useState} from "react";
import {connectWebSocket, disconnectWebSocket} from "@/shared/websocket/WebSocketClient";
import api from "@/infrastructure/api";
import {useAuth} from "@/shared/context/AuthContext";
import DriverStats from "@/presentation/drivers/DriverStats";
import CombinedDriverAlert from "@/presentation/drivers/CombinedDriverAlert";
import {MdPause, MdPlayArrow} from "react-icons/md";

const DriverDashboard = () => {
    const [detectionActive, setDetectionActive] = useState(false);
    const [metrics, setMetrics] = useState<any>(null);
    const {token, userId} = useAuth();

    useEffect(() => {
        if (!userId || !detectionActive) {
            setMetrics(null);
            return;
        }

        connectWebSocket(
            userId,
            () => {
            },
            (metricsUpdate) => setMetrics(metricsUpdate)
        );

        return () => {
            disconnectWebSocket();
        };
    }, [userId, detectionActive]);

    const startDetection = async () => {
        await api.post(`/detection/start_all`, null, {
            params: {userId},
            headers: {Authorization: `Bearer ${token}`},
        });
        setDetectionActive(true);
    };

    const stopDetection = async () => {
        await api.post(`/detection/stop_all`, null, {
            params: {userId},
            headers: {Authorization: `Bearer ${token}`},
        });
        disconnectWebSocket();
        setMetrics(null);
        setDetectionActive(false);
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-gray-900 via-gray-950 to-black text-white p-6">
            <div className="max-w-5xl mx-auto space-y-8">
                <h1 className="text-4xl font-extrabold text-center text-pink-500 drop-shadow-sm">
                    Tauler de control
                </h1>

                <div className="flex justify-center gap-6">
                    <button
                        onClick={startDetection}
                        disabled={detectionActive}
                        className="flex items-center gap-2 bg-green-600 hover:bg-green-700 disabled:bg-gray-700 px-6 py-3 rounded-xl font-semibold shadow transition"
                    >
                        <MdPlayArrow size={24}/> Preparat per començar
                    </button>
                    <button
                        onClick={stopDetection}
                        disabled={!detectionActive}
                        className="flex items-center gap-2 bg-yellow-500 hover:bg-yellow-600 disabled:bg-gray-700 px-6 py-3 rounded-xl font-semibold shadow transition"
                    >
                        <MdPause size={24}/> Prendre un descans
                    </button>
                </div>

                {metrics && (
                    <>
                        <CombinedDriverAlert fatigueDetected={metrics?.fatigueDetected === true}/>
                        <DriverStats metrics={metrics}/>
                    </>
                )}

                {!metrics && detectionActive && (
                    <p className="text-center text-gray-400 text-sm">Esperant dades de detecció...</p>
                )}
            </div>
        </div>
    );
};

export default DriverDashboard;
