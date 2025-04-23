const LocationInputs = ({ country, city, onChange, errors }) => {
  return (
    <div className="row mb-3">
      <div className="col">
        <div className="mb-3">
          <input
            name="country"
            type="text"
            className={`form-control ${errors.country ? 'is-invalid' : ''}`}
            placeholder="Country"
            value={country}
            onChange={onChange}
          />
          {errors.country && <div className="invalid-feedback">{errors.country}</div>}
        </div>
      </div>
      <div className="col">
        <div className="mb-3">
          <input
            name="city"
            type="text"
            className={`form-control ${errors.city ? 'is-invalid' : ''}`}
            placeholder="City"
            value={city}
            onChange={onChange}
          />
          {errors.city && <div className="invalid-feedback">{errors.city}</div>}
        </div>
      </div>
    </div>
  );
};
export default LocationInputs;
