import React, { useState, useEffect } from 'react';
import { Footer } from "../components/Footer";
import FormInput from "../components/FormInput";
import FormHeader from "../components/FormHeader";
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../Utils/AuthContext';
import { GoogleLogin } from '@react-oauth/google';
import { GoogleOAuthProvider } from '@react-oauth/google';
import { jwtDecode } from 'jwt-decode';

const SignIn = () => {
  const { login, isAuthenticated , googleLogin } = useAuth();
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });

  const [errors, setErrors] = useState({
    username: '',
    password: ''
  });

  const [passwordVisible, setPasswordVisible] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [apiError, setApiError] = useState('');

  const navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated) {
      navigate('/');
    }
  }, [isAuthenticated, navigate]);

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

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!isFormValid() || isSubmitting) return;

    setIsSubmitting(true);
    setApiError('');

    const result = await login(formData.username, formData.password);
    
    if (result.success) {
      navigate('/');
    } else {
      setApiError(result.message);
    }
    
    setIsSubmitting(false);
  };

  const handleGoogleSuccess = async (credentialResponse) => {
    try {
      const decoded = jwtDecode(credentialResponse.credential);
      const result = await googleLogin(credentialResponse.credential , decoded.email, decoded.name);

      if (result.success) {
        navigate('/');
      } else {
        setApiError(result.message);
      }
    } catch (error) {
      setApiError('Google login failed');
      console.error('Google login error:', error);
    }
  };

  const handleGoogleFailure = () => {
    setApiError('Google login failed. Please try again.');
  };

  return (
      <GoogleOAuthProvider clientId="929128218918-3ebp3objv8i9qjrlif9tqntqlvui7et8.apps.googleusercontent.com">
    <div className="bg-light d-flex flex-column vh-100">
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
                disabled={!isFormValid() || isSubmitting}
              >
                <strong>Sign in</strong>
              </button>
            </div>

            <div className="text-center my-3">
              <div className="d-flex align-items-center justify-content-center">
                <hr className="flex-grow-1" />
                <span className="px-2 text-muted">OR</span>
                <hr className="flex-grow-1" />
              </div>
            </div>

            <div className="mb-3 d-flex justify-content-center">
              <GoogleLogin
                  onSuccess={handleGoogleSuccess}
                  onError={handleGoogleFailure}
                  size="medium"
                  shape="rectangular"
                  theme="filled_blue"
                  text="signin_with"
              />
            </div>

            <p className="text-center text-secondary">
              <small>
                Not a member yet?{' '}
                <Link to="/signup" className="">
                  <strong>Sign Up</strong>
                </Link>
              </small>
            </p>
          </form>
        </div>
      </div>
      <Footer />
    </div>
      </GoogleOAuthProvider>
  );
};

export default SignIn;


