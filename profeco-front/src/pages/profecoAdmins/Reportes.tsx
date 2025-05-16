import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { FileWarning } from 'lucide-react';
import { useProfecoAdmin } from '../../context/ProfecoAdminContext';
import { useAuth } from "../../context/AuthContext";

interface ProductResponse {
  id: string;
  name: string;
}

interface StoreProductMinimal {
  productId: string;
}

interface InconsistencyResponse {
  id: string;
  publishedPrice: number;
  actualPrice: number;
  dateTime: string;
  status: string;
  customer: {
    name: string;
  } | null;
  storeProduct: StoreProductMinimal | null;
}

interface InconsistencyWithProductName extends InconsistencyResponse {
  productName?: string;
}

const Reportes: React.FC = () => {
  const { admin } = useProfecoAdmin();
  const { token } = useAuth();
  const [inconsistencies, setInconsistencies] = useState<InconsistencyWithProductName[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchProductName = async (productId: string): Promise<string> => {
      try {
        const res = await axios.get<ProductResponse>(`/api/v1/products/${productId}`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        return res.data.name;
      } catch {
        return "Producto no encontrado";
      }
    };

    const fetchInconsistencies = async () => {
      try {
        const response = await axios.get<InconsistencyResponse[]>('/api/v1/profeco-admin/inconsistencies', {
          headers: { Authorization: `Bearer ${token}` },
        });

        const dataWithNames = await Promise.all(
          response.data.map(async (inc) => {
            if (inc.storeProduct?.productId) {
              const name = await fetchProductName(inc.storeProduct.productId);
              return { ...inc, productName: name };
            }
            return { ...inc, productName: "Producto desconocido" };
          })
        );

        console.log('Inconsistencias obtenidas:', dataWithNames);
        setInconsistencies(dataWithNames);
      } catch (err) {
        console.error('Error al obtener los reportes:', err);
        setError('Hubo un problema al cargar los reportes.');
      } finally {
        setLoading(false);
      }
    };

    if(token) fetchInconsistencies();
    else {
      setError('No estás autorizado. Por favor inicia sesión.');
      setLoading(false);
    }
  }, [token]);

  // Función para borrar inconsistencia
  const handleDelete = async (id: string) => {
    if (!window.confirm('¿Estás seguro que deseas eliminar esta inconsistencia?')) return;

    try {
      await axios.delete(`/api/v1/profeco-admin/inconsistencies/${id}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setInconsistencies((prev) => prev.filter(inc => inc.id !== id));
    } catch (err) {
      console.error('Error al eliminar inconsistencia:', err);
      alert('Error al eliminar la inconsistencia. Intenta de nuevo.');
    }
  };

  const handleToggleStatus = async (id: string, currentStatus: string) => {
    const newStatus = currentStatus === "OPEN" ? "CLOSED" : "OPEN";

    try {
      await axios.patch(`/api/v1/profeco-admin/inconsistencies/${id}/status`, {
        status: newStatus
      }, {
        headers: { Authorization: `Bearer ${token}` }
      });

      setInconsistencies((prev) =>
        prev.map(inc =>
          inc.id === id ? { ...inc, status: newStatus } : inc
        )
      );
    } catch (err) {
      console.error('Error al actualizar estado:', err);
      alert('Error al cambiar el estado. Intenta de nuevo.');
    }
  };

  return (
    <div className="p-8 mt-20 max-w-7xl mx-auto">
      <header className="mb-8">
        <h1 className="text-3xl font-bold mb-2">Bienvenido, {admin?.name || 'Admin'}</h1>
        <p className="text-gray-600 text-lg">Lista de reportes de inconsistencias</p>
      </header>

      {loading && <p className="text-center text-gray-500">Cargando reportes...</p>}

      {error && <p className="text-center text-red-500">{error}</p>}

      {!loading && !error && inconsistencies.length === 0 && (
        <p className="text-center text-gray-500">No hay reportes disponibles.</p>
      )}

      {!loading && !error && inconsistencies.length > 0 && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {inconsistencies
          .slice()
          .sort((a, b) => a.status === 'CLOSED' ? 1 : -1)
          .map((inc) => (
            <div
              key={inc.id}
              className={`flex flex-col gap-4 p-6 border rounded-xl shadow hover:shadow-lg transition-shadow duration-300
                ${inc.status === "CLOSED" ? "bg-gray-100 opacity-60" : "bg-white"}`}
            >
              <div className="flex items-center gap-3">
                <FileWarning className="w-8 h-8 text-[#902E56]" />
                <h2 className="text-xl font-semibold">
                  {inc.productName ?? 'Producto desconocido'}
                </h2>
              </div>

              <div className="space-y-1 text-gray-700">
                <p><strong>Precio publicado:</strong> ${inc.publishedPrice.toFixed(2)}</p>
                <p><strong>Precio actual:</strong> ${inc.actualPrice.toFixed(2)}</p>
                <p><strong>Estado:</strong> {inc.status === "OPEN" ? "Abierto" : inc.status === "CLOSED" ? "Cerrado" : "Sin estado"}</p>
                <p><strong>Cliente:</strong> {inc.customer?.name ?? 'Desconocido'}</p>
                <p className="text-sm text-gray-500">
                  <strong>Fecha:</strong> {new Date(inc.dateTime).toLocaleString()}
                </p>
              </div>

              <div className="flex gap-4 mt-4">
                <button
                  onClick={() => handleToggleStatus(inc.id, inc.status)}
                  className={`flex-1 rounded-md py-2 transition
                    ${inc.status === "OPEN" 
                      ? "bg-blue-800 hover:bg-blue-900 text-white" 
                      : "bg-green-700 hover:bg-green-800 text-white"}`}
                >
                  {inc.status === "OPEN" ? "Cerrar" : "Abrir"}
                </button>
              </div>
            </div>
        ))}
        </div>
      )}
    </div>
  );
};

export default Reportes;
