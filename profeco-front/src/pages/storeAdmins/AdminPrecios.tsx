import { Trash2 } from "lucide-react";
import React, { useCallback, useEffect, useState } from "react";
import api from "../../api/axiosConfig"; // Import the Axios instance
import { useAuth } from "../../context/AuthContext";
import { useStoreAdmin } from "../../context/StoreAdminContext";

// Define a type for the product data returned by the product details API
interface ApiProduct {
  id: string;
  name: string;
  imageUrl?: string; // Assuming imageUrl can be optional
  // Add other product properties if available from the API
}

// Define a type for API error responses
interface ApiError {
    message: string;
    // Add other common error properties if available
}

// Interface for the data from /store-products/by-store/${store.id}
interface StoreProductResponse {
  id: string; // ID of the store-product association (e.g., "e5fb1d68-...")
  price: number; // This is the original/base price
  offerPrice?: number | null;
  offerStartDate?: string | null;
  offerEndDate?: string | null;
  storeId: string;
  productId: string; // ID of the actual product (e.g., "d4ea0c57-...")
}

// Interface for the combined product information used in the component's state
interface ProductDisplayInfo {
  storeProductId: string; // Corresponds to StoreProductResponse.id, used for keys and delete
  productId: string;      // Corresponds to StoreProductResponse.productId, used for fetching details
  name: string;
  image: string;
  originalPrice: number;
  offerPrice?: number | null;
  offerStartDate?: string | null;
  offerEndDate?: string | null;
  effectivePrice: number; // The price to actually display and use
  isOnOffer: boolean;
}

