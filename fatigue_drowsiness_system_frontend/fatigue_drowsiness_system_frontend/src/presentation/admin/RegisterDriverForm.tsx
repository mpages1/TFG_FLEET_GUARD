import React, {useState} from "react";
import api from "@/infrastructure/api";

const RegisterDriverForm = () => {
    const [formData, setFormData] = useState({
        name: "",
        email: "",
        password: "",
        licenseNumber: "",
        phone: "",
        dateOfBirth: "",
        address: "",
        roleId: 2,
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({...formData, [e.target.name]: e.target.value});
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        await api.post("/auth/register", formData);
        alert("Driver registered successfully!");
    };

    return (
        <div className="max-w-xl mx-auto mt-10 bg-gray-800 text-white p-6 rounded shadow">
            <h2 className="text-2xl font-bold mb-4">Enregistrar un nou conductor</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
                {Object.entries(formData).map(([key, value]) =>
                    key !== "roleId" ? (
                        <input
                            key={key}
                            name={key}
                            value={value}
                            onChange={handleChange}
                            placeholder={key.replace(/([A-Z])/g, " $1")}
                            className="w-full p-2 rounded bg-gray-700 border border-gray-600 placeholder-gray-400"
                            required
                        />
                    ) : null
                )}
                <button type="submit" className="w-full bg-green-600 py-2 rounded hover:bg-green-700">
                    Enregistrar
                </button>
            </form>
        </div>
    );
};

export default RegisterDriverForm;