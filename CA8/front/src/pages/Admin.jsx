import { useState } from 'react';
import Header from '../components/Header';
import { Footer } from "../components/Footer";
import AdminContainer from "../components/AdminContainer";

const Admin = () => {
    return (
        <div className="bg-light d-flex flex-column vh-100">
            <Header />
            <br /><br />
            <AdminContainer />
            <Footer />
        </div>
    );
};

export default Admin;