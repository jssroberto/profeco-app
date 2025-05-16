import React, { useEffect, useState } from "react";
import axios from "../api/axiosConfig";

interface Product {
  id: string;
  name: string;
  imageUrl: string;
  brandName: string;
  categoryName: string;
  offers: ProductOffer[];
}

interface ProductOffer {
  offerPrice: number;
  offerStartDate: string;
  offerEndDate: string;
  storeProduct: {
    id: string;
    price: number;
    storeId: string;
    productId: string;
  };
}

interface Store {
  id: string;
  name: string;
  imageUrl?: string;
  location?: string;
}

interface RawProduct {
  id: string;
  name: string;
  imageUrl: string;
  brandName: string;
  categoryName: string;
}

const MostSearchedProducts: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [offerIndexes, setOfferIndexes] = useState<{ [productId: string]: number }>({});
  const [currentPage, setCurrentPage] = useState(0);
  const productsPerPage = 4; // Cambiado de 5 a 4 productos por página

  useEffect(() => {
    const fetchProducts = async () => {
      setLoading(true);
      try {
        const { data } = await axios.get<RawProduct[]>("/products");
        const today = new Date().toISOString().split("T")[0];
        const productsWithOffers = await Promise.all(
          data.map(async (product: RawProduct) => {
            let imageUrl = product.imageUrl;
            if (imageUrl && !imageUrl.startsWith("http")) {
              imageUrl = `http://${imageUrl}`;
            }
            let offers: ProductOffer[] = [];
            try {
              const offersRes = await axios.get(
                `/store-product-offers/current-for-product/${product.id}?currentDate=${today}`
              );
              offers = offersRes.data;
            } catch (error) {
              console.error("Error fetching offers:", error);
            }
            return {
              id: product.id,
              name: product.name,
              imageUrl,
              brandName: product.brandName,
              categoryName: product.categoryName,
              offers,
            };
          })
        );
        setProducts(productsWithOffers);
      } catch (error) {
        console.error("Error fetching products:", error);
        setProducts([]);
      } finally {
        setLoading(false);
      }
    };
    fetchProducts();
  }, []);

  if (loading)
    return <div className="p-4">Cargando productos más buscados...</div>;

  const totalPages = Math.ceil(products.length / productsPerPage);
  const startIndex = currentPage * productsPerPage;
  const visibleProducts = products.slice(startIndex, startIndex + productsPerPage);

  // Calcular el número de productos visibles y los espacios vacíos para mantener el tamaño
  const emptySlots = productsPerPage - visibleProducts.length;

  return (
    <section className="w-full mx-auto mt-10 mb-4 px-4 max-w-7xl">
      <h2 className="text-xl font-medium text-[#611232] mb-6 ml-1">
        Productos más buscados
      </h2>
      <div className="flex flex-col gap-6 min-h-[900px]">
        {visibleProducts.map((product) => {
          return (
            <div
              key={product.id}
              className="bg-white rounded-xl shadow-md border border-gray-200 p-6 w-full"
            >
              <div className="flex flex-row gap-8 items-start w-full">
                {/* Producto: Imagen y detalles */}
                <div className="flex flex-row items-start gap-6 w-[35%] flex-shrink-0">
                  {/* Imagen */}
                  <div className="w-32 h-32 flex-shrink-0">
                    <img
                      src={product.imageUrl}
                      alt={product.name}
                      className="w-full h-full object-contain rounded-lg bg-gray-50 border border-gray-200 p-3"
                    />
                  </div>

                  {/* Detalles del producto */}
                  <div className="flex flex-col flex-1 min-w-0">
                    <h3 className="text-lg text-gray-800 font-bold leading-tight mb-3 line-clamp-2">
                      {product.name}
                    </h3>
                    <div className="space-y-2">
                      <p className="text-sm text-gray-600">
                        <span className="font-semibold">Marca:</span>{' '}
                        <span className="text-gray-700">{product.brandName}</span>
                      </p>
                      <p className="text-sm text-gray-600">
                        <span className="font-semibold">Categoría:</span>{' '}
                        <span className="text-gray-700">{product.categoryName}</span>
                      </p>
                    </div>
                  </div>
                </div>

                {/* Ofertas */}
                <div className="flex-1 border-l border-gray-200 pl-8">
                  <h4 className="text-sm font-semibold text-[#681837] mb-4">
                    Ofertas actuales:
                  </h4>
                  {product.offers.length === 0 ? (
                    <div className="flex items-center justify-center h-[120px]">
                      <span className="text-sm text-gray-400">Sin ofertas para hoy</span>
                    </div>
                  ) : (
                    <>
                      {/* Paginación de ofertas: abajo, centrada, de 2 en 2 */}
                      <div className={`grid ${product.offers.length === 1 ? 'grid-cols-1' : 'grid-cols-2'} gap-6 flex-1`}>
                        {product.offers
                          .slice(offerIndexes[product.id] || 0, (offerIndexes[product.id] || 0) + 2)
                          .map((offer, idx) => (
                            <div key={idx} className="min-w-0 w-full">
                              <OfferWithStore offer={offer} />
                            </div>
                          ))}
                      </div>
                      {product.offers.length > 2 && (
                        <div className="flex justify-center items-center gap-3 mt-4">
                          <button
                            onClick={() => {
                              setOfferIndexes(prev => {
                                const current = prev[product.id] || 0;
                                if (current === 0) return prev;
                                return { ...prev, [product.id]: current - 2 };
                              });
                            }}
                            disabled={(offerIndexes[product.id] || 0) === 0}
                            className="bg-gray-100 hover:bg-gray-200 rounded-full px-4 py-2 text-[#681837] font-semibold focus:outline-none disabled:opacity-50"
                            aria-label="Anterior"
                          >
                            Anterior
                          </button>
                          <span className="text-sm text-gray-700 select-none font-medium">
                            Página {Math.floor((offerIndexes[product.id] || 0) / 2) + 1} de {Math.ceil(product.offers.length / 2)}
                          </span>
                          <button
                            onClick={() => {
                              setOfferIndexes(prev => {
                                const current = prev[product.id] || 0;
                                if (current + 2 >= product.offers.length) return prev;
                                return { ...prev, [product.id]: current + 2 };
                              });
                            }}
                            disabled={(offerIndexes[product.id] || 0) + 2 >= product.offers.length}
                            className="bg-gray-100 hover:bg-gray-200 rounded-full px-4 py-2 text-[#681837] font-semibold focus:outline-none disabled:opacity-50"
                            aria-label="Siguiente"
                          >
                            Siguiente
                          </button>
                        </div>
                      )}
                    </>
                  )}
                </div>
              </div>
            </div>
          );
        })}
        {/* Rellenar con espacios vacíos para mantener el tamaño del contenedor al cambiar de página */}
        {emptySlots > 0 && Array.from({ length: emptySlots }).map((_, idx) => (
          <div key={idx} className="bg-transparent rounded-xl p-6 w-full min-h-[180px]" aria-hidden="true"></div>
        ))}
      </div>

      {/* Paginación centrada siempre visible */}
      <div className="flex justify-center items-center gap-2 mt-8" id="paginacion-mas-buscados">
        <button
          onClick={() => {
            setCurrentPage(prev => Math.max(0, prev - 1));
            document.getElementById('paginacion-mas-buscados')?.scrollIntoView({ behavior: 'smooth', block: 'center' });
          }}
          disabled={currentPage === 0}
          className="bg-gray-100 hover:bg-gray-200 rounded-full p-2 focus:outline-none disabled:opacity-50"
          aria-label="Página anterior"
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="#681837" className="w-5 h-5">
            <path strokeLinecap="round" strokeLinejoin="round" d="M15.75 19.5L8.25 12l7.5-7.5" />
          </svg>
        </button>
        <span className="text-sm text-gray-600 select-none">
          Página {currentPage + 1} de {totalPages}
        </span>
        <button
          onClick={() => {
            setCurrentPage(prev => Math.min(totalPages - 1, prev + 1));
            document.getElementById('paginacion-mas-buscados')?.scrollIntoView({ behavior: 'smooth', block: 'center' });
          }}
          disabled={currentPage === totalPages - 1}
          className="bg-gray-100 hover:bg-gray-200 rounded-full p-2 focus:outline-none disabled:opacity-50"
          aria-label="Página siguiente"
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="#681837" className="w-5 h-5">
            <path strokeLinecap="round" strokeLinejoin="round" d="M8.25 4.5l7.5 7.5-7.5 7.5" />
          </svg>
        </button>
      </div>
    </section>
  );
};

