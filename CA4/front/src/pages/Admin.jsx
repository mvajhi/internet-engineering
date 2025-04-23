import React, { useState, useEffect } from 'react';
import Header from '../components/Header';
import { Footer } from "../components/Footer";
import AdminContainer from "../components/AdminContainer";

const Admin = () => {
    const [searchQuery, setSearchQuery] = useState('');
    
    const handleSearch = (e) => {
        e.preventDefault();
        if (searchQuery.trim()) {
            // Navigate to search results
            console.log('Searching for:', searchQuery);
        }
    };
    
    return (
        <div className='bg-light d-flex flex-column vh-100'>
            <Header searchQuery={searchQuery} onSearchChange={setSearchQuery} onSearchSubmit={handleSearch} />
            <br/><br/>
            <AdminContainer/>
            <Footer />
        </div>
    );
};

export default Admin;