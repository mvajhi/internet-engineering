import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import Header from '../components/Header';
import { Footer } from '../components/Footer';
import BookDetailCard from '../components/BookDetailCard';
import ReviewsList from '../components/ReviewsList';
import AddReviewForm from '../components/AddReviewForm';

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
    const [isReviewModalOpen, setIsReviewModalOpen] = useState(false);
    const [userBooks, setUserBooks] = useState([]); // Store user's purchased books
    const reviewsPerPage = 4;

    // Fetch book data, reviews, and user's purchased books
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

                // Fetch user's purchased books
                let userPurchasedBooks = [];
                try {
                    const purchasedBooksResponse = await axios.get('/api/purchase/books');

                    if (purchasedBooksResponse.data.success) {
                        userPurchasedBooks = purchasedBooksResponse.data.data.books || [];
                        setUserBooks(userPurchasedBooks);
                    }
                } catch (purchaseErr) {
                    console.error('Error fetching purchased books:', purchaseErr);
                    // Continue with the rest of the data even if this fails
                }

                // Map API responses to our component's expected format
                // Check if the current book is in the user's purchased books
                const currentBookTitle = bookResponse.data.data.title;
                const userBookMatch = userPurchasedBooks.find(userBook => userBook.title === currentBookTitle);

                const owned = !!userBookMatch;
                const borrowed = owned && userBookMatch?.borrowed === true;

                const bookData = {
                    title: bookResponse.data.data.title,
                    author: bookResponse.data.data.author,
                    publisher: bookResponse.data.data.publisher,
                    year: bookResponse.data.data.year,
                    genres: bookResponse.data.data.genres,
                    about: bookResponse.data.data.synopsis,
                    price: bookResponse.data.data.price,
                    rating: reviewsResponse.data.data.averageRating,
                    imageUrl: '/assets/big_book.png',
                    owned: owned,
                    borrowed: borrowed
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
    }, [bookTitle]); // Remove userBooks from dependency array

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

    // Handle adding a review - open the modal
    const handleAddReview = () => {
        setIsReviewModalOpen(true);
    };

    // Handle review submission
    const handleSubmitReview = async (reviewData) => {
        try {
            const response = await axios.post(`/api/books/${bookTitle}/review`, {
                rate: reviewData.rating,
                comment: reviewData.comment
            });

            if (response.data.success) {
                try {
                    const bookResponse = await axios.get(`/api/books/${bookTitle}`);

                    const reviewsResponse = await axios.get(`/api/books/${bookTitle}/review`);

                    if (bookResponse.data.success) {
                        const updatedBookData = {
                            ...book,
                            rating: reviewsResponse.data.data.averageRating
                        };
                        setBook(updatedBookData);
                    }

                    // Process reviews from the API
                    const apiReviews = reviewsResponse.data.data.reviews;

                    // Format reviews to match our component's expected structure
                    const formattedReviews = apiReviews.map(review => {
                        // Format date for display
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

                    // Set displayed reviews (first page)
                    updateDisplayedReviews(formattedReviews, 1);
                    setCurrentPage(1);
                } catch (fetchErr) {
                    console.error('Error fetching updated data:', fetchErr);
                }
            } else {
                console.error('Failed to submit review:', response.data.message);
            }
        } catch (err) {
            console.error('Error submitting review:', err);
        }
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
            <AddReviewForm
                isOpen={isReviewModalOpen}
                onClose={() => setIsReviewModalOpen(false)}
                bookTitle={book?.title || ''}
                onSubmit={handleSubmitReview}
            />
            <Footer />
        </>
    );
};

export default BookPage;