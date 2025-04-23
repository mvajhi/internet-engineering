import React from 'react';
import PropTypes from 'prop-types';

/**
 * کامپوننت BookDetailCard برای نمایش جزئیات کامل یک کتاب
 */
const BookDetailCard = ({ book, onAddToCart }) => {
  const {
    title,
    author,
    publisher,
    year,
    about,
    price,
    rating,
    imageUrl = '/assets/big_book.png'
  } = book;

  // تابع رندر کردن ستاره‌های امتیاز
  const renderStars = () => {
    const stars = [];
    const fullStars = Math.floor(rating || 0);
    
    for (let i = 1; i <= 5; i++) {
      if (i <= fullStars) {
        stars.push(<i key={i} className="fas fa-star"></i>);
      } else {
        stars.push(<i key={i} className="far fa-star text-secondary"></i>);
      }
    }

    return stars;
  };

  return (
    <div className="bg-white mw-60 rounded-4 border-bottom border-2 border-brown p-md-4 w-sm-75 w-100 mt-5 pt-sm-5 p-1">
      <div className="row">
        <div className="col-12 col-md-4 text-center pt-2">
          <img className="rounded img-fluid" src={imageUrl} alt={title} />
        </div>
        <div className="col-12 col-md-8 d-flex flex-column">
          <div className="flex-grow-1">
            <table className="table table-borderless text-start align-items-stretch">
              <tbody>
                <tr>
                  <td colSpan="4">
                    <h1 className="h4 fw-bold">{title}</h1>
                  </td>
                </tr>
                <tr>
                  <td colSpan="4">
                    <div className="d-flex align-items-center">
                      <div className="text-warning">
                        {renderStars()}
                      </div>
                      <span className="ms-2 text-secondary">{rating?.toFixed(1) || "N/A"}</span>
                    </div>
                  </td>
                </tr>
                <tr className="text-secondary small">
                  <td>Author</td>
                  <td>Publisher</td>
                  <td>Year</td>
                  <td className="px-5"></td>
                </tr>
                <tr>
                  <td>{author}</td>
                  <td>{publisher}</td>
                  <td>{year}</td>
                  <td></td>
                </tr>
                <tr className="text-secondary small">
                  <td>About</td>
                </tr>
                <tr>
                  <td colSpan="4">{about}</td>
                </tr>
                <tr>
                  <td className="fw-bold">${price?.toFixed(2)}</td>
                </tr>
              </tbody>
            </table>
          </div>
          
          <div className="mt-auto">
            <button 
              className="btn btn-green-custom w-md-25 text-white rounded m-3 m-sm-0"
              onClick={onAddToCart}
            >
              <strong>Add to Cart</strong>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

BookDetailCard.propTypes = {
  book: PropTypes.shape({
    title: PropTypes.string.isRequired,
    author: PropTypes.string.isRequired,
    publisher: PropTypes.string,
    year: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    about: PropTypes.string,
    price: PropTypes.number.isRequired,
    rating: PropTypes.number,
    imageUrl: PropTypes.string
  }).isRequired,
  onAddToCart: PropTypes.func.isRequired
};

export default BookDetailCard;