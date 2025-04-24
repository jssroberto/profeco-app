// this is temporary
export interface Product {
    id: number;
    name: string;
    description: string;
    price: number;
    imageUrl?: string;
  }
  
  export const mockProducts: Product[] = [
    {
      id: 1,
      name: "Product 1",
      description: "Description for product 1",
      price: 19.99,
      imageUrl: "https://via.placeholder.com/300"
    },
    {
      id: 2,
      name: "Product 2",
      description: "Description for product 2",
      price: 29.99,
      imageUrl: "https://via.placeholder.com/300"
    },
  ];