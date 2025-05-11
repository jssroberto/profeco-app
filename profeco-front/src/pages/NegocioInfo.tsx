import { useState } from 'react';
import { useParams } from 'react-router-dom';
import BusinessHeader from '../components/negocios/BusinessHeader';
import ProductsSection from '../components/negocios/ProductsSection';
import OffersSection from '../components/negocios/OfferSection';
import ReviewSection from '../components/negocios/ReviewSection';
import { stores } from '../data/stores';
import { mockProducts } from '../data/products';

const normalize = (str: string) =>
  str.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "");

const Negociosinfo = () => {
  const { id } = useParams();
  const [isFavorite, setIsFavorite] = useState(false);

  const businessData = stores.find(store =>
    normalize(store.id).replace(/\s+/g, "-") === id
  );

  if (!businessData) {
    return <div className="p-6 text-center text-red-600">Negocio no encontrado.</div>;
  }

  const filteredProducts = mockProducts.filter((product) =>
    product.offers.some((offer) => offer.store_id === businessData.id)
  );

  const offers = [
    {
      id: "1",
      title: "Descuento en Lácteos",
      description: "Todos los productos lácteos con 20% de descuento",
      validUntil: "2024-05-01",
      discount: 20,
    },
    {
      id: "2",
      title: "2x1 en Frutas",
      description: "Lleva dos kilos de frutas seleccionadas y paga uno",
      validUntil: "2024-04-30",
      discount: 50,
    },
  ];

  const reviews = [
    {
      id: "1",
      author: "María González",
      rating: 5,
      comment: "Excelente servicio y buenos precios. El personal siempre es muy amable.",
      date: "2024-04-20",
    },
    {
      id: "2",
      author: "Juan Pérez",
      rating: 4,
      comment: "Buenos productos y ofertas interesantes. El estacionamiento podría mejorar.",
      date: "2024-04-15",
    },
  ];

  return (
    <div className="min-h-screen bg-white">
      <BusinessHeader
        {...businessData}
        isFavorite={isFavorite}
        onToggleFavorite={() => setIsFavorite(!isFavorite)}
      />
      <OffersSection offers={offers} />
      <ProductsSection products={filteredProducts} storeId={businessData.id}/>
      <ReviewSection reviews={reviews} />
    </div>
  );
};

export default Negociosinfo;