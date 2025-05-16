import React, { useState } from 'react';
import { useParams, useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import axios from "axios";

interface Product {
  name: string;
  store: string;
  image: string;
  publishedPrice: number;
  storeProductId: string;
}

const ReportInconsistency = () => {
  const { id } = useParams(); 
  const { token } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const product: Product | undefined = location.state?.product;

  const [realPrice, setRealPrice] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    setError(null);
    try {
      await axios.post(
        "http://localhost:8080/api/v1/customer/inconsistencies",
        {
          actualPrice: parseFloat(realPrice),
          storeProductId: id
        },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setSuccess(true);
      setTimeout(() => navigate(-1), 2000);
    } catch (err) {
      setError("No se pudo enviar el reporte. Intenta de nuevo.");
    } finally {
      setSubmitting(false);
    }
  };

  const calculateDifference = () => {
    const realPriceNum = parseFloat(realPrice);
    if (!isNaN(realPriceNum) && product) {
      const diff = realPriceNum - product.publishedPrice;
      const percentage = (diff / product.publishedPrice) * 100;
      return {
        amount: diff.toFixed(2),
        percentage: percentage.toFixed(1)
      };
    }
    return null;
  };

  const difference = calculateDifference();

  if (!product) return <div className="p-8 text-red-600">No se encontró información del producto.</div>;


  const imageUrl = `${product.image}`;

  const publishedPrice = Number(product.publishedPrice).toFixed(2);

  return (
    <div className="max-w-2xl mx-auto p-4 pt-8 mt-24">
      <h1 className="text-2xl font-bold mb-4">Reportar inconsistencia</h1>
      
      <div className="bg-[#ebf7ed] text-[#0e4716] p-4 rounded-lg mb-6">
        Tu reporte ayuda a mantener la transparencia en los precios. ProFeCo revisará la información y tomará las
        medidas necesarias. ¡Gracias por tu colaboración!
      </div>

      <div className="bg-gray-50 p-4 rounded-lg mb-6 flex items-center gap-4">
        <img src={imageUrl} alt={product.name} className="w-20 h-20 object-cover rounded-md" />
        <div>
          <h2 className="font-semibold text-lg">{product.name}</h2>
          <p className="text-gray-600">{product.store}</p>
        </div>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
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
                required
              />
            </div>
          </div>
        </div>

        <div className="bg-gray-50 p-4 rounded-lg flex justify-between items-start">
          <div className="text-center flex-1">
            <div className="text-sm text-gray-600 mb-1">Precio publicado</div>
            <div className="text-2xl font-bold text-[#0e7d34]">
              ${publishedPrice}
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
          <button
            type="button"
            className="px-6 py-2 border rounded-lg hover:bg-gray-50"
            onClick={() => navigate(-1)}
            disabled={submitting}
          >
            Cancelar
          </button>
          <button
            type="submit"
            className="px-6 py-2 bg-[#0e7d34] text-white rounded-lg hover:bg-[#0b6329] cursor-pointer"
            disabled={submitting}
          >
            {submitting ? "Enviando..." : "Enviar reporte"}
          </button>
        </div>
        {success && (
          <div className="mt-4 text-green-600 font-semibold">
            ¡Reporte enviado correctamente!
          </div>
        )}
        {error && (
          <div className="mt-4 text-red-600 font-semibold">
            {error}
          </div>
        )}
      </form>
    </div>
  );
};

export default ReportInconsistency;