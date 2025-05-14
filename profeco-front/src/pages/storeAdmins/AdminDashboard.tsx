// components/AdminLayout.tsx
import { LayoutDashboard, Star, AlertCircle, Tags, Bell } from 'lucide-react';
import { Link, Outlet } from 'react-router-dom';

export const AdminDashboard = () => {
  return (
    <div className="flex h-screen bg-gray-100">
      {/* Sidebar */}
      <div className="w-64 bg-white shadow-md">
        <div className="p-4 border-b">
          <h1 className="text-xl font-bold">Panel de Administración</h1>
          <p className="text-sm text-gray-600">Supermercado "La Mejor"</p>
        </div>
        
        <nav className="p-4">
          <Link to="/admin/comentarios" className="flex items-center p-2 mb-2 rounded hover:bg-gray-200">
            <Star className="w-5 h-5 mr-2" />
            Comentarios y calificaciones
          </Link>
          <Link to="/admin/productos" className="flex items-center p-2 mb-2 rounded hover:bg-gray-200">
            <Tags className="w-5 h-5 mr-2" />
            Gestionar Productos
          </Link>
          <Link to="/admin/inconsistencias" className="flex items-center p-2 mb-2 rounded hover:bg-gray-200">
            <AlertCircle className="w-5 h-5 mr-2" />
            Reporte de Inconsistencias
          </Link>
          <Link to="/admin/notificaciones" className="flex items-center p-2 mb-2 rounded hover:bg-gray-200">
            <Bell className="w-5 h-5 mr-2" />
            Notificaciones
          </Link>
        </nav>
      </div>

      {/* Main Content */}
      <div className="flex-1 overflow-auto p-6">
        <Outlet />
      </div>
    </div>
  );
};
