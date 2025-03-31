import React, {useEffect, useState} from "react";
import api from "@/infrastructure/api";

const DriverList = () => {
    const [drivers, setDrivers] = useState([]);

    useEffect(() => {
        api.get("/drivers")
            .then((res) => setDrivers(res.data))
            .catch((err) => console.error("Error loading drivers", err));
    }, []);

    return (
        <div className="max-w-5xl mx-auto mt-10 text-white">
            <h2 className="text-2xl font-bold mb-4">Driver List</h2>
            <table className="w-full border border-gray-600">
                <thead className="bg-gray-700">
                <tr>
                    <th className="p-2">Name</th>
                    <th className="p-2">Email</th>
                    <th className="p-2">Phone</th>
                    <th className="p-2">License</th>
                    <th className="p-2">Address</th>
                </tr>
                </thead>
                <tbody>
                {drivers.map((driver: any, index) => (
                    <tr key={index} className="bg-gray-800 border-t border-gray-600">
                        <td className="p-2">{driver.name}</td>
                        <td className="p-2">{driver.email}</td>
                        <td className="p-2">{driver.phone}</td>
                        <td className="p-2">{driver.licenseNumber}</td>
                        <td className="p-2">{driver.address}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default DriverList;