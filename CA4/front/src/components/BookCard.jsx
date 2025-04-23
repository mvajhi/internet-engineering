import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import PurchaseModal from './PurchaseModal';

const BookCard = ({ title, author, price, rating = 0, imageUrl, link, authorId }) => {
    const [isPurchaseModalOpen, setIsPurchaseModalOpen] = useState(false);
    
    // Render star ratings dynamically
    const renderStars = () => {
        const stars = [];
        const fullStars = Math.floor(rating);
        const hasHalfStar = rating % 1 >= 0.5;

        for (let i = 1; i <= 5; i++) {
            if (i <= fullStars) {
                stars.push(<i key={i} className="fas fa-star"></i>);
            } else if (i === fullStars + 1 && hasHalfStar) {
                stars.push(<i key={i} className="fas fa-star-half-alt"></i>);
            } else {
                stars.push(<i key={i} className="far fa-star text-secondary"></i>);
            }
        }

        return stars;
    };

    const handleAddToCart = () => {
        setIsPurchaseModalOpen(true);
    };

    const handlePurchaseComplete = (isBorrowing, days) => {
        // Handle successful purchase (can be expanded later)
        console.log(`Book ${title} added to cart. Borrowed: ${isBorrowing}, Days: ${days}`);
        setIsPurchaseModalOpen(false);
    };

    const book = {
        title,
        price,
        author
    };

    return (
        <div className="">
            <div className="card card-book border-0 shadow-sm rounded-4 h-100">
                <Link to={link}>
                    <img
                        alt={title}
                        className="card-img-top rounded-top-4"
                        height="200"
                        src={imageUrl}
                        width="200"
                    />
                </Link>
                <div className="card-body text-center d-flex flex-column">
                    <h2 className="fs-5 fw-bold mb-2">
                        <Link to={link} className="text-decoration-none text-dark">
                            {title}
                        </Link>
                    </h2>
                    <p className="text-muted mb-2 pb-2">
                        {author && (
                            <Link to={`/authors/${author}`} className="text-decoration-none text-muted">
                                {author}
                            </Link>
                        )}
                    </p>
                    <div className="d-flex justify-content-between mb-2 mt-auto">
                        <div className="text-warning">
                            {renderStars()}
                        </div>
                        <div className="">
                            ${price.toFixed(2)}
                        </div>
                    </div>
                    <button 
                        className="btn btn-green-custom w-100 text-white rounded mt-auto"
                        onClick={handleAddToCart}
                    >
                        <strong>Add to Cart</strong>
                    </button>
                </div>
            </div>
            
            <PurchaseModal
                isOpen={isPurchaseModalOpen}
                onClose={() => setIsPurchaseModalOpen(false)}
                book={book}
                onPurchaseComplete={handlePurchaseComplete}
            />
        </div>
    );
};

BookCard.propTypes = {
    title: PropTypes.string.isRequired,
    author: PropTypes.string.isRequired,
    price: PropTypes.number.isRequired,
    rating: PropTypes.number.isRequired,
    imageUrl: PropTypes.string.isRequired,
    link: PropTypes.string.isRequired,
    authorId: PropTypes.string
};

const BookList = ({ books }) => {
    return (
        <div className="row">
            {books.map((book, index) => (
                <BookCard
                    key={index}
                    title={book.title}
                    author={book.author}
                    price={book.price}
                    rating={book.rating}
                    imageUrl={book.imageUrl}
                    link={book.link}
                    authorId={book.authorId}
                />
            ))}
        </div>
    );
};

BookList.propTypes = {
    books: PropTypes.arrayOf(PropTypes.shape({
        title: PropTypes.string.isRequired,
        author: PropTypes.string.isRequired,
        price: PropTypes.number.isRequired,
        rating: PropTypes.number.isRequired,
        imageUrl: PropTypes.string,
        link: PropTypes.string.isRequired,
        authorId: PropTypes.string
    })).isRequired
};

export default BookCard;