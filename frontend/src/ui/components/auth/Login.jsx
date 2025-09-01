import React, {useContext, useState} from 'react';
import {useNavigate} from "react-router-dom";
import AuthContext from "../../../contexts/authContext.js";
import API from "../../../api/endpoints.js";

const initialFormData = {
    "email": "",
    "password": "",
};

const Login = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState(initialFormData);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const {login} = useContext(AuthContext);

    const handleChange = (event) => {
        const {name, value} = event.target;
        setFormData({...formData, [name]: value});
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        API.auth.login(formData)
            .then((response) => {
                console.log("The user has logged in successfully!");
                login(response);
                setFormData(initialFormData);
                navigate("/chatbot", {replace: true});
            })
            .catch((error) => {
                console.error('Login error:', error);
                setError(error.message || 'Login failed. Please try again.');
            })
            .finally(() => {
                setLoading(false);
            });

    };

    return (
        <div className="min-h-screen bg-gray-50 flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
            <div className="max-w-md w-full space-y-8">
                <div className="bg-white p-8 rounded-lg shadow-md">
                    <div className="text-center">
                        <h2 className="text-3xl font-bold text-gray-900 mb-8">Login</h2>
                    </div>
                    {error && (
                        <div className="bg-red-50 border border-red-300 text-red-700 px-4 py-3 rounded mb-4">
                            {error}
                        </div>
                    )}
                    <form onSubmit={handleSubmit} className="space-y-6">
                        <div>
                            <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
                                Email
                            </label>
                            <input
                                id="email"
                                name="email"
                                type="text"
                                required
                                value={formData.email}
                                onChange={handleChange}
                                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                                placeholder="Enter your email"
                            />
                        </div>
                        <div>
                            <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-1">
                                Password
                            </label>
                            <input
                                id="password"
                                name="password"
                                type="password"
                                required
                                value={formData.password}
                                onChange={handleChange}
                                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                                placeholder="Enter your password"
                            />
                        </div>
                        <button
                            type="submit"
                            disabled={loading}
                            className="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition duration-200 disabled:opacity-50"
                        >
                            {loading ? 'Logging in...' : 'Login'}
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default Login;
