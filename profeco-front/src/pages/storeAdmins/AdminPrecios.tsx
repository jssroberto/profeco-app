import { Edit3, Trash2 } from "lucide-react";
import React, { useCallback, useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import { useAuth } from "../../context/AuthContext";
import { useStoreAdmin } from "../../context/StoreAdminContext";

interface ApiProduct {
  id: string;
  name: string;
  imageUrl?: string;
}

interface SelectableProduct {
  id: string;
  name: string;
}

interface ApiError {
    message: string;
}

interface StoreProductResponse {
  id: string;
  price: number;
  offerPrice?: number | null;
  offerStartDate?: string | null;
  offerEndDate?: string | null;
  storeId: string;
  productId: string;
}


interface ProductDisplayInfo {
  storeProductId: string;
  productId: string;     
  name: string;
  image: string;
  originalPrice: number;
  offerPrice?: number | null;
  offerStartDate?: string | null;
  offerEndDate?: string | null;
  effectivePrice: number;
  isOnOffer: boolean;
}

const AdminPrecios: React.FC = () => {
  const { store } = useStoreAdmin();
  const [products, setProducts] = useState<ProductDisplayInfo[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const { token } = useAuth(); 

  const [showAddForm, setShowAddForm] = useState(false);
  const [selectedProductId, setSelectedProductId] = useState("");
  const [newPrice, setNewPrice] = useState("");
  const [formLoading, setFormLoading] = useState(false);
  const [formError, setFormError] = useState<string | null>(null);
  const [formSuccess, setFormSuccess] = useState<string | null>(null);

  const [selectableProducts, setSelectableProducts] = useState<SelectableProduct[]>([]);
  const [selectableProductsLoading, setSelectableProductsLoading] = useState<boolean>(false);
  const [selectableProductsError, setSelectableProductsError] = useState<string | null>(null);

  const [showEditDialog, setShowEditDialog] = useState<boolean>(false);
  const [editingProduct, setEditingProduct] = useState<ProductDisplayInfo | null>(null);
  const [editFormPrice, setEditFormPrice] = useState<string>("");
  const [editFormOfferPrice, setEditFormOfferPrice] = useState<string>("");
  const [editFormOfferStartDate, setEditFormOfferStartDate] = useState<string>("");
  const [editFormOfferEndDate, setEditFormOfferEndDate] = useState<string>("");
  const [editFormLoading, setEditFormLoading] = useState<boolean>(false);
  const [editFormError, setEditFormError] = useState<string | null>(null);
  const [editFormSuccess, setEditFormSuccess] = useState<string | null>(null);


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
                : `http://${product.imageUrl}`
              : "", 
            originalPrice: sp.price,
            offerPrice: sp.offerPrice,
            offerStartDate: sp.offerStartDate,
            offerEndDate: sp.offerEndDate,
            effectivePrice: effectivePrice,
            isOnOffer: activeOffer,
          };
        } catch (productFetchError) {
          console.error(`Error fetching product details for ${sp.productId}:`, productFetchError);
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
  }, [store?.id]); 

  useEffect(() => {
    if (token) {
        fetchProducts();
    } else {
        setLoading(false);
        setProducts([]);
        setError("Usuario no autenticado.");
    }
  }, [token, fetchProducts]);

  const fetchSelectableProducts = async () => {
    setSelectableProductsLoading(true);
    setSelectableProductsError(null);
    try {
      const response = await api.get<SelectableProduct[]>("/products");
      setSelectableProducts(response.data);
      if (response.data.length > 0) {
        setSelectedProductId(response.data[0].id);
      } else {
        setSelectedProductId("");
      }
    } catch (err) {
      console.error("Error fetching selectable products:", err);
      const errorMessage = (err as { response?: { data?: ApiError }; message?: string })?.response?.data?.message || (err as Error)?.message || "Error al cargar lista de productos";
      setSelectableProductsError(errorMessage);
      setSelectableProducts([]);
    } finally {
      setSelectableProductsLoading(false);
    }
  };

  useEffect(() => {
    if (showAddForm && token) {
      fetchSelectableProducts();
    }
  }, [showAddForm, token]);

  const handleDeleteProduct = async (storeProductId: string) => {
    if (!window.confirm("¿Seguro que deseas eliminar este producto de la tienda?")) return;
    try {
      await api.delete(`/store-products/${storeProductId}`);
      setProducts((prev) => prev.filter((p) => p.storeProductId !== storeProductId));
    } catch (error) {
      console.error("Error deleting product:", error);
      const errorMessage = (error as { response?: { data?: ApiError }; message?: string })?.response?.data?.message || (error as Error)?.message || "Error al eliminar el producto";
      alert(errorMessage);
    }
  };

  const handleOpenEditDialog = (product: ProductDisplayInfo) => {
    setEditingProduct(product);
    setEditFormPrice(product.originalPrice.toString());
    setEditFormOfferPrice(product.offerPrice?.toString() ?? "");
    setEditFormOfferStartDate(product.offerStartDate ? new Date(product.offerStartDate).toISOString().split('T')[0] : "");
    setEditFormOfferEndDate(product.offerEndDate ? new Date(product.offerEndDate).toISOString().split('T')[0] : "");
    setEditFormError(null);
    setEditFormSuccess(null);
    setShowEditDialog(true);
  };

  const handleCloseEditDialog = () => {
    setEditingProduct(null);
    setShowEditDialog(false);
    setEditFormError(null);
    setEditFormSuccess(null);
  };

  const handleSaveChanges = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!editingProduct) return;

    setEditFormLoading(true);
    setEditFormError(null);
    setEditFormSuccess(null);

    const newParsedPrice = parseFloat(editFormPrice);
    if (isNaN(newParsedPrice) || newParsedPrice < 0) {
      setEditFormError("El precio debe ser un número válido y no negativo.");
      setEditFormLoading(false);
      return;
    }

    const newParsedOfferPriceStr = editFormOfferPrice.trim();
    let newParsedOfferPrice: number | null = null;
    if (newParsedOfferPriceStr !== "") {
      newParsedOfferPrice = parseFloat(newParsedOfferPriceStr);
      if (isNaN(newParsedOfferPrice) || newParsedOfferPrice < 0) {
        setEditFormError("El precio de oferta debe ser un número válido y no negativo.");
        setEditFormLoading(false);
        return;
      }
      if (newParsedOfferPrice >= newParsedPrice) {
        setEditFormError("El precio de oferta debe ser menor que el precio original.");
        setEditFormLoading(false);
        return;
      }
    }

    const newOfferStartDate = editFormOfferStartDate.trim() === "" ? null : editFormOfferStartDate;
    const newOfferEndDate = editFormOfferEndDate.trim() === "" ? null : editFormOfferEndDate;

    if ((newOfferStartDate && !newOfferEndDate) || (!newOfferStartDate && newOfferEndDate)) {
      setEditFormError("Debe especificar tanto la fecha de inicio como la de fin de la oferta, o ninguna.");
      setEditFormLoading(false);
      return;
    }
    if (newOfferStartDate && newOfferEndDate && new Date(newOfferEndDate) < new Date(newOfferStartDate)) {
      setEditFormError("La fecha de fin de la oferta no puede ser anterior a la fecha de inicio.");
      setEditFormLoading(false);
      return;
    }
    if ((newOfferStartDate || newOfferEndDate) && newParsedOfferPrice === null) {
        setEditFormError("Si establece fechas de oferta, también debe ingresar un precio de oferta.");
        setEditFormLoading(false);
        return;
    }
    if (newParsedOfferPrice !== null && (!newOfferStartDate || !newOfferEndDate)) {
        setEditFormError("Si establece un precio de oferta, también debe ingresar las fechas de inicio y fin de la oferta.");
        setEditFormLoading(false);
        return;
    }

    const { 
        productId, 
        originalPrice: initialOriginalPrice, 
        offerPrice: initialOfferPrice, 
        offerStartDate: initialOfferStartDateISO, 
        offerEndDate: initialOfferEndDateISO 
    } = editingProduct;

    const initialFormattedOfferStartDate = initialOfferStartDateISO ? new Date(initialOfferStartDateISO).toISOString().split('T')[0] : null;
    const initialFormattedOfferEndDate = initialOfferEndDateISO ? new Date(initialOfferEndDateISO).toISOString().split('T')[0] : null;
    
    const priceHasChanged = newParsedPrice !== initialOriginalPrice;
    const offerHasChanged = 
        newParsedOfferPrice !== initialOfferPrice ||
        newOfferStartDate !== initialFormattedOfferStartDate ||
        newOfferEndDate !== initialFormattedOfferEndDate;

    if (!priceHasChanged && !offerHasChanged) {
      setEditFormSuccess("No se detectaron cambios.");
      setEditFormLoading(false);
      return;
    }

    const apiPromises: Promise<any>[] = [];

    if (priceHasChanged) {
      const priceUpdatePayload = {
        price: newParsedPrice,
        productId: productId,
      };
      console.log("Calling Price API (/store-products/by-product) with payload:", priceUpdatePayload);
      apiPromises.push(api.put('/store-products/by-product', priceUpdatePayload));
    }

    if (offerHasChanged) {
      const offerUpdatePayload = {
        offerPrice: newParsedOfferPrice,
        offerStartDate: newOfferStartDate,
        offerEndDate: newOfferEndDate,
        productId: productId,
      };
      console.log("Calling Offer API (/store-product-offers/apply) with payload:", offerUpdatePayload);
      apiPromises.push(api.post('/store-product-offers/apply', offerUpdatePayload));
    }

    try {
      await Promise.all(apiPromises);
      setEditFormSuccess("Producto actualizado correctamente.");
      fetchProducts();
      setTimeout(() => {
        handleCloseEditDialog();
      }, 1500);
    } catch (error) {
      console.error("Error updating product:", error);
      const errorMessage = (error as { response?: { data?: ApiError }; message?: string })?.response?.data?.message || (error as Error)?.message || "No se pudo actualizar el producto";
      setEditFormError(errorMessage);
    } finally {
      setEditFormLoading(false);
    }
  };

  const handleAddProduct = async (e: React.FormEvent) => {
    e.preventDefault();
    setFormLoading(true);
    setFormError(null);
    setFormSuccess(null);

    if (!selectedProductId.trim() || !newPrice.trim()) {
        setFormError("Debe seleccionar un producto y especificar un precio.");
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
      await api.post("/store-products", {
        price: priceValue,
        productId: selectedProductId,
        storeId: store?.id, 
      });
      setFormSuccess("Producto agregado correctamente");
      setSelectedProductId(selectableProducts.length > 0 ? selectableProducts[0].id : "");
      setNewPrice("");
      setShowAddForm(false);
      fetchProducts();
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
                  Acciones
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
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <button
                      onClick={() => handleOpenEditDialog(prod)} 
                      className="text-indigo-600 hover:text-indigo-900 p-3 rounded-md transition cursor-pointer mr-3"
                      title="Editar producto"
                    >
                      <Edit3 className="w-6 h-6" />
                    </button>
                    <button
                      onClick={() => handleDeleteProduct(prod.storeProductId)}
                      className="text-red-600 hover:text-red-800 p-3 rounded-md transition cursor-pointer"
                      title="Eliminar producto"
                    >
                      <Trash2 className="w-6 h-6" />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <button
            className="mb-4 px-6 py-2 bg-[#681837] text-white cursor-pointer rounded-lg hover:bg-[#561429] transition"
            onClick={() => setShowAddForm((v) => !v)}
          >
            {showAddForm ? "Cancelar" : "Agregar producto"}
          </button>

          {showAddForm && (
            <form
              onSubmit={handleAddProduct}
              className="bg-white rounded-xl shadow-md border border-gray-200 p-6 max-w-md flex flex-col gap-4"
            >
              {selectableProductsLoading && <p>Cargando productos...</p>}
              {selectableProductsError && <p style={{ color: "red" }}>{selectableProductsError}</p>}
              {!selectableProductsLoading && !selectableProductsError && selectableProducts.length === 0 && (
                <p className="text-sm text-gray-600">No hay productos disponibles para agregar o no se pudieron cargar.</p>
              )}
              {!selectableProductsLoading && !selectableProductsError && selectableProducts.length > 0 && (
                <>
                  <label className="block text-gray-700 font-medium">
                    Producto:
                    <select
                      value={selectedProductId}
                      onChange={(e) => setSelectedProductId(e.target.value)}
                      required
                      disabled={formLoading || selectableProductsLoading}
                      className="mt-2 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#681837] transition"
                    >
                      {selectableProducts.map((product) => (
                        <option key={product.id} value={product.id}>
                          {product.name}
                        </option>
                      ))}
                    </select>
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
                    disabled={formLoading || selectableProductsLoading || !selectedProductId || !newPrice}
                    className={`w-full py-2 rounded-lg font-semibold transition-colors ${
                      formLoading || selectableProductsLoading || !selectedProductId || !newPrice
                        ? "bg-gray-300 text-gray-500 cursor-not-allowed"
                        : "bg-[#681837] text-white hover:bg-[#561429] cursor-pointer"
                    }`}
                  >
                    {formLoading ? "Agregando..." : "Agregar"}
                  </button>
                </>
              )}
              {formError && (
                <div className="text-red-500 text-sm text-center">{formError}</div>
              )}
              {formSuccess && (
                <div className="text-green-600 text-sm text-center">{formSuccess}</div>
              )}
            </form>
          )}

          {showEditDialog && editingProduct && (
            <div 
              className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full flex justify-center items-center"
            >
              <form 
                onSubmit={handleSaveChanges}
                className="bg-white p-8 rounded-lg shadow-xl w-full max-w-lg flex flex-col gap-4"
              >
                <h2 className="text-xl font-semibold mb-2 text-center">Editar Producto: {editingProduct.name}</h2>
                
                <label className="block text-gray-700 font-medium">
                  Precio Original:
                  <input
                    type="number"
                    min="0"
                    step="0.01"
                    value={editFormPrice}
                    onChange={(e) => setEditFormPrice(e.target.value)}
                    required
                    disabled={editFormLoading}
                    className="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#681837] transition"
                    placeholder="Precio Original"
                  />
                </label>

                <label className="block text-gray-700 font-medium">
                  Precio de Oferta (opcional):
                  <input
                    type="number"
                    min="0"
                    step="0.01"
                    value={editFormOfferPrice}
                    onChange={(e) => setEditFormOfferPrice(e.target.value)}
                    disabled={editFormLoading}
                    className="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#681837] transition"
                    placeholder="Precio de Oferta"
                  />
                </label>

                <label className="block text-gray-700 font-medium">
                  Inicio de Oferta (opcional):
                  <input
                    type="date"
                    value={editFormOfferStartDate}
                    onChange={(e) => setEditFormOfferStartDate(e.target.value)}
                    disabled={editFormLoading}
                    className="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#681837] transition"
                  />
                </label>

                <label className="block text-gray-700 font-medium">
                  Fin de Oferta (opcional):
                  <input
                    type="date"
                    value={editFormOfferEndDate}
                    onChange={(e) => setEditFormOfferEndDate(e.target.value)}
                    disabled={editFormLoading}
                    className="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#681837] transition"
                    min={editFormOfferStartDate}
                  />
                </label>
                
                {editFormError && (
                  <div className="text-red-500 text-sm text-center py-2">{editFormError}</div>
                )}
                {editFormSuccess && (
                  <div className="text-green-600 text-sm text-center py-2">{editFormSuccess}</div>
                )}

                <div className="mt-4 flex justify-end space-x-3">
                  <button 
                    type="button"
                    onClick={handleCloseEditDialog}
                    disabled={editFormLoading}
                    className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-200 hover:bg-gray-300 rounded-md transition disabled:opacity-50"
                  >
                    Cancelar
                  </button>
                  <button 
                    type="submit"
                    disabled={editFormLoading || !editFormPrice}
                    className={`px-4 py-2 text-sm font-medium text-white bg-[#681837] hover:bg-[#561429] rounded-md transition ${
                      editFormLoading || !editFormPrice ? "opacity-50 cursor-not-allowed" : ""
                    }`}
                  >
                    {editFormLoading ? "Guardando..." : "Guardar Cambios"}
                  </button>
                </div>
              </form>
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default AdminPrecios;