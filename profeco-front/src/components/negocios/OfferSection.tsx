
interface Offer {
  id: string;
  title: string;
  description: string;
  validUntil: string;
  discount: number;
}

interface OffersSectionProps {
  offers: Offer[];
}

const OffersSection = ({ offers }: OffersSectionProps) => {
  return (
    <section className="py-8">
      <div className="max-w-7xl mx-auto px-4">
        <h2 className="text-2xl font-bold mb-6 text-[#681837]">Ofertas activas</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {offers.map((offer) => (
            <div
              key={offer.id}
              className="bg-white rounded-lg border border-gray-200 p-4 hover:shadow-lg transition-shadow"
            >
              <div className="flex items-start justify-between">
                <div>
                  <h3 className="font-semibold text-lg">{offer.title}</h3>
                  <p className="text-gray-600 mt-2">{offer.description}</p>
                </div>
                <span className="text-[#681837] font-bold text-xl">
                  -{offer.discount}%
                </span>
              </div>
              <div className="mt-4 text-sm text-gray-500">
                Válido hasta: {offer.validUntil}
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default OffersSection;