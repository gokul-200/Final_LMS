
import { Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import AdminDashboard from "./pages/AdminDashboard";
import EmployeeDashboard from "./pages/EmployeeDashboard";
import ApplyLeave from "./pages/ApplyLeave";
 
export default function App() {
  const role = localStorage.getItem("role");
 
  console.log("Role from localStorage:", role); // debug
 
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/admin" element={<AdminDashboard />} />
      <Route path="/apply-leave" element={<ApplyLeave />} />
      <Route path="/employee" element={<EmployeeDashboard />} />
 
 
      {/* Redirects */}
     
    </Routes>
  );
}