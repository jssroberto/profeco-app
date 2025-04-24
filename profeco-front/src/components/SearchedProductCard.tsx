import React from "react";
import { Link } from "react-router-dom";

interface StoreOffer {
  name: string;
  price: string;
  originalPrice?: string;
  oferta?: boolean;
}

interface SearchedProductCardProps {
  imageUrl: string;
  name: string;
  brand: string;
  category: string;
  offers: StoreOffer[];
}

const OfferBadge = () => (
  <div className="bg-[#F36464] text-white text-xs px-2 py-0.5 rounded-[4px] font-medium uppercase tracking-wider inline-block">
    Oferta
  </div>
);

const SearchedProductCard: React.FC<SearchedProductCardProps> = ({
  imageUrl,
  name,
  brand,
  category,
  offers,
}) => {
  return (
    <div className="w-full bg-white rounded-xl shadow-md flex items-stretch p-4 gap-3 border border-gray-200 hover:shadow-lg transition-all duration-300">
      {/* Product image */}
      <div className="flex-shrink-0 w-24 h-24 flex items-center justify-center rounded-lg bg-gray-50 border border-gray-100 overflow-hidden">
        <img
          src={imageUrl}
          alt={name}
          className="object-contain h-full w-full p-1"
        />
      </div>

      {/* Product Info */}
      <div className="flex flex-col justify-center flex-shrink-0 w-[180px]">
        <h3 className="text-lg text-gray-800 font-bold leading-tight mb-0.5">{name}</h3>
        <div className="text-xs text-gray-500 mb-0.5">
          <span className="font-semibold text-gray-700">Marca:</span> {brand}
        </div>
        <div className="text-xs text-gray-500">
          <span className="font-semibold text-gray-700">Categoría:</span> {category}
        </div>
      </div>

      {/* Offers from different supers */}
      <div className="flex flex-1 justify-between items-stretch gap-2">
        {offers.map((offer) => (
          <Link
            key={offer.name}
            to={`/productos/123`} // cambiar aqui por el id
            className="bg-gray-50 rounded-lg flex flex-col justify-center items-start min-w-[100px] flex-1 px-3 py-2 gap-1 border border-gray-200 hover:bg-white transition-colors"
          >
            <div className="text-sm text-gray-600 font-medium mb-0.5">
              {offer.name}
            </div>
            <div className="flex items-center gap-1">
              <span className="text-xl font-bold text-gray-800">{offer.price}</span>
              {offer.originalPrice && (
                <span className="text-xs text-gray-400 line-through">{offer.originalPrice}</span>
              )}
            </div>
            {offer.oferta && (
              <div className="mt-0.5">
                <OfferBadge />
              </div>
            )}
          </Link>
        ))}
      </div>
    </div>
  );
};

export default SearchedProductCard;