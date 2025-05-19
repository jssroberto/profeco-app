// AdminDashboard.tsx
import { AlarmClock, Bell, MessageSquare, Tags } from "lucide-react";
import { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/axiosConfig";
import { useAuth } from "../../context/AuthContext";
import { useStoreAdmin } from "../../context/StoreAdminContext";

// Added ApiError interface (assuming it's similar to AdminPrecios.tsx)
interface ApiError {
  message: string;
}

const StoreAdminDashboard: React.FC = () => {
  const navigate = useNavigate();
  const { admin, store } = useStoreAdmin();
  const { token } = useAuth(); // Added token from useAuth

  // State for inconsistency count
  const [inconsistencyCount, setInconsistencyCount] = useState<number | null>(null);
  const [loadingCount, setLoadingCount] = useState<boolean>(true);
  const [countError, setCountError] = useState<string | null>(null);
  
  console.log(store?.id);

  const fetchInconsistencyCount = useCallback(async () => {
    if (!token) { // Store ID is not used in the provided API endpoint
      setLoadingCount(false);
      setInconsistencyCount(0); 
      setCountError("Usuario no autenticado para cargar el contador.");
      return;
    }
    setLoadingCount(true);
    setCountError(null);
    try {
      const response = await api.get<number>(
        '/store-admin/inconsistencies/count', // Using the endpoint from user prompt
        { headers: { Authorization: `Bearer ${token}` } } // Assuming Bearer token auth
      );
      setInconsistencyCount(response.data);
    } catch (error) {
      console.error("Error fetching inconsistency count:", error);
      const errorMessage = (error as { response?: { data?: ApiError }; message?: string })?.response?.data?.message || (error as Error)?.message || "Error al cargar el contador de inconsistencias";
      setCountError(errorMessage);
      setInconsistencyCount(null);
    } finally {
      setLoadingCount(false);
    }
  }, [token]); // Dependency on token

  useEffect(() => {
    fetchInconsistencyCount();
  }, [fetchInconsistencyCount]); // fetchInconsistencyCount will change if token changes

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
        <h1 className="text-2xl font-bold">Bienvenido, {admin?.name}</h1>
        <p className="text-gray-600">{store?.name}, {store?.location}</p>
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
          {loadingCount && <p className="text-sm text-gray-700">Cargando inconsistencias...</p>}
          {countError && <p className="text-sm text-red-500">{countError}</p>}
          {!loadingCount && inconsistencyCount !== null && (
            <p className="text-sm text-gray-700">
              {inconsistencyCount > 0
                ? `${inconsistencyCount} inconsistencia${inconsistencyCount === 1 ? '' : 's'} reportada${inconsistencyCount === 1 ? '' : 's'} por consumidores requieren revisión`
                : "No hay inconsistencias reportadas."}
            </p>
          )}
          {!loadingCount && inconsistencyCount === null && !countError && (
             <p className="text-sm text-gray-700">No se pudo cargar el número de inconsistencias.</p>
          )}
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
