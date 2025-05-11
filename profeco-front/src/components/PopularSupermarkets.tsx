import { useState } from "react";
import SupermarketCard from "./SupermarketCard";
import { stores } from "../data/stores";

const PopularSupermarkets = () => {
  const [startIndex, setStartIndex] = useState(0);
  const [isFading, setIsFading] = useState(false);

  const handleShowMore = () => {
    setIsFading(true);
    setTimeout(() => {
      const nextIndex = startIndex + 3 < stores.length ? startIndex + 3 : 0;
      setStartIndex(nextIndex);
      setIsFading(false);
    }, 300);
  };

  return (
    <section className="w-full mx-auto mt-10 mb-4 px-4 max-w-7xl">
      <h2 className="text-xl font-medium text-[#611232] mb-4 ml-1 select-none">
        Supermercados populares
      </h2>

      <div className="flex items-center gap-3">
        <div
          className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 transition-opacity duration-300 ease-in-out"
          style={{ opacity: isFading ? 0 : 1 }}
        >
          {stores.slice(startIndex, startIndex + 3).map((market) => (
            <SupermarketCard key={market.id} {...market} />
          ))}
        </div>

        <button
          onClick={handleShowMore}
          className="w-14 h-14 bg-[#611232] rounded-full flex justify-center items-center hover:bg-[#4d0f1f] transition-all duration-300 ease-in-out transform hover:scale-110"
        >
          <svg
            className="w-7 h-7"
            viewBox="0 0 20 20"
            fill="white"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M7 5l5 5-5 5"
              stroke="white"
              strokeWidth="2"
              fill="none"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        </button>
      </div>
    </section>
  );
};

export default PopularSupermarkets;