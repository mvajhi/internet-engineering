import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios'; 
import Header from '../components/Header';
import { Footer } from '../components/Footer';
import BookDetailCard from '../components/BookDetailCard';
import ReviewsList from '../components/ReviewsList';

const BookPage = () => {
    const { bookTitle } = useParams();
    const navigate = useNavigate();
    const [book, setBook] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [allReviews, setAllReviews] = useState([]); // Store all reviews
    const [displayedReviews, setDisplayedReviews] = useState([]); // Reviews for current page
    const [currentPage, setCurrentPage] = useState(1);
    const [totalReviews, setTotalReviews] = useState(0);
    const reviewsPerPage = 4;
    
    // Fetch book data and reviews
    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                
                // Fetch book details
                const bookResponse = await axios.get(`/api/books/${bookTitle}`);
                
                if (!bookResponse.data.success) {
                    setError('Failed to load book details');
                    return;
                }
                
                // Fetch reviews
                const reviewsResponse = await axios.get(`/api/books/${bookTitle}/review`);
                
                if (!reviewsResponse.data.success) {
                    setError('Failed to load reviews');
                    return;
                }
                
                // Map API responses to our component's expected format
                const bookData = {
                    title: bookResponse.data.data.title,
                    author: bookResponse.data.data.author,
                    publisher: bookResponse.data.data.publisher,
                    year: bookResponse.data.data.year,
                    about: bookResponse.data.data.synopsis,
                    price: bookResponse.data.data.price,
                    rating: reviewsResponse.data.data.averageRating, // Use average rating from reviews API
                    imageUrl: '/assets/big_book.png' 
                };
                
                setBook(bookData);
                
                // Process reviews from the API
                const apiReviews = reviewsResponse.data.data.reviews;
                
                // Format reviews to match our component's expected structure
                const formattedReviews = apiReviews.map(review => {
                    // Get current date for the display - in a real app we'd get this from the API
                    const currentDate = new Date();
                    const month = currentDate.toLocaleString('en-US', { month: 'long' });
                    const day = currentDate.getDate();
                    const year = currentDate.getFullYear();
                    
                    return {
                        username: review.username,
                        content: review.comment,
                        rating: review.rate,
                        date: `${month} ${day}, ${year}`
                    };
                });
                
                setAllReviews(formattedReviews);
                setTotalReviews(formattedReviews.length);
                
                // Set initial displayed reviews (first page)
                updateDisplayedReviews(formattedReviews, 1);
                
            } catch (err) {
                setError(err.message || 'An error occurred while fetching data');
            } finally {
                setLoading(false);
            }
        };
        
        fetchData();
    }, [bookTitle]);
    
    // Update displayed reviews based on current page
    const updateDisplayedReviews = (reviews, page) => {
        const startIndex = (page - 1) * reviewsPerPage;
        const endIndex = startIndex + reviewsPerPage;
        setDisplayedReviews(reviews.slice(startIndex, endIndex));
    };
    
    // Handle add to cart
    const handleAddToCart = () => {
        // Implementation would go here
        console.log(`Added ${book?.title} to cart`);
        // You might navigate to cart or show a notification
    };
    
    // Handle page change for reviews
    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
        updateDisplayedReviews(allReviews, pageNumber);
    };
    
    // Handle adding a review
    const handleAddReview = () => {
        // Implementation would go here - maybe show a modal
        console.log('Add review clicked');
    };

    if (loading) {
        return (
            <>
                <Header />
                <div className="d-flex justify-content-center align-items-center" style={{ height: "50vh" }}>
                    <div className="spinner-border text-primary" role="status">
                        <span className="visually-hidden">Loading...</span>
                    </div>
                </div>
                <Footer />
            </>
        );
    }

    if (error || !book) {
        navigate('/404');
    }

    return (
        <>
            <Header />
            <div className="bg-light d-flex align-items-baseline justify-content-center">
                <BookDetailCard
                    book={book}
                    onAddToCart={handleAddToCart}
                />
            </div>
            <div className="bg-light d-flex align-items-baseline justify-content-center pb-5">
                <ReviewsList
                    reviews={displayedReviews}
                    totalReviews={totalReviews}
                    currentPage={currentPage}
                    onPageChange={handlePageChange}
                    onAddReview={handleAddReview}
                />
            </div>
            <Footer />
        </>
    );
};

export default BookPage;