import React, { useState } from 'react';
import FormInput from './FormInput';
import axios from 'axios';

const AddBookForm = ({ onClose, onSuccess }) => {
  const [step, setStep] = useState(1);
  const [DubpName, setDubpName] = useState(0);
  const [formData, setFormData] = useState({
    title: '',
    author: '',
    publisher: '',
    genres: '',
    year: '',
    price: '',
    imageLink: '',
    synopsis: '',
    content: ''
  });

  const [isSubmitting, setIsSubmitting] = useState(false);

  const isStep1Valid = () => {
    return (
      formData.title.trim() !== '' &&
      formData.author.trim() !== '' &&
      formData.publisher.trim() !== '' &&
      formData.genres.trim() !== '' &&
      formData.year.trim() !== '' &&
      formData.price.trim() !== '' &&
      formData.imageLink.trim() !== ''
    );
  };

  const isStep2Valid = () => {
    return (
      formData.synopsis.trim() !== '' &&
      formData.content.trim() !== ''
    );
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const nextStep = async () => {
    try {
      const response = await axios.get(`/api/books/${formData.title}`);
      
      if (response.data.success) {
        setDubpName(1);
      } else {
        setDubpName(0);
        setStep(2);
      }
    } catch (error) {
      setDubpName(0);
      setStep(2);
    }
  };

  const prevStep = () => {
    setStep(1);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    
    try {
      const bookData = {
        ...formData,
        genres: formData.genres.split(',').map(genre => genre.trim())
      };
      
      const response = await axios.post('/api/books', bookData);
      
      if (response.data.success) {
        if (onSuccess) onSuccess();
        onClose();
      } else {
        console.error('Failed to add book:', response.data.message);
      }
    } catch (error) {
      console.error('Error adding book:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  if (step === 1) {
    return (
      <form className='px-1'>
        <div className='px-2'>
        <FormInput
          name="title"
          type="text"
          placeholder="Name"
          value={formData.title}
          onChange={handleChange}
          error={DubpName ? "Book name already exists" : ""}
        />
        
        <FormInput
          name="author"
          type="text"
          placeholder="Author"
          value={formData.author}
          onChange={handleChange}
        />
        
        <FormInput
          name="publisher"
          type="text"
          placeholder="Publisher"
          value={formData.publisher}
          onChange={handleChange}
        />
        
        <FormInput
          name="genres"
          type="text"
          placeholder="Genres (comma separated)"
          value={formData.genres}
          onChange={handleChange}
        />
        
        <FormInput
          name="year"
          type="text"
          placeholder="Published Year"
          value={formData.year}
          onChange={handleChange}
        />
        
        <FormInput
          name="price"
          type="text"
          placeholder="Price"
          value={formData.price}
          onChange={handleChange}
        />
        
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
            type="button" 
            className={`btn w-100 mb-2 ${isStep1Valid() ? 'btn-green-custom text-white' : 'btn-secondary'}`}
            onClick={nextStep}
            disabled={!isStep1Valid()}
          >
            Next
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
  } else {
    return (
      <form onSubmit={handleSubmit} className='px-1'>
        <div className="form-group mb-3 px-2">
          <textarea 
            className="form-control" 
            id="synopsis" 
            name="synopsis"
            value={formData.synopsis}
            onChange={handleChange}
            rows="3"
            placeholder="Synopsis"
          ></textarea>
        </div>
        
        <div className="form-group mb-3 px-2">
          <textarea 
            className="form-control" 
            id="content" 
            name="content"
            value={formData.content}
            onChange={handleChange}
            rows="5"
            placeholder="Content"
          ></textarea>
        </div>
        
        <div className="d-flex flex-column mt-3">
          <button 
            type="submit" 
            className={`btn w-100 mb-2 ${isStep2Valid() ? 'btn-green-custom text-white' : 'btn-secondary'}`}
            disabled={!isStep2Valid() || isSubmitting}
          >
            Submit
          </button>
          <button 
            type="button" 
            className="btn bg-light rounded-3 w-100"
            onClick={prevStep}
          >
            Back
          </button>
        </div>
      </form>
    );
  }
};

export default AddBookForm;