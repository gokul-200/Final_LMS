// import { useState } from "react";
// import api from "../api";
// import { useNavigate } from "react-router-dom";
// import Navbar from "../components/Navbar";

// export default function ApplyLeave() {
//   const [reason, setReason] = useState("");
//   const navigate = useNavigate();

//   const submitLeave = () => {
//     api.post("/employee/apply-leave", { reason }).then(() => {
//       navigate("/employee");
//     });
//   };

//   return (
//     <>
//       <Navbar role="EMPLOYEE" />
//       <div className="flex justify-center items-center h-[80vh]">
//         <div className="bg-white shadow-lg p-6 rounded-xl w-96">
//           <h2 className="text-xl font-bold mb-4">Apply for Leave</h2>
//           <textarea 
//             className="w-full border rounded p-2 mb-4"
//             placeholder="Reason for leave"
//             value={reason}
//             onChange={(e) => setReason(e.target.value)}
//           />
//           <button 
//             onClick={submitLeave} 
//             className="bg-blue-600 text-white px-4 py-2 rounded w-full"
//           >
//             Submit
//           </button>
//         </div>
//       </div>
//     </>
//   );
// }

import { useState } from "react";
import api from "../api";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
 
export default function ApplyLeave() {
  const [reason, setReason] = useState("");
  const [fromDate, setFromDate] = useState("");
  const [toDate, setToDate] = useState("");
  const navigate = useNavigate();
 
  const username = localStorage.getItem("username"); // ✅ save this at login
 
  const submitLeave = async () => {
    try {
      await api.post("/employee/apply-leave", { username, fromDate, toDate,reason });
      alert("Leave applied successfully");
      navigate("/employee");
    } catch (err) {
      alert("Failed to apply leave");
    }
  };
 
  return (
    <>
      <Navbar role="EMPLOYEE" />
      <div className="flex justify-center items-center h-[80vh]">
        <div className="bg-white shadow-lg p-6 rounded-xl w-96">
          <h2 className="text-xl font-bold mb-4">Apply for Leave</h2>
          <label className="block mb-2">From Date</label>
          <input
            type="date"
            value={fromDate}
            onChange={(e) => setFromDate(e.target.value)}
            className="border w-full p-2 mb-4 rounded"
          />
          <label className="block mb-2">To Date</label>
          <input
            type="date"
            value={toDate}
            onChange={(e) => setToDate(e.target.value)}
            className="border w-full p-2 mb-4 rounded"
          />
          <textarea
            className="w-full border rounded p-2 mb-4"
            placeholder="Reason for leave"
            value={reason}
            onChange={(e) => setReason(e.target.value)}
          />
          <button
            onClick={submitLeave}
            className="bg-blue-600 text-white px-4 py-2 rounded w-full"
          >
            Submit
          </button>
        </div>
      </div>
    </>
  );
}