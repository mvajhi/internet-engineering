import React, { useEffect, useState } from 'react';
import BookCard from "./BookCard.jsx";
import addParamToUri from "../Utils/utils.jsx";
import axios from 'axios';

const tempBooksData = [
    {
        title: "Book Title 1",
        author: "Author One",
        price: 10.25,
        rating: 4,
        imageLink: "assets/book.png",
        link: "/book1"
    },
    {
        title: "Book Title 2",
        author: "Author One",
        price: 13.25,
        rating: 2,
        imageLink: "assets/book.png",
        link: "/book"
    },
    {
        title: "Who is Mehdi Vajhi",
        author: "Author One",
        price: 500.25,
        rating: 4,
        imageLink: "assets/book.png",
        link: "/book"
    },
    {
        title: "Mehdi Vajhi rises",
        author: "ALi Momtahen",
        price: 313,
        rating: 5,
        imageLink: "assets/book.png",
        link: "/book"
    },
    {
        title: "Mehdi Vajhi rises2",
        author: "ALi Momtahen",
        price: 1000,
        rating: 5,
        imageLink: "assets/book.png",
        link: "/book"
    },
];

export async function getNewReleases() {
    try {
        const filter = {
            order: "year",
            inverse: true
        };

        const response = await axios.get(addParamToUri('/api/books/search', filter));
        return response.data;
    } catch (error) {
        console.log("Error fetching new releases:", error);
        return [];
    }
}

const NewReleases = () => {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchTopRatedBooks = async () => {
            try {
                setLoading(true);
                const newReleasesBook = await getNewReleases();
                setBooks(newReleasesBook);
                console.log(books);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchTopRatedBooks();
    }, []);

    if (loading) return <div className="text-center py-5">Loading top rated books...</div>;
    if (error) return <div className="text-center py-5 text-danger">Error: {error}</div>;

    return (
        <section className="mt-4 px-1 bg-light">
            <h2 className="fw-light fs-3 lh-sm mb-3 px-5">New Releases</h2>
            <div className="d-flex flex-wrap justify-content-around gap-4">
                {books.map((book, index) => (
                    <BookCard
                        key={book.id || `new-release-${index}`}
                        title={book.title}
                        author={book.author}
                        price={book.price}
                        rating={book.averageRating}
                        imageLink={book.imageLink ||'/assets/book.png'}
                        link={`/books/${book.title}`}
                    />
                ))}
            </div>
        </section>
    );
};
export default NewReleases;