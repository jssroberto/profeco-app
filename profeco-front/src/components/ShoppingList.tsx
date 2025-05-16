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
  const itemsPerPage = 3;
  // Estado para la página de ofertas de cada producto
  const [offerPages, setOfferPages] = useState<{ [productId: string]: number }>({});

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

  const totalPages = Math.ceil(items.length / itemsPerPage);
  const startIndex = currentPage * itemsPerPage;
  const visibleItems = items.slice(startIndex, startIndex + itemsPerPage);

  if (!customerId) return <div>No hay cliente.</div>;
  if (loading) return <div className="p-4">Cargando lista de compras...</div>;

  return (
    <section className="w-full -mt-2">
      <div className="space-y-4">
        {items.length === 0 ? (
          <div className="bg-gray-50 rounded-xl p-8 text-center text-gray-500">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-12 h-12 mx-auto mb-4 text-gray-400">
              <path strokeLinecap="round" strokeLinejoin="round" d="M15.75 10.5V6a3.75 3.75 0 10-7.5 0v4.5m11.356-1.993l1.263 12c.07.665-.45 1.243-1.119 1.243H4.25a1.125 1.125 0 01-1.12-1.243l1.264-12A1.125 1.125 0 015.513 7.5h12.974c.576 0 1.059.435 1.119 1.007zM8.625 10.5a.375.375 0 11-.75 0 .375.375 0 01.75 0zm7.5 0a.375.375 0 11-.75 0 .375.375 0 01.75 0z" />
            </svg>
            <p className="text-lg">No tienes productos en tu lista de compras.</p>
          </div>
        ) : (
          visibleItems.map(item => {
            const offersPerPage = 1;
            // Ordenar ofertas de menor a mayor precio de oferta
            const sortedOffers = (item.offers || []).slice().sort((a, b) => a.offerPrice - b.offerPrice);
            const offerPage = offerPages[item.id] || 0;
            const totalOfferPages = Math.ceil(sortedOffers.length / offersPerPage);
            const offerStart = offerPage * offersPerPage;
            const paginatedOffers = sortedOffers.slice(offerStart, offerStart + offersPerPage);
            return (
              <div
                key={item.id}
                className="bg-gray-50 rounded-xl border border-gray-200 p-4 relative group hover:shadow-lg transition-shadow duration-200"
              >
                <div className="flex flex-col gap-4">
                  {/* Producto: Imagen y detalles */}
                  <div className="flex gap-4 items-center">
                    {/* Imagen */}
                    <div className="w-20 h-20 flex-shrink-0">
                      <img
                        src={item.imageUrl}
                        alt={item.name}
                        className="w-full h-full object-contain rounded-lg bg-white border border-gray-200 p-2"
                      />
                    </div>
                    {/* Detalles del producto */}
                    <div className="flex-1 min-w-0">
                      <h3 className="text-lg font-bold text-gray-800 mb-1 truncate">
                        {item.name}
                      </h3>
                      <div className="text-sm text-gray-600">
                        <span className="font-medium">{item.brandName}</span>
                        <span className="mx-2">•</span>
                        <span className="text-gray-500">{item.categoryName}</span>
                      </div>
                    </div>
                  </div>

                  {/* Ofertas */}
                  {(!item.offers || item.offers.length === 0) ? (
                    <div className="text-sm text-gray-400 text-center py-2 bg-white rounded-lg border border-gray-200">
                      Sin ofertas disponibles
                    </div>
                  ) : (
                    <div className="grid gap-2">
                      {paginatedOffers.map((offer, idx) => (
                        <div
                          key={idx}
                          className="bg-white rounded-lg border border-gray-200 p-3 hover:border-[#681837]/20 transition-all duration-200"
                        >
                          <div className="flex items-center justify-between gap-2">
                            <span className="text-xl font-bold text-[#681837]">
                              ${offer.offerPrice.toFixed(2)}
                            </span>
                            <span className="bg-[#681837]/10 text-[#681837] text-xs font-medium px-2 py-1 rounded-full">
                              OFERTA
                            </span>
                          </div>
                          <div className="text-xs text-gray-500 mt-1">
                            Válido: {new Date(offer.offerStartDate).toLocaleDateString()} - {new Date(offer.offerEndDate).toLocaleDateString()}
                          </div>
                        </div>
                      ))}
                      {totalOfferPages > 1 && (
                        <>
                          <div className="flex justify-center items-center gap-2 mt-2">
                            <button
                              onClick={() => setOfferPages(p => ({ ...p, [item.id]: Math.max(0, offerPage - 1) }))}
                              disabled={offerPage === 0}
                              className="w-8 h-8 flex items-center justify-center rounded-full border border-[#681837] text-[#681837] disabled:opacity-50 disabled:cursor-not-allowed hover:bg-[#681837] hover:text-white transition-colors text-base font-bold"
                            >&lt;</button>
                            <button
                              onClick={() => setOfferPages(p => ({ ...p, [item.id]: Math.min(totalOfferPages - 1, offerPage + 1) }))}
                              disabled={offerPage === totalOfferPages - 1}
                              className="w-8 h-8 flex items-center justify-center rounded-full border border-[#681837] text-[#681837] disabled:opacity-50 disabled:cursor-not-allowed hover:bg-[#681837] hover:text-white transition-colors text-base font-bold"
                            >&gt;</button>
                          </div>
                          <div className="flex justify-center mt-1">
                            <span className="text-xs text-[#681837] font-medium">{offerPage + 1} / {totalOfferPages}</span>
                          </div>
                        </>
                      )}
                    </div>
                  )}
                </div>

                {/* Botón de eliminar */}
                <button
                  onClick={() => handleRemoveFromList(item.id)}
                  className="absolute top-3 right-3 p-1.5 text-gray-400 hover:text-red-500 rounded-full hover:bg-white transition-colors"
                  title="Eliminar de la lista"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-4 h-4">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0" />
                  </svg>
                </button>
              </div>
            );
          })
        )}
      </div>

      {totalPages > 1 && (
        <div className="flex justify-center items-center gap-4 mt-8">
          <button
            onClick={() => setCurrentPage(prev => prev > 0 ? prev - 1 : prev)}
            disabled={currentPage === 0}
            className="px-4 py-2 rounded-lg border border-[#681837] text-[#681837] disabled:opacity-50 disabled:cursor-not-allowed hover:bg-[#681837] hover:text-white transition-colors"
          >
            Anterior
          </button>
          <span className="text-[#681837] font-medium">
            Página {currentPage + 1} de {totalPages}
          </span>
          <button
            onClick={() => setCurrentPage(prev => prev < totalPages - 1 ? prev + 1 : prev)}
            disabled={currentPage === totalPages - 1}
            className="px-4 py-2 rounded-lg border border-[#681837] text-[#681837] disabled:opacity-50 disabled:cursor-not-allowed hover:bg-[#681837] hover:text-white transition-colors"
          >
            Siguiente
          </button>
        </div>
      )}
    </section>
  );
};

export default ShoppingList;
