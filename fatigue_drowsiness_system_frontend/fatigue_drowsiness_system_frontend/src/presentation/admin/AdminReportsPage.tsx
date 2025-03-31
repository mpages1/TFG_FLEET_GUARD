import React, {useEffect, useState} from "react";
import api from "@/infrastructure/api";
import {useAuth} from "@/shared/context/AuthContext";

const AdminReportsPage = () => {
    const [reports, setReports] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const {token} = useAuth();

    useEffect(() => {
        const fetchReports = async () => {
            try {
                const res = await api.get(`/admin/reports?page=${page}&size=10`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                console.log("Reports fetched:", res.data);
                setReports(res.data.content);
                setTotalPages(res.data.totalPages);
            } catch (error) {
                console.error("Error loading reports", error);
            }
        };

        fetchReports();
    }, [page, token]);

    return (
        <div className="p-6 text-white">
            <h1 className="text-3xl font-bold mb-6 text-center text-pink-400">Informes del Conductor</h1>
            <div className="overflow-x-auto">
                <table className="w-full bg-gray-900 rounded-lg shadow-lg overflow-hidden">
                    <thead>
                    <tr className="bg-gray-700 text-pink-400 text-sm uppercase tracking-wider">
                        <th className="p-3 text-center border-r border-gray-600">ID</th>
                        <th className="p-3 text-center border-r border-gray-600">Hora</th>
                        <th className="p-3 text-center border-r border-gray-600">Fatiga</th>
                        <th className="p-3 text-center">Detecció</th>
                    </tr>
                    </thead>
                    <tbody>
                    {reports.map((r: any, index) => (
                        <tr
                            key={index}
                            className={index % 2 === 0 ? "bg-gray-800" : "bg-gray-850"}
                        >
                            <td className="p-3 text-center border-r border-gray-700">{r.driverId}</td>
                            <td className="p-3 text-center border-r border-gray-700">{r.latestTimestampCamera}</td>
                            <td className="p-3 text-center border-r border-gray-700">{r.fatigueScore.toFixed(2)}</td>
                            <td className="p-3 text-center">
                                {r.fatigueDetected ? (
                                    <span className="text-red-500 font-semibold">SI</span>
                                ) : (
                                    <span className="text-green-400 font-semibold">No</span>
                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            <div className="mt-6 flex justify-center gap-4 items-center text-sm">
                <button
                    onClick={() => setPage((prev) => Math.max(prev - 1, 0))}
                    disabled={page === 0}
                    className="bg-gray-700 px-4 py-2 rounded disabled:opacity-50 hover:bg-gray-600"
                >
                    Anterior
                </button>
                <span className="text-gray-300">Pàgina {page + 1} of {totalPages}</span>
                <button
                    onClick={() => setPage((prev) => Math.min(prev + 1, totalPages - 1))}
                    disabled={page >= totalPages - 1}
                    className="bg-gray-700 px-4 py-2 rounded disabled:opacity-50 hover:bg-gray-600"
                >
                    següent
                </button>
            </div>
        </div>
    );
};

export default AdminReportsPage;
