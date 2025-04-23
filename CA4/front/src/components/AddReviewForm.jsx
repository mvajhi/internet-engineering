import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import Modal from './Modal';
import axios from 'axios';

const AddReviewForm = ({ isOpen, onClose, bookTitle, onSubmit }) => {
  const [rating, setRating] = useState(0);
  const [review, setReview] = useState('');
  const [error, setError] = useState('');
  const [canReview, setCanReview] = useState(false);
  const [isCheckingOwnership, setIsCheckingOwnership] = useState(true);

  // Check if user owns the book
  useEffect(() => {
    const checkBookOwnership = async () => {
      if (!bookTitle || !isOpen) return;
      
      setIsCheckingOwnership(true);
      try {
        const response = await axios.get(`/api/books/${bookTitle}/content`);
        if (response.data.success) {
          // User owns the book
          setCanReview(true);
        } else {
          // User doesn't own the book
          setCanReview(false);
        }
      } catch (error) {
        // Error means user doesn't own the book
        setCanReview(false);
      } finally {
        setIsCheckingOwnership(false);
      }
    };

    checkBookOwnership();
  }, [bookTitle, isOpen]);

  // Handle star rating click
  const handleStarClick = (selectedRating) => {
    setRating(selectedRating);
  };

  // Handle form submission
  const handleSubmit = () => {
    // Validate form
    if (rating === 0) {
      setError('Please select a rating');
      return;
    }

    if (!canReview) {
      return;
    }

    // Submit the review
    onSubmit({
      rating,
      comment: review
    });

    // Reset form
    setRating(0);
    setReview('');
    setError('');
    onClose();
  };

  // Render stars for rating
  const renderStars = () => {
    const stars = [];
    for (let i = 1; i <= 5; i++) {
      const isActive = i <= rating;
      stars.push(
        <span
          key={i}
          onClick={() => canReview && handleStarClick(i)}
          style={{
            display: 'inline-flex',
            alignItems: 'center',
            justifyContent: 'center',
            width: 48,
            height: 48,
            borderRadius: 12,
            background: '#f7fafc',
            cursor: canReview ? 'pointer' : 'not-allowed',
            transition: 'background 0.2s',
            border: 'none',
            userSelect: 'none',
          }}
        >
          {isActive ? (
            <i className="fas fa-star" style={{ color: '#f6ad55', fontSize: 28, transition: 'color 0.2s' }}></i>
          ) : (
            <i className="far fa-star text-secondary" style={{ fontSize: 28, transition: 'color 0.2s' }}></i>
          )}
        </span>
      );
    }
    return stars;
  };

  const isFormValid = rating > 0 && canReview;

  return (
    <Modal 
      isOpen={isOpen} 
      onClose={onClose} 
      title="Add Review" 
    >
      <div className="text-center mb-4">
        <div 
          className="mx-auto mb-3"
          style={{ 
            width: '120px', 
            height: '180px', 
            borderRadius: '8px',
            overflow: 'hidden',
            background: 'linear-gradient(to bottom, #1a365d, #4a5568)'
          }}
        >
          <img 
            src="/assets/book.png" 
            alt={bookTitle} 
            style={{ width: '100%', height: '100%', objectFit: 'cover' }}
          />
        </div>
        <h5 className="fw-bold">{bookTitle}</h5>
      </div>

      <div className="mb-4">
        <div className="d-flex flex-column align-items-start ms-2">
          <span className=" mb-3" style={{fontSize: '1rem', alignSelf: 'flex-start'}}>Rating</span>
          <div className="w-100 d-flex align-items-center justify-content-between mb-1">
            <div className="d-flex w-100 justify-content-between mx-3">
              {renderStars()}
            </div>
          </div>
          <small className="text-muted mt-2 fw-light" style={{alignSelf: 'flex-start'}}>tap to rate</small>
        </div>
      </div>

      <div className="mb-4">
        <textarea 
          className="form-control bg-light border-0 rounded-3"
          rows="4" 
          placeholder="Not bad."
          value={review}
          onChange={(e) => setReview(e.target.value)}
          disabled={!canReview}
        ></textarea>
      </div>
      
      {error && (
        <div className="text-danger text-center mb-3">
          {error}
        </div>
      )}
      
      {!canReview && !isCheckingOwnership && (
        <div className="text-danger text-center mb-3">
          You can't review this book.
        </div>
      )}
      
      <div className="d-flex flex-column mt-3">
        <button 
          type="button" 
          className={`btn w-100 mb-2 ${isFormValid ? 'btn-green-custom text-white' : 'btn-secondary'}`}
          onClick={handleSubmit}
          disabled={!isFormValid}
        >
          Submit Reviews
        </button>
        <button 
          type="button" 
          className="btn bg-light rounded-3 w-100" 
          onClick={onClose}
        >
          Cancel
        </button>
      </div>
    </Modal>
  );
};

AddReviewForm.propTypes = {
  isOpen: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  bookTitle: PropTypes.string.isRequired,
  onSubmit: PropTypes.func.isRequired
};

export default AddReviewForm;