const OfferWithStore: React.FC<{ offer: ProductOffer }> = ({ offer }) => {
  const [store, setStore] = React.useState<Store | null>(null);
  React.useEffect(() => {
    const fetchStore = async () => {
      try {
        const res = await axios.get(`/stores/${offer.storeProduct.storeId}`);
        setStore({
          ...res.data,
          imageUrl:
            res.data.imageUrl && !res.data.imageUrl.startsWith("http")
              ? `http://${res.data.imageUrl}`
              : res.data.imageUrl,
        });
      } catch {
        setStore(null);
      }
    };
    fetchStore();
  }, [offer.storeProduct.storeId]);

  return (
    <div className="flex flex-col items-center w-full bg-gray-50 rounded-lg border border-gray-200 p-4 h-full">
      <span className="text-lg font-bold text-[#681837] mb-1">
        ${offer.offerPrice.toFixed(2)}
      </span>
      <span className="text-xs text-gray-500 mb-2 text-center">
        Válido: {new Date(offer.offerStartDate).toLocaleDateString()} a {new Date(offer.offerEndDate).toLocaleDateString()}
      </span>
      <hr className="w-full border-t border-gray-300 my-1" />
      {store ? (
        <div className="flex flex-row items-center gap-3 mt-2 w-full">
          <img
            src={store.imageUrl}
            alt={store.name}
            className="w-12 h-12 object-contain rounded flex-shrink-0"
          />
          <div className="flex flex-col min-w-0">
            <span className="text-sm font-semibold text-gray-800 truncate">
              {store.name}
            </span>
            <span className="text-xs text-gray-500 truncate">
              {store.location}
            </span>
          </div>
        </div>
      ) : (
        <span className="text-xs text-gray-400 py-4">Cargando supermercado...</span>
      )}
    </div>
  );
};

export default MostSearchedProducts;