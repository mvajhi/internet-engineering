import React, { useEffect, useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
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
import BookPage from "./pages/BookPage.jsx";

function ProtectedRoute({ children }) {
  const [loading, setLoading] = useState(true);
  const [authenticated, setAuthenticated] = useState(false);

  useEffect(() => {
    fetch('/api/user')
      .then(res => res.json())
      .then(data => {
        setAuthenticated(data.success);
        setLoading(false);
      })
      .catch(() => {
        setAuthenticated(false);
        setLoading(false);
      });
  }, []);

  if (loading) return null; 
  if (!authenticated) return <Navigate to="/signin" replace />;
  return children;
}

function AdminRoute({ children }) {
  const [loading, setLoading] = useState(true);
  const [isAdmin, setIsAdmin] = useState(false);

  useEffect(() => {
    fetch('/api/user')
      .then(res => res.json())
      .then(data => {
        setIsAdmin(data.success && data.data && data.data.admin === true);
        setLoading(false);
      })
      .catch(() => {
        setIsAdmin(false);
        setLoading(false);
      });
  }, []);

  if (loading) return null;
  if (!isAdmin) return <Navigate to="/404" replace />;
  return children;
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/signin" element={<SignIn />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/logout" element={<Logout />} />
        {/* Protected Routes */}
        <Route
          path="/homepage"
          element={
            <ProtectedRoute>
              <HomePage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/user"
          element={
            <ProtectedRoute>
              <User />
            </ProtectedRoute>
          }
        />
        <Route
          path="/"
          element={
            <ProtectedRoute>
              <HomePage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/search-books"
          element={
            <ProtectedRoute>
              <Search />
            </ProtectedRoute>
          }
        />
        <Route
          path="/cart"
          element={
            <ProtectedRoute>
              <Cart />
            </ProtectedRoute>
          }
        />
        <Route
          path="/history"
          element={
            <ProtectedRoute>
              <History />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin"
          element={
            <AdminRoute>
              <Admin />
            </AdminRoute>
          }
        />
        <Route
          path="/books/:bookTitle/"
          element={
            <ProtectedRoute>
              <BookPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/books/:bookTitle/content"
          element={
            <ProtectedRoute>
              <BookContent />
            </ProtectedRoute>
          }
        />
        <Route
          path="/authors/:authorId"
          element={
            <ProtectedRoute>
              <AuthorPage />
            </ProtectedRoute>
          }
        />
        <Route path="/*" element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;