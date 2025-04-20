import React, { useState } from 'react';
import { FaSearch } from 'react-icons/fa';

const Header = ({ searchQuery, onSearchChange, onSearchSubmit }) => {
    return (
        <>
            <header className="bg-white shadow-sm">
                <div className="container d-flex justify-content-between align-items-center py-3 px-4">
                    <a href="homepage.html">
                        <img src="assets/logo.png" alt="MioBook" height="30" />
                    </a>
                    <div className="d-none d-md-flex bg-light rounded w-50">
                        <SearchBar />
                    </div>
                    <button className="btn btn-green-custom text-white">Buy now</button>
                </div>

            </header>
            <div className="d-md-none m-3">
                <SearchBar />
            </div>
        </>
    );
};

const SearchBar = () => {
    const [selected, setSelected] = useState("Author");

    const handleSelect = (value) => {
        setSelected(value);
    };

    return (
        <div className="d-flex bg-light-custom rounded p-2 w-100 text-secondary">
            <div className="dropdown">
                <button
                    className="btn bg-light-custom dropdown-toggle border-0"
                    type="button"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                >
                    {selected}
                </button>
                <ul className="dropdown-menu bg-light">
                    <li>
                        <button
                            className="dropdown-item"
                            onClick={() => handleSelect("Author")}
                        >
                            Author
                        </button>
                    </li>
                    <li>
                        <button className="dropdown-item" onClick={() => handleSelect("Name")}>
                            Name
                        </button>
                    </li>
                </ul>
            </div>
            <div className="vr mx-2"></div>
            <input
                type="text"
                placeholder="Search"
                className="form-control border-0 bg-light-custom"
            />
        </div>
    );
};


export default Header;