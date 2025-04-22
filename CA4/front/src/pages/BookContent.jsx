import Header from '../components/Header';
import { Footer } from "../components/Footer";
import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";

const BookContent = () => {
    const { bookTitle } = useParams();
    const [data, setData] = useState("");

    const navigator = useNavigate();
    useEffect(() => {
        const fetchBookContent = async () => {
            try {
                const response = await axios.get(`/api/books/${bookTitle}/content`);
                if (!response.data.success) {
                    if (response.data.message.includes("purchased")) {
                        navigator(`/books/${bookTitle}`);
                    }
                    navigator("/notfound");
                }
                setData(response.data.data);
            } catch (err) {
                console.error("Error fetching book content:", err);
            }
        };

        fetchBookContent();
    }, [bookTitle]);

    const [searchQuery, setSearchQuery] = useState('');
    const handleSearch = (e) => {
        e.preventDefault();
        if (searchQuery.trim()) {
            // Navigate to search results
            console.log('Searching for:', searchQuery);
        }
    };

    return (
        <>
            <div className="bg-light d-flex flex-column vh-100">
                <Header searchQuery={searchQuery} onSearchChange={setSearchQuery} onSearchSubmit={handleSearch} />
                <div className="flex-grow-1 mx-1 px-4">
                    <div className="bg-light d-flex align-items-baseline justify-content-center pb-1">
                        <div className="bg-white mw-60 rounded-4 border-bottom border-2 border-brown p-md-4 w-100 mt-5 pt-5">
                            <div className="d-flex justify-content-between align-items-center px-2">
                                <div className="d-flex justify-content-start">
                                    <p className="h3 fw-bold">
                                        <img src="/assets/book_content.svg" className="me-2" alt="cart" />
                                        {data.title}
                                    </p>
                                </div>
                                <p className="d-flex align-items-center">
                                    By {data.author}
                                </p>
                            </div>
                        </div>
                    </div>
                    <div className="bg-light d-flex align-items-baseline justify-content-center">
                        <div className="bg-white mw-60 rounded-4 border-bottom border-2 border-brown p-md-4 w-100 mt-5 pt-5 mb-4 px-2">
                            <div className="d-flex justify-content-between align-items-center">
                                <p>{data.content}</p>
                            </div>
                        </div>
                    </div>
                </div>
                <Footer />
            </div>
        </>
    );
};

export default BookContent;