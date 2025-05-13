import React, { useState } from 'react';
import { Footer } from "../components/Footer";
import Header from "../components/Header";
import { useNavigate, Link } from 'react-router-dom';


export default function NotFound() {
    return (
        <div className="bg-light d-flex flex-column vh-100">
            <Header />
            <main class="d-flex flex-grow-1 align-items-center justify-content-center text-center">
                <div>
                    <h1 class="display-1">404</h1>
                    <p class="lead">Page not found</p>
                    <Link to="/homepage" class="btn btn-lg btn-green-custom text-white" >Back to
                        homepage
                    </Link>
                </div>
            </main>
            <Footer />
        </div>
    );
};


