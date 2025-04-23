import React from 'react';
import Header from '../components/Header';
import { Footer } from "../components/Footer";
import UserContainer from "../components/UserContainer";

const User = () => {
    return (
        <div className='bg-light d-flex flex-column vh-100'>
            <Header />
            <br /><br />
            <UserContainer />
            <Footer />
        </div>
    );
};

export default User;