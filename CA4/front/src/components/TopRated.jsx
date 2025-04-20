import { useEffect, useState } from 'react';
import BookCard from './BookCard.jsx';


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


async function topRated() {

    try {
        const filter = {
            order: "rating",
            inverse: true
        };

        // TODO: Must be Uncommented
        // const response = await fetch('/api/books/search', {
        //     method: 'GET',
        //     headers: {
        //         'Content-Type': 'application/json',
        //     },
        //
        //     body: JSON.stringify(filter)
        // });


        // return await response.json()
        return tempBooksData
    } catch (error) {

    } finally {

    }
}
const TopRatedBooks = () => {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchTopRatedBooks = async () => {
            try {
                setLoading(true);
                const topRatedBooks = await topRated();
                setBooks(topRatedBooks);
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
            <h2 className="fw-light fs-3 lh-sm mb-3 px-5">Top Rated</h2>
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


export default TopRatedBooks;