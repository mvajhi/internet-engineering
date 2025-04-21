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
    const [showFilters, setShowFilters] = useState(false);
    const [currentPage, setCurrentPage] = useState(1);
    const booksPerPage = 2;

    const [filters, setFilters] = useState({
        bookName: '',
        authorName: '',
        genre: '',
        publishedYear: '',
        sortBy: '',
        order: ''
    });

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

    function applyFilters() {

    }

    return (
        <>
            <Header />

            <main className="container pt-5 px-2 mt-4">

                {/* Filter */}
                <div className="d-flex justify-content-end mb-4">
                    <button
                        className="btn btn btn-green-custom text-white"
                        onClick={() => setShowFilters(!showFilters)}
                    >
                        <i className={`fas fa-${showFilters ? 'times' : 'filter'} me-2`}></i>
                        {showFilters ? 'Close Filters' : 'Filters'}
                    </button>
                </div>
                {/* Filters Panel */}
                {showFilters && (
                    <>
                        {/* Overlay */}
                        <div
                            className="position-fixed top-0 start-0 w-100 h-100 bg-dark bg-opacity-50"
                            style={{ zIndex: 1040 }}
                            onClick={() => setShowFilters(false)}
                        ></div>

                        {/* Filter Panel */}
                        <div
                            className="position-fixed top-0 start-0 h-100 bg-white shadow-sm"
                            style={{
                                width: '400px',
                                zIndex: 1050,
                                overflowY: 'auto'
                            }}
                        >
                            <div className="card h-100 border-0 rounded-0">
                                <div className="card-header bg-white border-0 justify-content-between">
                                    <div>
                                        <button
                                        className="btn d-flex justify-content-end mb-4"
                                        onClick={() => setShowFilters(false)}
                                        >×</button>
                                    </div>


                                    <h5 className="card-title border-bottom border-dark text-center">Filters</h5>
                                </div>

                                <div className="card-body">
                                    <div className="d-flex flex-column gap-3">
                                        {/* Book Name */}
                                        <div className="d-flex align-items-center">
                                            <label className="form-label me-3" style={{ minWidth: '120px' }}>Book Name</label>
                                            <input
                                                type="text"
                                                className="form-control"
                                                name="bookName"
                                                value={filters.bookName}
                                                // onChange={handleFilterChange}
                                                placeholder="Sample"
                                                style={{ flex: 1 }}
                                            />
                                        </div>

                                        {/* Author Name */}
                                        <div className="d-flex align-items-center">
                                            <label className="form-label me-3" style={{ minWidth: '120px' }}>Author Name</label>
                                            <input
                                                type="text"
                                                className="form-control"
                                                name="authorName"
                                                value={filters.authorName}
                                                // onChange={handleFilterChange}
                                                placeholder="Sample"
                                                style={{ flex: 1 }}
                                            />
                                        </div>

                                        {/* Genre */}
                                        <div className="d-flex align-items-center">
                                            <label className="form-label me-3" style={{ minWidth: '120px' }}>Genre</label>
                                            <select
                                                className="form-select"
                                                name="genre"
                                                value={filters.genre}
                                                // onChange={handleFilterChange}
                                                style={{ flex: 1 }}
                                            >
                                                <option value="">All Genres</option>
                                                <option value="Fiction">Fiction</option>
                                                <option value="Non-Fiction">Non-Fiction</option>
                                                <option value="Science">Science</option>
                                                <option value="Biography">Biography</option>
                                            </select>
                                        </div>

                                        {/* Published Year */}
                                        <div className="d-flex align-items-center">
                                            <label className="form-label me-3" style={{ minWidth: '120px' }}>Published Year</label>
                                            <input
                                                type="number"
                                                className="form-control"
                                                name="publishedYear"
                                                value={filters.publishedYear}
                                                // onChange={handleFilterChange}
                                                placeholder="2015"
                                                min="1900"
                                                max={new Date().getFullYear()}
                                                style={{ flex: 1 }}
                                            />
                                        </div>

                                        {/* Sort By */}
                                        <div className="d-flex align-items-center">
                                            <label className="form-label me-3" style={{ minWidth: '120px' }}>Sort By</label>
                                            <div className="d-flex gap-2">
                                                <button
                                                    type="button"
                                                    className={`btn rounded-3 px-5 ${filters.sortBy === 'Rating' ? 'btn-green-custom' : 'btn-outline-dark'}`}
                                                    onClick={() => setFilters({...filters, sortBy: 'Rating'})}
                                                    style={{ minWidth: '100px' }}
                                                >
                                                    Rating
                                                </button>
                                                <button
                                                    type="button"
                                                    className={`btn rounded-3 px-5 ${filters.sortBy === 'Reviews' ? 'btn-green-custom' : 'btn-outline-dark'}`}
                                                    onClick={() => setFilters({...filters, sortBy: 'Reviews'})}
                                                    style={{ minWidth: '100px' }}
                                                >
                                                    Reviews
                                                </button>
                                            </div>
                                        </div>

                                        {/* Order */}
                                        <div className="d-flex align-items-center mt-2">
                                            <label className="form-label me-3" style={{ minWidth: '120px' }}>Order</label>
                                            <div className="d-flex gap-2">
                                                <button
                                                    type="button"
                                                    className={`btn rounded-3 px-5 ${filters.order === 'Descending' ? 'btn-green-custom' : 'btn-outline-dark'}`}
                                                    onClick={() => setFilters({...filters, order: 'Descending'})}
                                                    style={{ minWidth: '100px' }}
                                                >
                                                    Desc
                                                </button>
                                                <button
                                                    type="button"
                                                    className={`btn rounded-3 px-5 ${filters.order === 'Ascending' ? 'btn-green-custom' : 'btn-outline-dark'}`}
                                                    onClick={() => setFilters({...filters, order: 'Ascending'})}
                                                    style={{ minWidth: '100px' }}
                                                >
                                                    Asc
                                                </button>
                                            </div>
                                        </div>
                                    </div>

                                    <div className="d-flex justify-content-center mt-4">
                                        <button
                                            className="btn btn-green-custom text-white w-50 py-2"
                                            onClick={applyFilters}
                                        >
                                            Apply Filters
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </>
                )}
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

            <Footer />
        </>
    );
};

export default SearchPage;