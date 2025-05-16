import React, { useState } from "react";
import { useAuth } from "../../context/AuthContext";

interface StoreWishProps {
  storeId: string;
}

const StoreWish: React.FC<StoreWishProps> = ({ storeId }) => {
  const [description, setDescription] = useState("");
  const [message, setMessage] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const { token } = useAuth();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setMessage(null);

    try {
      const response = await fetch("http://localhost:8080/api/v1/customer/wishes", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          description,
          storeId,
        }),
      });

      if (response.ok) {
        setMessage("¡Deseo agregado exitosamente!");
        setDescription("");
      } else {
        setMessage("Ocurrió un error al agregar el deseo.");
      }
    } catch (error) {
      setMessage("Ocurrió un error al conectar con el servidor.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="bg-white rounded-xl shadow-md border border-gray-200 p-6 mb-8 ml-32 max-w-xl flex flex-col gap-4"
    >
      <label className="block text-gray-700 font-medium mb-1">
        Escribe tu deseo:
        <input
          type="text"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          required
          disabled={loading}
          className="mt-2 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#681837] transition"
          placeholder="¿Qué te gustaría encontrar en este supermercado?"
        />
      </label>
      <button
        type="submit"
        disabled={loading || !description.trim()}
        className={`w-full py-2 rounded-lg font-semibold transition-colors ${
          loading || !description.trim()
            ? "bg-gray-300 text-gray-500 cursor-not-allowed"
            : "bg-[#681837] text-white hover:bg-[#561429] cursor-pointer"
        }`}
      >
        {loading ? "Enviando..." : "Agregar deseo"}
      </button>
      {message && (
        <div
          className={`text-center mt-2 text-sm font-medium ${
            message.includes("exitosamente")
              ? "text-green-600"
              : "text-red-500"
          }`}
        >
          {message}
        </div>
      )}
    </form>
  );
};

export default StoreWish;