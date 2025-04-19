import React, { useState } from 'react';
import { Footer } from "../components/Footer";
import FormInput from "../components/FormInput";
import FormHeader from "../components/FormHeader";



const SignIn = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });

  const [errors, setErrors] = useState({
    username: '',
    password: ''
  });

  const [passwordVisible, setPasswordVisible] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));

    // Basic validation
    if (!value.trim()) {
      setErrors(prev => ({
        ...prev,
        [name]: 'This field is required'
      }));
    } else {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const isFormValid = () => {
    return (
      formData.username &&
      formData.password &&
      Object.values(errors).every(error => !error)
    );
  };

  const togglePasswordVisibility = () => {
    setPasswordVisible(!passwordVisible);
  };

  return (
    <div className="bg-light d-flex align-items-center justify-content-center vh-100">
      <div className="bg-white rounded-4 w-100 main-box">
        <FormHeader 
          title="Sign in" 
          subtitle="MioBook" 
        />
        <form>
          <FormInput 
            name="username"
            type="text" 
            placeholder="Username"
            value={formData.username}
            onChange={handleChange}
            error={errors.username}
          />
          
          <FormInput 
            name="password"
            type={passwordVisible ? "text" : "password"} 
            placeholder="Password"
            value={formData.password}
            onChange={handleChange}
            error={errors.password}
            hasIcon={true}
            passwordVisible={passwordVisible}
            togglePasswordVisibility={togglePasswordVisibility}
          />
          
          <div className="mb-3">
            <button 
              type="submit" 
              className={`btn w-100 rounded ${isFormValid() ? 'btn-green-custom text-white' : 'btn-secondary'}`}
              disabled={!isFormValid()}
            >
              <strong>Sign in</strong>
            </button>
          </div>
          
          <p className="text-center text-secondary">
            <small>
              Not a member yet?{' '}
              <a href="/signup" className="">
                <strong>Sign Up</strong>
              </a>
            </small>
          </p>
        </form>
      </div>
      <Footer />
    </div>
  );
};

export default SignIn;


