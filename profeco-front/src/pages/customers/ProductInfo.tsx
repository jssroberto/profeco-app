import { Heart } from "lucide-react";
import { Link } from "react-router-dom";
import Button from "../../components/ui/Button";
import Card from "../../components/ui/Card";

const ProductInfo = () => {
  return (
    <div className="max-w-6xl mx-auto p-4 mt-24">
      <div className="grid md:grid-cols-2 gap-8">
        <div className="relative">
          <button className="absolute top-4 left-4 bg-white p-2 rounded-full shadow-md cursor-pointer">
            <Heart className="w-6 h-6 text-gray-600" />
          </button>
          <img
            src="https://www.superaki.mx/cdn/shop/files/7501030452553_230224_f1c3e040-4e1e-42b1-b171-cba003079b55.jpg?v=1709054843"
            alt="Pan Blanco Artesanal Bimbo"
            className="w-full object-contain"
          />
        </div>

        <div className="space-y-6">
          <div>
            <div className="inline-block bg-green-100 text-green-800 px-3 py-1 rounded-md text-sm">
              Oferta
            </div>
            <h1 className="text-2xl font-semibold mt-2">
              Pan Blanco Artesanal Bimbo
            </h1>
            <div className="flex items-baseline gap-2 mt-4">
              <span className="text-4xl font-bold">$50</span>
            </div>
            <div className="flex gap-4 text-gray-600 mt-4 text-sm">
              <span>Marca: BIMBO</span>
              <span>Vendido por: Walmart</span>
            </div>
          </div>

          <div className="space-y-2">
            <Button>Agregar a WishList</Button>
            <Link to={`/productos/123/reportar`}>
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
              <Card className="p-4">
                <div className="flex justify-between items-center">
                  <span className="font-medium">Walmart Súper</span>
                  <div>
                    <span className="font-bold">$50.00</span>
                    <span className="ml-2 text-xs bg-red-100 text-red-600 px-2 py-1 rounded">
                      OFERTA
                    </span>
                  </div>
                </div>
              </Card>
              <Card className="p-4">
                <div className="flex justify-between items-center">
                  <span className="font-medium">Bodega Aurrera</span>
                  <span className="font-bold">$65.50</span>
                </div>
              </Card>
              <Card className="p-4">
                <div className="flex justify-between items-center">
                  <span className="font-medium">Soriana</span>
                  <span className="font-bold">$55</span>
                </div>
              </Card>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductInfo;
