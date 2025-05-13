import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

const RoleProtectedRoute = ({ allowedRoles }: { allowedRoles: string[] }) => {
  const { isAuthenticated, role, isLoading } = useAuth();

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (!role || !allowedRoles.includes(role)) {
    return <Navigate to="/unauthorized" replace />;
  }

  return <Outlet />;
};

export default RoleProtectedRoute;