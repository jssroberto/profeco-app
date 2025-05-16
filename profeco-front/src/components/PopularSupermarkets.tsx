import { useEffect, useState } from "react";
import SupermarketCard from "./SupermarketCard";
import axios from "axios";
import { useAuth } from "../context/AuthContext";

interface Supermarket {
  id: string;
  name: string;
  imageUrl: string;
  location: string;
  rating: number;
  reviews: number;
  description: string;
}

const PopularSupermarkets = () => {
  const [supermarkets, setSupermarkets] = useState<Supermarket[]>([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(0);
  const supermarketsPerPage = 2;
  const { token } = useAuth();

  useEffect(() => {
    const fetchSupermarkets = async () => {
      try {
        const { data } = await axios.get("http://localhost:8080/api/v1/stores", {headers: {
              Authorization: `Bearer ${token}`,
            }});
        const marketsWithRatings = await Promise.all(
          data.map(async (store: any) => {
            let rating = 0;
            let reviews = 0;
            try {
              const res = await axios.get(
                `http://localhost:8080/api/v1/ratings/store/${store.id}/average-score`
              );
              rating = res.data || 0;
              reviews = Array.isArray(store.ratingsIds) ? store.ratingsIds.length : 0;
            } catch {
            }
            return {
              id: store.id,
              name: store.name,
              imageUrl: `http://${store.imageUrl}`,
              location: store.location,
              rating,
              reviews,
              description: `Ubicación: ${store.location}`,
            };
          })
        );
        setSupermarkets(marketsWithRatings);
      } catch {
        setSupermarkets([]);
      } finally {
        setLoading(false);
      }
    };

    fetchSupermarkets();
  }, []);

  const totalPages = Math.ceil(supermarkets.length / supermarketsPerPage);
  const startIndex = currentPage * supermarketsPerPage;
  const visibleSupermarkets = supermarkets.slice(startIndex, startIndex + supermarketsPerPage);

  const nextPage = () => {
    if (currentPage < totalPages - 1) {
      setCurrentPage(prev => prev + 1);
    }
  };

  const prevPage = () => {
    if (currentPage > 0) {
      setCurrentPage(prev => prev - 1);
    }
  };

  if (loading) {
    return <div className="p-4">Cargando supermercados...</div>;
  }

  return (
    <section className="w-full mx-auto mt-10 mb-4 px-4 max-w-7xl">
      <h2 className="text-xl font-medium text-[#611232] mb-4 ml-1">
        Supermercados populares
      </h2>
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {visibleSupermarkets.map((market) => (
          <SupermarketCard
            key={market.id}
            id={market.id}
            name={market.name}
            image={market.imageUrl}
            reviews={market.reviews}
            rating={market.rating}
            description={market.description}
          />
        ))}
      </div>
      {supermarkets.length > supermarketsPerPage && (
        <div className="flex justify-center items-center mt-6 gap-4">
          <button
            onClick={prevPage}
            disabled={currentPage === 0}
            className="px-4 py-2 rounded-lg border border-[#aaadb0] text-[#681837] disabled:opacity-50 disabled:cursor-not-allowed hover:bg-[#681837] hover:text-white transition-colors"
          >
            Anterior
          </button>
          <span className="text-[#681837]">
            Página {currentPage + 1} de {totalPages}
          </span>
          <button
            onClick={nextPage}
            disabled={currentPage === totalPages - 1}
            className="px-4 py-2 rounded-lg border border-[#aaadb0] text-[#681837] disabled:opacity-50 disabled:cursor-not-allowed hover:bg-[#681837] hover:text-white transition-colors"
          >
            Siguiente
          </button>
        </div>
      )}
    </section>
  );
};

export default PopularSupermarkets;