import React, { useEffect, useState } from "react";
import axios from "../api/axiosConfig";

interface ShoppingListProps {
  customerId?: string;
}

interface ShoppingItem {
  id: string;
  name: string;
  imageUrl?: string;
  brandName?: string;
  categoryName?: string;
  offers?: ProductOffer[];
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

const ShoppingList: React.FC<ShoppingListProps> = ({ customerId }) => {
  const [items, setItems] = useState<ShoppingItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const itemsPerPage = 4;

  const handleRemoveFromList = async (productId: string) => {
    try {
      await axios.delete(`/preferences/shopping-list/${productId}`);
      setItems(prev => prev.filter(item => item.id !== productId));
    } catch (error) {
      console.error("Error al eliminar producto de la lista:", error);
    }
  };

  useEffect(() => {
    const fetchShoppingList = async () => {
      setLoading(true);
      try {
        const { data } = await axios.get(`/preferences/shopping-list`);
        const ids: string[] = Array.from(data.shoppingListProductIds || []);
        const today = new Date().toISOString().split("T")[0];
        const products = await Promise.all(
          ids.map(async (id) => {
            try {
              const res = await axios.get(`/products/${id}`);
              const product = res.data;
              let imageUrl = product.imageUrl;
              if (imageUrl && !imageUrl.startsWith('http')) {
                imageUrl = `http://${imageUrl}`;
              }
              let offers: ProductOffer[] = [];
              try {
                const offersRes = await axios.get(`/store-product-offers/current-for-product/${id}?currentDate=${today}`);
                offers = offersRes.data;
              } catch (error) {
                console.error("Error fetching offers:", error);
              }
              return {
                id: product.id,
                name: product.name,
                imageUrl,
                brandName: product.brandName || 'N/A',
                categoryName: product.categoryName || 'N/A',
                offers,
              };
            } catch {
              return null;
            }
          })
        );
        setItems(products.filter((item): item is Required<ShoppingItem> => Boolean(item)));
      } catch {
        setItems([]);
      } finally {
        setLoading(false);
      }
    };
    fetchShoppingList();
  }, [customerId]);

  if (!customerId) return <div className="p-4">No hay cliente.</div>;
  if (loading) return <div className="p-4">Cargando lista de compras...</div>;

  const totalPages = Math.ceil(items.length / itemsPerPage);
  const startIndex = currentPage * itemsPerPage;
  const visibleItems = items.slice(startIndex, startIndex + itemsPerPage);

  return (
    <section className="w-full mx-auto mt-10 mb-4 px-4 max-w-7xl">
      <h2 className="text-xl font-medium text-[#611232] mb-6 ml-1">
        Lista de compras
      </h2>
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 max-w-full">
        {items.length === 0 ? (
          <div className="bg-white rounded-xl shadow-md border border-gray-200 p-6 text-center text-gray-500 lg:col-span-2">
            No tienes productos en tu lista de compras.
          </div>
        ) : (
          visibleItems.map(item => (
            <div
              key={item.id}
              className="bg-white rounded-xl shadow-md border border-gray-200 flex flex-col p-4 w-full relative"
              style={{ minHeight: '220px' }}
            >
              <div className="flex flex-row items-start gap-4 flex-1">
                {/* Producto: Imagen y detalles */}
                <div className="flex flex-col items-center w-[40%] flex-shrink-0 py-2">
                  {/* Imagen */}
                  <div className="w-36 h-36 mb-4">
                    <img
                      src={item.imageUrl}
                      alt={item.name}
                      className="w-full h-full object-contain rounded-lg bg-gray-50 border border-gray-200 p-2"
                    />
                  </div>
                  {/* Detalles del producto */}
                  <div className="flex flex-col items-center text-center w-full">
                    <h3 className="text-base text-gray-800 font-bold leading-tight mb-2 line-clamp-2 w-full">
                      {item.name}
                    </h3>
                    <div className="space-y-1 w-full">
                      <p className="text-sm text-gray-600">
                        <span className="font-semibold">Marca:</span>{' '}
                        <span className="text-gray-700">{item.brandName}</span>
                      </p>
                      <p className="text-sm text-gray-600">
                        <span className="font-semibold">Categoría:</span>{' '}
                        <span className="text-gray-700">{item.categoryName}</span>
                      </p>
                    </div>
                  </div>
                </div>

                {/* Ofertas */}
                <div className="flex-1 border-l border-gray-200 pl-8">
                  <h4 className="text-sm font-semibold text-[#681837] mb-3">
                    Ofertas actuales:
                  </h4>
                  {!item.offers || item.offers.length === 0 ? (
                    <div className="h-20 flex items-center justify-center">
                      <span className="text-xs text-gray-400 block">Sin ofertas para hoy</span>
                    </div>
                  ) : (
                    <div className="space-y-3">
                      {item.offers.slice(0, 2).map((offer, idx) => (
                        <div key={idx} className="bg-gray-50 rounded-lg border border-gray-200 p-4">
                          <span className="text-lg font-bold text-[#681837] mb-1 block">
                            ${offer.offerPrice.toFixed(2)}
                          </span>
                          <span className="text-xs text-gray-500 block">
                            Válido: {new Date(offer.offerStartDate).toLocaleDateString()} a {new Date(offer.offerEndDate).toLocaleDateString()}
                          </span>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              </div>

              {/* Botón de eliminar */}
              <div className="flex justify-end mt-4 pt-2 border-t border-gray-100">
                <button
                  onClick={() => handleRemoveFromList(item.id)}
                  className="p-2 text-gray-400 hover:text-red-500 rounded-full hover:bg-gray-100 transition-colors"
                  title="Eliminar de la lista"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-5 h-5">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0" />
                  </svg>
                </button>
              </div>
            </div>
          ))
        )}
      </div>

      {/* Paginación */}
      {totalPages > 1 && (
        <div className="flex justify-center items-center gap-3 mt-6">
          <button
            onClick={() => setCurrentPage(prev => Math.max(0, prev - 1))}
            disabled={currentPage === 0}
            className="bg-gray-100 hover:bg-gray-200 rounded-full p-2 focus:outline-none disabled:opacity-50"
            aria-label="Página anterior"
          >
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="#681837" className="w-5 h-5">
              <path strokeLinecap="round" strokeLinejoin="round" d="M15.75 19.5L8.25 12l7.5-7.5" />
            </svg>
          </button>
          
          <span className="text-sm text-gray-600">
            Página {currentPage + 1} de {totalPages}
          </span>

          <button
            onClick={() => setCurrentPage(prev => Math.min(totalPages - 1, prev + 1))}
            disabled={currentPage === totalPages - 1}
            className="bg-gray-100 hover:bg-gray-200 rounded-full p-2 focus:outline-none disabled:opacity-50"
            aria-label="Página siguiente"
          >
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="#681837" className="w-5 h-5">
              <path strokeLinecap="round" strokeLinejoin="round" d="M8.25 4.5l7.5 7.5-7.5 7.5" />
            </svg>
          </button>
        </div>
      )}
    </section>
  );
};

export default ShoppingList;
