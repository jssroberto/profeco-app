import { Navigate, Outlet } from "react-router-dom";

const ProtectedRoute = () => {
  console.log("you are in a private page, u have a token i suppose");
  const token = localStorage.getItem("accessToken");
  return token ? <Outlet /> : <Navigate to="/login" replace />;
};

export default ProtectedRoute;
