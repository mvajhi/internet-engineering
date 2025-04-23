import React, { useState } from 'react';
import LocationInputs from './LocationInputs';
import RoleSelection from './RoleSelection';
import FormInput from './FormInput';
import { useNavigate, Link } from 'react-router-dom';

const AuthorForm = () => {

  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: '',
    country: '',
    city: '',
    role: 'customer'
  });

  const [errors, setErrors] = useState({
    username: '',
    password: '',
    email: '',
    country: '',
    city: ''
  });

  const [passwordVisible, setPasswordVisible] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));

    // Validate on change
    validateField(name, value);
  };

  const validateField = (name, value) => {
    let error = '';

    switch (name) {
      case 'email':
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
          error = 'Please enter a valid email address';
        }
        break;
      case 'password':
        if (value.length > 0 && value.length < 4) {
          error = 'Password must be at least 4 characters';
        }
        break;
      case 'username':
      case 'country':
      case 'city':
        if (!value.trim()) {
          error = 'This field is required';
        }
        break;
      default:
        break;
    }

    setErrors(prev => ({
      ...prev,
      [name]: error
    }));
  };

  const isFormValid = () => {
    return (
      formData.username &&
      formData.password &&
      formData.email &&
      formData.country &&
      formData.city &&
      Object.values(errors).every(error => !error)
    );
  };

  const togglePasswordVisibility = () => {
    setPasswordVisible(!passwordVisible);
  };

  const handleRoleChange = (role) => {
    setFormData(prev => ({
      ...prev,
      role
    }));
  };

  const [isSubmitting, setIsSubmitting] = useState(false);

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);

    const obj = {
      "username": formData.username,
      "password": formData.password,
      "email": formData.email,
      "role": formData.role,
      "address": {
        "country": formData.country,
        "city": formData.city
      }
    };

    try {
      const response = await fetch('/api/users/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(obj),
      });

      const data = await response.json();

      if (data.success) {
        navigate('/signin');
      } else {
        if (data.message.includes('email')) {
          setErrors(prev => ({
            ...prev,
            "email": "Email already exists"
          }));
        }
        if (data.message.includes('username')) {
          setErrors(prev => ({
            ...prev,
            "username": "Username already exists"
          }));
        }
        console.log(data);
      }
    } catch (error) {
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
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

      <FormInput
        name="email"
        type="email"
        placeholder="Email"
        value={formData.email}
        onChange={handleChange}
        error={errors.email}
      />

      <LocationInputs
        country={formData.country}
        city={formData.city}
        onChange={handleChange}
        errors={errors}
      />

      <RoleSelection
        role={formData.role}
        setRole={handleRoleChange}
      />

      <div className="mb-3">
        <button
          type="submit"
          className={`btn w-100 rounded ${isFormValid() ? 'btn-green-custom text-white' : 'btn-secondary'}`}
          disabled={!isFormValid()}
        >
          <strong>Sign up</strong>
        </button>
      </div>

      <p className="text-center text-secondary">
        <small>
          Already have an account?{' '}
          <Link to="/signin" className="">
            <strong>Sign In</strong>
          </Link>
        </small>
      </p>
    </form>
  );
};

export default AuthorForm;