import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import axios from 'axios';
import SignIn from "./SignIn.jsx";

const GoogleCallback = () => {
  const [params] = useSearchParams();
  const navigate = useNavigate();

  useEffect(() => {
    const credential = params.get('credential');
    if (credential) {
      console.log("SDFSDFSDFSFSDFSFD");
      axios.post('/api/auth/google', { credential })
        .then(response => {
          localStorage.setItem('token', response.data.token);
          navigate('/');
        })
        .catch(() => navigate('/login'));
    } else {
      navigate('/login');
    }
  }, [params, navigate]);

  return <div>Signing in with Google...</div>;
};

export default GoogleCallback;