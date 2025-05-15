// components/RoleProtectedRoute.tsx
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

interface RoleProtectedRouteProps {
  allowedRoles: string[];
}

export const RoleProtectedRoute = ({ allowedRoles }: RoleProtectedRouteProps) => {
  const { token } = useAuth();
  const userRole = localStorage.getItem('userRole'); 

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  if (!allowedRoles.includes(userRole || '')) {
    return <Navigate to="/unauthorized" replace />;
  }

  return <Outlet />;
};