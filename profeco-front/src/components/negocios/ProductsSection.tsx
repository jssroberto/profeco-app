import { Link } from "react-router-dom";

interface Product {
  id: string;
  name: string;
  image: string;
  price: number;
  originalPrice?: number;
  category: string;
}

interface ProductsSectionProps {
  products: Product[];
}

const ProductsSection = ({ products }: ProductsSectionProps) => {
  return (
    <section className="py-8 bg-gray-50">
      <div className="max-w-7xl mx-auto px-4">
        <h2 className="text-2xl font-bold mb-6 text-[#681837]">Productos</h2>
        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {products.map((product) => (
            <Link
              key={product.id}
              to={`/productos/${product?.storeProductId}`}
              className="bg-white rounded-lg border border-gray-200 overflow-hidden hover:shadow-lg transition-shadow"
            >
              <img
                src={product.image}
                alt={product.name}
                className="w-full h-48 object-cover"
              />
              <div className="p-4">
                <h3 className="font-medium text-gray-900">{product.name}</h3>
                <p className="text-sm text-gray-500 mb-2">{product.category}</p>
                <div className="flex items-baseline gap-2">
                  <span className="text-lg font-bold text-[#681837]">
                    ${product.price.toFixed(2)}
                  </span>
                  {product.originalPrice && (
                    <span className="text-sm text-gray-400 line-through">
                      ${product.originalPrice.toFixed(2)}
                    </span>
                  )}
                </div>
              </div>
            </Link>
          ))}
        </div>
      </div>
    </section>
  );
};

export default ProductsSection;