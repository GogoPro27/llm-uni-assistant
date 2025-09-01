import React from 'react';

function ChatSessionSelect({ session, isSelected, onSelect }) {
    const handleClick = () => {
        onSelect(session);
    };

    return (
        <div 
            className={`
                p-3 mx-2 my-1 rounded-lg cursor-pointer transition-colors duration-200
                hover:bg-gray-100 dark:hover:bg-gray-700
                ${isSelected 
                    ? 'bg-gray-200 dark:bg-gray-600' 
                    : 'bg-white dark:bg-gray-800'
                }
                border border-gray-200 dark:border-gray-600
            `}
            onClick={handleClick}
        >
            <div className="text-sm font-medium text-gray-900 dark:text-gray-100 truncate">
                {session.title || 'New Chat'}
            </div>
            {/*<div className="text-xs text-gray-500 dark:text-gray-400 mt-1">*/}
            {/*    {new Date(session.createdAt).toLocaleDateString()}*/}
            {/*</div>*/}
        </div>
    );
}

export default ChatSessionSelect;