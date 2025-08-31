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
    });

    const login = (jwtToken) => {
        const payload = decode(jwtToken);
        if (payload) {
            authService.setToken(jwtToken);
            setState({
                user: payload,
                isLoading: false,
            });
        } else {
            logout();
        }
    };

    const logout = () => {
        authService.removeToken();
        setState({
            user: null,
            isLoading: false,
        });
    }

    useEffect(() => {
        const jwtToken = authService.getToken();
        if (jwtToken) {
            const payload = decode(jwtToken);
            if (payload) {
                setState({
                    user: payload,
                    isLoading: false,
                });
            } else {
                logout();
            }
        } else {
            setState({
                user: null,
                isLoading: false,
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