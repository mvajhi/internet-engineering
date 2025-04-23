import Header from '../components/Header';
import { Footer } from "../components/Footer";
import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from "axios";
import Table from '../components/Table';


const BookTable = (books) => {
    console.log(books.books);
    const columns = [
        { key: "image", header: "Image", type: "image", alt: "Cover of the book", src: "assets/book2.png" },
        { key: "title", header: "Name", type: "text" },
        { key: "author", header: "Author", type: "text" },
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
    ];
    return <div className="table-responsive rounded-4">
        <Table
            items={books.books}
            columns={columns}
            tmp_img="/assets/no_history.svg"
        />
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
                <div className="table-responsive rounded-4 border">
                    <HistoryAccordion orders={orders} />
                </div>
        </div>
    );
};

const History = () => {
    return (
        <div className='bg-light d-flex flex-column vh-100'>
            <Header />
            <br /><br />
            <div className="container w-100 w-sm-75 pt-4 flex-grow-1 mb-3">
                <HistoryTable />
            </div>
            <Footer />
        </div>
    );
};

export default History;
