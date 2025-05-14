// pages/AdminComentarios.tsx
import { Star } from 'lucide-react';

const comments = [
  { id: 1, rating: 5, comment: 'Excelente atención y productos frescos', cliente: 'Juan Pérez', fecha: '2023-05-10' },
  { id: 2, rating: 4, comment: 'Buen servicio pero faltaban algunos productos', cliente: 'María Gómez', fecha: '2023-05-12' },
  { id: 3, rating: 3, comment: 'Precios algo altos pero buena calidad', cliente: 'Carlos Ruiz', fecha: '2023-05-14' },
];

export const AdminComentarios = () => {
  const averageRating = comments.reduce((sum, item) => sum + item.rating, 0) / comments.length;

  return (
    <div className='p-10 mt-15'>
      <div className="flex items-center mb-6">
        <h3 className="text-2xl font-bold">Comentarios y calificaciones</h3>
      </div>

      <div className="mb-8 p-6 bg-white rounded-lg shadow text-center">
        <div className="text-5xl font-bold mb-2">{averageRating.toFixed(1)}</div>
        <div className="flex justify-center">
          {[...Array(5)].map((_, i) => (
            <Star 
              key={i} 
              className={`w-6 h-6 ${i < Math.round(averageRating) ? 'fill-yellow-400 text-yellow-400' : 'text-gray-300'}`} 
            />
          ))}
        </div>
        <p className="text-gray-600 mt-2">Basado en {comments.length} calificaciones</p>
      </div>

      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Calificación</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Comentario</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Cliente</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Fecha</th>
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
                        className={`w-4 h-4 ${i < item.rating ? 'fill-yellow-400 text-yellow-400' : 'text-gray-300'}`} 
                      />
                    ))}
                  </div>
                </td>
                <td className="px-6 py-4 text-sm text-gray-900">{item.comment}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{item.cliente}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{item.fecha}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};