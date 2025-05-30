import { useEffect, useState } from 'react';
import Header from '../components/Header';
import BookCard from '../components/BookCard';
import TopRatedBooks from "../components/TopRated.jsx";
import { getTopRated } from "../components/TopRated.jsx"; 
import NewReleases from "../components/NewReleases.jsx";
import { getNewReleases } from "../components/NewReleases.jsx";
import { Footer } from "../components/Footer";

const HomePage = () => {
    return (
        <div className="bg-light">
            <Header />

            <br /><br />
            <div className="p-4 rounded-3 d-flex flex-column flex-md-row align-items-center w-100 hello-box">
                <div className="container py-5 px-4">
                    <div className="p-4 rounded-3 d-flex flex-column flex-md-row align-items-center w-60">
                        <div className="text-center text-md-start col-md-8 px-4 ps-md-5">
                            <p className="mb-3 fs-4 ps-md-5">
                                Welcome to MioBook – the online bookstore where you can buy or borrow books with ease.
                            </p>
                            <p className="mb-3 fs-4 ps-md-5">
                                Whether you're looking for the latest bestseller, a classic novel, or a niche title, MioBook has
                                you
                                covered.
                            </p>
                            <p className="mb-3 fs-4 ps-md-5">
                                Here, you can quickly find books by title, author, and genre. And if you’re not sure to buy? Try
                                borrowing instead! Rent a book for just a fraction of the price and enjoy full access for a set
                                period.
                            </p>
                            <p className="mb-3 fs-4 ps-md-5">
                                Your next great read is just a click away. Visit MioBook today and let the perfect book find
                                you!
                                📚✨
                            </p>
                        </div>
                        <div className="col-md-4 mt-4 mt-md-0 text-end pe-md-5">
                            <img src="/assets/shelf.svg" className="img-fluid pe-md-2" alt="" />
                        </div>
                    </div>
                </div>
            </div>

            <div className="container py-5 bg-light">
                <NewReleases />
                <TopRatedBooks />
            </div>
            <Footer />

        </div>
    );
};

export default HomePage;