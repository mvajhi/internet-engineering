import {useEffect, useState} from 'react';
import Header from '../components/Header';
import BookCard from '../components/BookCard';
import TopRatedBooks from "../components/TopRated.jsx";
import getTopRated from "../components/TopRated.jsx";
import NewReleases from "../components/NewReleases.jsx";

function getNewReleases() {
    return undefined;
}

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


            <br/><br/>
    <div className="p-4 rounded-3 d-flex flex-column flex-md-row align-items-center w-100 hello-box">
        <div className="container py-5 px-4">
            <div className="p-4 rounded-3 d-flex flex-column flex-md-row align-items-center w-60">
                <div className="text-center text-md-start col-md-8 px-4 ps-md-5">
                    <p className="mb-3 fs-4 ps-md-5">
                        Welcome to MioBook – the online bookstore where you can buy or borrow books with ease.
                    </p>
                    <p className="mb-3 fs-4 ps-md-5">
                        Whether you're looking for the latest bestseller, a classic novel, or a niche title, MioBook has
                        you
                        covered.
                    </p>
                    <p className="mb-3 fs-4 ps-md-5">
                        Here, you can quickly find books by title, author, and genre. And if you’re not sure to buy? Try
                        borrowing instead! Rent a book for just a fraction of the price and enjoy full access for a set
                        period.
                    </p>
                    <p className="mb-3 fs-4 ps-md-5">
                        Your next great read is just a click away. Visit MioBook today and let the perfect book find
                        you!
                        📚✨
                    </p>
                </div>
                <div className="col-md-4 mt-4 mt-md-0 text-end pe-md-5">
                    <img src="/assets/shelf.svg" className="img-fluid pe-md-2" alt=""/>
                </div>
            </div>
        </div>
    </div>

            <div className="container py-5">

                <NewReleases />
                {/*<section className="mt-5">*/}
                {/*    <h2 className="fw-light fs-3 mb-4">New Releases</h2>*/}
                {/*    <div className="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-4">*/}
                {/*        {newReleases.map(book => (*/}
                {/*            <BookCard key={book.id} book={book} />*/}
                {/*        ))}*/}
                {/*    </div>*/}
                {/*</section>*/}
                <TopRatedBooks />
            </div>

            <footer className="bg-dark text-white text-center py-3">
                <p className="mb-0">Copyright © {new Date().getFullYear()} - MioBook</p>
            </footer>
        </div>
    );
};

export default HomePage;