import React from 'react';
import PropTypes from 'prop-types';

/**
 * Component for displaying individual book reviews
 */
const ReviewCard = ({ review }) => {
  const { username, content, rating, date } = review;

  // Function to render rating stars
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
    <div className="list-group-item border-0 pb-3">
      <div className="d-flex align-items-start">
        <img alt="User profile picture" className="rounded-circle me-3" height="40" src="/assets/user.png"/>
        <div className="flex-grow-1">
          <h6 className="mb-1">
            {username}
          </h6>
          <p className="mb-1">
            {content}
          </p>
        </div>
        <div className="text-end ">
          <div className="text-warning">
            {renderStars()}
          </div>
          <p className="justify-content-start pt-1 comment-date">
            {date}
          </p>
        </div>
      </div>
      <hr className="my-1 text-secondary"/>
    </div>
  );
};

ReviewCard.propTypes = {
  review: PropTypes.shape({
    username: PropTypes.string.isRequired,
    content: PropTypes.string.isRequired,
    rating: PropTypes.number.isRequired,
    date: PropTypes.string.isRequired
  }).isRequired
};

export default ReviewCard;