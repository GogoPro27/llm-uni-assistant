import React from 'react';
import EnrollmentsGrid from "../components/enrollment/EnrollmentsGrid.jsx";
import Navbar from "../components/common/Navbar.jsx";

function EnrollmentsPage() {
    return (
        <div className="flex flex-col h-full">
            <Navbar/>
            <EnrollmentsGrid/>
        </div>
    );
}

export default EnrollmentsPage;