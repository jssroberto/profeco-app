import { Link, useParams } from "react-router-dom";
import Button from "../../components/ui/Button";
import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import axios from "axios";

interface ProductInfo {
  id: string;
  name: string;
  imageUrl: string;
  price: number;
  storeName: string;
  offer?: {
    offerPrice: number;
    offerStartDate: string;
    offerEndDate: string;
  };
}

interface StoreProductOffer {
  offerPrice: number;
  offerStartDate: string;
  offerEndDate: string;
  storeProduct: {
    id: string;
  };
}

interface StoreProduct {
  id: string;
  price: number;
  storeId: string;
  storeName: string;
}

const ProductInfo = () => {
  const { id } = useParams<{ id: string }>();
  const { token } = useAuth();
  const [product, setProduct] = useState<ProductInfo | null>(null);
  const [availableStores, setAvailableStores] = useState<StoreProduct[]>([]);
  const [loading, setLoading] = useState(true);
  const [adding, setAdding] = useState(false);
  const [addSuccess, setAddSuccess] = useState(false);
  const [addError, setAddError] = useState<string | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      if (!id) return;
      if (!token) {
        setLoading(false);
        setAddError("Debes iniciar sesión para ver esta información.");
        return;
      }
      try {
        setLoading(true);
        const productStoreResponse = await axios.get(
          `http://localhost:8080/api/v1/store-products/${id}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );

        if (productStoreResponse.data?.productId) {
          // Obtener información del producto base
          const productResponse = await axios.get(
            `http://localhost:8080/api/v1/products/${productStoreResponse.data.productId}`,
            { headers: { Authorization: `Bearer ${token}` } }
          );

          // Obtener información de la tienda
          const storeResponse = await axios.get(
            `http://localhost:8080/api/v1/stores/${productStoreResponse.data.storeId}`,
            { headers: { Authorization: `Bearer ${token}` } }
          );

          // Obtener ofertas actuales para este producto
          const today = new Date().toISOString().split("T")[0];
          const offersResponse = await axios.get<StoreProductOffer[]>(
            `http://localhost:8080/api/v1/store-product-offers/current-for-product/${productResponse.data.id}?currentDate=${today}`,
            { headers: { Authorization: `Bearer ${token}` } }
          );
          
          const currentOffer = offersResponse.data.find(
            (offer) => offer.storeProduct.id === id
          );

          setProduct({
            id: productResponse.data.id,
            name: productResponse.data.name,
            imageUrl: productResponse.data.imageUrl.startsWith("http")
              ? productResponse.data.imageUrl
              : `http://${productResponse.data.imageUrl}`,
            price: productStoreResponse.data.price,
            storeName: storeResponse.data.name,
            ...(currentOffer && {
              offer: {
                offerPrice: currentOffer.offerPrice,
                offerStartDate: currentOffer.offerStartDate,
                offerEndDate: currentOffer.offerEndDate
              }
            })
          });

          // Obtener supermercados donde existe el producto base
          const productName = productResponse.data.name;
          const storesRes = await axios.get(
            `http://localhost:8080/api/v1/store-products/by-product-name?name=${encodeURIComponent(productName)}`,
            { headers: { Authorization: `Bearer ${token}` } }
          );
          console.log('Respuesta de store-products/by-product-name:', storesRes.data);
          // Enriquecer con nombre de tienda
          const storesWithNames = await Promise.all(
            storesRes.data.map(async (sp: { id: string; price: number; storeId: string }) => {
              const storeRes = await axios.get(
                `http://localhost:8080/api/v1/stores/${sp.storeId}`,
                { headers: { Authorization: `Bearer ${token}` } }
              );
              return {
                id: sp.id,
                price: sp.price,
                storeId: sp.storeId,
                storeName: storeRes.data.name,
              };
            })
          );
          console.log('Supermercados enriquecidos:', storesWithNames);
          setAvailableStores(storesWithNames);
        }
      } catch (error) {
        if (axios.isAxiosError(error) && error.response?.status === 401) {
          setAddError("Tu sesión ha expirado o no tienes permisos. Inicia sesión de nuevo.");
        } else {
          setAddError("Ocurrió un error al cargar la información del producto.");
        }
        console.error("Error al cargar datos:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [id, token]);
  
  const handleAddToShoppingList = async () => {
    if (!product?.id) return;
    setAdding(true);
    setAddError(null);
    try {
      await axios.post(
        `http://localhost:8080/api/v1/preferences/shopping-list/${product.id}`,
        {},
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setAddSuccess(true);
      setTimeout(() => setAddSuccess(false), 2000);
    } catch (error) {
      setAddError("No se pudo agregar a la lista.");
    } finally {
      setAdding(false);
    }
  };

  if (loading) return <div className="p-8">Cargando...</div>;
  if (!product) return <div className="p-8">Producto no encontrado.</div>;
  if (!token) return <div className="p-8">Debes iniciar sesión para ver esta información.</div>;

  return (
    <div className="max-w-6xl mx-auto p-4 mt-24">
      <div className="grid md:grid-cols-2 gap-8">
        <div className="relative">
          <img
            src={product.imageUrl}
            alt={product.name}
            className="w-full object-contain"
          />
        </div>

        <div className="space-y-6">
          <div>
            <h1 className="text-2xl font-semibold mt-2">{product.name}</h1>
            <div className="flex items-baseline gap-2 mt-4">
              {product.offer ? (
                <div className="flex flex-col">
                  <div className="flex items-center gap-2">
                    <span className="text-4xl font-bold text-green-700">${product.offer.offerPrice.toFixed(2)}</span>
                    <span className="text-sm bg-green-100 text-green-800 px-2 py-1 rounded-full">¡Oferta!</span>
                  </div>
                  <span className="text-lg text-gray-400 line-through">${product.price.toFixed(2)}</span>
                  <span className="text-sm text-gray-500">
                    Válido: {new Date(product.offer.offerStartDate).toLocaleDateString()} - {new Date(product.offer.offerEndDate).toLocaleDateString()}
                  </span>
                </div>
              ) : (
                <span className="text-4xl font-bold">${product.price.toFixed(2)}</span>
              )}
            </div>
            <div className="flex gap-4 text-gray-600 mt-4 text-sm">
              <span>Vendido por: {product.storeName}</span>
            </div>
          </div>

          <div className="space-y-2">
            <Button onClick={handleAddToShoppingList} disabled={adding}>
              {adding ? "Agregando..." : "Agregar a Shopping List"}
            </Button>
            {addSuccess && (
              <div className="text-green-600 text-sm font-semibold">¡Agregado!</div>
            )}
            {addError && (
              <div className="text-red-600 text-sm font-semibold">{addError}</div>
            )}
            <Link
              to={`/productos/${id}/reportar`}
              state={{
                product: {
                  name: product.name,
                  store: product.storeName,
                  image: product.imageUrl,
                  publishedPrice: product.price,
                  storeProductId: id
                }
              }}
            >
              <Button variant="outline" className="text-red-500">
                Reportar inconsistencia
              </Button>
            </Link>
          </div>

          {availableStores.length > 0 && (
            <div className="mt-6">
              <h3 className="text-lg font-semibold mb-2">Disponible en supermercados:</h3>
              <ul className="list-disc ml-6">
                {availableStores.map((sp) => (
                  <li key={sp.id}>
                    {sp.storeName} <span className="text-gray-500">(Precio: ${sp.price.toFixed(2)})</span>
                  </li>
                ))}
              </ul>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ProductInfo;
