import React from "react";

export default function Header({ title, subtitle }) {
    return (
        <div className="mt-2">
            <h1 className="text-3xl font-semibold tracking-tight text-gray-900">{title}</h1>
            {subtitle ? <p className="mt-1 text-base text-gray-600">{subtitle}</p> : null}
        </div>
    );
}

