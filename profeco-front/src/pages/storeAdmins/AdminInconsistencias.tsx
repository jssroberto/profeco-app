// pages/AdminInconsistencias.tsx
import { AlertCircle } from 'lucide-react';

const inconsistencies = [
  { id: 1, producto: 'Leche Entera 1L', tipo: 'Precio incorrecto', supermercado: 'Sucursal Centro', fecha: '2023-05-15' },
  { id: 2, producto: 'Pan Integral', tipo: 'Producto vencido', supermercado: 'Sucursal Norte', fecha: '2023-05-16' },
  { id: 3, producto: 'Yogur Natural', tipo: 'Falta stock', supermercado: 'Sucursal Sur', fecha: '2023-05-17' },
];

export const AdminInconsistencias = () => {
  return (
    <div className='p-20 mt-10'>
      <div className="flex items-center mb-6">
        <AlertCircle className="w-8 h-8 mr-2" />
        <h2 className="text-2xl font-bold">Reporte de Inconsistencias</h2>
      </div>

      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Producto</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Tipo</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Supermercado</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Fecha</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {inconsistencies.map((item) => (
              <tr key={item.id}>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{item.id}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{item.producto}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{item.tipo}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{item.supermercado}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{item.fecha}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};