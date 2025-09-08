import React from "react";

export default function Card({ children }) {
    return (
        <div className="min-w-0 rounded-2xl border border-gray-200 bg-white p-5 shadow-sm">
            {children}
        </div>
    );
}