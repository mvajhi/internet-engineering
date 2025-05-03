const FormInput = ({
  name,
  type,
  placeholder,
  value,
  onChange,
  error,
  hasIcon = false,
  passwordVisible = false,
  togglePasswordVisibility = () => { }
}) => {
  return (
    <div className="mb-3">
      <input
        name={name}
        type={type}
        className={`form-control ${error ? 'is-invalid' : ''}`}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
      />
      {hasIcon && (
        <i
          className={`bi ${passwordVisible ? 'bi-eye' : 'bi-eye-slash'} field-icon user-select-none`}
          onClick={togglePasswordVisibility}
        >
          &nbsp;&nbsp;
        </i>
      )}
      {error && <div className="invalid-feedback">{error}</div>}
    </div>
  );
};

export default FormInput;
