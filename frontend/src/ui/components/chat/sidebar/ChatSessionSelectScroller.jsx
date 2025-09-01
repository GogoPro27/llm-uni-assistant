import React, { useState, useEffect } from 'react';
import ChatSessionSelect from './ChatSessionSelect.jsx';
import API from '../../../../api/endpoints.js';

function ChatSessionSelectScroller({ selectedSubject, onSessionSelect, selectedSession }) {
    const [sessions, setSessions] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [opening, setOpening] = useState(false);

    useEffect(() => {
        if (selectedSubject?.id) {
            console.log('Fetching sessions for subject:', selectedSubject.id);
            fetchSessions();
        } else {
            setSessions([]);
        }
    }, [selectedSubject]);

    const fetchSessions = async () => {
        setLoading(true);
        setError(null);
        return API.chatSessions.getSessionsBySubjectId(selectedSubject.id)
            .then(response => {
                console.log('Fetched chat sessions:', response);
                const sortedSessions = (response || []).sort((a, b) =>
                    new Date(b.updatedAt) - new Date(a.updatedAt)
                );
                setSessions(sortedSessions);
                return sortedSessions;
            })
            .catch(err => {
                setError('Failed to load chat sessions');
                console.error('Error fetching chat sessions:', err);
                return [];
            })
            .finally(() => {
                setLoading(false);
            });
    };

    const handleOpenSession = async () => {
        if (!selectedSubject?.id || opening) return;
        try {
            setOpening(true);
            const created = await API.chatSessions.openSession(selectedSubject.id);
            const updated = await fetchSessions(); // refresh, newest will be at top due to sorting
            const sessionToSelect = created?.id ? created : (updated && updated.length ? updated[0] : null);
            if (sessionToSelect && onSessionSelect) {
                onSessionSelect(sessionToSelect);
            }
        } catch (err) {
            console.error('Error opening new chat session:', err);
        } finally {
            setOpening(false);
        }
    };

    const renderContent = () => {
        if (!selectedSubject) {
            return (
                <div className="p-4 text-center text-gray-500 dark:text-gray-400">
                    Select a subject to view chat sessions
                </div>
            );
        }

        if (loading) {
            return (
                <div className="p-4 text-center text-gray-500 dark:text-gray-400">
                    Loading sessions...
                </div>
            );
        }

        if (error) {
            return (
                <div className="p-4 text-center text-red-500">
                    {error}
                </div>
            );
        }

        if (sessions.length === 0) {
            return (
                <div className="p-4 text-center text-gray-500 dark:text-gray-400">
                    No chat sessions yet
                </div>
            );
        }

        return (
            <div className="flex-1 overflow-y-auto">
                {sessions.map((session) => (
                    <ChatSessionSelect
                        key={session.id}
                        session={session}
                        isSelected={selectedSession?.id === session.id}
                        onSelect={onSessionSelect}
                    />
                ))}
            </div>
        );
    };

    return (
        <div className="flex flex-col h-full">
            <div className="px-4 py-2 border-b border-gray-200 dark:border-gray-600">
                <div className="flex items-center gap-2">
                    <button
                        type="button"
                        onClick={handleOpenSession}
                        disabled={!selectedSubject || loading || opening}
                        title="Open new session"
                        aria-label="Open new session"
                        className={`inline-flex items-center justify-center h-6 w-6 rounded border text-blue-600 border-gray-300 hover:bg-gray-100 disabled:opacity-50 disabled:cursor-not-allowed dark:border-gray-600 dark:hover:bg-gray-700`}
                    >
                        <span className="text-base leading-none">+</span>
                    </button>
                    <h3 className="text-sm font-semibold text-gray-900 dark:text-gray-100">
                        Chat Sessions
                    </h3>
                </div>
            </div>
            {renderContent()}
        </div>
    );
}

export default ChatSessionSelectScroller;