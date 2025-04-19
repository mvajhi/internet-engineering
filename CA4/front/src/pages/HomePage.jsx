import { useState, useEffect } from 'react';
import Header from '../components/Header';
import BookCard from '../components/BookCard';

const HomePage = () => {
    const [newReleases, setNewReleases] = useState([]);
    const [topRated, setTopRated] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchQuery, setSearchQuery] = useState('');

    useEffect(() => {
        const fetchBooks = async () => {
            try {
                const [newReleasesData, topRatedData] = await Promise.all([
                    getNewReleases(),
                    getTopRated()
                ]);
                setNewReleases(newReleasesData);
                setTopRated(topRatedData);
            } catch (error) {
                console.error('Error fetching books:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchBooks();
    }, []);

    const handleSearch = (e) => {
        e.preventDefault();
        if (searchQuery.trim()) {
            // Navigate to search results
            console.log('Searching for:', searchQuery);
        }
    };

    if (loading) {
        return <div className="text-center py-5">Loading books...</div>;
    }

    return (
        <div className="bg-light">
            <Header searchQuery={searchQuery} onSearchChange={setSearchQuery} onSearchSubmit={handleSearch} />

            <div className="container py-5">
                {/* Welcome Section */}
                <div className="p-4 rounded-3 bg-white shadow-sm mb-5">
                    <h1 className="text-center mb-4">Welcome to MioBook</h1>
                    <p className="text-center fs-5">
                        Your online bookstore for buying or borrowing books with ease.
                    </p>
                </div>

                {/* New Releases */}
                <section className="mt-5">
                    <h2 className="fw-light fs-3 mb-4">New Releases</h2>
                    <div className="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-4">
                        {newReleases.map(book => (
                            <BookCard key={book.id} book={book} />
                        ))}
                    </div>
                </section>

                {/* Top Rated */}
                <section className="mt-5">
                    <h2 className="fw-light fs-3 mb-4">Top Rated</h2>
                    <div className="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-4">
                        {topRated.map(book => (
                            <BookCard key={book.id} book={book} />
                        ))}
                    </div>
                </section>
            </div>

            <footer className="bg-dark text-white text-center py-3">
                <p className="mb-0">Copyright © {new Date().getFullYear()} - MioBook</p>
            </footer>
        </div>
    );
};

export default HomePage;