import React, {useEffect, useState} from 'react';
import AuthContext from "../contexts/authContext.js";
import authService from "../api/auth.js";

const decode = (token) => {
    try {
        const payload = JSON.parse(atob(token.split(".")[1]));

        if (payload.exp && payload.exp * 1000 < Date.now()) {
            console.log('Token has expired');
            return null;
        }

        return payload;
    } catch (error) {
        console.log('Invalid token format:', error);
        return null;
    }
};

const AuthProvider = ({children}) => {
    const [state, setState] = useState({
        user: null,
        isLoading: true,
        fullName: null,
        roles: [], // Add roles to the state
    });

    const login = (loginResponse) => {
        const jwtPayload = decode(loginResponse.token);
        if (jwtPayload) {
            console.log('Login successful:', loginResponse);
            authService.setToken(loginResponse.token);

            const fullName = [loginResponse.name, loginResponse.surname].filter(Boolean).join(' ');
            if (fullName) {
                localStorage.setItem('userFullName', fullName);
            }

            const roles = loginResponse.roles || []; // Extract roles from the login response
            localStorage.setItem('userRoles', JSON.stringify(roles)); // Store roles in localStorage

            setState({
                user: jwtPayload,
                isLoading: false,
                fullName,
                roles, // Update roles in the state
            });
        } else {
            logout();
        }
    };

    const logout = () => {
        authService.removeToken();
        localStorage.removeItem('userFullName');
        localStorage.removeItem('userRoles'); // Remove roles from localStorage
        setState({
            user: null,
            isLoading: false,
            fullName: null,
            roles: [], // Clear roles in the state
        });
    };

    useEffect(() => {
        const jwtToken = authService.getToken();
        if (jwtToken) {
            const payload = decode(jwtToken);
            if (payload) {
                const fullName = [payload.name, payload.surname].filter(Boolean).join(' ');
                if (fullName) {
                    localStorage.setItem('userFullName', fullName);
                }

                const roles = JSON.parse(localStorage.getItem('userRoles')) || []; // Retrieve roles from localStorage
                setState({
                    user: payload,
                    isLoading: false,
                    fullName,
                    roles, // Set roles in the state
                });
            } else {
                logout();
            }
        } else {
            setState({
                user: null,
                isLoading: false,
                fullName: null,
                roles: [],
            });
        }
    }, []);

    return (
        <AuthContext.Provider value={{login, logout, ...state, isLoggedIn: !!state.user}}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthProvider;