import React, { useState, useEffect } from 'react';
import Header from '../components/Header.jsx';
import {Footer} from '../components/Footer.jsx';
import BookCard from '../components/BookCard.jsx';

const tempBooksData = [
    {
        title: "Book Title 1",
        author: "Author One",
        price: 10.25,
        rating: 4,
        imageUrl: "assets/book.png",
        link: "/book1"
    },
    {
        title: "Book Title 2",
        author: "Author One",
        price: 13.25,
        rating: 2,
        imageUrl: "assets/book.png",
        link: "/book"
    },
    {
        title: "Who is Mehdi Vajhi",
        author: "Author One",
        price: 500.25,
        rating: 4,
        imageUrl: "assets/book.png",
        link: "/book"
    },
    {
        title: "Mehdi Vajhi rises",
        author: "ALi Momtahen",
        price: 313,
        rating: 5,
        imageUrl: "assets/book.png",
        link: "/book"
    },
    {
        title: "Mehdi Vajhi rises2",
        author: "ALi Momtahen",
        price: 1000,
        rating: 5,
        imageUrl: "assets/book.png",
        link: "/book"
    },
];

const SearchPage = () => {
    // State for search parameters and results
    const [searchTerm, setSearchTerm] = useState('');
    const [searchType, setSearchType] = useState('Title');
    const [books, setBooks] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [currentPage, setCurrentPage] = useState(1);
    const booksPerPage = 2;

    // Mock data - replace with actual API call
    const mockBooks = Array(15).fill().map((_, i) => ({
        title: `Book Title ${i + 1}`,
        author: `Author ${String.fromCharCode(65 + (i % 5))}`, // A, B, C, etc.
        price: (10 + (i * 0.5)).toFixed(2),
        rating: 3 + (i % 3), // Ratings between 3-5
        imageUrl: 'public/assets/book.png',
        link: `/book/${i + 1}`
    }));

    // Simulate API search
    const searchBooks = () => {
        setIsLoading(true);

        // Simulate API delay
        setTimeout(() => {
            const filteredBooks = tempBooksData

            //     mockBooks.filter(book => {
            //     const term = searchTerm.toLowerCase();
            //     if (searchType === 'Title') {
            //         return book.title.toLowerCase().includes(term);
            //     } else if (searchType === 'Author') {
            //         return book.author.toLowerCase().includes(term);
            //     }
            //     return true;
            // });

            setBooks(filteredBooks);
            setIsLoading(false);
            setCurrentPage(1); // Reset to first page on new search
        }, 500);
    };

    // Initial load or when search parameters change
    useEffect(() => {
        searchBooks();
    }, [searchTerm, searchType]);

    // Pagination logic
    // const indexOfLastBook = currentPage * booksPerPage;
    // const indexOfFirstBook = indexOfLastBook - booksPerPage;
    const currentBooks = books //.slice(indexOfFirstBook, indexOfLastBook);
    const totalPages = Math.ceil(books.length / booksPerPage);

    const paginate = (pageNumber) => setCurrentPage(pageNumber);

    let hghgg = 0;
    return (
        <>
            <Header />

            <main className="container pt-5 px-2 mt-4">
                {/* Search Controls */}
                {/*<div className="row mb-4">*/}
                {/*    <div className="col-md-8 mx-auto">*/}
                {/*        <div className="input-group">*/}
                {/*            <select*/}
                {/*                className="form-select w-auto"*/}
                {/*                value={searchType}*/}
                {/*                onChange={(e) => setSearchType(e.target.value)}*/}
                {/*            >*/}
                {/*                <option value="Title">Title</option>*/}
                {/*                <option value="Author">Author</option>*/}
                {/*                <option value="Genre">Genre</option>*/}
                {/*            </select>*/}
                {/*            <input*/}
                {/*                type="text"*/}
                {/*                className="form-control"*/}
                {/*                placeholder={`Search by ${searchType}...`}*/}
                {/*                value={searchTerm}*/}
                {/*                onChange={(e) => setSearchTerm(e.target.value)}*/}
                {/*            />*/}
                {/*            <button*/}
                {/*                className="btn btn-green-custom text-white"*/}
                {/*                onClick={searchBooks}*/}
                {/*            >*/}
                {/*                <i className="fas fa-search me-2"></i>Search*/}
                {/*            </button>*/}
                {/*        </div>*/}
                {/*    </div>*/}
                {/*</div>*/}

                {/* Results Header */}
                <h2 className="fw-light fs-3 lh-sm px-3 pb-4">
                    {searchTerm
                        ? `Results for "${searchTerm}" in ${searchType}`
                        : "Browse All Books"}
                    {books.length > 0 && ` (${books.length} found)`}
                </h2>

                {/* Loading State */}
                {isLoading && (
                    <div className="text-center py-5">
                        <div className="spinner-border text-green-custom" role="status">
                            <span className="visually-hidden">Loading...</span>
                        </div>
                    </div>
                )}

                {/* Results Grid */}
                {!isLoading && (
                    <>
                        <div className="d-flex flex-wrap justify-content-around gap-4">
                            {books.length > 0 ? (
                                books.map(book => (
                                    <BookCard
                                        title={book.title}
                                        author={book.author}
                                        price={book.price}
                                        rating={book.rating}
                                        imageUrl={book.imageUrl}
                                        link={book.link}
                                    />
                                ))
                            ) : (
                                <div className="col-12 text-center py-5">
                                    <i className="fas fa-book-open fa-3x text-muted mb-3"></i>
                                    <h3>No books found</h3>
                                    <p>Try adjusting your search criteria</p>
                                </div>
                            )}
                        </div>

                        {/* Pagination */}
                        {books.length > booksPerPage && (
                            <nav className="pb-5 mb-5 mt-5" aria-label="Page navigation">
                                <ul className="pagination m-0 justify-content-center align-items-center">
                                    <li className={`page-item ${currentPage === 1 ? 'disabled' : ''}`}>
                                        <button
                                            className="page-link text-dark border-0 rounded-3 bg-transparent btn-outline-primary"
                                            onClick={() => paginate(currentPage - 1)}
                                            aria-label="Previous"
                                        >
                                            <i className="fas fa-chevron-left"></i>
                                        </button>
                                    </li>

                                    {Array.from({ length: totalPages }, (_, i) => (
                                        <li
                                            key={i}
                                            className={`page-item ${currentPage === i + 1 ? 'active' : ''}`}
                                        >
                                            <button
                                                className="page-link text-dark border-0 rounded-3"
                                                onClick={() => paginate(i + 1)}
                                            >
                                                {i + 1}
                                            </button>
                                        </li>
                                    ))}

                                    <li className={`page-item ${currentPage === totalPages ? 'disabled' : ''}`}>
                                        <button
                                            className="page-link text-dark border-0 rounded-3 bg-transparent btn-outline-primary"
                                            onClick={() => paginate(currentPage + 1)}
                                            aria-label="Next"
                                        >
                                            <i className="fas fa-chevron-right"></i>
                                        </button>
                                    </li>
                                </ul>
                            </nav>
                        )}
                    </>
                )}
            </main>

            {/*<Footer />*/}
        </>
    );
};

export default SearchPage;