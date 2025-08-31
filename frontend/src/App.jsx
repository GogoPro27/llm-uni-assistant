import React from 'react';
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import Login from "./ui/components/auth/Login.jsx";
import ProtectedRoute from "./ui/components/routing/ProtectedRoute.jsx";
import ChatBotPage from "./ui/pages/ChatBotPage.jsx";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<Login/>}/>
                <Route path="/" element={<ProtectedRoute/>}>
                    <Route index element={<Navigate to="/chatbot" replace/>}/>
                    <Route path="chatbot" element={<ChatBotPage/>}/>
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
