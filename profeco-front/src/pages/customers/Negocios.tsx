import React, { useState } from "react";
import SearchBar from "../../components/Searchbar";
import PopularSupermarkets from "../../components/PopularSupermarkets";

const Negocios: React.FC = () => {

  const [query, setQuery] = useState("");

  return (
    <div className="pt-26 px-4 sm:px-6 lg:px-8 min-h-screen bg-white">
      <div className="max-w-7xl mx-auto">
        {/* Search bar */}
        <SearchBar 
          context="negocios"
          placeholder="Buscar supermercados..."
          onQueryChange={setQuery}
        />

        {/* Section #1: supermerados populares */}
        <div className="w-full max-w-5xl">
          <PopularSupermarkets query={query} />
        </div>

        {/* Section #2 : more supermarkets */}
      </div>
    </div>
  );
};

export default Negocios;
