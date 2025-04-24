import React from "react";
import SearchedProductCard from "./SearchedProductCard";

const items = [
  {
    imageUrl: "https://res.cloudinary.com/walmart-labs/image/upload/w_960,dpr_auto,f_auto,q_auto:best/gr/images/product-images/img_large/00750102051534L.jpg",
    name: "Leche entera 1L",
    brand: "LALA",
    category: "Lácteos",
    offers: [
      {
        name: "Walmart Súper",
        price: "$24.50",
        originalPrice: "$26.90",
        oferta: true,
      },
      {
        name: "Bodega Aurrera",
        price: "$25.50",
      },
      {
        name: "Soriana",
        price: "$27.00",
      },
    ],
  },
  {
    imageUrl: "https://th.bing.com/th/id/OIP.up8KBujuqSLit640tdfyaQHaHa?rs=1&pid=ImgDetMain",
    name: "Queso crema 400g",
    brand: "Philadelphia",
    category: "Lácteos",
    offers: [
      {
        name: "Walmart Súper",
        price: "$35.50",
        originalPrice: "$39.90",
        oferta: true,
      },
      {
        name: "Bodega Aurrera",
        price: "$36.50",
      },
      {
        name: "Soriana",
        price: "$38.00",
      },
    ],
  },
];

const SearchProducts: React.FC = () => (
  <section className="w-full mt-6 mb-8 max-w-4xl mx-auto px-4">
    <h2 className="text-xl font-medium text-[#611232] mb-4 ml-1">Productos encontrados: {items.length}</h2>
    <div className="flex flex-col gap-3">
      {items.map((item, i) => (
        <SearchedProductCard key={i} {...item} />
      ))}
    </div>
  </section>
);

export default SearchProducts;