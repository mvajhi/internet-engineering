import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios'; // Import axios
import Header from '../components/Header';
import { Footer } from '../components/Footer';
import BookCard from '../components/BookCard';

const AuthorPage = () => {
    const { authorId } = useParams(); // Use authorId which likely corresponds to the name or an ID
    const [author, setAuthor] = useState(null); // Initialize author as null
    const [books, setBooks] = useState([]);
    const [loadingAuthor, setLoadingAuthor] = useState(true); // Loading state for author details
    const [loadingBooks, setLoadingBooks] = useState(true); // Loading state for books

    const navigator = useNavigate();

    // Fetch Author Details
    useEffect(() => {
        const fetchAuthorDetails = async () => {
            setLoadingAuthor(true);
            try {
                const response = await axios.get(`/api/author/${authorId}`);
                if (response.data.success) {
                    const authorData = response.data.data;
                    setAuthor({
                        ...authorData,
                        birthDate: authorData.born,
                        deathDate: authorData.died || null,
                        booksCount: authorData.booksCount || 0,
                        imageLink: authorData.imageLink || '/assets/author_big_pic.png'
                    });
                } else {
                    console.error("Failed to fetch author details:", response.data.message);
                    navigator('/404');
                }
            } catch (error) {
                console.error("Error fetching author details:", error);
            } finally {
                setLoadingAuthor(false);
            }
        };

        if (authorId) {
            fetchAuthorDetails();
        }
    }, [authorId]);

    // Fetch Author's Books
    useEffect(() => {
        const fetchBooks = async () => {
            setLoadingBooks(true);
            try {
                const response = await axios.get(`/api/author/${authorId}/books`);
                if (response.data.success) {
                    setBooks(response.data.data);
                } else {
                    console.error("Failed to fetch books:", response.data.message);
                    setBooks([]);
                }
            } catch (error) {
                console.error("Error fetching books:", error);
                setBooks([]);
            } finally {
                setLoadingBooks(false);
            }
        };

        if (authorId) {
            fetchBooks();
        }
    }, [authorId]);

    const formatDate = (dateString) => {
        if (!dateString) return 'N/A';
        try {
            const date = new Date(dateString);
            if (isNaN(date.getTime())) {
                return 'Invalid Date';
            }
            return date.toISOString().split('T')[0];
        } catch (e) {
            return 'Invalid Date Format';
        }
    };

    if (loadingAuthor) {
        return (
            <>
                <Header />
                <div className="text-center py-5">
                    <div className="spinner-border" role="status">
                        <span className="visually-hidden">Loading author details...</span>
                    </div>
                </div>
                <Footer />
            </>
        );
    }

    if (!author) {
        return (
            <>
                <Header />
                <div className="container text-center py-5">
                    <p>Could not load author details.</p>
                </div>
                <Footer />
            </>
        );
    }

    return (
        <>
            <Header />

            {/* Top background section */}
            <div className="bg-image position-relative overflow-hidden border-bottom border-dark border-4 vh-25" style={{ height: '50vh' }}>
                <img
                    src="/assets/author_back.png"
                    alt="background"
                />
            </div>

            {/* Author Info Card - fully separated */}
            <div className="container position-relative mt-n3">
                <div className="row g-0 align-items-center">
                    <div className="col-12 col-md-3 d-flex flex-column align-items-center">
                        <img
                            src={author.imageLink}
                            alt={author.name}
                            className="rounded-4 shadow author-image img-fluid"
                            style={{
                                marginTop: '-95%',
                                maxWidth: 'min(280px, 80vw)',
                                height: '420px',
                                objectFit: 'cover'
                            }}
                        />
                    </div>
                </div>
            </div>

            <div className="container px-4 px-lg-5 pt-5">
                <div className="d-flex flex-column flex-md-row justify-content-between align-items-md-center mb-4">
                    <h1 className="fw-bold fs-1 mt-3 mb-2 text-center">{author.name}</h1>

                    <div className="mt-3 mt-md-0">
                        <div className="row justify-content-md-end text-center text-md-start">
                            {author.penName && (
                                <div className="col-6 col-md-auto px-md-3 mb-3 mb-md-0">
                                    <div className="text-secondary small mb-1">Pen Name</div>
                                    <div>{author.penName}</div>
                                </div>
                            )}
                            {author.nationality && (
                                <div className="col-6 col-md-auto px-md-3 mb-3 mb-md-0">
                                    <div className="text-secondary small mb-1">Nationality</div>
                                    <div>{author.nationality}</div>
                                </div>
                            )}
                            {author.birthDate && (
                                <div className="col-6 col-md-auto px-md-3 mb-3 mb-md-0">
                                    <div className="text-secondary small mb-1">Born</div>
                                    <div>{formatDate(author.birthDate)}</div>
                                </div>
                            )}
                            {author.deathDate && (
                                <div className="col-6 col-md-auto px-md-3 mb-3 mb-md-0">
                                    <div className="text-secondary small mb-1">Died</div>
                                    <div>{formatDate(author.deathDate)}</div>
                                </div>
                            )}
                            {author.booksCount > 0 && (
                                <div className="col-6 col-md-auto px-md-3 mb-3 mb-md-0">
                                    <div className="text-secondary small mb-1">Books</div>
                                    <div>{author.booksCount}</div>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>

            <div className="container px-4 px-lg-5 mb-5 mt-5 pt-2">
                <div className="mt-5">
                    {loadingBooks ? (
                        <div className="text-center py-5">
                            <div className="spinner-border" role="status">
                                <span className="visually-hidden">Loading books...</span>
                            </div>
                        </div>
                    ) : books.length > 0 ? (
                        <section className="mt-5 pt-3">
                            <div className="row gx-4 gy-4 justify-content-center">
                                {books.map((book, index) => (
                                    <div key={index} className="col-sm-6 col-md-4 col-lg-3 d-flex justify-content-center">
                                        <BookCard
                                            title={book.title}
                                            author={book.author?.name || author.name}
                                            price={book.price}
                                            rating={book.averageRating}
                                            imageLink={book.imageLink || '/assets/book.png'}
                                            link={`/books/${book.id || book.title}`}
                                        />
                                    </div>
                                ))}
                            </div>
                        </section>
                    ) : (
                        <div className="text-center py-5">
                            <p>No books found for this author.</p>
                        </div>
                    )}
                </div>
            </div>
            <Footer />
        </>
    );
};

export default AuthorPage;