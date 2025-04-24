import React from "react";
import SearchProducts from "../components/SearchProducts";
import SearchBar from "../components/Searchbar";

const Productos: React.FC = () => {
  return (
    <div className="pt-26 px-4 sm:px-6 lg:px-8 min-h-screen bg-white">
      <div className="max-w-7xl mx-auto">
        {/* Search bar  */}
        <SearchBar
          context="productos"
          placeholder="Buscar productos..."
        />

        {/* Section #1: idk */}
        <div className="w-full max-w-3xl">
          <SearchProducts />
        </div>
      </div>
    </div>
  );
};

export default Productos;
