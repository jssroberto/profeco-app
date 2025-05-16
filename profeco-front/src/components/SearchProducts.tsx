import React, { useEffect, useState } from "react";
import SearchedProductCard from "./SearchedProductCard";
import { useAuth } from "../context/AuthContext";

interface Product {
  id: string;
  name: string;
  imageUrl: string;
  categoryName: string;
  brandName: string;
}

const SearchProducts: React.FC<{ query: string }> = ({ query }) => {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const { token } = useAuth();

  useEffect(() => {
    const fetchProducts = async () => {
      setLoading(true);
      try {
        const res = await fetch("http://localhost:8080/api/v1/products",
          {
            headers: { Authorization: `Bearer ${token}`}
          }
        );
        const data = await res.json();
        setProducts(Array.isArray(data) ? data : []);
      } catch {
        setProducts([]);
      } finally {
        setLoading(false);
      }
    };
    fetchProducts();
  }, []);

  const filtered = Array.isArray(products)
  ? products.filter(
      (p) =>
        p.name.toLowerCase().includes(query.toLowerCase()) ||
        p.brandName.toLowerCase().includes(query.toLowerCase()) ||
        p.categoryName.toLowerCase().includes(query.toLowerCase())
    )
  : [];

  return (
    <section className="w-full mt-6 mb-8 max-w-4xl mx-auto px-4">
      <h2 className="text-xl font-medium text-[#611232] mb-4 ml-1">
        Productos encontrados: {filtered.length}
      </h2>
      <div className="flex flex-col gap-3">
        {loading ? (
          <div className="text-gray-500">Cargando productos...</div>
        ) : filtered.length === 0 ? (
          <div className="text-gray-500">No se encontraron productos.</div>
        ) : (
          filtered.map((item) => (
            <SearchedProductCard
              key={item.id}
              imageUrl={
                item.imageUrl.startsWith("http")
                  ? item.imageUrl
                  : `http://${item.imageUrl}`
              }
              name={item.name}
              brand={item.brandName}
              category={item.categoryName}
              offers={[]} 
            />
          ))
        )}
      </div>
    </section>
  );
};

export default SearchProducts;