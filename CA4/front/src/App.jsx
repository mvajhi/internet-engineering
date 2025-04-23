import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router';
import 'bootstrap/dist/css/bootstrap.min.css';
import SignIn from './pages/SignIn';
import SignUp from './pages/SignUp';
import User from './pages/User';
import Logout from './pages/Logout';
import Cart from "./pages/Cart";
import History from "./pages/History";
import Admin from "./pages/Admin";
import BookContent from "./pages/BookContent";
import NotFound from "./pages/NotFound";
import HomePage from "./pages/HomePage.jsx";
import Search from "./pages/Search.jsx";
import AuthorPage from "./pages/AuthorPage.jsx";


function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/signin" element={<SignIn />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/logout" element={<Logout />} />
        <Route path="/homepage" element={<HomePage />} />
        <Route path="/user" element={<User />} />
        <Route path="/" element={<HomePage />} />
        <Route path="/search-books" element={<Search />} />
        <Route path="/cart" element={<Cart />} />
        <Route path="/history" element={<History />} />
        <Route path="/admin" element={<Admin />} />
        <Route path="/books/:bookTitle/content" element={<BookContent />} />
        <Route path="/authors/:authorId" element={<AuthorPage />} />
        <Route path="/*" element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;