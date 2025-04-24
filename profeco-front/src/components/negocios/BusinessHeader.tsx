import { Star } from 'lucide-react';

interface BusinessHeaderProps {
  name: string;
  image: string;
  rating: number;
  totalRatings: number;
  description: string;
  isFavorite: boolean;
  onToggleFavorite: () => void;
}

const BusinessHeader = ({
  name,
  image,
  rating,
  totalRatings,
  description,
  isFavorite,
  onToggleFavorite
}: BusinessHeaderProps) => {
  return (
    <div className="w-full">
      <div className="w-full h-[300px] relative">
        <img
          src={image}
          alt={name}
          className="w-full h-full object-cover"
        />
      </div>
      
      <div className="max-w-7xl mx-auto px-4 py-6">
        <div className="flex justify-between items-start">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">{name}</h1>
            <div className="flex items-center mt-2 gap-2">
              <div className="flex items-center">
                {[...Array(5)].map((_, index) => (
                  <Star
                    key={index}
                    className={`w-5 h-5 ${
                      index < Math.round(rating)
                        ? 'fill-yellow-400 text-yellow-400'
                        : 'fill-gray-200 text-gray-200'
                    }`}
                  />
                ))}
              </div>
              <span className="text-gray-600">
                {rating.toFixed(1)} ({totalRatings} calificaciones)
              </span>
            </div>
          </div>
          
          <button
            onClick={onToggleFavorite}
            className={`px-4 py-2 rounded-lg cursor-pointer ${
              isFavorite
                ? 'bg-[#681837] text-white'
                : 'border border-[#681837] text-[#681837]'
            } transition-colors`}
          >
            {isFavorite ? 'Guardado como favorito' : 'Guardar como favorito'}
          </button>
        </div>
        
        <p className="mt-4 text-gray-600 max-w-3xl">
          {description}
        </p>
      </div>
    </div>
  );
};

export default BusinessHeader;