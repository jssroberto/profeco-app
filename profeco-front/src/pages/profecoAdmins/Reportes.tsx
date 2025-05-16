import React, { useEffect, useState } from 'react';
import api from '../../api/axiosConfig';
import { useAuth } from "../../context/AuthContext";
import { useProfecoAdmin } from '../../context/ProfecoAdminContext';

interface Inconsistency {
  id: string;
  publishedPrice: number;
  actualPrice: number;
  dateTime: string;
  status: string;
  customerId: string;
  storeProductId: string;
  storeName: string;
  productName: string;
}

const Reportes: React.FC = () => {
  const { admin } = useProfecoAdmin();
  const { token } = useAuth();
  const [inconsistencies, setInconsistencies] = useState<Inconsistency[]>([]);

  const updateInconsistencyStatus = async (id: string, status: string) => {
    if (token) {
      try {
        await api.patch(`/profeco-admin/inconsistencies/${id}/close`);
        setInconsistencies(prevInconsistencies =>
          prevInconsistencies.map(inc =>
            inc.id === id ? { ...inc, status: status } : inc
          )
        );
        console.log(`Inconsistency ${id} status updated to ${status}`);
      } catch (error) {
        console.error("Error updating inconsistency status:", error);
      }
    }
  };

  useEffect(() => {
    const fetchInconsistencies = async () => {
      if (token) {
        try {
          const response = await api.get<Inconsistency[]>('/profeco-admin/inconsistencies');
          console.log("Inconsistencies:", response.data);
          setInconsistencies(response.data);
        } catch (error) {
          console.error("Error fetching inconsistencies:", error);
        }
      }
    };

    fetchInconsistencies();
  }, [token]);

  if (!token) {
    return (
      <div className="p-8 mt-20 max-w-7xl mx-auto text-center">
        <p className="text-red-500 text-lg">No estás autorizado. Por favor inicia sesión.</p>
      </div>
    );
  }
  return (
    <div className="p-8 mt-20 max-w-7xl mx-auto">
      <header className="mb-8">
        <h1 className="text-3xl font-bold mb-2 text-gray-800">Bienvenido, {admin?.name || 'Admin'}</h1>
      </header>
      {inconsistencies.length > 0 ? (
        <div className="">
          <h2 className="text-2xl font-semibold mb-10 text-gray-700">Reportes de Inconsistencias</h2>
          <div className="space-y-8">
            {inconsistencies.map((item) => (
              <div key={item.id} className="bg-white p-6 rounded-lg shadow-md flex justify-between items-center">
                <div className="flex-grow flex space-x-10 items-start">
                  <div className="min-w-36">
                    <p className="text-sm text-gray-500 uppercase tracking-wider">Tienda</p>
                    <p className="text-xl font-semibold text-gray-800 truncate">{item.storeName}</p>
                  </div>
                  <div className="min-w-48">
                    <p className="text-sm text-gray-500 uppercase tracking-wider">Producto</p>
                    <p className="text-xl font-semibold text-gray-800 truncate">{item.productName}</p>
                  </div>
                  <div className="min-w-0">
                    <p className="text-sm text-gray-500 uppercase tracking-wider">Precio Publicado</p>
                    <p className="text-lg text-gray-700">${item.publishedPrice.toFixed(2)}</p>
                  </div>
                  <div className="min-w-0">
                    <p className="text-sm text-gray-500 uppercase tracking-wider">Precio Real</p>
                    <p className="text-lg text-gray-700">${item.actualPrice.toFixed(2)}</p>
                  </div>
                  <div className="min-w-0">
                    <p className="text-sm text-gray-500 uppercase tracking-wider">Fecha y Hora</p>
                    <p className="text-base text-gray-700 whitespace-nowrap">{new Date(item.dateTime).toLocaleString()}</p>
                  </div>
                </div>
                <button
                  className={`font-bold py-3 px-6 rounded focus:outline-none focus:shadow-outline transition-colors duration-300 self-center ml-6 flex-shrink-0 text-base ${
                    item.status === 'CLOSED' ? 'bg-gray-500 hover:bg-gray-700 cursor-not-allowed' : 'bg-blue-500 hover:bg-blue-700 cursor-pointer'
                  } text-white`}
                  onClick={() => {
                    if (window.confirm("¿Deseas marcar esta inconsistencia como 'Cerrada'? Se aplicará una penalización a la tienda.")) {
                      updateInconsistencyStatus(item.id, "CLOSED");
                    }
                  }}
                  disabled={item.status === 'CLOSED'}
                >
                  {item.status === 'OPEN' ? 'Abierto' : item.status === 'CLOSED' ? 'Cerrado' : item.status}
                </button>
              </div>
            ))}
          </div>
        </div>
      ) : (
        <div className="bg-white shadow-xl rounded-lg p-6 text-center">
          <p className="text-gray-700 text-xl">No hay inconsistencias reportadas por el momento.</p>
        </div>
      )}
    </div>
  );
};

export default Reportes;
