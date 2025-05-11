import { useParams, Link } from "react-router-dom";
import { Heart } from "lucide-react";
import Button from "../components/ui/Button";
import Card from "../components/ui/Card";
import { mockProducts } from "../data/products";
import { stores } from "../data/stores";

const parsePrice = (priceStr: string): number => {
  return parseFloat(priceStr.replace("$", "").replace(",", ""));
};

const getStoreNameById = (storeId: string): string => {
  return stores.find((store) => store.id === storeId)?.name || "Tienda desconocida";
};

const ProductInfo = () => {
  const { id, tienda } = useParams<{ id: string; tienda?: string }>();
  const product = mockProducts.find((p) => p.id === id);

  if (!product) {
    return (
      <div className="max-w-4xl mx-auto mt-24 p-4 text-center text-gray-600">
        Producto no encontrado
      </div>
    );
  }

  const { imageUrl, name, brand, offers } = product;

  const selectedOffer =
    tienda && offers.find((offer) => offer.store_id === tienda);

  const bestOffer = selectedOffer
    ? selectedOffer
    : offers.reduce((min, curr) =>
        parsePrice(curr.price) < parsePrice(min.price) ? curr : min
      );

  const bestOfferStoreName = getStoreNameById(bestOffer.store_id);

  return (
    <div className="max-w-6xl mx-auto p-4 mt-24">
      <div className="grid md:grid-cols-2 gap-8">
        <div className="relative">
          <button className="absolute top-4 left-4 bg-white p-2 rounded-full shadow-md cursor-pointer">
            <Heart className="w-6 h-6 text-gray-600" />
          </button>
          <img src={imageUrl} alt={name} className="w-full object-contain" />
        </div>

        <div className="space-y-6">
          <div>
            {bestOffer.oferta && (
              <div className="inline-block bg-green-100 text-green-800 px-3 py-1 rounded-md text-sm">
                Oferta
              </div>
            )}
            <h1 className="text-2xl font-semibold mt-2">{name}</h1>
            <div className="flex items-baseline gap-2 mt-4">
              <span className="text-4xl font-bold">{bestOffer.price}</span>
              {bestOffer.originalPrice && (
                <span className="line-through text-gray-500 text-lg">
                  {bestOffer.originalPrice}
                </span>
              )}
            </div>
            <div className="flex gap-4 text-gray-600 mt-4 text-sm">
              <span>Marca: {brand}</span>
              <span>Vendido por: {bestOfferStoreName}</span>
            </div>
          </div>

          <div className="space-y-2">
            <Button>Agregar a WishList</Button>
            <Link to={`/productos/${id}/${bestOffer.store_id}/reportar`}>
              <Button variant="outline" className="text-red-500">
                Reportar inconsistencia
              </Button>
            </Link>
          </div>

          <div className="mt-8">
            <h2 className="text-xl font-semibold text-[#9C2759] mb-4">
              Comparar precios con otros supermercados
            </h2>
            <div className="grid gap-4">
              {offers.map((offer) => (
                <Card key={offer.store_id} className="p-4">
                  <div className="flex justify-between items-center">
                    <span className="font-medium">{getStoreNameById(offer.store_id)}</span>
                    <div>
                      <span className="font-bold">{offer.price}</span>
                      {offer.oferta && (
                        <span className="ml-2 text-xs bg-red-100 text-red-600 px-2 py-1 rounded">
                          OFERTA
                        </span>
                      )}
                    </div>
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

export default ProductInfo;