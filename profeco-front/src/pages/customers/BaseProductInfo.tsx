import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import axios from "axios";
import Card from "../../components/ui/Card";
import Button from "../../components/ui/Button";
import { useAuth } from "../../context/AuthContext";

const BaseProductInfo = () => {
  const { id } = useParams();
  const { token } = useAuth();
  const [product, setProduct] = useState<any>(null);
  const [storeProducts, setStoreProducts] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [adding, setAdding] = useState(false);
  const [addSuccess, setAddSuccess] = useState(false);
  const [addError, setAddError] = useState<string | null>(null);

  useEffect(() => {
    const fetchProductAndStores = async () => {
      try {
        setLoading(true);
        // Get base product info
        const productRes = await axios.get(
          `http://localhost:8080/api/v1/products/${id}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        setProduct(productRes.data);

        const storeProductsRes = await axios.get(
          `http://localhost:8080/api/v1/store-products/by-product-name?name=${encodeURIComponent(productRes.data.name)}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );

        const storeProductsWithStore = await Promise.all(
          storeProductsRes.data.map(async (sp: any) => {
            const storeRes = await axios.get(
              `http://localhost:8080/api/v1/stores/${sp.storeId}`,
              { headers: { Authorization: `Bearer ${token}` } }
            );
            return {
              ...sp,
              storeName: storeRes.data.name,
              storeId: sp.storeId,
              storeProductId: sp.id,
              price: sp.price
            };
          })
        );
        setStoreProducts(storeProductsWithStore);
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    };
    fetchProductAndStores();
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
    } catch (e) {
      setAddError("No se pudo agregar a la lista.");
    } finally {
      setAdding(false);
    }
  };

  if (loading) return <div className="p-8">Cargando...</div>;
  if (!product) return <div className="p-8">Producto no encontrado</div>;

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
          <div>
            <Button onClick={handleAddToShoppingList} disabled={adding}>
              {adding ? "Agregando..." : "Agregar a Shopping List"}
            </Button>
            {addSuccess && (
              <div className="text-green-600 text-sm font-semibold mt-2">
                ¡Agregado!
              </div>
            )}
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
              {storeProducts.map((sp, idx) => (
                <Card
                  key={sp.storeProductId}
                  className="p-4 flex justify-between items-center"
                >
                  <div>
                    <div className="font-medium">{sp.storeName}</div>
                    <div className="text-sm text-gray-500">
                      ID tienda: {sp.storeId}
                    </div>
                  </div>
                  <div className="flex items-center gap-4">
                    <span className="font-bold text-lg">
                      ${sp.price.toFixed(2)}
                    </span>
                    <Link
                      to={`/negocios/${sp.storeId}/productos/${sp.storeProductId}`}
                    >
                      <Button variant="outline">Ver producto en tienda</Button>
                    </Link>
                  </div>
                </Card>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BaseProductInfo;
