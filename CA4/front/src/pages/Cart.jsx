import Header from '../components/Header';
import { Footer } from "../components/Footer";
import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from "axios";
import { TmpTable } from '../components/TmpTable';

const CartTable = () => {
    const [books, setBooks] = useState([]);
    const [username, setUsername] = useState("");

    useEffect(() => {
        const fetchUserName = async () => {
            const response = await axios.get("/api/user");
            if (response.data.success) {
                setUsername(response.data.data.username);
            }
        };

        fetchUserName();
    }, []);

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


    const tableHeaders = ["Image", "Name", "Author", "Price", "Borrow Days", "",];

    return (
        <div className="card p-4 pb-0 border-0 rounded-4 pb-3">
            <div className="d-flex align-items-center mb-3">
                <img src="assets/cart.svg" className="me-2" alt="cart" />
                <div className="fs-3 fw-bold">Cart</div>
            </div>
            {books.length ? BookTable(tableHeaders, books, username, fetchBooks) : TmpTable("assets/no_product.svg")}
        </div>
    );
};

const BookRow = ({ book, username, onRemove }) => {
    const handleRemove = async () => {
        try {
            const TmpTable = (img) => {
                return (
                    <div className="text-center">
                        <img src={img} className="img-fluid" alt="No product" />
                    </div>
                );
            }
            await axios.delete("/api/cart", {
                data: {
                    username: username,
                    title: book.title,
                },
            });

            if (onRemove) {
                onRemove(book.title);
            }
        } catch (error) {
            console.error("Error removing book from cart:", error);
        }
    };

    return (
        <tr className="border-bottom">
            <td className="align-middle">
                <img
                    alt={`Cover of the book '${book.title}'`}
                    className="img-fluid"
                    src="assets/book2.png" // TODO تصویر باید به صورت استاتیک یا پویا تنظیم شود
                />
            </td>
            <td className="align-middle">
                <div className="text-sm text-dark">{book.title}</div>
            </td>
            <td className="align-middle">
                <div className="text-sm text-dark">{book.author}</div>
            </td>
            <td className="align-middle">
                <div className="text-sm text-dark">
                    {book.borrowed ? (
                        <div className="d-flex align-items-center">
                            <div className="text-decoration-line-through me-2">{`$${book.price}`}</div>
                            <div>{`$${book.price * book.borrowedDays / 10}`}</div>
                        </div>
                    ) : (
                        `$${book.price}`
                    )}
                </div>
            </td>
            <td className="align-middle">
                <div className="text-sm text-dark">
                    {book.borrowed ? (
                        book.borrowedDays
                    ) : (
                        "Not Borrowed"
                    )}
                </div>
            </td>
            <td className="align-middle">
                <button
                    className="btn bg-light rounded-3"
                    onClick={handleRemove}
                >
                    Remove
                </button>
            </td>
        </tr>
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

const BookTable = (tableHeaders, books, username, fetchBooks) => {

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

    return <div className="table-responsive rounded-4">
        <table className="table border-0 border-bottom border-light">
            <thead>
                <tr>
                    {tableHeaders.map((header, index) => (
                        <td key={index} className="text-secondary bg-light">
                            {header}
                        </td>
                    ))}
                </tr>
            </thead>
            <tbody>
                {books.map((book, index) => (
                    <BookRow key={index} book={book} username={username} onRemove={fetchBooks} />
                ))}
            </tbody>
        </table>
        <div className="d-flex mt-1 mb-4 justify-content-center">
            <button className="btn btn-green-custom mt-2 text-white rounded px-5" onClick={PurchaseCart}>
                <span className="px-md-5 px-sm-0">Purchase</span>
            </button>
        </div>
    </div>;
}

export default Cart;
