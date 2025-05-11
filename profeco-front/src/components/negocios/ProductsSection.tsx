import { Product } from "../../data/products";
import { Link } from "react-router-dom";

interface ProductsSectionProps {
  products: Product[];
  storeId: string;
}

const ProductsSection = ({ products, storeId }: ProductsSectionProps) => {
  return (
    <section className="py-8 bg-gray-50">
      <div className="max-w-7xl mx-auto px-4">
        <h2 className="text-2xl font-bold mb-6 text-[#681837]">Productos</h2>
        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {products.map((product) => {
            const offer = product.offers.find((offer) => offer.store_id === storeId);

            if (!offer) {
              return null;
            }

            return (
              <Link to={`/productos/${product.id}/${storeId}`} key={product.id}>
                <div
                  key={product.id}
                  className="bg-white rounded-lg border border-gray-200 overflow-hidden hover:shadow-lg transition-shadow"
                >
                  <img
                    src={product.imageUrl}
                    alt={product.name}
                    className="w-full h-48 object-cover"
                  />
                  <div className="p-4">
                    <h3 className="font-medium text-gray-900">{product.name}</h3>
                    <p className="text-sm text-gray-500 mb-2">{product.category}</p>

                    <div className="flex items-baseline gap-2">
                      <span className="text-lg font-bold text-[#681837]">
                        {offer.price}
                      </span>
                      {offer.originalPrice && (
                        <span className="text-xs text-gray-400 line-through">
                          {offer.originalPrice}
                        </span>
                      )}
                      {offer.oferta && (
                        <span className="bg-red-200 text-red-600 px-2 py-0.5 text-xs rounded-full">
                          Oferta
                        </span>
                      )}
                    </div>
                  </div>
                </div>
              </Link>
            );
          })}
        </div>
      </div>
    </section>
  );
};

export default ProductsSection;