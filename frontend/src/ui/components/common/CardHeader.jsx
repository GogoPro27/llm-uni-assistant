import React from "react";

export default function CardHeader({ title, aside }) {
    return (
        <div className="flex items-center justify-between">
            <h3 className="text-base font-semibold text-gray-900">{title}</h3>
            {aside}
        </div>
    );
}

