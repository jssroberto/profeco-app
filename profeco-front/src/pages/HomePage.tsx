import React from "react";
import SearchBar from "../components/Searchbar";
import MostSearchedProducts from "../components/MostSearchedProducts";
import PopularSupermarkets from "../components/PopularSupermarkets";

const HomePage: React.FC = () => {
  return (
    <div className="pt-26 px-4 sm:px-6 lg:px-8 min-h-screen bg-white">
      <div className="max-w-7xl mx-auto">

        {/* Section #1 : title with searchbar */}
        <h1 className="text-3xl font-bold text-black mb-6 select-none">Bienvenido!</h1>
        <div className="w-full max-w-3xl">
          <SearchBar
            context="global"
            
          />
        </div>

        {/* Section #2 : prods maas buscados  */}
        <div className="w-full max-w-3xl">
          <MostSearchedProducts />
        </div>

        {/* Section #3 : Supermercati molto popolari  */}
        <div className="w-full max-w-5xl">
        <PopularSupermarkets />
        </div>
      </div>
    </div>
  );
};

export default HomePage;