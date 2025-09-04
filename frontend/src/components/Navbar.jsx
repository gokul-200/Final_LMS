import { useNavigate } from "react-router-dom";

export default function Navbar({ role }) {
  const navigate = useNavigate();

  const logout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <nav className="flex justify-between items-center bg-blue-600 p-4 text-white">
      <h1 className="text-xl font-bold">Leave Management</h1>
      <div>
        {role === "ADMIN" && (
          <button onClick={() => navigate("/admin")} className="mr-4">Dashboard</button>
        )}
        {role === "EMPLOYEE" && (
          <button onClick={() => navigate("/employee")} className="mr-4">Dashboard</button>
        )}
        <button onClick={logout} className="bg-red-500 px-3 py-1 rounded">Logout</button>
      </div>
    </nav>
  );
}
