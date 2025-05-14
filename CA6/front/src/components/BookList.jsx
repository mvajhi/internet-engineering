import React from 'react';
import PropTypes from 'prop-types';
import BookCard from './BookCard';

const BookList = ({ books, title, loading = false }) => {
  if (loading) {
    return (
      <section className="my-4">
        <h2 className="fw-light fs-3 mb-4">{title || 'Books'}</h2>
        <div className="d-flex justify-content-center align-items-center py-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
        </div>
      </section>
    );
  }

  if (!books || books.length === 0) {
    return (
      <section className="my-4">
        <h2 className="fw-light fs-3 mb-4">{title || 'Books'}</h2>
        <div className="alert alert-info">No books found.</div>
      </section>
    );
  }

  console.log('Books:');
  console.log(books);

  return (
    <section className="my-4">
      {title && <h2 className="fw-light fs-3 mb-4">{title}</h2>}
      <div className="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-4">
        {books.map((book, index) => (
          <div key={book.id || index} className="col">
            <BookCard
              title={book.title}
              author={book.author}
              price={book.price}
              rating={book.rating || 0}
              imageLink={book.imageLink || '/assets/book.png'}
              link={book.link || `/books/${book.title}`}
            />
          </div>
        ))}
      </div>
    </section>
  );
};

BookList.propTypes = {
  books: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
      title: PropTypes.string.isRequired,
      author: PropTypes.string.isRequired,
      price: PropTypes.number.isRequired,
      rating: PropTypes.number,
      imageLink: PropTypes.string,
      link: PropTypes.string
    })
  ).isRequired,
  title: PropTypes.string,
  loading: PropTypes.bool
};

export default BookList;