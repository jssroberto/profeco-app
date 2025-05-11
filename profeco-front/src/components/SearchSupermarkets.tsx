import { useLocation } from "react-router-dom";
import SupermarketCard from "./SupermarketCard";
import { stores } from "../data/stores";

const normalize = (str: string) =>
  str.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "");

const SearchSupermarkets = () => {
  const location = useLocation();
  const params = new URLSearchParams(location.search);
  const rawQuery = params.get("search") || "";
  const searchQuery = normalize(rawQuery);

  const filteredStores = rawQuery
    ? stores.filter((store) => normalize(store.name).includes(searchQuery))
    : stores;

  return (
    <section className="w-full mx-auto mt-10 mb-4 px-4 max-w-7xl">
      <h2 className="text-xl font-medium text-[#611232] mb-4 ml-1 select-none">
        {rawQuery
          ? `Supermercados encontrados: ${filteredStores.length}`
          : "Todos los supermercados"}
      </h2>

      {filteredStores.length > 0 ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {filteredStores.map((market) => (
            <SupermarketCard key={market.id} {...market} />
          ))}
        </div>
      ) : (
        <p className="text-gray-500">No se encontraron supermercados.</p>
      )}
    </section>
  );
};

export default SearchSupermarkets;
