
import { useEffect, useState, useMemo } from "react";
import api from "../api";
import Navbar from "../components/Navbar";
import { Check, X } from "lucide-react"; // ✅ icons
 
export default function AdminDashboard() {
  const [requests, setRequests] = useState([]);
  const [search, setSearch] = useState("");
 
  useEffect(() => {
    api.get("/admin/leave-requests").then((res) => setRequests(res.data));
  }, []);
 
  const handleAction = (id, status) => {
    api.post(`/admin/leave/${id}/${status}`).then(() => {
      setRequests((prev) =>
        prev.map((r) =>
          r.id === id ? { ...r, status, actionTime: Date.now() } : r
        )
      );
 
      // auto-remove after 10s
      setTimeout(() => {
        setRequests((prev) => prev.filter((r) => r.id !== id));
      }, 10000);
    });
  };
 
  // filter locally by username
  const filtered = useMemo(() => {
    const q = search.trim().toLowerCase();
    if (!q) return requests;
    return requests.filter((r) =>
      (r.user?.username || "").toLowerCase().includes(q)
    );
  }, [search, requests]);
 
  return (
    <>
      <Navbar role="ADMIN" />
      <div className="p-6 max-w-5xl mx-auto">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 mb-4">
          <h2 className="text-2xl font-bold">Leave Requests</h2>
          <input
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="border rounded-lg px-3 py-2 w-full sm:w-72"
            placeholder="Search by username…"
          />
        </div>
 
        <div className="overflow-x-auto bg-white rounded-xl shadow">
          <table className="w-full text-left">
            <thead className="bg-blue-50">
              <tr className="[&>th]:p-3">
                <th>Employee</th>
                <th>Reason</th>
                <th>From → To</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((req) => (
                <tr key={req.id} className="border-t [&>td]:p-3">
                  <td>{req.user?.username}</td>
                  <td>{req.reason}</td>
                  <td>
                    {req.fromDate || "-"} → {req.toDate|| "-"}
                  </td>
                  <td className="font-medium">{req.status}</td>
                  <td className="space-x-2">
                    {req.status === "PENDING" ? (
                      <>
                        <button
                          className="bg-green-600 text-white px-3 py-1 rounded"
                          onClick={() => handleAction(req.id, "APPROVED")}
                        >
                          Approve
                        </button>
                        <button
                          className="bg-red-600 text-white px-3 py-1 rounded"
                          onClick={() => handleAction(req.id, "REJECTED")}
                        >
                          Reject
                        </button>
                      </>
                    ) : req.status === "APPROVED" ? (
                      <Check className="text-green-600 inline" size={20} />
                    ) : (
                      <X className="text-red-600 inline" size={20} />
                    )}
                  </td>
                </tr>
              ))}
              {filtered.length === 0 && (
                <tr>
                  <td colSpan="5" className="p-6 text-center text-gray-500">
                    No requests found
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
}
 
