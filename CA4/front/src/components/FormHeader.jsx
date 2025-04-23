const FormHeader = ({ title, subtitle }) => {
  return (
    <>
      <h2 className="text-center login-title">{title}</h2>
      <p className="text-center login-subtitle">{subtitle}</p>
    </>
  );
};
export default FormHeader;
