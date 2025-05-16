// pages/AdminInconsistencias.tsx
import { AlertCircle } from "lucide-react";
import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext";

export const AdminInconsistencias = () => {
  const [inconsistencies, setInconsistencies] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const { token } = useAuth();

  useEffect(() => {
    const fetchInconsistencies = async () => {
      setLoading(true);
      try {
        const data = await fetch(
          "http://localhost:8080/api/v1/store-admin/inconsistencies",
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        ).then((res) => res.json());
        setInconsistencies(data);
      } catch {
        setInconsistencies([]);
      } finally {
        setLoading(false);
      }
    };
    if (token) fetchInconsistencies();
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
                ID
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Tipo
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
                <td colSpan={6} className="text-center py-8 text-gray-400">
                  Cargando...
                </td>
              </tr>
            ) : inconsistencies.length === 0 ? (
              <tr>
                <td colSpan={6} className="text-center py-8 text-gray-400">
                  No hay inconsistencias reportadas.
                </td>
              </tr>
            ) : (
              inconsistencies.map((item) => (
                <tr key={item.id}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {item.id}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    Precio Incorrecto
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    ${item.publishedPrice}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    ${item.actualPrice}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {item.dateTime}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {item.status}
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
