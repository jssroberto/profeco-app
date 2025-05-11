export interface Product {
  id?: string;
  imageUrl: string;
  name: string;
  brand: string;
  category: string;
  offers: StoreOffer[];
}

export interface StoreOffer {
  store_id: string;
  price: string;
  originalPrice?: string;
  oferta?: boolean;
}

export const mockProducts: Product[] = [
  {
    id: "1",
    imageUrl: "https://res.cloudinary.com/walmart-labs/image/upload/w_960,dpr_auto,f_auto,q_auto:best/gr/images/product-images/img_large/00750102051534L.jpg",
    name: "Leche entera 1L",
    brand: "LALA",
    category: "Lácteos",
    offers: [
      { store_id: "1", price: "$24.50", originalPrice: "$26.90", oferta: true },
      { store_id: "2", price: "$25.50" },
      { store_id: "3", price: "$27.00" }
    ],
  },
  {
    id: "2",
    imageUrl: "https://th.bing.com/th/id/OIP.up8KBujuqSLit640tdfyaQHaHa?rs=1&pid=ImgDetMain",
    name: "Queso crema 400g",
    brand: "Philadelphia",
    category: "Lácteos",
    offers: [
      { store_id: "1", price: "$35.50", originalPrice: "$39.90", oferta: true },
      { store_id: "2", price: "$36.50" },
      { store_id: "3", price: "$38.00" }
    ],
  },
  {
    id: "3",
    imageUrl: "https://www.superaki.mx/cdn/shop/files/7501030452553_230224_f1c3e040-4e1e-42b1-b171-cba003079b55.jpg?v=1709054843",
    name: "Pan Blanco Artesanal Bimbo",
    brand: "BIMBO",
    category: "Panadería",
    offers: [
      { store_id: "1", price: "$50.00", originalPrice: "$65.00", oferta: true },
      { store_id: "2", price: "$65.50" },
      { store_id: "3", price: "$55.00" }
    ],
  }
];