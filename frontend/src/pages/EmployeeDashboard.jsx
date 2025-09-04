import { useEffect, useState } from "react";
import api from "../api";
import Navbar from "../components/Navbar";
import { useNavigate } from "react-router-dom";

export default function EmployeeDashboard() {
  const [requests, setRequests] = useState([]);
  const [balance, setBalance] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const username=localStorage.getItem("username");
    api.get(`/employee/my-leaves?username=${username}`).then(res => setRequests(res.data));
    // console.log(requests);
    api.get(`/employee/leave-balance?username=${username}`).then(res => setBalance(res.data));
  }, []);

  return (
    <>
      <Navbar role="EMPLOYEE" />
      <div className="p-6">
        <div className="flex justify-between mb-4">
          <h2 className="text-2xl font-bold">My Leave Requests</h2>
          <button 
            onClick={() => navigate("/apply-leave")}
            className="bg-blue-600 text-white px-3 py-1 rounded"
          >
            Apply Leave
          </button>
        </div>
        <h3 className="mb-2">Leave Balance: {balance?.remainingLeaves ?? 0} remaining</h3>
        <table className="w-full border-collapse border rounded-lg shadow-lg">
          <thead className="bg-blue-100">
            <tr>
              <th className="p-2">Reason</th>
              <th className="p-2">Status</th>
            </tr>
          </thead>
          <tbody>
            {requests.map(req => (
              <tr key={req.id} className="border-b">
                <td className="p-2">{req.reason}</td>
                <td className="p-2">{req.status}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}