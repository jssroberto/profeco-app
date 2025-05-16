import React, { useState } from "react";
import { Link } from "react-router-dom";
import SearchBar from "../../components/Searchbar";
import SearchProducts from "../../components/SearchProducts";

const Productos: React.FC = () => {
  const [query, setQuery] = useState("");

  return (
    <div className="pt-26 px-4 sm:px-6 lg:px-8 min-h-screen bg-white">
      <div className="max-w-7xl mx-auto">
        {/* Search bar  */}
        <SearchBar
          context="productos"
          placeholder="Buscar productos..."
          onQueryChange={setQuery}
        />

        {/* Section #1: idk */}
        <div className="w-full max-w-3xl">
          {/* 
            Sugerencia: 
            Modifica SearchProducts para que cada resultado de producto tenga un Link a `/producto/{id}` 
            donde id es el id base del producto, no el storeProductId.
          */}
          <SearchProducts query={query} />
        </div>
      </div>
    </div>
  );
};

export default Productos;
