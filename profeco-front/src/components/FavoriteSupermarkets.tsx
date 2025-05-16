import React, { useEffect, useState } from "react";
import SupermarketCard from "./SupermarketCard";
import api from "../api/axiosConfig";

interface FavoriteSupermarketsProps {
  customerId?: string;
}

interface Supermarket {
  id: string;
  name: string;
  imageUrl: string;
  location: string;
  rating: number;
  reviews: number;
  description: string;
}

const FavoriteSupermarkets: React.FC<FavoriteSupermarketsProps> = ({ customerId }) => {
  const [supermarkets, setSupermarkets] = useState<Supermarket[]>([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(0);
  const supermarketsPerPage = 2;

  useEffect(() => {
    const fetchSupermarkets = async () => {
      setLoading(true);
      try {
        // Obtener los IDs de supermercados favoritos
        const { data } = await api.get(`/preferences/favorite-stores`);
        const ids: string[] = Array.from(data.favoriteStoreIds || []);
        // Obtener la info de cada supermercado favorito
        const marketsWithRatings = await Promise.all(
          ids.map(async (id) => {
            try {
              const res = await api.get(`/stores/${id}`);
              const store = res.data;
              // Obtener rating promedio y número de reviews
              let rating = 0;
              let reviews = 0;
              try {
                const ratingRes = await api.get(`/ratings/store/${id}/average-score`);
                rating = ratingRes.data || 0;
                reviews = Array.isArray(store.ratingsIds) ? store.ratingsIds.length : 0;
              } catch (e) {
                // Si falla, deja rating y reviews en 0
              }
              let imageUrl = store.imageUrl;
              if (imageUrl && !imageUrl.startsWith('http')) {
                imageUrl = `http://${imageUrl}`;
              }
              return {
                id: store.id,
                name: store.name,
                imageUrl,
                location: store.location,
                rating,
                reviews,
                description: `Ubicación: ${store.location}`,
              };
            } catch (err) {
              return null;
            }
          })
        );
        setSupermarkets(marketsWithRatings.filter(Boolean) as Supermarket[]);
      } catch (err) {
        setSupermarkets([]);
      } finally {
        setLoading(false);
      }
    };
    fetchSupermarkets();
  }, [customerId]);

  const totalPages = Math.ceil(supermarkets.length / supermarketsPerPage);
  const startIndex = currentPage * supermarketsPerPage;
  const visibleSupermarkets = supermarkets.slice(startIndex, startIndex + supermarketsPerPage);

  if (!customerId) return <div>No hay cliente.</div>;
  if (loading) return <div className="p-4">Cargando supermercados favoritos...</div>;

  return (
    <section className="w-full mx-auto mt-4 mb-4 px-4 max-w-7xl">
      <h2 className="text-xl font-medium text-[#611232] mb-4 ml-1">
        Supermercados favoritos
      </h2>
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {supermarkets.length === 0 ? (
          <div className="col-span-full">No tienes supermercados favoritos.</div>
        ) : (
          visibleSupermarkets.map((market) => (
            <SupermarketCard
              key={market.id}
              id={market.id}
              name={market.name}
              image={market.imageUrl}
              reviews={market.reviews}
              rating={market.rating}
              description={market.description}
            />
          ))
        )}
      </div>
      {totalPages > 1 && (
        <div className="flex justify-center items-center gap-4 mt-8">
          <button
            onClick={() => setCurrentPage(prev => prev > 0 ? prev - 1 : prev)}
            disabled={currentPage === 0}
            className="px-4 py-2 rounded-lg border border-[#681837] text-[#681837] disabled:opacity-50 disabled:cursor-not-allowed hover:bg-[#681837] hover:text-white transition-colors"
          >
            Anterior
          </button>
          <span className="text-[#681837] font-medium">
            Página {currentPage + 1} de {totalPages}
          </span>
          <button
            onClick={() => setCurrentPage(prev => prev < totalPages - 1 ? prev + 1 : prev)}
            disabled={currentPage === totalPages - 1}
            className="px-4 py-2 rounded-lg border border-[#681837] text-[#681837] disabled:opacity-50 disabled:cursor-not-allowed hover:bg-[#681837] hover:text-white transition-colors"
          >
            Siguiente
          </button>
        </div>
      )}
    </section>
  );
};

export default FavoriteSupermarkets;
