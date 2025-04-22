import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from "axios";
import Table from './Table';


const AddingSection = () => {

  return (
    <div className="py-3 justify-content-center d-flex">
      <button className={"btn rounded btn-green-custom text-white py-2 mx-sm-5 mx-2"}>
        Add Author
      </button>
      <button className={"btn rounded btn-green-custom text-white py-2 mx-sm-5 mx-2"}>
        Add Book
      </button>
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
    <div className="card p-4 pb-0 border-0 rounded-4 pb-3 mb-3">
      <div class="row align-items-center">
        <div class="col-md-10 py-2">
          <div class="d-flex align-items-center mb-2">
            <img src="assets/person.svg" className="me-2" alt="Profile" /> {userData.username}
          </div>
          <div class="d-flex align-items-center">
            <img src="assets/mail.svg" className="me-2" alt="Email" /> {userData.email}
          </div>
        </div>

        <div class="col-md-2 text-end">
          <ul className="pagination m-2 justify-content-sm-end justify-content-start align-items-center">
            <li className="page-item active" aria-current="page">
              <Link to="/logout" className="page-link text-dark border-0 rounded-3">&nbsp; Logout &nbsp;</Link>
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
};

const BookTable = () => {
  const [books, setBooks] = useState([]);
  const [username, setUsername] = useState("");

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
        const response = await axios.get("/api/books");

        if (response.data.success) {
          setBooks(response.data.data);
        }
      };

      fetchBooks();
    }
  }, [username]);


  const columns = [
    { key: "image", header: "Image", type: "image", alt: "Cover of the book", src: "assets/book2.png" },
    { key: "title", header: "Name", type: "text" },
    { key: "author", header: "author", type: "text" },
    { key: "genres", header: "Genre", type: "array", separator: ", " },
    { key: "publisher", header: "Publisher", type: "text" },
    { key: "year", header: "Published Year", type: "text" },
    {
      key: "price",
      header: "Price",
      type: "text",
      customRender: (book) =>
        `$${book.price}`
    },
    {
      key: "totalBuy",
      header: "Total Buys",
      type: "text",
      customRender: (book) =>
        book.totalBuy ? book.totalBuy : "0"
    },
  ];

  return (
    <div className="card p-4 pb-0 border-0 rounded-4 pb-3 mb-3">
      <div className="d-flex align-items-center mb-3">
        <img src="assets/book.svg" className="me-2" alt="book" />
        <div className="fs-3 fw-bold">Books</div>
      </div>
      <Table
        items={books}
        columns={columns}
        tmp_img="/assets/no_history.svg"
      />
    </div>
  );
};

const AuthorTable = () => {
  const [books, setBooks] = useState([]);
  const [username, setUsername] = useState("");

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
        const response = await axios.get("/api/author");

        if (response.data.success) {
          setBooks(response.data.data);
        }
      };

      fetchBooks();
    }
  }, [username]);


  const columns = [
    { key: "image", header: "Image", type: "image", alt: "Cover of the book", src: "assets/book2.png" },
    { key: "name", header: "Name", type: "text" },
    { key: "penName", header: "Pen Name", type: "text" },
    { key: "nationality", header: "Nationality", type: "text" },
    { key: "born", header: "Born", type: "text" },
    {
      key: "died",
      header: "Died",
      type: "text",
      customRender: (author) =>
        author.died ? author.died : "—"
    },
  ];

  return (
    <div className="card p-4 pb-0 border-0 rounded-4 pb-3 mb-3">
      <div className="d-flex align-items-center mb-3">
        <img src="assets/author.svg" className="me-2" alt="book" />
        <div className="fs-3 fw-bold">Authors</div>
      </div>
      <Table
        items={books}
        columns={columns}
        tmp_img="/assets/no_history.svg"
      />
    </div>
  );
};

const AdminContainer = () => {
  return (
    <div className="container w-100 w-sm-75 pt-4 flex-grow-1">
      <ProfileSection />
      <AddingSection />
      <div className="row mb-3" />
      <BookTable />
      <div className="row mb-4 p-2" />
      <AuthorTable />
    </div>
  );
};

export default AdminContainer;