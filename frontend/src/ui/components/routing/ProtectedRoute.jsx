import React, {useContext} from 'react';
import {Navigate, Outlet} from 'react-router-dom';
import AuthContext from '../../../contexts/authContext.js';

const ProtectedRoute = () => {
    const {isLoggedIn, isLoading} = useContext(AuthContext);

    if (isLoading) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="text-center">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
                    <p className="text-gray-600">Loading...</p>
                </div>
            </div>
        );
    }

    return isLoggedIn ? <Outlet /> : <Navigate to="/login" replace />;
};

export default ProtectedRoute;
