import React, { useState } from 'react';
import Header from '../components/Header';
import { Footer } from "../components/Footer";
import UserContainer from "../components/UserContainer";

const User = () => {
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
            <UserContainer/>
            <Footer />
        </div>
    );
};

export default User;