import React, {useContext, useEffect, useRef, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import logo from '../../../assets/finkilogo.jpg';
import AuthContext from '../../../contexts/authContext.js';

function Navbar() {
    const navigate = useNavigate();
    const {logout} = useContext(AuthContext);
    const fullName = localStorage.getItem('userFullName') || '';
    const initials = fullName
        ? fullName.split(' ').map(n => n[0]).join('').toUpperCase()
        : '';
    const showUser = Boolean(fullName);

    const [dropdownOpen, setDropdownOpen] = useState(false);
    const dropdownRef = useRef(null);

    const toggleDropdown = () => {
        setDropdownOpen((prev) => !prev);
    };

    const handleLogout = () => {
        logout();
        setDropdownOpen(false);
    };

    // Close dropdown when clicking outside
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setDropdownOpen(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    return (
        <header
            className="flex items-center justify-between px-4 py-2 border-b border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-900 relative">
            <div className="flex items-center gap-3 cursor-pointer" onClick={() => navigate('/')}>
                <img src={logo} alt="Logo" className="h-8 w-auto"/>
            </div>
            <div className="relative flex items-center gap-2 ml-auto">
                {showUser ? (
                    <>
                        <div
                            className="h-8 w-8 rounded-full bg-blue-600 text-white flex items-center justify-center text-sm font-semibold select-none cursor-pointer"
                            onClick={toggleDropdown}
                        >
                            {initials}
                        </div>
                        {dropdownOpen && (
                            <div
                                ref={dropdownRef}
                                className="absolute right-0 top-full mt-1 w-48 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-lg shadow-lg z-10"
                            >
                                <button
                                    className="block w-full text-left px-4 py-2 text-sm text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700"
                                    onClick={handleLogout}
                                >
                                    Logout
                                </button>
                                <button
                                    className="block w-full text-left px-4 py-2 text-sm text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700"
                                    onClick={() => {
                                        setDropdownOpen(false);
                                        navigate('/enrollments'); // Navigate to EnrollmentsPage
                                    }}
                                >
                                    My Enrollments
                                </button>
                            </div>
                        )}
                        <span className="text-sm font-medium text-gray-900 dark:text-gray-100">
                            {fullName}
                        </span>
                    </>
                ) : (
                    <span className="text-sm text-gray-500 dark:text-gray-400">Guest</span>
                )}
            </div>
        </header>
    );
}

export default Navbar;
