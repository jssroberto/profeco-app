import React from "react";
import MostSearchedProducts from "../../components/MostSearchedProducts";
import PopularSupermarkets from "../../components/PopularSupermarkets";
import { useCustomer } from "../../context/CustomerContext";
import FavoriteSupermarkets from "../../components/FavoriteSupermarkets"; // Nuevo componente
import ShoppingList from "../../components/ShoppingList"; // Nuevo componente

const HomePage: React.FC = () => {
  const { customer } = useCustomer();

  return (
    <div className="pt-26 px-4 sm:px-6 lg:px-8 min-h-screen bg-white">
      <div className="max-w-7xl mx-auto">
        {/* Sección de bienvenida */}
        <div className="bg-gradient-to-r from-[#681837] to-[#9C2759] rounded-2xl p-8 mb-12 text-white shadow-lg">
          <div className="max-w-3xl">
            <h1 className="text-4xl font-bold mb-2">
              ¡Bienvenido{customer?.name ? `, ${customer.name}` : ''}!
            </h1>
            <p className="text-lg text-white/90">
              Encuentra las mejores ofertas en tus productos favoritos
            </p>
          </div>
        </div>

        {/* Sección de preferencias */}
        <div className="w-full max-w-3xl mb-12">
          <div className="flex items-center gap-3 mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="#681837" className="w-6 h-6">
              <path strokeLinecap="round" strokeLinejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
            </svg>
            <h2 className="text-2xl font-bold text-[#681837]">Tus Favoritos</h2>
          </div>
          <FavoriteSupermarkets customerId={customer?.id} />
        </div>

        {/* Section #2: lista de compras */}
        <div className="w-full max-w-5xl mb-8">
          <ShoppingList customerId={customer?.id} />
        </div>

        {/* Section #3 : productos más buscados */}
        <div className="w-full max-w-5xl">
          <MostSearchedProducts />
        </div>

        {/* Section #4 : supermercados populares */}
        <div className="w-full max-w-5xl">
          <PopularSupermarkets />
        </div>
      </div>
    </div>
  );
};

export default HomePage;