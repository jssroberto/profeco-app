import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import BusinessHeader from '../../components/negocios/BusinessHeader';
import OffersSection from '../../components/negocios/OfferSection';
import ProductsSection from '../../components/negocios/ProductsSection';
import ReviewSection from '../../components/negocios/ReviewSection';



const Negociosinfo = () => {
  // per dopo
  const { id } = useParams();
  const [isFavorite, setIsFavorite] = useState(false);

  const businessData = {
    name: "Walmart - Sucursal Centro",
    image: "https://cdn.britannica.com/16/204716-050-8BB76BE8/Walmart-store-Mountain-View-California.jpg",
    rating: 4.5,
    totalRatings: 1250,
    description: "Walmart Sucursal Centro es tu destino para encontrar todo lo que necesitas. Ofrecemos una amplia variedad de productos frescos, abarrotes, electrónicos y más, todo a precios increíbles. Nuestro compromiso es brindarte la mejor experiencia de compra con atención personalizada y ofertas exclusivas.",
  };

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

  const products = [
    {
      id: "1",
      name: "Pan Blanco Artesanal",
      image: "https://www.superaki.mx/cdn/shop/files/7501030452553_230224_f1c3e040-4e1e-42b1-b171-cba003079b55.jpg?v=1709054843",
      price: 23.50,
      originalPrice: 29.90,
      category: "Panadería",
    },
    {
      id: "2",
      name: "Leche Entera",
      image: "https://res.cloudinary.com/walmart-labs/image/upload/w_960,dpr_auto,f_auto,q_auto:best/gr/images/product-images/img_large/00750102051534L.jpg",
      price: 25.90,
      category: "Lácteos",
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
      <ProductsSection products={products} />
      <ReviewSection reviews={reviews} />
    </div>
  );
};

export default Negociosinfo;