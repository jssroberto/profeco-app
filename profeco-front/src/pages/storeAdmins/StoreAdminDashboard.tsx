// AdminDashboard.tsx
import { MessageSquare, AlarmClock, Tags, Bell } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { useEffect } from "react";

const StoreAdminDashboard: React.FC = () => {
  const navigate = useNavigate();

  const cards = [
    {
      icon: <MessageSquare className="w-6 h-6" />,
      title: "Comentarios y calificaciones",
      path: "/comentarios",
    },
    {
      icon: <AlarmClock className="w-6 h-6" />,
      title: "Reporte de Inconsistencias",
      path: "/inconsistencias",
    },
    {
      icon: <Tags className="w-6 h-6" />,
      title: "Precios y ofertas",
      path: "/precios",
    },
    {
      icon: <Bell className="w-6 h-6" />,
      title: "Notificar clientes",
      path: "/notificaciones",
    },
  ];

  return (
    <div className="p-8 space-y-6 mt-20">
      <div>
        <h1 className="text-2xl font-bold">Bienvenido, </h1>
        <p className="text-gray-600"></p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {cards.map(({ icon, title, path }, index) => (
          <button
            key={index}
            onClick={() => navigate(path)}
            className="flex items-center gap-4 p-6 bg-[#902E56] text-white rounded-xl shadow-md hover:bg-[#902e55af] transition cursor-pointer"
          >
            {icon}
            <span className="font-semibold">{title}</span>
          </button>
        ))}
      </div>

      <div className="bg-orange-50 border border-orange-200 rounded-xl p-4 shadow-md flex justify-between items-center">
        <div>
          <h2 className="text-orange-600 font-semibold">Notificaciones</h2>
          <p className="text-sm text-gray-700">
            3 inconsistencias reportadas por consumidores requieren revisión
          </p>
        </div>
        <button
          onClick={() => navigate("/inconsistencias")}
          className="bg-orange-600 text-white text-sm font-semibold py-2 px-4 rounded hover:bg-orange-700"
        >
          Ver ahora
        </button>
      </div>
    </div>
  );
};

export default StoreAdminDashboard;
