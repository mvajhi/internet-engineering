import React, { useState, useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './Utils/AuthContext';
import axios from "axios";
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
import GoogleCallback from "./pages/GoogleCallback.jsx"

function ProtectedRoute({ children }) {
  const { isAuthenticated, loading } = useAuth();
  
  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center vh-100">
        <div className="spinner-border" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }
  
  return isAuthenticated ? children : <Navigate to="/signin" />;
}

function AdminRoute({ children }) {
  const [isAdmin, setIsAdmin] = useState(false);
  const { isAuthenticated, loading, user } = useAuth();
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const checkAdminStatus = async () => {
      // Reset admin status check
      setIsLoading(true);
      
      // If not authenticated, we know the user isn't an admin
      if (!isAuthenticated) {
        setIsAdmin(false);
        setIsLoading(false);
        return;
      }

      try {
        // If we already have user data with admin flag
        if (user && typeof user.admin !== 'undefined') {
          setIsAdmin(user.admin === true);
          setIsLoading(false);
          return;
        }
        
        // Otherwise fetch user data
        const response = await axios.get("/api/user");
        
        if (response.data && response.data.data) {
          // Update user info in state
          setIsAdmin(response.data.data.admin === true);
        } else {
          setIsAdmin(false);
        }
      } catch (error) {
        console.error("Error checking admin status:", error);
        setIsAdmin(false);
      } finally {
        setIsLoading(false);
      }
    };
  
    if (!loading) {
      checkAdminStatus();
    }
  }, [isAuthenticated, loading, user]);

  // Show loading spinner while checking admin status
  if (loading || isLoading) {
    return (
      <div className="d-flex justify-content-center align-items-center vh-100">
        <div className="spinner-border" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }
  
  // Redirect if not authenticated or not admin
  if (!isAuthenticated || !isAdmin) {
    return <Navigate to="/404" replace />;
  }
  
  // Render children if admin
  return children;
}

function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/signin" element={<SignIn />} />
          <Route path="/callback" element={<GoogleCallback />} />
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

function App() {
  return (
    <AuthProvider>
      <AppRoutes />
    </AuthProvider>
  );
}

export default App;