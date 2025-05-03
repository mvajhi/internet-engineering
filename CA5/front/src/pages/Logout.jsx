import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from "axios";


const Logout = () => {
  const navigate = useNavigate();
  const handleLogout = async () => {
    await axios.post("/api/users/logout");
    navigate("/signin");
  };

  useEffect(() => {
    handleLogout();
  }, []);

  return (
    <div className="container">
      <h1>Logging out...</h1>
      <p>You will be redirected to the sign-in page shortly.</p>
    </div>
  );
};

export default Logout;