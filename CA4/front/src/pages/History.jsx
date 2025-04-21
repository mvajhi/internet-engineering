import Header from '../components/Header';
import { Footer } from "../components/Footer";
import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from "axios";
import { TmpTable } from '../components/TmpTable';

const tableHeaders = ["Image", "Name", "Author", "Price", "Borrow Days",];

const BookRow = ({ book }) => {
    console.log(book);
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
        </tr>
    );
};

const BookTable = (books) => {
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
                {books.books.map((book, index) => (
                    <BookRow key={index} book={book} />
                ))}
            </tbody>
        </table>
    </div>;
}

const AccordionItem = ({ id, purchaseDate, totalCost, books }) => {
    return (
        <div className="accordion-item">
            <h2 className="accordion-header" id={`flush-heading${id}`}>
                <button
                    className="accordion-button collapsed"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target={`#flush-collapse${id}`}
                    aria-expanded="false"
                    aria-controls={`flush-collapse${id}`}
                >
                    {purchaseDate} | ${totalCost}
                </button>
            </h2>
            <div
                id={`flush-collapse${id}`}
                className="accordion-collapse collapse"
                aria-labelledby={`flush-heading${id}`}
                data-bs-parent="#accordionFlushExample"
            >
                <div className="accordion-body">
                    <BookTable books={books} />
                </div>
            </div>
        </div>
    );
};

const HistoryAccordion = ({ orders }) => {
    console.log(orders)
    return (
        <div className="accordion accordion-flush" id="accordionFlushExample">
            {orders.slice().reverse().map((order, index) => (
                <AccordionItem
                    key={index}
                    id={index + 1}
                    purchaseDate={order.purchaseDate}
                    totalCost={order.totalCost}
                    books={order.items}
                />
            ))}
        </div>
    );
};


const HistoryTable = () => {
    const [username, setUsername] = useState("");
    const [orders, setOrders] = useState([]);

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
        const response = await axios.get("/api/purchase/history");

        if (response.data.success) {
            setOrders(response.data.data.purchasesHistory);
        }
    };
    useEffect(() => {
        if (username) {
            fetchBooks();
        }
    }, [username]);

    return (
        <div className="card p-4 pb-0 border-0 rounded-4 pb-3">
            <div className="d-flex align-items-center mb-3">
                <img src="assets/history.svg" className="me-2" alt="cart" />
                <div className="fs-3 fw-bold">History</div>
            </div>
            {orders.length ?
                <div className="table-responsive rounded-4 border">
                    <HistoryAccordion orders={orders} />
                </div>
                : TmpTable("assets/no_history.svg")}
        </div>
    );
};

const History = () => {
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
            <div className="container w-100 w-sm-75 pt-4 flex-grow-1 mb-3">
                <HistoryTable />
            </div>
            <Footer />
        </div>
    );
};

export default History;
