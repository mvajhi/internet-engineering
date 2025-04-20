import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';


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
                    <ProfileIcon userName="John Doe" />
                </div>

            </header>
            <div className="d-md-none m-3">
                <SearchBar />
            </div>
        </>
    );
};

const ProfileIcon = ({ userName }) => {
    const firstLetter = userName.charAt(0).toUpperCase();
    return (
        <div className="dropdown">
            {/* آیکون پروفایل */}
            <button
                className="btn btn-success rounded-circle d-flex justify-content-center align-items-center  px-3 py-2 user-select-none"
                type="button"
                id="profileDropdown"
                data-bs-toggle="dropdown"
                aria-expanded="false"
            >
                {firstLetter}
            </button>

            {/* منوی کشویی */}
            <ul
                className="dropdown-menu dropdown-menu-end bg-select-bar rounded-3 mt-1"
                aria-labelledby="profileDropdown"
            >
                <li className="dropdown-header fw-bold text-dark fs-6">{userName}</li>
                <li>
                    <hr className="dropdown-divider mx-3 bg-dark" />
                </li>
                {/* TODO : Add links to the pages */}
                <DropdownItem href="#profile" iconSrc="/assets/profile_menu/person.svg" text="Profile" />
                <DropdownItem href="#my-books" iconSrc="/assets/profile_menu/book.svg" text="My Books" />
                <DropdownItem href="#buy-cart" iconSrc="/assets/profile_menu/cart.svg" text="Buy Cart" />
                <DropdownItem href="#purchase-history" iconSrc="/assets/profile_menu/history.svg" text="Purchase History" />
                <li>
                    <hr className="dropdown-divider mx-3" />
                </li>
                <DropdownItem href="#logout" iconSrc="/assets/profile_menu/logout.svg" text="Logout" />
            </ul>
        </div>
    );
};

const DropdownItem = ({ href, iconSrc, text }) => {
    return (
        <li>
            <Link className="dropdown-item d-flex align-items-center pb-2" to={href}>
                <img src={iconSrc} className="me-2" alt="" /> {text}
            </Link>
        </li>
    );
};

const SearchBar = () => {
    const [selected, setSelected] = useState("Author");

    const handleSelect = (value) => {
        setSelected(value);
    };

    return (
        <div className="d-flex bg-light-custom rounded p-1 w-100 text-secondary">
            <div className="dropdown">
                <button
                    className="btn bg-light-custom dropdown-toggle border-0"
                    type="button"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                >
                    {selected}
                </button>
                <ul className="dropdown-menu bg-select-bar rounded-3 border-1 mt-2">
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