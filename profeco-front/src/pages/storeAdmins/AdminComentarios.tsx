import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Star } from "lucide-react";
import { useAuth } from "../../context/AuthContext";
import axios from "axios";
import { useStoreAdmin } from "../../context/StoreAdminContext";

export const AdminComentarios = () => {
  const { store } = useStoreAdmin();
  const { token } = useAuth();
  const [comments, setComments] = useState<any[]>([]);
  const [average, setAverage] = useState<number>(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchComments = async () => {
      setLoading(true);
      try {
        // Obtener comentarios
        const { data: ratings } = await axios.get(
          `http://localhost:8080/api/v1/ratings/store/${store?.id}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        setComments(ratings);

        // Obtener promedio
        const promedioRes = await axios.get(
          `http://localhost:8080/api/v1/ratings/store/${store?.id}/average-score`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        const score = promedioRes.data;
        console.log(score);
        setAverage(score);
      } catch (e) {
        setComments([]);
        setAverage(0);
      } finally {
        setLoading(false);
      }
    };
    if (store?.id && token) fetchComments();
  }, [store?.id, token]);

  if (loading) {
    return <div className="p-10">Cargando comentarios...</div>;
  }

  return (
    <div className="p-10 mt-15">
      <div className="flex items-center mb-6">
        <h3 className="text-2xl font-bold">Comentarios y calificaciones</h3>
      </div>

      <div className="mb-8 p-6 bg-white rounded-lg shadow text-center">
        <div className="text-5xl font-bold mb-2">{average}</div>
        <div className="flex justify-center">
          {[...Array(5)].map((_, i) => (
            <Star
              key={i}
              className={`w-6 h-6 ${
                i < Math.round(average)
                  ? "fill-yellow-400 text-yellow-400"
                  : "text-gray-300"
              }`}
            />
          ))}
        </div>
        <p className="text-gray-600 mt-2">
          Basado en {comments.length} calificaciones
        </p>
      </div>

      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Calificación
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Comentario
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Cliente
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Fecha
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {comments.map((item) => (
              <tr key={item.id}>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="flex">
                    {[...Array(5)].map((_, i) => (
                      <Star
                        key={i}
                        className={`w-4 h-4 ${
                          i <
                          (item.score > 5
                            ? Math.round((item.score / 2147483647) * 5)
                            : Math.round(item.score))
                            ? "fill-yellow-400 text-yellow-400"
                            : "text-gray-300"
                        }`}
                      />
                    ))}
                  </div>
                </td>
                <td className="px-6 py-4 text-sm text-gray-900">
                  {item.comment}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {item.customerName}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {item.date}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
