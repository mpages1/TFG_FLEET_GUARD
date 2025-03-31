import React from "react";

interface Props {
    metrics: {
        drivingTime: number;
        drivingDistance: number;
        fuelConsumption: number;
        fatigueScore: number;
        drowsinessScore: number;
        fatigueDetected: boolean;
    };
}

const DriverStats = ({metrics}: Props) => {
    console.log("DriverStats - metrics:", metrics);

    return (
        <div className="bg-gray-800 p-6 rounded-lg shadow-md space-y-4 text-white">
            <div><strong>Temps de conducció:</strong> {metrics.drivingTime?.toFixed(1)} min</div>
            <div><strong>Distància:</strong> {metrics.drivingDistance?.toFixed(2)} km</div>
            <div><strong>Consum de combustible:</strong> {metrics.fuelConsumption?.toFixed(2)} L</div>
            <div><strong>Fatiga:</strong> {metrics.fatigueScore?.toFixed(2)}</div>
            <div><strong>Somnolència:</strong> {metrics.drowsinessScore?.toFixed(2)}</div>
            <div><strong>Detecció:</strong> {metrics.fatigueDetected ? "Yes" : "No"}</div>
        </div>
    );
};

export default DriverStats;
