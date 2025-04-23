import React, { useState } from 'react';
import FormInput from './FormInput';
import axios from 'axios';

const AddAuthorForm = ({ onClose, onSuccess }) => {
  const [formData, setFormData] = useState({
    username: '', 
    name: '',
    penName: '',
    nationality: '',
    born: '',
    died: '',
    imageLink: ''
  });

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isBornFocused, setIsBornFocused] = useState(false);
  const [isDiedFocused, setIsDiedFocused] = useState(false);
  const [dupName, setDupName] = useState(0);

  const isFormValid = () => {
    return (
      formData.name.trim() !== '' &&
      formData.penName.trim() !== '' &&
      formData.nationality.trim() !== '' &&
      formData.born.trim() !== '' &&
      formData.imageLink.trim() !== '' &&
      !dupName
    );
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    if (name === 'name') {
      setDupName(0);
    }
  };

  const checkAuthorExists = async () => {
    if (!formData.name.trim()) return;
    
    try {
      const response = await axios.get(`/api/author/${formData.name}`);
      if (response.data.success) {
        setDupName(1);
        return true;
      } else {
        setDupName(0);
        return false;
      }
    } catch (error) {
      setDupName(0);
      return false;
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    const isDuplicate = await checkAuthorExists();
    if (isDuplicate) return;
    
    setIsSubmitting(true);
    
    try {
      const authorData = {
        ...formData,
        born: formData.born, 
        died: formData.died || null,
      };
      
      const response = await axios.post('/api/author', authorData);
      
      if (response.data.success) {
        if (onSuccess) onSuccess();
        onClose();
      } else {
        console.error('Failed to add author:', response.data.message);
      }
    } catch (error) {
      console.error('Error adding author:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className='px-1'>
      <div className='px-2'>
      <FormInput
        name="name"
        type="text"
        placeholder="Name"
        value={formData.name}
        onChange={handleChange}
        onBlur={checkAuthorExists}
        error={dupName ? "Author name already exists" : ""}
      />
      
      <FormInput
        name="penName"
        type="text"
        placeholder="Pen Name"
        value={formData.penName}
        onChange={handleChange}
      />
      
      <FormInput
        name="nationality"
        type="text"
        placeholder="Nationality"
        value={formData.nationality}
        onChange={handleChange}
      />
      
      <div className="input-group mb-3 position-relative">
        <input
          type={isBornFocused || formData.born ? 'date' : 'text'}
          className="form-control ps-5"
          name="born"
          value={formData.born}
          onChange={handleChange}
          onFocus={() => setIsBornFocused(true)}
          onBlur={() => setIsBornFocused(false)}
          placeholder="Born"
          style={{
            backgroundImage: 'url(/assets/date.svg)',
            backgroundRepeat: 'no-repeat',
            backgroundPosition: '10px center',
          }}
        />
      </div>
      
      <div className="input-group mb-3 position-relative">
        <input
          type={isBornFocused || formData.born ? 'date' : 'text'}
          className="form-control ps-5"
          name="died"
          value={formData.died}
          onChange={handleChange}
          onFocus={() => setIsDiedFocused(true)}
          onBlur={() => setIsDiedFocused(false)}
          placeholder="Died"
          style={{
            backgroundImage: 'url(/assets/date.svg)',
            backgroundRepeat: 'no-repeat',
            backgroundPosition: '10px center',
          }}
        />
      </div>
      
      <FormInput
        name="imageLink"
        type="text"
        placeholder="Image Link"
        value={formData.imageLink}
        onChange={handleChange}
      />
      </div>
      
      <div className="d-flex flex-column mt-3">
        <button 
          type="submit" 
          className={`btn w-100 mb-2 ${isFormValid() ? 'btn-green-custom text-white' : 'btn-secondary'}`}
          disabled={!isFormValid() || isSubmitting}
        >
          Submit
        </button>
        <button 
          type="button" 
          className="btn bg-light rounded-3 w-100" 
          onClick={onClose}
        >
          Cancel
        </button>
      </div>
    </form>
  );
};

export default AddAuthorForm;