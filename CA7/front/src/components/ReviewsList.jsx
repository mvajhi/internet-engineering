import React from 'react';
import PropTypes from 'prop-types';
import ReviewCard from './ReviewCard';

const ReviewsList = ({ reviews, totalReviews, currentPage, onPageChange, onAddReview }) => {

    // Handle page click
    const handlePageClick = (pageNumber) => {
        if (onPageChange) {
            onPageChange(pageNumber);
        }
    };

    // Generate pagination buttons
    const renderPagination = () => {
        const totalPages = Math.ceil(totalReviews / 4); // Assuming 4 reviews per page
        let pages = [];

        for (let i = 1; i <= Math.min(5, totalPages); i++) {
            pages.push(
                <li key={i} className={`page-item ${currentPage === i ? 'active' : ''}`} aria-current={currentPage === i ? 'page' : null}>
                    <span
                        onClick={() => handlePageClick(i)}
                        className={`page-link text-dark border-0 rounded-3 ${currentPage !== i ? 'bg-transparent btn-outline-primary' : ''}`}
                        style={{ cursor: 'pointer' }}
                    >
                        {i}
                    </span>
                </li>
            );
        }

        return pages;
    };

    // Render content based on whether there are reviews or not
    const renderContent = () => {
        if (totalReviews === 0) {
            return (
                <div className="text-center py-5">
                    <img src="/assets/no_history.svg" alt="No reviews" className="img-fluid mb-3" />
                </div>
            );
        }

        return (
            <>
                <div className="list-group">
                    {reviews.map((review, index) => (
                        <ReviewCard key={index} review={review} />
                    ))}
                </div>
                <nav className="pb-1 mb-3 mt-2" aria-label="Page navigation">
                    <ul className="pagination m-0 justify-content-center align-items-center">
                        <li className="page-item">
                            <span
                                className="page-link text-dark border-0 rounded-3 bg-transparent btn-outline-primary"
                                aria-label="Previous"
                                onClick={() => currentPage > 1 && handlePageClick(currentPage - 1)}
                                style={{ cursor: currentPage > 1 ? 'pointer' : 'not-allowed' }}
                            >
                                <i className="fas fa-chevron-left"></i>
                            </span>
                        </li>

                        {renderPagination()}

                        <li className="page-item">
                            <span
                                className="page-link text-dark border-0 rounded-3 bg-transparent btn-outline-primary"
                                aria-label="Next"
                                onClick={() => currentPage < Math.ceil(totalReviews / 4) && handlePageClick(currentPage + 1)}
                                style={{ cursor: currentPage < Math.ceil(totalReviews / 4) ? 'pointer' : 'not-allowed' }}
                            >
                                <i className="fas fa-chevron-right"></i>
                            </span>
                        </li>
                    </ul>
                </nav>
            </>
        );
    };

    return (
        <div className="bg-white mw-60 rounded-4 border-bottom border-2 border-brown p-md-4 w-100 mt-5 pt-5 px-3">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <div className="d-flex justify-content-start">
                    <h2 className="h5 mb-0">
                        Reviews
                    </h2>
                    <p className="text-secondary small d-flex ms-1">
                        {totalReviews}
                    </p>
                </div>
                <button className="btn btn-light d-flex align-items-center" onClick={onAddReview}>
                    &nbsp;Add reviews
                    <img src="/assets/comment.svg" className="ms-2" alt="" />
                </button>
            </div>

            {renderContent()}
        </div>
    );
};

ReviewsList.propTypes = {
    reviews: PropTypes.arrayOf(
        PropTypes.shape({
            username: PropTypes.string.isRequired,
            content: PropTypes.string.isRequired,
            rating: PropTypes.number.isRequired,
            date: PropTypes.string.isRequired
        })
    ).isRequired,
    totalReviews: PropTypes.number.isRequired,
    currentPage: PropTypes.number.isRequired,
    onPageChange: PropTypes.func,
    onAddReview: PropTypes.func
};

export default ReviewsList;