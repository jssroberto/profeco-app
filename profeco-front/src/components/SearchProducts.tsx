import React from "react";
import { useLocation } from "react-router-dom";
import SearchedProductCard from "./SearchedProductCard";
import { mockProducts } from "../data/products";

const normalize = (str: string) =>
  str.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "");

const SearchProducts: React.FC = () => {
  const location = useLocation();
  const params = new URLSearchParams(location.search);
  const rawQuery = params.get("search");
  const searchQuery = normalize(rawQuery || "");

  const filteredProducts = rawQuery
  ? mockProducts.filter((product) => {
      const name = normalize(product.name);
      return name.includes(searchQuery);
    })
  : mockProducts;

  return (
    <section className="w-full mt-6 mb-8 max-w-4xl mx-auto px-4">
      <h2 className="text-xl font-medium text-[#611232] mb-4 ml-1">
        {rawQuery
          ? `Productos encontrados: ${filteredProducts.length}`
          : "Escribe algo para buscar productos"}
      </h2>

      <div className="flex flex-col gap-3">
        {filteredProducts.length > 0 ? (
          filteredProducts.map((item, i) => (
            <SearchedProductCard key={i} {...item} />
          ))
        ) : rawQuery ? (
          <p className="text-gray-500">No se encontraron productos.</p>
        ) : null}
      </div>
    </section>
  );
};

export default SearchProducts;
