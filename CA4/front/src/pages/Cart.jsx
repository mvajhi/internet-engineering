import Header from '../components/Header';
import { Footer } from "../components/Footer";
import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from "axios";
import Table from '../components/Table';

const CartTable = () => {
    const [books, setBooks] = useState([]);
    const [username, setUsername] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserName = async () => {
            const response = await axios.get("/api/user");
            if (response.data.success) {
                setUsername(response.data.data.username);
            }
        };

        fetchUserName();
    }, []);

    const handleRemove = async (title) => {
        try {
            await axios.delete("/api/cart", {
                data: {
                    username: username,
                    title: title,
                },
            });

            fetchBooks();
        } catch (error) {
            console.error("Error removing book from cart:", error);
        }
    };

    const fetchBooks = async () => {
        const response = await axios.get("/api/cart");

        if (response.data.success) {
            setBooks(response.data.data.items);
        }
    };
    useEffect(() => {
        if (username) {

            fetchBooks();
        }
    }, [username]);

    const handleBookClick = (title) => {
        navigate(`/books/${title}`);
    };

    const handleAuthorClick = (author) => {
        navigate(`/authors/${author}`);
    };

    const columns = [
        { 
            key: "image", 
            header: "Image", 
            type: "image", 
            alt: "Cover of the book", 
            src: "assets/book2.png",
            customRender: (book) => (
                <img 
                    src={book.image || "assets/book2.png"} 
                    alt="Cover of the book" 
                    className="img-fluid rounded book-cover-img"
                    style={{ cursor: 'pointer', maxWidth: '50px' }}
                    onClick={() => handleBookClick(book.title)}
                />
            )
        },
        { 
            key: "title", 
            header: "Name", 
            type: "text",
            customRender: (book) => (
                <span 
                    style={{ cursor: 'pointer' }}
                    onClick={() => handleBookClick(book.title)}
                >
                    {book.title}
                </span>
            )
        },
        { 
            key: "author", 
            header: "Author", 
            type: "text",
            customRender: (book) => (
                <span 
                    style={{ cursor: 'pointer' }}
                    onClick={() => handleAuthorClick(book.author)}
                >
                    {book.author}
                </span>
            )
        },
        {
            key: "price",
            header: "Price",
            type: "text",
            customRender: (book) =>
                book.borrowed ? (
                    <div className="d-flex align-items-center">
                        <div className="text-decoration-line-through me-2">{`$${book.price}`}</div>
                        <div>{`$${book.price * book.borrowedDays / 10}`}</div>
                    </div>
                ) : (
                    `$${book.price}`
                )
        },
        {
            key: "borrowedDays",
            header: "Borrow Days",
            type: "text",
            customRender: (book) =>
                book.borrowed ? (
                    book.borrowedDays
                ) : (
                    "Not Borrowed"
                )
        },
        {
            key: "borrowedDays",
            header: "",
            type: "text",
            customRender: (book) =>
                <button
                    className="btn bg-light rounded-3"
                    onClick={() => {handleRemove(book.title)}}
                >
                    Remove
                </button>
        },
    ];

    const PurchaseCart = async () => {
        try {
            const response = await axios.post("/api/purchase", {
                username: username,
            });

            if (response.data.success) {
                fetchBooks();
            } else {
                alert(response.data.message);
            }
        } catch (error) {
            console.error("Error during purchase:", error);
        }
    }


    return (
        <div className="card p-4 pb-0 border-0 rounded-4 pb-3">
            <div className="d-flex align-items-center mb-3">
                <img src="assets/cart.svg" className="me-2" alt="cart" />
                <div className="fs-3 fw-bold">Cart</div>
            </div>
            <Table
                items={books}
                columns={columns}
                tmp_img="/assets/no_product.svg"
            />
                    <div className="d-flex mt-1 mb-4 justify-content-center">
            {books.length ? <button className="btn btn-green-custom mt-2 text-white rounded px-5" onClick={PurchaseCart}>
                <span className="px-md-5 px-sm-0">Purchase</span>
            </button> : ""}
        </div>
        </div>
    );
};

const Cart = () => {
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
            <br /><br />
            <div className="container w-100 w-sm-75 pt-4 flex-grow-1">
                <CartTable />
            </div>
            <Footer />
        </div>
    );
};

export default Cart;
