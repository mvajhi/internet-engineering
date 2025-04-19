import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router';
import 'bootstrap/dist/css/bootstrap.min.css';
import SignIn from './pages/SignIn';
import SignUp from './pages/SignUp';
import Books from './pages/Book';


function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/signin" element={<SignIn />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/books" element={<Books />} />
        {/* <Route path="/" element={<SignIn />} /> */}
      </Routes>
    </BrowserRouter>
  );
}

export default App;