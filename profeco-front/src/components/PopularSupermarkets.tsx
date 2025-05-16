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

  if (loading) {
    return <div className="p-4">Cargando supermercados...</div>;
  }

  return (
    <section className="w-full mx-auto mt-10 mb-4 px-4 max-w-7xl ">
      <h2 className="text-xl font-medium text-[#611232] mb-4 ml-1">
        Supermercados populares
      </h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {supermarkets.map((market) => (
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
    </section>
  );
};

export default PopularSupermarkets;