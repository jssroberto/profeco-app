import { AlertCircle } from "lucide-react";
import { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import { useAuth } from "../../context/AuthContext";

interface Inconsistency {
  id: string;
  publishedPrice: number;
  actualPrice: number;
  dateTime: string;
  status: string;
  productName: string;
}

export const AdminInconsistencias = () => {
  const [inconsistencies, setInconsistencies] = useState<Inconsistency[]>([]);
  const [loading, setLoading] = useState(true);
  const { token } = useAuth();

  useEffect(() => {
    const fetchInconsistencies = async () => {
      setLoading(true);
      try {
        const response = await api.get<Inconsistency[]>(
          "/store-admin/inconsistencies",
        );
        setInconsistencies(response.data);
      } catch (error) {
        console.error("Error fetching inconsistencies:", error);
        setInconsistencies([]);
      } finally {
        setLoading(false);
      }
    };
    if (token) {
      fetchInconsistencies();
    } else {
      setLoading(false);
      setInconsistencies([]);
    }
  }, [token]);

  return (
    <div className="p-20 mt-10">
      <div className="flex items-center mb-6">
        <AlertCircle className="w-8 h-8 mr-2" />
        <h2 className="text-2xl font-bold">Reporte de Inconsistencias</h2>
      </div>

      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Producto
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Precio Publicado
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Precio Real
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Fecha
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Estado
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {loading ? (
              <tr>
                <td colSpan={5} className="text-center py-8 text-gray-400">
                  Cargando...
                </td>
              </tr>
            ) : inconsistencies.length === 0 ? (
              <tr>
                <td colSpan={5} className="text-center py-8 text-gray-400">
                  No hay inconsistencias reportadas.
                </td>
              </tr>
            ) : (
              inconsistencies.map((item) => (
                <tr key={item.id}>
                  <td className="px-6 py-4 whitespace-nowrap text-base text-gray-700">
                    {item.productName}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-base text-gray-700">
                    ${item.publishedPrice.toFixed(2)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-base text-gray-700">
                    ${item.actualPrice.toFixed(2)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-base text-gray-700">
                    {new Date(item.dateTime).toLocaleString()}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-base text-gray-700">
                    {item.status === 'OPEN' ? 'Abierto' : item.status === 'CLOSED' ? 'Cerrado' : item.status}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};
