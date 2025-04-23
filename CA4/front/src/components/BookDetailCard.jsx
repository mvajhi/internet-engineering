import React from 'react';
import PropTypes from 'prop-types';


const BookDetailCard = ({ book, onAddToCart }) => {
  const {
    title,
    author,
    publisher,
    year,
    genres,
    about,
    price,
    rating,
    imageLink,
    owned = false,
    borrowed = false
  } = book;

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

  const formattedGenres = Array.isArray(genres) ? genres.join(', ') : genres;

  const getStatusText = () => {
    if (owned) {
      if (borrowed) {
        return "Borrowed";
      } else {
        return "Owned";
      }
    } else {
      return "Available";
    }
  };

  return (
    <div className="bg-white mw-60 rounded-4 border-bottom border-2 border-brown p-md-4 w-sm-75 w-100 mt-5 pt-sm-5 p-1">
      <div className="row">
        <div className="col-12 col-md-4 text-center pt-2">
          <div className='position-relative'>
            <img className="rounded img-fluid" src={imageLink} alt={title} style={{maxWidth: '280px', height: '420px', objectFit: 'cover' }}/>
            <div className="position-absolute bottom-0 start-0 end-0 bg-white bg-opacity-50 d-flex justify-content-end align-items-center p-2">
              <div className='btn btn-green-custom text-white py-1 me-2'>
                {getStatusText()}
              </div>
            </div>
          </div>
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
                  <td>Genre</td>
                </tr>
                <tr>
                  <td>{author}</td>
                  <td>{publisher}</td>
                  <td>{year}</td>
                  <td>{formattedGenres}</td>
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
              className={`btn fs-bold w-md-25 rounded m-3 m-sm-0 ${!owned ? 'btn-green-custom text-white' : 'btn-secondary'}`}
              onClick={onAddToCart}
              disabled={owned}
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
    genres: PropTypes.oneOfType([PropTypes.string, PropTypes.array]),
    about: PropTypes.string,
    price: PropTypes.number.isRequired,
    rating: PropTypes.number,
    imageLink: PropTypes.string,
    owned: PropTypes.bool,
    borrowed: PropTypes.bool
  }).isRequired,
  onAddToCart: PropTypes.func.isRequired
};

export default BookDetailCard;