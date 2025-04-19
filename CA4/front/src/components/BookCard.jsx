import { FaStar, FaRegStar } from 'react-icons/fa';

const BookCard = ({ book }) => {
    const renderStars = (rating) => {
        return [...Array(5)].map((_, i) => (
            i < rating ? <FaStar key={i} className="text-warning" /> : <FaRegStar key={i} className="text-secondary" />
        ));
    };

    return (
        <div className="card h-100 shadow-sm">
            <img src={book.image || '/assets/book.png'} className="card-img-top" alt={book.title} />
            <div className="card-body">
                <h5 className="card-title">{book.title}</h5>
                <p className="card-text text-muted">{book.author}</p>
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <div>{renderStars(book.rating)}</div>
                    <div>${book.price.toFixed(2)}</div>
                </div>
                <button className="btn btn-primary w-100">Add to Cart</button>
            </div>
        </div>
    );
};

export default BookCard;