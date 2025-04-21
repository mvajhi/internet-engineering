import React, {useEffect, useState} from 'react';
import BookCard from "./BookCard.jsx";
import addParamToUri from "../Utils/utils.jsx"

const tempBooksData = [
    {
        title: "Book Title 1",
        author: "Author One",
        price: 10.25,
        rating: 4,
        imageUrl: "assets/book.png",
        link: "/book1"
    },
    {
        title: "Book Title 2",
        author: "Author One",
        price: 13.25,
        rating: 2,
        imageUrl: "assets/book.png",
        link: "/book"
    },
    {
        title: "Who is Mehdi Vajhi",
        author: "Author One",
        price: 500.25,
        rating: 4,
        imageUrl: "assets/book.png",
        link: "/book"
    },
    {
        title: "Mehdi Vajhi rises",
        author: "ALi Momtahen",
        price: 313,
        rating: 5,
        imageUrl: "assets/book.png",
        link: "/book"
    },
    {
        title: "Mehdi Vajhi rises2",
        author: "ALi Momtahen",
        price: 1000,
        rating: 5,
        imageUrl: "assets/book.png",
        link: "/book"
    },
];

async function getNewReleases() {

    try {
        const filter = {
            order: "reviewNumber",
            inverse: true
        };

        const parameter = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        }
        // TODO: Must be Uncommented
        const response = await fetch(
            addParamToUri('/api/books/search', filter),
            parameter);

        return await response.json()
    } catch (error) {
        console.log(error)
        return {}
    } finally {

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
        <section className="mt-4 px-1">
            <h2 className="fw-light fs-3 lh-sm mb-3 px-5">New Releases</h2>
            <div className="d-flex flex-wrap justify-content-around gap-4">
                {books.map((book) => (
                    <BookCard
                        title={book.title}
                        author={book.author}
                        price={book.price}
                        rating={book.rating}
                        imageUrl={'/assets/book.png'}
                        link={`/books/${book.title}`}
                    />
                ))}
            </div>
        </section>
    );
};
export default NewReleases;