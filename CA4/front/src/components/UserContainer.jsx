import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from "axios";
import Table from './Table';



const CreditSection = () => {
  const [balance, setBalance] = useState(0);
  const [creditInput, setCreditInput] = useState("");
  const [isButtonEnabled, setIsButtonEnabled] = useState(false);

  useEffect(() => {
    const fetchUserDetails = async () => {
      try {
        const response = await axios.get("/api/user");
        if (response.data.success) {
          setBalance(response.data.data.balance);
        }
      } catch (error) {
        console.error("Error fetching user details:", error);
      }
    };

    fetchUserDetails();
  }, []);

  const handleInputChange = (e) => {
    const value = e.target.value;
    setCreditInput(value);

    if (value && /^\d+$/.test(value) && parseInt(value, 10) >= 1000) {
      setIsButtonEnabled(true);
    } else {
      setIsButtonEnabled(false);
    }
  };

  const handleAddCredit = async () => {
    try {
      const response = await axios.post("/api/credit", {
        username: "",
        credit: parseInt(creditInput, 10),
      });

      if (response.data.success) {
        const userResponse = await axios.get("/api/user");
        if (userResponse.data.success) {
          setBalance(userResponse.data.data.balance);
          setCreditInput("");
          setIsButtonEnabled(false);
        }
      }
    } catch (error) {
      console.error("Error adding credit:", error);
    }
  };

  return (
    <div className="col-md-8 col-12 pe-md-5 pe-0 px-0">
      <div className="card p-4 border-0 d-flex align-items-stretch rounded-4">
        <div className="h4 fw-bold">${balance}</div>
        <div className="d-flex flex-wrap mt-3">
          <input
            className="form-control me-3 w-25 flex-fill rounded-3"
            placeholder="$Amount"
            type="text"
            value={creditInput}
            onChange={handleInputChange}
          />
          <button
            className={`btn ${isButtonEnabled ? "btn-green-custom text-white" : "btn-secondary"} mt-2 mt-sm-0 ms-sm-3 rounded`}
            onClick={handleAddCredit}
            disabled={!isButtonEnabled}
          >
            Add more credit
          </button>
        </div>
      </div>
    </div>
  );
};

const ProfileSection = () => {
  const [userData, setUserData] = useState({
    username: "",
    email: "",
  });

  useEffect(() => {
    const fetchUserDetails = async () => {
      const response = await axios.get("/api/user");
      if (response.data.success) {
        const { username, email } = response.data.data;
        setUserData({ username, email });
      }
    };

    fetchUserDetails();
  }, []);

  return (
    <div className="col-md-4 col-12 ps-md-5 ps-0 mt-4 mt-md-0 px-0">
      <div className="card p-3 border-0 d-flex align-items-start rounded-4">
        <div className="d-flex mb-2">
          <img src="assets/person.svg" className="me-2" alt="Profile" /> {userData.username}
        </div>
        <div className="d-flex mb-3 flex-wrap align-items-center">
          <img src="assets/mail.svg" className="me-2" alt="Email" /> {userData.email}
        </div>
        <ul className="pagination m-0 justify-content-center align-items-center">
          <li className="page-item active" aria-current="page">
            <Link to="/logout" className="page-link text-dark border-0 rounded-3">&nbsp; Logout &nbsp;</Link>
          </li>
        </ul>
      </div>
    </div>
  );
};

const HistoryTable = () => {
  const [books, setBooks] = useState([]);
  const [username, setUsername] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserName = async () => {
      const response = await axios.get("/api/user");
      if (response.data.success) {
        setUsername(response.data.data.username);
      }
    };

    fetchUserName();
  }, []);

  useEffect(() => {
    if (username) {
      const fetchBooks = async () => {
        const response = await axios.get("/api/purchase/books");

        if (response.data.success) {
          setBooks(response.data.data.books);
        }
      };

      fetchBooks();
    }
  }, [username]);

  const handleBookClick = (title) => {
    navigate(`/books/${title}`);
  };

  const handleAuthorClick = (author) => {
    navigate(`/authors/${author}`);
  };

  const columns = [
    { 
      key: "image", 
      header: "Image", 
      type: "image", 
      alt: "Cover of the book", 
      src: "assets/book2.png",
      customRender: (book) => (
        <img 
          src={book.image || "assets/book2.png"} 
          alt="Cover of the book" 
          className="img-fluid rounded book-cover-img"
          style={{ cursor: 'pointer', maxWidth: '50px' }}
          onClick={() => handleBookClick(book.title)}
        />
      )
    },
    { 
      key: "title", 
      header: "Name", 
      type: "text",
      customRender: (book) => (
        <span 
          className="text-dark" 
          style={{ cursor: 'pointer' }}
          onClick={() => handleBookClick(book.title)}
        >
          {book.title}
        </span>
      )
    },
    { 
      key: "author", 
      header: "Author", 
      type: "text",
      customRender: (book) => (
        <span 
          className="text-dark" 
          style={{ cursor: 'pointer' }}
          onClick={() => handleAuthorClick(book.author)}
        >
          {book.author}
        </span>
      )
    },
    { key: "genres", header: "Genre", type: "array", separator: ", " },
    { key: "publisher", header: "Publisher", type: "text" },
    { key: "year", header: "Published Year", type: "text" },
    {
      key: "borrowed",
      header: "Status",
      type: "text",
      customRender: (book) =>
        book.borrowed
          ? (
            <>
              Borrowed
              <div className="text-muted fw-lighter small-text">
                {`Until ${book.borrowedDate}`}
              </div>
            </>
          )
          : "Owned"
    },
    { key: "actions", header: "", type: "link", to: "/books/{title}/content", text: "Read" },
  ];

  return (
    <div className="card p-4 pb-0 border-0 rounded-4 pb-3 mb-3">
      <div className="d-flex align-items-center mb-3">
        <img src="assets/book.svg" className="me-2" alt="book" />
        <div className="fs-3 fw-bold">My Books</div>
      </div>
      <Table
        items={books}
        columns={columns}
        tmp_img="/assets/no_history.svg"
      />
    </div>
  );
};

const UserContainer = () => {
  return (
    <div className="container w-100 w-sm-75 pt-4 flex-grow-1">
      <div className="row mb-4 p-2">
        <CreditSection />
        <ProfileSection />
      </div>
      <HistoryTable />
    </div>
  );
};

export default UserContainer;