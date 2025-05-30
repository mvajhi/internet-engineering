const customerIcon = "/assets/customer.svg";
const managerIcon = "/assets/manager.svg";

const RoleSelection = ({ role, setRole }) => {
    return (
        <div className="mb-4">
            <p className="">I am</p>
            <div className="d-flex">
                <input
                    type="radio"
                    id="customer"
                    name="role"
                    className="btn-check"
                    checked={role === 'customer'}
                    onChange={() => setRole('customer')}
                />
                <label htmlFor="customer" className="btn flex-grow-1 radio-label me-4 text-start">
                    <img src={customerIcon} className="me-4" alt="" /> Customer
                </label>

                <input
                    type="radio"
                    id="manager"
                    name="role"
                    className="btn-check"
                    checked={role === 'admin'}
                    onChange={() => setRole('admin')}
                />
                <label htmlFor="manager" className="btn flex-grow-1 radio-label text-start">
                    <img src={managerIcon} className="me-4" alt="" /> Manager
                </label>
            </div>
        </div>
    );
};
export default RoleSelection;
