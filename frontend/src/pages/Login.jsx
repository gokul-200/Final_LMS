
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const res = await api.post("/auth/login", { username, password });
      // localStorage.setItem("token", res.data.token);
      if(res.status===200){

      localStorage.setItem("username",username);
      // console.log(res.data.role);
      localStorage.setItem("role", res.data.role);

      alert(res.data.message);

      if(res.data.role==="ADMIN"){
        navigate("/admin");
      }else if (res.data.role==="EMPLOYEE"){
        navigate("/employee");
      }else{
        alert("Unknown role:"+ res.data.role);
      }

    }
    else{
      alert("Login failed");
    }
  }catch(err){
    alert(err.response?.data?.message|| "Login failed");
  }

    
  };

  return (
    <div className="flex justify-center items-center h-screen bg-gray-100">
      <div className="bg-white shadow-lg rounded-lg p-6 w-96">
        <h2 className="text-2xl font-bold mb-4">Login</h2>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          className="border w-full p-2 rounded mb-4"
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="border w-full p-2 rounded mb-4"
        />
        <button
          onClick={handleLogin}
          className="w-full bg-blue-600 text-white py-2 rounded"
        >
          Login
        </button>
        <p className="mt-4 text-sm">
          Don't have an account?{" "}
          <span
            onClick={() => navigate("/register")}
            className="text-blue-600 cursor-pointer"
          >
            Register
          </span>
        </p>
      </div>
    </div>
  );
}