import React, { useState } from 'react';
import { useParams, Link } from "react-router-dom";
import { mockProducts } from '../data/products';
import { stores } from '../data/stores';

const normalize = (str: string): string =>
  str.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "").replace(/\s+/g, "-");

const ReportInconsistency = () => {
  const { id, tienda } = useParams<{ id: string; tienda: string }>();
  const productIndex = parseInt(id || "0", 10) - 1;
  const productData = mockProducts[productIndex];

  const offer = productData?.offers.find(o =>
    normalize(o.store_id).includes(normalize(tienda || ""))
  );
  const storeName = stores.find(store => store.id === offer?.store_id)?.name ?? "Tienda desconocida";

  const [realPrice, setRealPrice] = useState("");
  const [description, setDescription] = useState("");
  const [observationDate, setObservationDate] = useState("2024-04-02");

  if (!productData || !offer) {
    return (
      <div className="max-w-xl mx-auto mt-24 text-center text-gray-600">
        No se encontró el producto o la tienda especificada.
      </div>
    );
  }

  const { name, imageUrl } = productData;
  const publishedPrice = parseFloat(offer.price.replace("$", "").replace(",", ""));

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log("Reporte enviado");
  };

  const calculateDifference = () => {
    const realPriceNum = parseFloat(realPrice);
    if (!isNaN(realPriceNum)) {
      const diff = realPriceNum - publishedPrice;
      const percentage = (diff / publishedPrice) * 100;
      return {
        amount: diff.toFixed(2),
        percentage: percentage.toFixed(1)
      };
    }
    return null;
  };

  const difference = calculateDifference();

  return (
    <div className="max-w-2xl mx-auto p-4 pt-8 mt-24">
      <h1 className="text-2xl font-bold mb-4">Reportar inconsistencia</h1>

      <div className="bg-[#ebf7ed] text-[#0e4716] p-4 rounded-lg mb-6">
        Tu reporte ayuda a mantener la transparencia en los precios. ProFeCo revisará la información y tomará las
        medidas necesarias. ¡Gracias por tu colaboración!
      </div>

      <div className="bg-gray-50 p-4 rounded-lg mb-6 flex items-center gap-4">
        <img src={imageUrl} alt={name} className="w-20 h-20 object-cover rounded-md" />
        <div>
          <h2 className="font-semibold text-lg">{name}</h2>
          <p className="text-gray-600">{storeName}</p>
        </div>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
        <div>
          <label className="block mb-2 font-medium">
            Tipo de inconsistencia<span className="text-red-500">*</span>
          </label>
          <select className="w-full p-2 border rounded-lg bg-white">
            <option>Precio mayor al publicado</option>
          </select>
        </div>

        <div className="flex gap-4">
          <div className="flex-1">
            <label className="block mb-2 font-medium">
              Precio real observado<span className="text-red-500">*</span>
            </label>
            <div className="relative">
              <span className="absolute left-3 top-2 text-gray-500">$</span>
              <input
                type="number"
                value={realPrice}
                onChange={(e) => setRealPrice(e.target.value)}
                className="w-full pl-7 pr-3 py-2 border rounded-lg"
                placeholder="0.00"
                step="0.01"
              />
            </div>
          </div>

          <div className="flex-1">
            <label className="block mb-2 font-medium">
              Fecha de la observación<span className="text-red-500">*</span>
            </label>
            <input
              type="date"
              value={observationDate}
              onChange={(e) => setObservationDate(e.target.value)}
              className="w-full p-2 border rounded-lg"
            />
          </div>
        </div>

        <div>
          <label className="block mb-2 font-medium">
            Descripción detallada<span className="text-red-500">*</span>
          </label>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            className="w-full p-3 border rounded-lg min-h-[100px]"
            placeholder="El precio mostrado en la aplicación es diferente al precio en tienda..."
          />
          <p className="text-sm text-gray-500 mt-1">
            Mínimo 20 caracteres. Incluye cualquier información relevante como ubicación exacta del producto, respuesta del personal, etc.
          </p>
        </div>

        <div className="bg-gray-50 p-4 rounded-lg flex justify-between items-start">
          <div className="text-center flex-1">
            <div className="text-sm text-gray-600 mb-1">Precio publicado</div>
            <div className="text-2xl font-bold text-[#0e7d34]">
              ${publishedPrice.toFixed(2)}
            </div>
            <div className="text-sm text-gray-500">
              Actualizado: 02/04/2024
            </div>
          </div>
          <div className="text-center flex-1">
            <div className="text-sm text-gray-600 mb-1">Precio real</div>
            <div className="text-2xl font-bold text-[#d91e1e]">
              ${realPrice || "0.00"}
            </div>
            {difference && (
              <div className="text-sm bg-[#fff7e1] text-[#8b6c15] px-2 py-1 rounded inline-block">
                Diferencia: +${difference.amount} ({difference.percentage}%)
              </div>
            )}
          </div>
        </div>

        <div className="flex justify-end gap-4 pt-4">
          <Link to={`/productos/${id}/${offer.store_id}`}>
            <button
              type="button"
              className="px-6 py-2 border rounded-lg hover:bg-gray-50 cursor-pointer"
            >
              Cancelar
            </button>
          </Link>
          <button
            type="submit"
            className="px-6 py-2 bg-[#0e7d34] text-white rounded-lg hover:bg-[#0b6329]"
          >
            Enviar reporte
          </button>
        </div>
      </form>
    </div>
  );
};

export default ReportInconsistency;