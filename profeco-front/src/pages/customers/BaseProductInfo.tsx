import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import axios from "axios";
import Card from "../../components/ui/Card";
import Button from "../../components/ui/Button";
import { useAuth } from "../../context/AuthContext";

// Tipos para producto y store-product
interface Product {
  id: string;
  name: string;
  imageUrl: string;
  brandName: string;
  categoryName: string;
}

interface StoreProductWithOffer {
  id: string; // This is the store-product ID
  storeId: string;
  storeName: string;
  storeProductId: string; // This is effectively sp.id from the raw data
  price: number;
  offer?: {
    offerPrice: number;
    offerStartDate: string;
    offerEndDate: string;
  } | null;
}

interface StoreProduct { // Raw data from API for store-products by product name
  id: string; // store-product ID
  storeId: string;
  price: number;
}

interface StoreProductOffer {
  offerPrice: number;
  offerStartDate: string;
  offerEndDate: string;
  inconsistency: null | string;
  storeProduct: {
    id: string; // store-product ID
    price: number;
    storeId: string;
    productId: string; // base product ID
    storeName?: string;
  };
}

const BaseProductInfo = () => {
  const { id } = useParams<{ id: string }>(); // id is base product ID
  const { token } = useAuth();
  const [product, setProduct] = useState<Product | null>(null);
  const [storeProducts, setStoreProducts] = useState<StoreProductWithOffer[]>([]);
  const [currentOffers, setCurrentOffers] = useState<StoreProductOffer[]>([]);
  const [loading, setLoading] = useState(true);

  // State for "Add to Shopping List" functionality
  const [adding, setAdding] = useState(false);
  const [addSuccess, setAddSuccess] = useState(false);
  const [addError, setAddError] = useState<string | null>(null);

  useEffect(() => {
    const fetchProductAndStores = async () => {
      if (!id || !token) {
        setLoading(false);
        return;
      }
      try {
        setLoading(true);
        // Get base product info
        const productRes = await axios.get(
          `http://localhost:8080/api/v1/products/${id}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        setProduct(productRes.data);

        // Get all store-products for this product name
        // productRes.data must be available here
        if (!productRes.data || !productRes.data.name) {
            throw new Error("Product data or name is missing after fetch.");
        }
        const storeProductsRes = await axios.get(
          `http://localhost:8080/api/v1/store-products/by-product-name?name=${encodeURIComponent(productRes.data.name)}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );

        const today = new Date().toISOString().split("T")[0];
        const storeProductsRaw: StoreProduct[] = storeProductsRes.data;
        const storeProductsWithStoreAndOffer: StoreProductWithOffer[] = await Promise.all(
          storeProductsRaw.map(async (sp) => {
            const storeRes = await axios.get(
              `http://localhost:8080/api/v1/stores/${sp.storeId}`,
              { headers: { Authorization: `Bearer ${token}` } }
            );
            
            let offerData: StoreProductWithOffer["offer"] = null;
            try {
              const offerRes = await axios.get(
                `http://localhost:8080/api/v1/store-product-offers/current-for-store-product/${sp.id}?currentDate=${today}`,
                { headers: { Authorization: `Bearer ${token}` } }
              );
              // API might return a single object or an array with one object
              const currentOffer = Array.isArray(offerRes.data) ? offerRes.data[0] : offerRes.data;
              if (currentOffer && typeof currentOffer.offerPrice === 'number') {
                offerData = {
                  offerPrice: currentOffer.offerPrice,
                  offerStartDate: currentOffer.offerStartDate,
                  offerEndDate: currentOffer.offerEndDate,
                };
              }
            } catch {
              // No offer or error fetching offer
            }

            return {
              id: sp.id, // store-product ID
              storeId: sp.storeId,
              storeName: storeRes.data.name,
              storeProductId: sp.id, // store-product ID, same as id here
              price: sp.price,
              offer: offerData,
            };
          })
        );
        setStoreProducts(storeProductsWithStoreAndOffer);

        // Obtener ofertas actuales para este producto (base product ID) y agregar el nombre de la tienda
        const offersResponse = await axios.get(
          `http://localhost:8080/api/v1/store-product-offers/current-for-product/${id}?currentDate=${today}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );

        const offersWithStoreName = await Promise.all(
          (offersResponse.data as StoreProductOffer[]).map(async (offer: StoreProductOffer) => {
            // Ensure storeProduct and storeId exist before fetching
            if (!offer.storeProduct || !offer.storeProduct.storeId) {
              return { ...offer, storeProduct: { ...offer.storeProduct, storeName: "Unknown Store" } };
            }
            try {
                const storeRes = await axios.get(
                `http://localhost:8080/api/v1/stores/${offer.storeProduct.storeId}`,
                { headers: { Authorization: `Bearer ${token}` } }
                );
                return {
                ...offer,
                storeProduct: {
                    ...offer.storeProduct,
                    storeName: storeRes.data.name
                }
                };
            } catch (storeError) {
                console.error(`Failed to fetch store name for store ID ${offer.storeProduct.storeId}:`, storeError);
                return { ...offer, storeProduct: { ...offer.storeProduct, storeName: "Error Fetching Store Name" } };
            }
          })
        );
        
        setCurrentOffers(offersWithStoreName);
      } catch (error) {
        console.error("Error fetching data:", error);
        setProduct(null); // Clear product on error
        setStoreProducts([]);
        setCurrentOffers([]);
      } finally {
        setLoading(false);
      }
    };

    fetchProductAndStores();
  }, [id, token]);

  const handleAddToShoppingList = async () => {
    if (!product?.id) return; // Use base product ID
    setAdding(true);
    setAddError(null);
    setAddSuccess(false);
    try {
      await axios.post(
        `http://localhost:8080/api/v1/preferences/shopping-list/${product.id}`,
        {}, // Empty body for POST as per diff
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setAddSuccess(true);
      setTimeout(() => setAddSuccess(false), 3000); // Show success for 3 seconds
    } catch (e) {
      console.error("Error adding to shopping list:", e);
      setAddError("No se pudo agregar el producto a la lista de compras.");
    } finally {
      setAdding(false);
    }
  };

  if (loading) return <div className="p-8">Cargando...</div>;
  if (!product) return <div className="p-8">Producto no encontrado. Es posible que el ID no sea válido o haya ocurrido un error.</div>;

  return (
    <div className="max-w-4xl mx-auto p-4 mt-24">
      <div className="flex flex-col md:flex-row gap-8">
        <img
          src={
            product.imageUrl?.startsWith("http")
              ? product.imageUrl
              : `http://${product.imageUrl}`
          }
          alt={product.name}
          className="w-full md:w-80 h-80 object-contain bg-white rounded-lg border"
        />
        <div className="flex-1 space-y-4">
          <h1 className="text-3xl font-bold">{product.name}</h1>
          <div className="text-gray-600">
            <span className="mr-4">Categoría: {product.categoryName}</span>
            <span>Marca: {product.brandName}</span>
          </div>
          {/* Add to Shopping List Button and Feedback */}
          <div className="mt-4">
            <Button onClick={handleAddToShoppingList} disabled={adding || addSuccess} className="w-full md:w-auto">
              {adding ? "Agregando..." : addSuccess ? "¡Agregado!" : "Agregar a Shopping List"}
            </Button>
            {addError && (
              <div className="text-red-600 text-sm font-semibold mt-2">
                {addError}
              </div>
            )}
          </div>

          <div className="mt-8">
            <h2 className="text-xl font-semibold text-[#9C2759] mb-4">
              Disponible en supermercados
            </h2>
            <div className="grid gap-4">
              {storeProducts.length === 0 && (
                <div>No disponible en ningún supermercado actualmente.</div>
              )}
              {storeProducts.map((sp) => (
                <Card key={sp.storeProductId} className="p-4 flex justify-between items-center">
                  <div>
                    <div className="font-medium">{sp.storeName}</div>
                    <div className="text-sm text-gray-500">ID tienda: {sp.storeId}</div>
                  </div>
                  <div className="flex items-center gap-4">
                    {/* Displaying offer or regular price */}
                     {sp.offer ? (
                      <div className="flex flex-col items-end text-right">
                        <span className="font-bold text-lg text-green-700">
                          ${sp.offer.offerPrice.toFixed(2)}
                        </span>
                        <span className="text-xs text-gray-400 line-through">
                          ${sp.price.toFixed(2)}
                        </span>
                      </div>
                    ) : (
                      <span className="font-bold text-lg">${sp.price.toFixed(2)}</span>
                    )}
                    {/* Link to view product in store or offer details */}
                    <Link to={`/negocios/${sp.storeId}/productos/${sp.storeProductId}`}>
                      <Button variant="outline">
                        {sp.offer ? "Ver oferta en tienda" : "Ver en tienda"}
                      </Button>
                    </Link>
                  </div>
                </Card>
              ))}
            </div>
          </div>

          <div className="mt-8">
            <h2 className="text-xl font-semibold text-[#9C2759] mb-4">
              Ofertas actuales para este producto
            </h2>
            <div className="grid gap-4">
              {currentOffers.length === 0 ? (
                <div>No hay ofertas actuales para este producto.</div>
              ) : (
                currentOffers.map((offer, index) => (
                  <Card key={`${offer.storeProduct.id}-${index}`} className="p-4">
                    <div className="flex justify-between items-start">
                      <div>
                        <p className="font-medium">{offer.storeProduct.storeName || "Tienda Desconocida"}</p>
                        <p className="font-semibold text-lg text-green-700">Precio de oferta: ${offer.offerPrice.toFixed(2)}</p>
                        <p className="text-sm text-gray-500 line-through">Precio regular: ${offer.storeProduct.price.toFixed(2)}</p>
                        <p className="text-xs text-gray-500 mt-1">
                          Válido: {new Date(offer.offerStartDate).toLocaleDateString()} - {new Date(offer.offerEndDate).toLocaleDateString()}
                        </p>
                         {offer.inconsistency && (
                          <p className="text-xs text-orange-600 mt-1">Nota: {offer.inconsistency}</p>
                        )}
                      </div>
                      <div className="text-right">
                        <p className="text-green-600 font-bold text-xl">
                          {Math.round((1 - offer.offerPrice / offer.storeProduct.price) * 100)}% OFF
                        </p>
                         <Link to={`/negocios/${offer.storeProduct.storeId}/productos/${offer.storeProduct.id}`} className="mt-2 inline-block">
                            <Button variant="default">Ver en tienda</Button>
                         </Link>
                      </div>
                    </div>
                  </Card>
                ))
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BaseProductInfo;