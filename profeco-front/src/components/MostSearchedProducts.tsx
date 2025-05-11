import React, {useState} from "react";
import MostSearchedProductCard from "./SearchedProductCard";
import { mockProducts } from "../data/products";

const MostSearchedProducts: React.FC = () => {
  const [startIndex, setStartIndex] = useState(0);
  const [isFading, setIsFading] = useState(false);

  const handleShowMore = () => {
    setIsFading(true);

    setTimeout(() => {
      const nextIndex = startIndex + 2 < mockProducts.length ? startIndex + 2 : 0;
      setStartIndex(nextIndex);
      setIsFading(false);
    }, 300);
  };

  return (
    <section className="w-full mt-6 mb-8 max-w-4xl mx-auto px-4">
      <h2 className="text-xl font-medium text-[#611232] mb-4 ml-1 select-none">
        Productos más buscados
      </h2>

      <div className="flex items-center gap-3">
        {/* Cuadrícula con fade controlado por estado */}
        <div
          className="grid grid-rows-2 grid-flow-col gap-3 flex-shrink-0 transition-opacity duration-300 ease-in-out"
          style={{ opacity: isFading ? 0 : 1 }}
        >
          {mockProducts.slice(startIndex, startIndex + 2).map((item, i) => (
            <MostSearchedProductCard key={i} {...item} />
          ))}
        </div>

        {/* Botón de la derecha */}
        <button
          onClick={handleShowMore}
          className="w-14 h-14 aspect-square bg-[#611232] rounded-full flex justify-center items-center hover:bg-[#4d0f1f] transition-all duration-300 ease-in-out transform hover:scale-110"
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

export default MostSearchedProducts;