export interface Store {
  id: string;
  name: string;
  image: string;
  rating: number;
  reviews: number;
  description: string;
}

export const stores: Store[] = [
  {
    id: "1",
    name: "Walmart - Sucursal Centro",
    image: "https://cdn.britannica.com/16/204716-050-8BB76BE8/Walmart-store-Mountain-View-California.jpg",
    rating: 4.5,
    reviews: 1250,
    description:
      "Walmart Sucursal Centro es tu destino para encontrar todo lo que necesitas. Ofrecemos una amplia variedad de productos frescos, abarrotes, electrónicos y más, todo a precios increíbles. Nuestro compromiso es brindarte la mejor experiencia de compra con atención personalizada y ofertas exclusivas.",
  },
  {
    id: "2",
    name: "Bodega Aurrera",
    image: "https://cdn.britannica.com/16/204716-050-8BB76BE8/Walmart-store-Mountain-View-California.jpg",
    rating: 4.3,
    reviews: 980,
    description:
      "Bodega Aurrera te ofrece productos de calidad a precios bajos todos los días. Desde alimentos hasta artículos para el hogar, encuentra lo que necesitas con grandes ahorros y promociones.",
  },
  {
    id: "3",
    name: "Soriana",
    image: "https://cdn.britannica.com/16/204716-050-8BB76BE8/Walmart-store-Mountain-View-California.jpg",
    rating: 4.2,
    reviews: 1120,
    description:
      "En Soriana, trabajamos para brindarte productos frescos, tecnología, ropa y más. Visítanos para descubrir nuestras ofertas semanales y atención amigable en cada compra.",
  }
];
