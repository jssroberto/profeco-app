import React, { useState } from 'react';
import { Star } from 'lucide-react';

interface Review {
  id: string;
  author: string;
  rating: number;
  comment: string;
  date: string;
}

interface ReviewSectionProps {
  reviews: Review[];
}

const ReviewSection = ({ reviews }: ReviewSectionProps) => {
  const [rating, setRating] = useState(0);
  const [comment, setComment] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Handle review submission
    console.log({ rating, comment });
  };

  return (
    <section className="py-8">
      <div className="max-w-7xl mx-auto px-4">
        <h2 className="text-2xl font-bold mb-6 text-[#681837]">Reseñas y calificaciones</h2>
        
        {/* Review Form */}
        <div className="bg-white rounded-lg border border-gray-200 p-6 mb-8">
          <h3 className="text-xl font-semibold mb-4">Deja tu reseña</h3>
          <form onSubmit={handleSubmit}>
            <div className="flex items-center gap-2 mb-4">
              {[1, 2, 3, 4, 5].map((value) => (
                <button
                  key={value}
                  type="button"
                  onClick={() => setRating(value)}
                  className="focus:outline-none"
                >
                  <Star
                    className={`w-8 h-8 ${
                      value <= rating
                        ? 'fill-yellow-400 text-yellow-400'
                        : 'fill-gray-200 text-gray-200'
                    }`}
                  />
                </button>
              ))}
            </div>
            
            <textarea
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              placeholder="Comparte tu experiencia..."
              className="w-full p-3 border border-gray-300 rounded-lg min-h-[120px] mb-4"
              required
            />
            
            <button
              type="submit"
              className="px-6 py-2 bg-[#681837] text-white rounded-lg hover:bg-[#561429] transition-colors"
            >
              Publicar reseña
            </button>
          </form>
        </div>

        {/* Reviews List */}
        <div className="space-y-6">
          {reviews.map((review) => (
            <div
              key={review.id}
              className="bg-white rounded-lg border border-gray-200 p-6"
            >
              <div className="flex justify-between items-start mb-4">
                <div>
                  <h4 className="font-semibold">{review.author}</h4>
                  <div className="flex items-center mt-1">
                    {[...Array(5)].map((_, index) => (
                      <Star
                        key={index}
                        className={`w-4 h-4 ${
                          index < review.rating
                            ? 'fill-yellow-400 text-yellow-400'
                            : 'fill-gray-200 text-gray-200'
                        }`}
                      />
                    ))}
                  </div>
                </div>
                <span className="text-sm text-gray-500">{review.date}</span>
              </div>
              <p className="text-gray-600">{review.comment}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default ReviewSection;