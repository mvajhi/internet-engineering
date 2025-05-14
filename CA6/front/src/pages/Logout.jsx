import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../Utils/AuthContext';

const Logout = () => {
  const navigate = useNavigate();
  const { logout } = useAuth();

  useEffect(() => {
    const performLogout = async () => {
      await logout();
      navigate('/signin');
    };

    performLogout();
  }, [logout, navigate]);

  return (
    <div className="container">
      <h1>Logging out...</h1>
      <p>You will be redirected to the sign-in page shortly.</p>
    </div>
  );
};

export default Logout;