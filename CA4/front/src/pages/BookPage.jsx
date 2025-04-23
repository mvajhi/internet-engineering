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
    
    // Generate sample reviews for testing pagination
    const generateSampleReviews = () => {
        const reviewers = ["John Doe", "Jane Smith", "Alex Brown", "Sarah Wilson", 
                          "Michael Johnson", "Emily Davis", "Robert Lee", "Lisa Wang", 
                          "David Miller", "Anna Garcia", "Thomas Robinson", "Olivia Martinez"];
        
        const contents = [
            "I bought it 3 weeks ago and now come back just to say \"Awesome\". I really enjoyed it.",
            "Great book. The characters are well developed and the story is engaging.",
            "Interesting read but not my favorite from this author.",
            "Could not put it down! Finished it in one sitting.",
            "The plot twists kept me engaged throughout the entire book.",
            "A disappointing read. The story was predictable and characters shallow.",
            "This book changed my perspective on so many things. Highly recommended!",
            "Beautifully written with amazing attention to detail.",
            "Average at best. Nothing special but not terrible either.",
            "The author's best work to date. A masterpiece!",
            "I expected more based on the reviews. It was just okay.",
            "A perfect vacation read. Light but not too simple."
        ];
        
        const sampleReviews = [];
        
        // Create sample reviews
        for (let i = 0; i < reviewers.length; i++) {
            const month = ["January", "February", "March", "April"][Math.floor(Math.random() * 4)];
            const day = Math.floor(Math.random() * 28) + 1;
            
            sampleReviews.push({
                username: reviewers[i],
                content: contents[i],
                rating: Math.floor(Math.random() * 3) + 3, // Ratings between 3-5
                date: `${month} ${day}, 2025`
            });
        }
        
        return sampleReviews;
    };
    
    // Fetch book data
    useEffect(() => {
        const fetchBookData = async () => {
            try {
                setLoading(true);
                const response = await axios.get(`/api/books/${bookTitle}`);
                
                if (response.data.success) {
                    // Map API response to our component's expected format
                    const bookData = {
                        title: response.data.data.title,
                        author: response.data.data.author,
                        publisher: response.data.data.publisher,
                        year: response.data.data.year,
                        about: response.data.data.synopsis,
                        price: response.data.data.price,
                        rating: response.data.data.averageRating,
                        // Use default image if not provided by API
                        imageUrl: '/assets/big_book.png' 
                    };
                    
                    setBook(bookData);
                    
                    // Generate sample reviews for pagination demo
                    const reviews = generateSampleReviews();
                    setAllReviews(reviews);
                    setTotalReviews(reviews.length);
                    
                    // Set initial displayed reviews (first page)
                    updateDisplayedReviews(reviews, 1);
                } else {
                    setError('Failed to load book details');
                }
            } catch (err) {
                setError(err.message || 'An error occurred while fetching book details');
            } finally {
                setLoading(false);
            }
        };
        
        fetchBookData();
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
        return (
            <>
                <Header />
                <div className="container mt-5 text-center">
                    <h2>Error</h2>
                    <p>{error || 'Book not found'}</p>
                    <button className="btn btn-primary" onClick={() => navigate('/')}>
                        Return to Homepage
                    </button>
                </div>
                <Footer />
            </>
        );
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