import React, { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';

const AuthContext = createContext(null);

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')) || null);
  const [loading, setLoading] = useState(true);

  // Listen for storage events to sync auth state across tabs
  useEffect(() => {
    const handleStorageChange = (e) => {
      if (e.key === 'token') {
        // If token was removed in another tab
        if (!e.newValue) {
          setToken(null);
          setUser(null);
        } else if (e.newValue !== token) {
          // If token was updated in another tab
          setToken(e.newValue);
        }
      } else if (e.key === 'user') {
        if (!e.newValue) {
          setUser(null);
        } else {
          try {
            const newUser = JSON.parse(e.newValue);
            setUser(newUser);
          } catch (error) {
            console.error('Error parsing user from localStorage', error);
          }
        }
      }
    };

    window.addEventListener('storage', handleStorageChange);
    return () => {
      window.removeEventListener('storage', handleStorageChange);
    };
  }, [token]);

  // Configure axios interceptors for handling auth errors
  useEffect(() => {
    const interceptor = axios.interceptors.response.use(
      response => response,
      error => {
        if (error.response && error.response.status === 401) {
          // Only clear auth data on specific 401 errors, not all of them
          // This helps prevent logout on network issues or server problems
          if (error.config && error.config.url !== '/api/users/check-session') {
            setToken(null);
            setUser(null);
          }
        }
        return Promise.reject(error);
      }
    );

    return () => axios.interceptors.response.eject(interceptor);
  }, []);

  // Set up authentication headers whenever token changes
  useEffect(() => {
    if (token) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      localStorage.setItem('token', token);
    } else {
      delete axios.defaults.headers.common['Authorization'];
      localStorage.removeItem('token');
    }
    
    // Update loading state after token is processed
    setLoading(false);
  }, [token]);

  // Handle user data in localStorage
  useEffect(() => {
    if (user) {
      localStorage.setItem('user', JSON.stringify(user));
    } else {
      localStorage.removeItem('user');
    }
  }, [user]);

  const login = async (username, password) => {
    setLoading(true);
    try {
      const response = await axios.post('/api/users/login', { username, password });
      
      if (response.data && response.data.token) {
        setToken(response.data.token);
        setUser({ username: response.data.username });
        
        return { success: true };
      } else {
        setLoading(false);
        return { 
          success: false, 
          message: response.data?.message || 'Invalid credentials' 
        };
      }
    } catch (error) {
      setLoading(false);
      return { 
        success: false, 
        message: error.response?.data?.message || 'Authentication failed' 
      };
    }
  };

  const logout = async () => {
    setLoading(true);
    try {
      if (token) {
        await axios.post('/api/users/logout');
      }
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      setToken(null);
      setUser(null);
      setLoading(false);
    }
  };

  const googleLogin = async (credential, email, name) => {
    try {
      let body;
      body = {
        "credential" : credential,
        "email" : email,
        "name" : name
      }
      const response = await fetch("/api/auth/google", {
        method:"POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(body)
      });

      const data = await response.json();

      if (response.ok) {
        localStorage.setItem('token', data.token);
        return { success: true };
      } else {
        return { success: false, message: data.message };
      }
    } catch (error) {
      return { success: false, message: 'Network error' };
    }
  };

  // Check token validity on initial load - with improved error handling
  useEffect(() => {
    const verifyToken = async () => {
      if (!token) {
        setLoading(false);
        return;
      }
      
      try {
        // Only verify the token if we have one stored
        const response = await axios.get('/api/users/check-session');
        if (response.data && response.data.status === false) {
          // Only log out if the server explicitly says the token is invalid
          setToken(null);
          setUser(null);
        }
      } catch (error) {
        // Don't automatically log out on network errors or server issues
        console.error('Token verification error:', error);
        // Only log out on specific authentication errors
        if (error.response && error.response.status === 401 && 
            error.response.data && error.response.data.message === 'Invalid token') {
          setToken(null);
          setUser(null);
        }
      } finally {
        setLoading(false);
      }
    };
    
    verifyToken();
  }, [token]);

  // Refresh user data when needed but don't log out on failure
  useEffect(() => {
    const fetchUserData = async () => {
      if (!token || !user) return;
      
      try {
        const response = await axios.get('/api/user');
        if (response.data && response.data.success) {
          setUser(prevUser => ({...prevUser, ...response.data.data}));
        }
      } catch (error) {
        // Just log the error, don't log out the user
        console.error('Error fetching user data:', error);
      }
    };
    
    if (token && user) {
      fetchUserData();
    }
  }, [token, user?.username]);

  const value = {
    token,
    user,
    loading,
    login,
    logout,
    isAuthenticated: !!token,
    googleLogin
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};