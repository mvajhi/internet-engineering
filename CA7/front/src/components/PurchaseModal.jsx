import React, { useState, useEffect } from 'react';
import Modal from './Modal';
import axios from 'axios';

const customGreen = '#184C43';

const PurchaseModal = ({ isOpen, onClose, book, onPurchaseComplete }) => {
  const [isBorrowing, setIsBorrowing] = useState(false);
  const [borrowDays, setBorrowDays] = useState(1);
  const [finalPrice, setFinalPrice] = useState(book?.price || 0);

  useEffect(() => {
    if (isBorrowing) {
      setFinalPrice((book?.price * 0.1 * borrowDays).toFixed(2));
    } else {
      setFinalPrice(book?.price);
    }
  }, [isBorrowing, borrowDays, book?.price]);

  const handleBorrowToggle = () => {
    setIsBorrowing(!isBorrowing);
  };

  const handleSelectDays = (days) => {
    setBorrowDays(days);
  };

  const handleAddToCart = async () => {
    try {
      let endpoint, requestData;
      
      if (isBorrowing) {
        endpoint = '/api/cart/borrow';
        requestData = {
          username: "", 
          title: book.title,
          days: borrowDays
        };
      } else {
        endpoint = '/api/cart';
        requestData = {
          username: "", 
          title: book.title
        };
      }
      
      const response = await axios.post(endpoint, requestData);
      
      if (response.data.success) {
        onPurchaseComplete(isBorrowing, borrowDays);
        onClose();
      }
    } catch (error) {
      console.error('Error adding to cart:', error);
    }
  };

  const dayButtonClass = (day) => {
    return `btn rounded-2 fw-medium fs-6 p-2 flex-grow-0 flex-shrink-0 ${
      borrowDays === day 
        ? 'text-white shadow-sm' 
        : 'text-success border-2 border-success bg-white'
    }`;
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Add to Cart">
      <div className="p-3 px-4">
        <p className="mb-4 fw-light fs-5">
          Would you like to buy or borrow this book?
        </p>
        
        <div className="d-flex align-items-center mb-3 gap-2">
          <label htmlFor="borrowCheckbox" className="d-flex align-items-center fs-5 fw-normal user-select-none gap-2" style={{cursor: 'pointer'}}>
            <input
              type="checkbox"
              className="d-none"
              id="borrowCheckbox"
              checked={isBorrowing}
              onChange={handleBorrowToggle}
            />
            <span className="d-inline-block position-relative" style={{
              width: '25px', 
              height: '25px', 
              border: '2px solid #111', 
              borderRadius: '4px',
              background: '#fff'
            }}>
              {isBorrowing && (
                <svg 
                  viewBox="0 0 20 20"
                  width="20"
                  height="20"
                  className="position-absolute"
                  style={{top: '1px', left: '1px'}}
                >
                  <polyline
                    points="3,11 8,16 17,5"
                    style={{
                      fill: 'none',
                      stroke: '#111',
                      strokeWidth: 2.5,
                      strokeLinecap: 'round',
                      strokeLinejoin: 'round'
                    }}
                  />
                </svg>
              )}
            </span>
            Borrow this book
          </label>
        </div>

        {isBorrowing && (
          <div className="mb-3 ms-1 d-flex flex-wrap gap-3">
            {[1, 2, 3, 4, 5, 6, 7, 8, 9].map((day) => (
              <button
                key={day}
                type="button"
                onClick={() => handleSelectDays(day)}
                className={dayButtonClass(day)}
                style={{
                  flex: '0 0 30%',
                  minWidth: '90px',
                  backgroundColor: borrowDays === day ? customGreen : '',
                  borderColor: customGreen
                }}
              >
                {day} Day{day > 1 ? 's' : ''}
              </button>
            ))}
          </div>
        )}

        <div className="d-flex justify-content-between align-items-center mt-4 gap-3">
          <div className="fw-medium fs-5">
            Final Price: <span className="fw-bold">${finalPrice}</span>
          </div>
          <button
            type="button"
            onClick={handleAddToCart}
            className="btn fw-medium fs-5 py-2 px-4 shadow-sm text-white"
            style={{
              backgroundColor: customGreen,
              borderRadius: '10px',
              minWidth: '120px'
            }}
          >
            Add
          </button>
        </div>
      </div>
    </Modal>
  );
};

export default PurchaseModal;