const AdminPrecios: React.FC = () => {
  const { store } = useStoreAdmin();
  const [products, setProducts] = useState<ProductDisplayInfo[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const { token } = useAuth(); // Token is now implicitly handled by axios interceptor, but can be kept for conditional rendering if needed before API calls

  // Form states
  const [showForm, setShowForm] = useState(false);
  const [newProductId, setNewProductId] = useState("");
  const [newPrice, setNewPrice] = useState("");
  const [formLoading, setFormLoading] = useState(false);
  const [formError, setFormError] = useState<string | null>(null);
  const [formSuccess, setFormSuccess] = useState<string | null>(null);

  const isOfferActive = (
    offerPrice?: number | null,
    startDate?: string | null,
    endDate?: string | null
  ): boolean => {
    if (offerPrice == null || !startDate || !endDate) {
      return false;
    }
    const now = new Date();
    const offerStart = new Date(startDate);
    const offerEnd = new Date(endDate);
    // Set time to end of day for offerEnd for inclusive check
    offerEnd.setHours(23, 59, 59, 999);
    return now >= offerStart && now <= offerEnd;
  };

  const fetchProducts = useCallback(async () => {
    if (!store?.id) {
      setProducts([]);
      setLoading(false);
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const response = await api.get<StoreProductResponse[]>(
        `/store-products/by-store/${store.id}`
      );
      const storeProducts = response.data;

      const productsWithInfoPromises = storeProducts.map(async (sp) => {
        try {
          const productRes = await api.get<ApiProduct>(`/products/${sp.productId}`);
          const product = productRes.data;

          const activeOffer = isOfferActive(sp.offerPrice, sp.offerStartDate, sp.offerEndDate);
          const effectivePrice = activeOffer && sp.offerPrice != null ? sp.offerPrice : sp.price;

          return {
            storeProductId: sp.id,
            productId: sp.productId,
            name: product.name || "Producto sin nombre",
            image: product.imageUrl
              ? product.imageUrl.startsWith("http")
                ? product.imageUrl
                : `http://${product.imageUrl}` // Consider a more robust way to handle image URLs if needed
              : "", // Default or placeholder image
            originalPrice: sp.price,
            offerPrice: sp.offerPrice,
            offerStartDate: sp.offerStartDate,
            offerEndDate: sp.offerEndDate,
            effectivePrice: effectivePrice,
            isOnOffer: activeOffer,
          };
        } catch (productFetchError) {
          console.error(`Error fetching product details for ${sp.productId}:`, productFetchError);
          // Return a partial object or null, and filter out nulls later
          // This ensures one failed product doesn't break the whole list
          const activeOffer = isOfferActive(sp.offerPrice, sp.offerStartDate, sp.offerEndDate);
          const effectivePrice = activeOffer && sp.offerPrice != null ? sp.offerPrice : sp.price;
          return {
            storeProductId: sp.id,
            productId: sp.productId,
            name: "Producto no disponible",
            image: "",
            originalPrice: sp.price,
            offerPrice: sp.offerPrice,
            offerStartDate: sp.offerStartDate,
            offerEndDate: sp.offerEndDate,
            effectivePrice: effectivePrice,
            isOnOffer: activeOffer,
          };
        }
      });

      const resolvedProducts = await Promise.all(productsWithInfoPromises);
      setProducts(resolvedProducts.filter(Boolean) as ProductDisplayInfo[]);
    } catch (error) {
      console.error("Error fetching store products:", error);
      const errorMessage = (error as { response?: { data?: ApiError }; message?: string })?.response?.data?.message || (error as Error)?.message || "Error al cargar productos de la tienda";
      setError(errorMessage);
      setProducts([]);
    } finally {
      setLoading(false);
    }
  }, [store?.id]); // Removed token from dependencies as Axios handles it

  useEffect(() => {
    if (token) { // Ensure token exists before trying to fetch, even if Axios handles it
        fetchProducts();
    } else {
        setLoading(false);
        setProducts([]);
        setError("Usuario no autenticado.");
    }
  }, [token, fetchProducts]);

  const handleDeleteProduct = async (storeProductId: string) => {
    if (!window.confirm("¿Seguro que deseas eliminar este producto de la tienda?")) return;
    try {
      await api.delete(`/store-products/${storeProductId}`);
      setProducts((prev) => prev.filter((p) => p.storeProductId !== storeProductId));
      // Optionally, add a success message
    } catch (error) {
      console.error("Error deleting product:", error);
      const errorMessage = (error as { response?: { data?: ApiError }; message?: string })?.response?.data?.message || (error as Error)?.message || "Error al eliminar el producto";
      alert(errorMessage);
    }
  };

  const handleAddProduct = async (e: React.FormEvent) => {
    e.preventDefault();
    setFormLoading(true);
    setFormError(null);
    setFormSuccess(null);

    if (!newProductId.trim() || !newPrice.trim()) {
        setFormError("El ID del producto y el precio son obligatorios.");
        setFormLoading(false);
        return;
    }

    const priceValue = parseFloat(newPrice);
    if (isNaN(priceValue) || priceValue < 0) {
        setFormError("El precio debe ser un número válido y no negativo.");
        setFormLoading(false);
        return;
    }

    try {
      // The storeId might need to be part of the payload, depending on your API
      // Assuming the API infers storeId from the authenticated user or another source
      // When adding a product, it's added with its regular price. Offers are managed separately or not set initially.
      await api.post("/store-products", {
        price: priceValue, // This corresponds to originalPrice
        productId: newProductId,
        storeId: store?.id, // Ensure storeId is included if your backend /store-prodaucts POST endpoint expects it
      });
      setFormSuccess("Producto agregado correctamente");
      setNewProductId("");
      setNewPrice("");
      setShowForm(false);
      fetchProducts(); // Refresh the list of products
    } catch (error) {
      console.error("Error adding product:", error);
      const errorMessage = (error as { response?: { data?: ApiError }; message?: string })?.response?.data?.message || (error as Error)?.message || "No se pudo agregar el producto";
      setFormError(errorMessage);
    } finally {
      setFormLoading(false);
    }
  };

  return (
    <div className="p-20 mt-10">
      <h1 className="text-2xl font-bold mb-6">Productos de la tienda</h1>
      {loading && <p>Cargando...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
      {!loading && !error && (
        <>
          <table className="min-w-full divide-y divide-gray-200 mb-8">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Imagen
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Nombre
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Precio
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Oferta
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Operación
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {products.map((prod) => (
                <tr key={prod.storeProductId}>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <img
                      src={prod.image || ""}
                      alt={prod.name}
                      className="w-20 h-20 object-cover rounded"
                    />
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">{prod.name}</td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {prod.isOnOffer && prod.offerPrice != null ? (
                      <>
                        <span style={{ textDecoration: "line-through" }} className="text-gray-500 mr-2">
                          ${prod.originalPrice.toFixed(2)}
                        </span>
                        <span className="text-green-600 font-bold">
                          ${prod.effectivePrice.toFixed(2)}
                        </span>
                      </>
                    ) : (
                      `$${prod.effectivePrice.toFixed(2)}`
                    )}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {prod.isOnOffer && prod.offerStartDate && prod.offerEndDate ? (
                      <div className="text-sm">
                        <p className="text-green-600 font-semibold">¡En oferta!</p>
                        <p>Inicio: {new Date(prod.offerStartDate).toLocaleDateString()}</p>
                        <p>Fin: {new Date(prod.offerEndDate).toLocaleDateString()}</p>
                      </div>
                    ) : (
                      "-"
                    )}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <button
                      onClick={() => handleDeleteProduct(prod.storeProductId)}
                      className="text-red-600 hover:text-red-800 p-2 rounded transition cursor-pointer"
                      title="Eliminar producto"
                    >
                      <Trash2 className="w-5 h-5" />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <button
            className="mb-4 px-6 py-2 bg-[#681837] text-white cursor-pointer rounded-lg hover:bg-[#561429] transition"
            onClick={() => setShowForm((v) => !v)}
          >
            {showForm ? "Cancelar" : "Agregar producto"}
          </button>

          {showForm && (
            <form
              onSubmit={handleAddProduct}
              className="bg-white rounded-xl shadow-md border border-gray-200 p-6 max-w-md flex flex-col gap-4"
            >
              <label className="block text-gray-700 font-medium">
                ID del producto:
                <input
                  type="text"
                  value={newProductId}
                  onChange={(e) => setNewProductId(e.target.value)}
                  required
                  disabled={formLoading}
                  className="mt-2 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#681837] transition"
                  placeholder="ID del producto"
                />
              </label>
              <label className="block text-gray-700 font-medium">
                Precio:
                <input
                  type="number"
                  min="0"
                  step="0.01"
                  value={newPrice}
                  onChange={(e) => setNewPrice(e.target.value)}
                  required
                  disabled={formLoading}
                  className="mt-2 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#681837] transition"
                  placeholder="Precio"
                />
              </label>
              <button
                type="submit"
                disabled={formLoading || !newProductId || !newPrice}
                className={`w-full py-2 rounded-lg font-semibold transition-colors ${
                  formLoading || !newProductId || !newPrice
                    ? "bg-gray-300 text-gray-500 cursor-not-allowed"
                    : "bg-[#681837] text-white hover:bg-[#561429] cursor-pointer"
                }`}
              >
                {formLoading ? "Agregando..." : "Agregar"}
              </button>
              {formError && (
                <div className="text-red-500 text-sm text-center">{formError}</div>
              )}
              {formSuccess && (
                <div className="text-green-600 text-sm text-center">{formSuccess}</div>
              )}
            </form>
          )}
        </>
      )}
    </div>
  );
};

export default AdminPrecios;