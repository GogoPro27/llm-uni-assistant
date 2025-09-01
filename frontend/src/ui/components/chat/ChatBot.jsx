import React, { useState, useEffect, useRef, useLayoutEffect } from 'react';
import API from '../../../api/endpoints.js';

function ChatBot({ selectedSession }) {
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const [loading, setLoading] = useState(false);
    const [sending, setSending] = useState(false);
    const messagesEndRef = useRef(null);
    const messagesContainerRef = useRef(null);

    useEffect(() => {
        if (selectedSession?.id) {
            fetchMessages().then(() => {
                setTimeout(scrollToBottom, 100);
            });
        } else {
            setMessages([]);
        }
    }, [selectedSession]);

    const scrollToBottom = (smooth = true) => {
        if (messagesContainerRef.current) {
            messagesContainerRef.current.scrollTop = messagesContainerRef.current.scrollHeight;
        } else {
            messagesEndRef.current?.scrollIntoView({ behavior: smooth ? 'smooth' : 'auto', block: 'end' });
        }
    };

    // When messages change during sending, snap to bottom without animation
    useLayoutEffect(() => {
        if (sending) {
            scrollToBottom(false);
        }
    }, [messages, sending]);

    const fetchMessages = async (shouldStayAtBottom = false) => {
        setLoading(true);
        return API.chatSessions.getSession(selectedSession.id)
            .then(response => {
                console.log('Fetched session data:', response);
                setMessages(response.messages || []);
                if (shouldStayAtBottom) {
                    requestAnimationFrame(() => {
                        scrollToBottom(false);
                    });
                }
            })
            .catch(error => {
                console.error('Error fetching messages:', error);
                setMessages([]);
            })
            .finally(() => {
                setLoading(false);
            });
    };

    const handleSendMessage = async (e) => {
        e.preventDefault();
        if (!newMessage.trim() || !selectedSession?.id || sending) return;

        setSending(true);
        const messageData = {
            message: newMessage.trim(),
            chatSessionId: selectedSession.id
        };

        try {
            await API.chatSessions.sendMessage(messageData);
            setNewMessage('');
            // Refresh messages after sending and snap to bottom without animation
            await fetchMessages(true);
        } catch (error) {
            console.error('Error sending message:', error);
        } finally {
            setSending(false);
        }
    };

    const formatTimestamp = (timestamp) => {
        return new Date(timestamp).toLocaleTimeString([], { 
            hour: '2-digit', 
            minute: '2-digit' 
        });
    };

    if (!selectedSession) {
        return (
            <div className="w-4/5 h-full flex items-center justify-center bg-gray-50 dark:bg-gray-900">
                <div className="text-center text-gray-500 dark:text-gray-400">
                    <div className="text-6xl mb-4">💬</div>
                    <h2 className="text-xl font-semibold mb-2">No chat session selected</h2>
                    <p>Select a chat session from the sidebar to start chatting</p>
                </div>
            </div>
        );
    }

    return (
        <div className="w-4/5 h-full flex flex-col bg-white dark:bg-gray-800">
            <div ref={messagesContainerRef} className="flex-1 overflow-y-auto px-6 py-4 space-y-4">
                {messages.length === 0 ? (
                    <div className="text-center text-gray-500 dark:text-gray-400">
                        No messages yet. Start the conversation!
                    </div>
                ) : (
                    messages.map((message, index) => (
                        <div
                            key={message.id ?? message.timeStamp ?? index}
                            className={`flex ${message.origin === 'user' ? 'justify-end' : 'justify-start'}`}
                        >
                            <div
                                className={`max-w-xs lg:max-w-md px-4 py-2 rounded-lg ${
                                    message.origin === 'user'
                                        ? 'bg-blue-500 text-white rounded-br-none'
                                        : 'bg-gray-200 dark:bg-gray-700 text-gray-900 dark:text-gray-100 rounded-bl-none'
                                }`}
                            >
                                <p className="text-sm">{message.message}</p>
                                <p className={`text-xs mt-1 ${
                                    message.origin === 'user' 
                                        ? 'text-blue-100' 
                                        : 'text-gray-500 dark:text-gray-400'
                                }`}>
                                    {formatTimestamp(message.timeStamp)}
                                </p>
                            </div>
                        </div>
                    ))
                )}
                <div ref={messagesEndRef} />
                {loading && (
                    <div className="sr-only">Loading messages...</div>
                )}
            </div>

            {/* Message Input */}
            <div className="flex-shrink-0 px-6 py-4 border-t border-gray-200 dark:border-gray-600">
                <form onSubmit={handleSendMessage} className="flex space-x-4">
                    <input
                        type="text"
                        value={newMessage}
                        onChange={(e) => setNewMessage(e.target.value)}
                        placeholder="Type your message..."
                        disabled={sending}
                        className="flex-1 px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:text-gray-100 disabled:opacity-50"
                    />
                    <button
                        type="submit"
                        disabled={!newMessage.trim() || sending}
                        className="px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                        {sending ? 'Sending...' : 'Send'}
                    </button>
                </form>
            </div>
        </div>
    );
}

export default ChatBot;