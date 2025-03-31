import React, {useEffect, useState} from "react";
import api from "@/infrastructure/api";
import {useAuth} from "@/shared/context/AuthContext";

const AdminUsersPage = () => {
    const {token} = useAuth();
    const [drivers, setDrivers] = useState<any[]>([]);
    const [editingDriver, setEditingDriver] = useState<any | null>(null);
    const [errorMsg, setErrorMsg] = useState("");

    useEffect(() => {
        const fetchDrivers = async () => {
            const res = await api.get("/drivers/all", {
                headers: {Authorization: `Bearer ${token}`},
            });
            setDrivers(res.data);
        };
        fetchDrivers();
    }, [token]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setEditingDriver({
            ...editingDriver,
            [e.target.name]: e.target.value,
        });
    };

    const handleSave = async () => {
        const {name, phone, licenseNumber, dateOfBirth, address} = editingDriver;

        if (!name || !phone || !licenseNumber || !dateOfBirth || !address) {
            setErrorMsg("All fields are required.");
            return;
        }

        try {
            await api.put(`/admin/drivers/${editingDriver.id}`, editingDriver, {
                headers: {Authorization: `Bearer ${token}`},
            });
            setEditingDriver(null);
            setErrorMsg("");
        } catch (e) {
            setErrorMsg("Error saving changes.");
        }
    };

    return (
        <div className="p-6 text-white">
            <h1 className="text-2xl font-bold mb-4">Gestió de Conductors</h1>


            <table className="w-full bg-gray-900 rounded-lg shadow-lg overflow-hidden">
                <thead>
                <tr className="bg-gray-700 text-pink-400 text-sm uppercase tracking-wider">
                    <th className="p-3 text-center border-r border-gray-600">Nom</th>
                    <th className="p-3 text-center border-r border-gray-600">Telèfon</th>
                    <th className="p-3 text-center border-r border-gray-600">LLicència</th>
                    <th className="p-3 text-center">Accions</th>
                </tr>
                </thead>
                <tbody>
                {drivers.map((driver, index) => (
                    <tr
                        key={driver.id}
                        className={index % 2 === 0 ? "bg-gray-800" : "bg-gray-850"}
                    >
                        <td className="p-3 text-center border-r border-gray-700">{driver.name}</td>
                        <td className="p-3 text-center border-r border-gray-700">{driver.phone}</td>
                        <td className="p-3 text-center border-r border-gray-700">{driver.licenseNumber}</td>
                        <td className="p-3 text-center">
                            <button
                                onClick={() => setEditingDriver(driver)}
                                className="text-blue-400 hover:text-blue-600 underline transition"
                            >
                                Edit
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>


            {editingDriver && (
                <div className="mt-6 bg-gray-900 p-4 rounded">
                    <h2 className="text-xl font-semibold mb-2">Edit Driver</h2>

                    {errorMsg && <div className="text-red-400 mb-2">{errorMsg}</div>}

                    <input name="name" placeholder="Name" value={editingDriver.name || ""} onChange={handleChange}
                           className="mb-2 block w-full p-2 rounded bg-gray-700"/>
                    <input name="phone" placeholder="Phone" value={editingDriver.phone || ""} onChange={handleChange}
                           className="mb-2 block w-full p-2 rounded bg-gray-700"/>
                    <input name="licenseNumber" placeholder="License Number" value={editingDriver.licenseNumber || ""}
                           onChange={handleChange}
                           className="mb-2 block w-full p-2 rounded bg-gray-700"/>
                    <input name="address" placeholder="Address" value={editingDriver.address || ""}
                           onChange={handleChange}
                           className="mb-2 block w-full p-2 rounded bg-gray-700"/>
                    <input type="date" name="dateOfBirth" value={editingDriver.dateOfBirth || ""}
                           onChange={handleChange}
                           className="mb-4 block w-full p-2 rounded bg-gray-700"/>

                    <div className="flex gap-4">
                        <button onClick={handleSave} className="bg-green-600 px-4 py-2 rounded">
                            Save
                        </button>
                        <button onClick={() => setEditingDriver(null)} className="bg-gray-600 px-4 py-2 rounded">
                            Cancel
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AdminUsersPage;
