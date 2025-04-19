import { FaSearch } from 'react-icons/fa';

const Header = ({ searchQuery, onSearchChange, onSearchSubmit }) => {
    return (
        <header className="bg-white shadow-sm sticky-top">
            <div className="container d-flex justify-content-between align-items-center py-3 px-4">
                <a href="/" className="d-flex align-items-center">
                    <img src="/assets/logo.png" alt="MioBook" height="32" />
                </a>

                {/* Desktop Search */}
                <form onSubmit={onSearchSubmit} className="d-none d-md-flex bg-light rounded p-2 w-50">
                    <input
                        type="text"
                        placeholder="Search..."
                        className="form-control border-0 bg-light"
                        value={searchQuery}
                        onChange={(e) => onSearchChange(e.target.value)}
                    />
                    <button type="submit" className="btn btn-link text-secondary">
                        <FaSearch />
                    </button>
                </form>

                <button className="btn btn-primary">Buy now</button>
            </div>

            {/* Mobile Search */}
            <div className="d-md-none p-3 bg-light">
                <form onSubmit={onSearchSubmit} className="d-flex">
                    <input
                        type="text"
                        placeholder="Search..."
                        className="form-control"
                        value={searchQuery}
                        onChange={(e) => onSearchChange(e.target.value)}
                    />
                    <button type="submit" className="btn btn-primary ms-2">
                        <FaSearch />
                    </button>
                </form>
            </div>
        </header>
    );
};

export default Header;