import React from "react";
import { Link } from "react-router-dom";

interface Supermarket {
  id:string;
  name: string;
  image: string;
  reviews: number;
  rating: number;
  description: string;
}

const renderStars = (rating: number) => {
  return Array(5)
    .fill(0)
    .map((_, i) => (
      <svg
        key={i}
        width={16}
        height={16}
        viewBox="0 0 20 20"
        fill={i < Math.round(rating) ? "#F4B942" : "#E5E7EA"}
        className="inline-block"
      >
        <path d="M10 15.27L16.18 19l-1.63-7.03L20 7.24l-7.19-.61L10 0 7.19 6.63 0 7.24l5.45 4.73L3.82 19z" />
      </svg>
    ));
};

const SupermarketCard: React.FC<Supermarket> = ({
  id,
  name,
  image,
  reviews,
  rating,
  description,
}) => {
  return (
    <div className="flex flex-col bg-white rounded-xl border border-[#E3E5EA] shadow-md hover:shadow-lg transition-shadow duration-200 w-full">
      <div className="relative h-48 overflow-hidden rounded-t-xl bg-gray-100">
        <img
          src={image}
          alt={name}
          className="absolute inset-0 w-full h-full object-cover"
          loading="lazy"
        />
      </div>

      <div className="flex flex-col flex-1 p-6">
        <h3 className="font-bold text-xl text-[#1A1F2C] mb-2 line-clamp-1">
          {name}
        </h3>

        <div className="flex items-center text-base text-gray-600 mb-2 gap-1">
          <span>{renderStars(rating)}</span>
          <span className="ml-2 font-medium text-gray-700">
            {rating.toFixed(1)}
          </span>
          <span className="text-gray-400 ml-1">({reviews} opiniones)</span>
        </div>

        <p className="text-base text-gray-500 mb-6 line-clamp-2">
          {description}
        </p>

        <Link to={`/negocios/${id}`} className="mt-auto">
          <button
            className="border border-[#aaadb0] text-[#681837] font-medium py-3 rounded-lg transition-colors ease-in-out duration-500 hover:bg-[#681837] hover:text-white w-full cursor-pointer"
            style={{ boxShadow: "0 1px 2px 0 rgba(30,30,50,0.04)" }}
          >
            Ir al supermercado
          </button>
        </Link>
      </div>
    </div>
  );
};

export default SupermarketCard;
