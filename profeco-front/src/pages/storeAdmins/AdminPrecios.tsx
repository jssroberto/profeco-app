import React, { useEffect, useState } from "react";
import { useStoreAdmin } from "../../context/StoreAdminContext";
import { useAuth } from "../../context/AuthContext";
import { Trash2 } from "lucide-react";

interface StoreProduct {
  price: number;
  storeId: string;
  productId: string;
}

interface ProductInfo {
  id: string;
  name: string;
  image: string;
  price: number;
}

const AdminPrecios: React.FC = () => {
  const { store,  } = useStoreAdmin();
  const [products, setProducts] = useState<ProductInfo[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const { token } = useAuth();


  // estwados
  const [showForm, setShowForm] = useState(false);
  const [newProductId, setNewProductId] = useState("");
  const [newPrice, setNewPrice] = useState("");
  const [formLoading, setFormLoading] = useState(false);
  const [formError, setFormError] = useState<string | null>(null);
  const [formSuccess, setFormSuccess] = useState<string | null>(null);


    // func pa eliminar prduct
    const handleDeleteProduct = async (productId: string) => {
    if (!window.confirm("¿Seguro que deseas eliminar este producto de la tienda?")) return;
    try {
      const res = await fetch(`http://localhost:8080/api/v1/store-products/${productId}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error("No se pudo eliminar el producto");
      setProducts((prev) => prev.filter((p) => p.id !== productId));
    } catch (err: any) {
      alert(err.message || "Error al eliminar el producto");
    }
  };

  useEffect(() => {
    const fetchProducts = async () => {
      if (!store?.id) return;
      setLoading(true);
      setError(null);
      try {
        const res = await fetch(
          `http://localhost:8080/api/v1/store-products/by-store/${store.id}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        if (!res.ok) throw new Error("Error al cargar productos");
        const storeProducts: StoreProduct[] = await res.json();

        const productsWithInfo = await Promise.all(
          storeProducts.map(async (sp) => {
            try {
              const productRes = await fetch(
                `http://localhost:8080/api/v1/products/${sp.productId}`,
                {
                  headers: { Authorization: `Bearer ${token}` },
                }
              );
              if (!productRes.ok) throw new Error();
              const product = await productRes.json();
              return {
                id: sp.productId,
                name: product.name || "Producto sin nombre",
                image: product.imageUrl
                  ? product.imageUrl.startsWith("http")
                    ? product.imageUrl
                    : `http://${product.imageUrl}`
                  : "",
                price: sp.price,
              };
            } catch {
              return null;
            }
          })
        );

        setProducts(productsWithInfo.filter(Boolean) as ProductInfo[]);
      } catch (err: any) {
        setError(err.message || "Error desconocido");
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, [store?.id, token]);

  // Handler para el form de agregar producto
  const handleAddProduct = async (e: React.FormEvent) => {
    e.preventDefault();
    setFormLoading(true);
    setFormError(null);
    setFormSuccess(null);

    try {
      const res = await fetch("http://localhost:8080/api/v1/store-products", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          price: parseFloat(newPrice),
          productId: newProductId,
        }),
      });
      if (!res.ok) throw new Error("No se pudo agregar el producto");
      setFormSuccess("Producto agregado correctamente");
      setNewProductId("");
      setNewPrice("");
      setShowForm(false);
      // Refresca la lista
      setLoading(true);
      setTimeout(() => {
        window.location.reload();
      }, 1000);
    } catch (err: any) {
      setFormError(err.message || "Error desconocido");
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
                  Operación
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {products.map((prod) => (
                <tr key={prod.id}>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <img
                      src={prod.image || ""}
                      alt={prod.name}
                      className="w-16 h-16 object-cover rounded"
                    />
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">{prod.name}</td>
                  <td className="px-6 py-4 whitespace-nowrap">${prod.price}</td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <button
                      onClick={() => handleDeleteProduct(prod.id)}
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