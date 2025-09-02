import React from 'react';
import {useNavigate} from 'react-router-dom';

function EnrollmentCard({enrollment}) {
    const {subjectName, professorGroupName, professorGroupId} = enrollment;
    const navigate = useNavigate();

    // Check if the user has the "professor" role
    const userRoles = JSON.parse(localStorage.getItem('userRoles')) || [];
    const isProfessor = userRoles.includes('professor');

    return (
        <div
            className="border border-gray-300 rounded-lg p-4 shadow-md hover:shadow-lg transition-shadow bg-white dark:bg-gray-800">
            <h3 className="text-lg font-semibold text-gray-900 dark:text-gray-100">
                {subjectName}
            </h3>
            <p className="text-sm text-gray-700 dark:text-gray-300">
                Professor Group: {professorGroupName}
            </p>
            {isProfessor && (
                <button
                    className="mt-2 px-4 py-2 bg-blue-600 text-white text-sm font-medium rounded hover:bg-blue-700 transition-colors"
                    onClick={() => navigate(`/enrollments/${professorGroupId}`)}
                >
                    View
                </button>
            )}
        </div>
    );
}

export default EnrollmentCard;