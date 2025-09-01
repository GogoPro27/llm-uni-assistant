import React, { useState } from 'react';
import SubjectSelectGrid from './SubjectSelectGrid.jsx';
import ChatSessionSelectScroller from './ChatSessionSelectScroller.jsx';
import logo from '../../../../assets/finkilogo.jpg';

function ChatSidebar({ onSessionSelect, selectedSession }) {
    const [selectedSubject, setSelectedSubject] = useState(null);

    const handleSubjectSelect = (subject) => {
        setSelectedSubject(subject);
        if (onSessionSelect) {
            onSessionSelect(null);
        }
    };

    const handleSessionSelect = (session) => {
        if (onSessionSelect) {
            onSessionSelect(session);
        }
    };

    return (
        <div className="chat-sidebar flex flex-col h-full">
            <div className="flex-shrink-0">
                <SubjectSelectGrid
                    onSubjectSelect={handleSubjectSelect}
                    selectedSubject={selectedSubject}
                />
            </div>

            <div className="flex-1 overflow-hidden">
                <ChatSessionSelectScroller
                    selectedSubject={selectedSubject}
                    onSessionSelect={handleSessionSelect}
                    selectedSession={selectedSession}
                />
            </div>
        </div>
    );
}

export default ChatSidebar;