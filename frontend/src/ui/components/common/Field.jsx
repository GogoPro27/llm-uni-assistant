import React from "react";

export default function Field({ label, children }) {
    return (
        <label className="block">
            <span className="block text-sm font-medium text-gray-700">{label}</span>
            {children}
        </label>
    );
}


