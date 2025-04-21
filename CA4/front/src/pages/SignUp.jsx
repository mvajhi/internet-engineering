import React, { useState } from 'react';
import { Footer } from "../components/Footer";
import UserForm from "../components/UserForm";
import FormHeader from "../components/FormHeader";


export default function SignUp() {
  return (
    <div className="bg-light d-flex flex-column vh-100">
      <div className="bg-light d-flex align-items-center justify-content-center vh-100">
        <div className="bg-white rounded-4 w-100 main-box">
          <FormHeader
            title="Sign Up"
            subtitle="MioBook"
          />
          <UserForm />
        </div>
      </div>
      <Footer />
    </div>
  );
};


