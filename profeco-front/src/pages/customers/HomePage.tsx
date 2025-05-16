import React from "react";
import MostSearchedProducts from "../../components/MostSearchedProducts";
import PopularSupermarkets from "../../components/PopularSupermarkets";
import { useCustomer } from "../../context/CustomerContext";
import FavoriteSupermarkets from "../../components/FavoriteSupermarkets";
import ShoppingList from "../../components/ShoppingList";

const HomePage: React.FC = () => {
  const { customer } = useCustomer();

  return (
    <div className="min-h-screen bg-gradient-to-br from-[#f8e6ef] via-[#f7f2fa] to-[#f3e6f7] pb-16">
      <div className="max-w-7xl mx-auto pt-10 px-4 sm:px-6 lg:px-8">
        {/* Banner de bienvenida moderno */}
        <div className="relative overflow-hidden rounded-3xl mb-12 shadow-2xl bg-gradient-to-br from-[#681837] via-[#8C2245] to-[#9C2759] p-12 md:p-16 text-white flex flex-col gap-6">
          {/* Círculos decorativos blur */}
          <div className="absolute -top-16 -left-16 w-64 h-64 bg-[#fff]/10 rounded-full blur-3xl z-0" />
          <div className="absolute -bottom-20 -right-20 w-80 h-80 bg-[#fff]/10 rounded-full blur-3xl z-0" />
          <div className="relative z-10">
            <span className="inline-block bg-white/20 backdrop-blur px-4 py-1 rounded-full text-xs font-semibold tracking-widest mb-4 shadow border border-white/30">
              ProfecoApp
            </span>
            <h1 className="text-5xl md:text-6xl font-extrabold mb-4 drop-shadow-lg">
              ¡Bienvenido{customer?.name ? `, ${customer.name}` : ''}!
            </h1>
            <p className="text-2xl md:text-3xl font-light text-white/90 max-w-2xl mb-2">
              Encuentra las mejores ofertas, administra tus favoritos y tu lista de compras en un solo lugar.
            </p>
            <p className="text-base md:text-lg text-white/70 max-w-2xl">
              Explora supermercados, descubre productos populares y mantente al tanto de las promociones más recientes.
            </p>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-10 mb-12">
          {/* Sección de favoritos */}
          <div className="bg-white/90 rounded-3xl p-8 shadow-xl flex flex-col h-full">
            <div className="flex items-center gap-3 mb-6">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="#681837" className="w-7 h-7">
                <path strokeLinecap="round" strokeLinejoin="round" d="M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z" />
              </svg>
              <h2 className="text-2xl font-bold text-[#681837] tracking-tight">Tus Favoritos</h2>
            </div>
            <FavoriteSupermarkets customerId={customer?.id} />
          </div>

          {/* Sección de lista de compras */}
          <div className="bg-white/90 rounded-3xl p-8 shadow-xl flex flex-col h-full">
            <div className="flex items-center gap-3 mb-6">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="#681837" className="w-7 h-7">
                <path strokeLinecap="round" strokeLinejoin="round" d="M15.75 10.5V6a3.75 3.75 0 10-7.5 0v4.5m11.356-1.993l1.263 12c.07.665-.45 1.243-1.119 1.243H4.25a1.125 1.125 0 01-1.12-1.243l1.264-12A1.125 1.125 0 015.513 7.5h12.974c.576 0 1.059.435 1.119 1.007zM8.625 10.5a.375.375 0 11-.75 0 .375.375 0 01.75 0zm7.5 0a.375.375 0 11-.75 0 .375.375 0 01.75 0z" />
              </svg>
              <h2 className="text-2xl font-bold text-[#681837] tracking-tight">Lista de Compras</h2>
            </div>
            <ShoppingList customerId={customer?.id} />
          </div>
        </div>

        {/* Sección de productos más buscados */}
        <div className="bg-white/90 rounded-3xl p-8 shadow-xl mb-10">
          <div className="flex items-center gap-3 mb-6">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="#681837" className="w-7 h-7">
              <path strokeLinecap="round" strokeLinejoin="round" d="M15.042 21.672L13.684 16.6m0 0l-2.51 2.225.569-9.47 5.227 7.917-3.286-.672zM12 2.25V4.5m5.834.166l-1.591 1.591M20.25 10.5H18M7.757 14.743l-1.59 1.59M6 10.5H3.75m4.007-4.243l-1.59-1.59" />
            </svg>
            <h2 className="text-2xl font-bold text-[#681837] tracking-tight">Productos Más Buscados</h2>
          </div>
          <MostSearchedProducts />
        </div>

        {/* Sección de supermercados populares */}
        <div className="bg-white/90 rounded-3xl p-8 shadow-xl mb-12">
          <div className="flex items-center gap-3 mb-6">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="#681837" className="w-7 h-7">
              <path strokeLinecap="round" strokeLinejoin="round" d="M13.5 21v-7.5a.75.75 0 01.75-.75h3a.75.75 0 01.75.75V21m-4.5 0H2.36m11.14 0H18m0 0h3.64m-1.39 0V9.349m-16.5 11.65V9.35m0 0a3.001 3.001 0 003.75-.615A2.993 2.993 0 009.75 9.75c.896 0 1.7-.393 2.25-1.016a2.993 2.993 0 002.25 1.016c.896 0 1.7-.393 2.25-1.016a3.001 3.001 0 003.75.614m-16.5 0a3.004 3.004 0 01-.621-4.72L4.318 3.44A1.5 1.5 0 015.378 3h13.243a1.5 1.5 0 011.06.44l1.19 1.189a3 3 0 01-.621 4.72m-13.5 8.65h3.75a.75.75 0 00.75-.75V13.5a.75.75 0 00-.75-.75H6.75a.75.75 0 00-.75.75v3.75c0 .415.336.75.75.75z" />
            </svg>
            <h2 className="text-2xl font-bold text-[#681837] tracking-tight">Supermercados Populares</h2>
          </div>
          <PopularSupermarkets />
        </div>
      </div>
    </div>
  );
};

export default HomePage;