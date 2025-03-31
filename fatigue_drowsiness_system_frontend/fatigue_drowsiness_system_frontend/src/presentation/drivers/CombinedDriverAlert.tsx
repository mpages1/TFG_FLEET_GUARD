import React from "react";

interface Props {
    fatigueDetected: boolean;
}

const CombinedDriverAlert = ({fatigueDetected}: Props) => {
    if (!fatigueDetected) return null;

    return (
        <div className="text-white text-center p-4 rounded shadow-md animate-pulse bg-red-700 text-xl font-bold">
            Fatiga o somnolència detectada! Si us plau, atureu el vehicle immediatament i descanseu.
        </div>
    );
};

export default CombinedDriverAlert;
