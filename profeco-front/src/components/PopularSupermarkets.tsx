
import SupermarketCard from "./SupermarketCard";

interface Supermarket {
  name: string;
  image: string;
  reviews: number;
  rating: number;
  description: string;
}

const supermarkets: Supermarket[] = [
  {
    name: "Walmart",
    image: "https://doral.guide/wp-content/uploads/2020/06/Walmart-Pharmacy.jpg",
    reviews: 120,
    rating: 3.0,
    description: "Gran variedad de productos y promociones exclusivas.",
  },
  {
    name: "Soriana",
    image: "https://doral.guide/wp-content/uploads/2020/06/Walmart-Pharmacy.jpg",
    reviews: 120,
    rating: 3.0,
    description: "Gran variedad de productos y promociones exclusivas.",
  },
  {
    name: "Bodega Aurrera",
    image: "https://doral.guide/wp-content/uploads/2020/06/Walmart-Pharmacy.jpg",
    reviews: 120,
    rating: 3.0,
    description: "Gran variedad de productos y promociones exclusivas.",
  },

];

const PopularSupermarkets = () => (
  <section className="w-full mx-auto mt-10 mb-4 px-4 max-w-7xl ">
    <h2 className="text-xl font-medium text-[#611232] mb-4 ml-1">
      Supermercati popolari
    </h2>
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
      {supermarkets.map((market) => (
        <SupermarketCard key={market.name} {...market} />
      ))}
    </div>
  </section>
);

export default PopularSupermarkets;