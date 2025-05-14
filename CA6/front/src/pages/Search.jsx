import React, { useState, useEffect } from 'react';
import Header from '../components/Header.jsx';
import { Footer } from '../components/Footer.jsx';
import BookCard from '../components/BookCard.jsx';
import addParamToUri from "../Utils/utils.jsx";
import { useLocation } from 'react-router-dom';
import axios from 'axios';

async function getBookWithFilter(filters) {
    try {
        // Using axios instead of fetch - token will be automatically included
        const response = await axios.get(
            addParamToUri('/api/books/search', filters));
        return response.data;
    } catch (error) {
        console.log("Error fetching books with filter:", error);
        return [];
    }
}

const SearchPage = () => {
    const location = useLocation();

    // State for search parameters and results
    const [searchTerm, setSearchTerm] = useState('');
    const [searchType, setSearchType] = useState('Title');
    const [books, setBooks] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [showFilters, setShowFilters] = useState(false);
    const [currentPage, setCurrentPage] = useState(1);
    const [paginated, setPaginated] = useState(false);

    const booksPerPage = 10;

    const [filters, setFilters] = useState({
        title: location.state?.searchParams?.title,
        author: location.state?.searchParams?.author || null,
        pageSize: booksPerPage + 1
    });

    // Simulate API search
    const searchBooks = () => {
        setIsLoading(true);

        // Simulate API delay
        setTimeout(async () => {
            console.log("search books with filters", filters);

            const actualBooks = await getBookWithFilter(filters);
            console.log(actualBooks);
            if (actualBooks.length > booksPerPage) {
                setBooks(actualBooks.slice(0, booksPerPage))
                setPaginated(true)
            } else {
                setPaginated(false);
                setBooks(actualBooks);
            }
            setIsLoading(false);
            setCurrentPage(currentPage); // Reset to first page on new search
        }, 500);
    };
    const paginate = (pageNumber) => {
        setCurrentPage(pageNumber);
        setFilters({
            ...filters,
            pageSize: pageNumber
        });
        searchBooks();

    }

    // Initial load or when search parameters change
    useEffect(() => {
        searchBooks();
    }, [searchTerm, searchType]);

    // Pagination logic
    const currentBooks = books //.slice(indexOfFirstBook, indexOfLastBook);
    const totalPages = Math.ceil(books.length / booksPerPage);

    // const paginate = (pageNumber) => setCurrentPage(pageNumber);


    function applyFilters() {
        setIsLoading(true);
        setShowFilters(false);
        searchBooks();
        setIsLoading(false);
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
                                maxWidth: 'fit-content',
                                zIndex: 1050,
                                overflowY: 'auto'
                            }}
                        >
                            <div className="card h-100 border-0 rounded-0 px-3">
                                <div className="card-header bg-white border-0 justify-content-between">
                                    <div className="text-end">
                                        <button
                                            className="btn mb-4"
                                            onClick={() => setShowFilters(false)}
                                        >×</button>
                                    </div>


                                    <h5 className="card-title border-bottom border-dark text-center fw-bold fs-3 pb-3 pt-4">Filters</h5>
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
                                                value={filters.title}
                                                onChange={(event) => {
                                                    setFilters({
                                                        ...filters,
                                                        title: event.target.value
                                                    })
                                                }}
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
                                                value={filters.author}
                                                onChange={(event) => {
                                                    setFilters({
                                                        ...filters,
                                                        author: event.target.value
                                                    })
                                                }}
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
                                                onChange={(event) => {
                                                    setFilters({
                                                        ...filters,
                                                        genre: event.target.value
                                                    })
                                                }}
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
                                                value={filters.year}
                                                onChange={(event) => {
                                                    setFilters({
                                                        ...filters,
                                                        year: event.target.value
                                                    })
                                                }}
                                                placeholder="2015"
                                                min="1900"
                                                max={new Date().getFullYear()}
                                                style={{ flex: 1 }}
                                            />
                                        </div>

                                        {/* Sort By */}
                                        <div className="d-flex align-items-center">
                                            <label className="form-label me-3" style={{ minWidth: '120px' }}>Sort By</label>
                                            <div className="d-flex flex-fill gap-2">
                                                <button
                                                    type="button"
                                                    className={`btn rounded-3 px-1 ${filters.sortBy === 'rating' ? 'btn-green-custom text-white' : 'btn-outline-dark'}`}
                                                    onClick={() => setFilters({ ...filters, sortBy: 'rating' })}
                                                    style={{ flexBasis: '50%' }}
                                                >
                                                    Rating
                                                </button>
                                                <button
                                                    type="button"
                                                    className={`btn rounded-3 px-1 ${filters.sortBy === 'reviewNumber' ? 'btn-green-custom text-white' : 'btn-outline-dark'}`}
                                                    onClick={() => setFilters({ ...filters, sortBy: 'reviewNumber' })}
                                                    style={{ flexBasis: '50%' }}
                                                >
                                                    Reviews
                                                </button>
                                            </div>
                                        </div>

                                        {/* Order */}
                                        <div className="d-flex align-items-center mt-2">
                                            <label className="form-label me-3" style={{ minWidth: '120px' }}>Order</label>
                                            <div className="d-flex flex-row flex-fill gap-2">
                                                <button
                                                    type="button"
                                                    className={`btn rounded-3 px-1 ${filters.inverse === false ? 'btn-green-custom text-white' : 'btn-outline-dark'}`}
                                                    onClick={() => setFilters({ ...filters, inverse: false })}
                                                    style={{ flexBasis: '50%' }}
                                                >
                                                    Asc
                                                </button>
                                                <button
                                                    type="button"
                                                    className={`btn rounded-3 px-1 ${filters.inverse === true ? 'btn-green-custom text-white' : 'btn-outline-dark'}`}
                                                    onClick={() => setFilters({ ...filters, inverse: true })}
                                                    style={{ flexBasis: '50%' }}
                                                >
                                                    Desc
                                                </button>
                                            </div>
                                        </div>
                                    </div>

                                    <div
                                        style={{
                                            width: 'fit-content',
                                            left: '50%',
                                            transform: 'translateX(-50%)'
                                        }}
                                        className="position-absolute bottom-0 w-100 d-flex justify-content-center"
                                    >
                                        <button
                                            className="btn btn-green-custom w-50 text-white py-2 mb-4 rounded-3"
                                            onClick={applyFilters}
                                        >
                                            Apply
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
                                        rating={book.averageRating}
                                        imageLink={book.imageLink || '/assets/book.png'}
                                        link={'/books/' + book.title}
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
                        {paginated && (
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
        </>
    );
};

export default SearchPage;