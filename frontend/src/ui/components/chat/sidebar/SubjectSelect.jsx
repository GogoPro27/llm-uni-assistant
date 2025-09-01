import React from 'react';

function SubjectSelect({ subject, isSelected, onSelect }) {
    const handleClick = () => {
        onSelect(subject);
    };

    return (
        <div
            onClick={handleClick}
            className={`
        w-16 h-8 border-2 rounded-full px-2 py-1 cursor-pointer transition-all duration-200 
        hover:bg-slate-800 hover:scale-105 flex items-center justify-center
        ${isSelected
                ? 'border-blue-500 bg-slate-800 font-bold text-blue-400'
                : 'border-slate-600 text-slate-300 hover:border-slate-500'
            }
      `}
        >
            <span className="text-sm font-medium">{subject.shortName}</span>
        </div>
    );
}

export default SubjectSelect;
