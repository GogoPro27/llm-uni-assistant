import React from "react";
import { Link } from "react-router-dom";

export default function Breadcrumbs({ items }) {
    if (!items?.length) return null;
    return (
        <nav className="text-sm text-gray-600" aria-label="Breadcrumb">
            <ol className="flex items-center gap-2">
                {items.map((item, idx) => (
                    <li key={`${item.label}-${idx}`} className="flex items-center gap-2">
                        {item.to ? (
                            <Link to={item.to} className="text-indigo-600 hover:text-indigo-700 hover:underline">
                                {item.label}
                            </Link>
                        ) : (
                            <span className="text-gray-700">{item.label}</span>
                        )}
                        {idx < items.length - 1 && <span className="text-gray-400">/</span>}
                    </li>
                ))}
            </ol>
        </nav>
    );
}

