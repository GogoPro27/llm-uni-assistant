import React, { useState } from 'react';
import ChatSidebar from "../components/chat/sidebar/ChatSidebar.jsx";
import ChatBot from "../components/chat/ChatBot.jsx";
import Navbar from "../components/common/Navbar.jsx";

function ChatBotPage(props) {
    const [selectedSession, setSelectedSession] = useState(null);

    const handleSessionSelect = (session) => {
        setSelectedSession(session);
    };

    return (
        <div className="flex flex-col h-full">
            <Navbar />
            <div className="flex flex-1 min-h-0">
                <ChatSidebar onSessionSelect={handleSessionSelect} selectedSession={selectedSession}/>
                <ChatBot selectedSession={selectedSession}/>
            </div>
        </div>
    );
}

export default ChatBotPage;