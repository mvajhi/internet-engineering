import React, { useState } from 'react';
import { Footer } from "../components/Footer";
import FormInput from "../components/FormInput";
import FormHeader from "../components/FormHeader";
import { useNavigate } from 'react-router-dom';


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

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [apiError, setApiError] = useState('');

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    setApiError('');

    try {
      const response = await fetch('/api/users/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });

      const data = await response.json();

      if (data.success) {
        setApiError('');
        navigate('/');
      } else {
        setApiError(data.message || 'Login failed');
        console.log(apiError);
      }
    } catch (error) {
      setApiError('Network error. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="bg-light d-flex align-items-center justify-content-center vh-100">
      <div className="bg-white rounded-4 w-100 main-box">
        <FormHeader
          title="Sign in"
          subtitle="MioBook"
        />
        <form onSubmit={handleSubmit}>
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
            {apiError == "" ? "" : <p className='text-center text-danger'>Username or password is incorrect.</p>}
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


