import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import BusinessHeader from "../../components/negocios/BusinessHeader";
import OffersSection from "../../components/negocios/OfferSection";
import ProductsSection from "../../components/negocios/ProductsSection";
import ReviewSection from "../../components/negocios/ReviewSection";
import { useAuth } from "../../context/AuthContext";
import StoreWish from "../../components/negocios/StoreWish";

const Negociosinfo = () => {
  const { id } = useParams();
  const [isFavorite, setIsFavorite] = useState(false);
  const [businessData, setBusinessData] = useState<any>(null);
  const [offers, setOffers] = useState<any[]>([]);
  const [products, setProducts] = useState<any[]>([]);
  const [reviews, setReviews] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  // token
  const { token } = useAuth();

  const fetchBusinessData = async () => {
    try {
      //  carga de datos del super
      const { data } = await axios.get(
        `http://localhost:8080/api/v1/stores/${id}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      let rating = 0;
      try {
        const ratingRes = await axios.get(
          `http://localhost:8080/api/v1/ratings/store/${id}/average-score`
        );

        if (ratingRes) {
          rating = ratingRes.data;
        }
      } catch {
        rating = 0;
      }

      setBusinessData({
        name: data.name,
        image: data.imageUrl.startsWith("http")
          ? data.imageUrl
          : `http://${data.imageUrl}`,
        rating,
        totalRatings: Array.isArray(data.ratingsIds)
          ? data.ratingsIds.length
          : 0,
        description: `Ubicación: ${data.location}`,
      });

      // carga de productos SIN OFERTA
      const { data: storeProducts } = await axios.get(
        `http://localhost:8080/api/v1/store-products/by-store/${id}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      const productsWithInfo = await Promise.all(
        storeProducts.map(async (sp: any) => {
          try {
            const { data: product } = await axios.get(
              `http://localhost:8080/api/v1/products/${sp.productId}`,
              {
                headers: {
                  Authorization: `Bearer ${token}`,
                },
              }
            );
            
            return {
              id: sp.productId,
              name: product.name || "Producto sin nombre",
              storeProductId: sp.id,
              image: product.imageUrl
                ? product.imageUrl.startsWith("http")
                  ? product.imageUrl
                  : `http://${product.imageUrl}`
                : "",
              price: sp.price,
            };
          } catch {
            return null;
          }
        })
      );
      setProducts(productsWithInfo.filter(Boolean));

      // carga de productos CON OFERTA
      const { data: offerProducts } = await axios.get(
        `http://localhost:8080/api/v1/store-product-offers/active-any`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      const filteredOfferProducts = offerProducts.filter(
        (offer: any) => offer.storeProduct.storeId === id
      );

      const offersWithInfo = await Promise.all(
        filteredOfferProducts.map(async (offer: any) => {
          try {
            const { data: product } = await axios.get(
              `http://localhost:8080/api/v1/products/${offer.storeProduct.productId}`,
              {
                headers: {
                  Authorization: `Bearer ${token}`,
                },
              }
            );
            return {
              id: offer.storeProduct.productId,
              title: product.name || "Producto sin nombre",
              description: `Precio original: $${offer.storeProduct.price} - Ahora: $${offer.offerPrice}`,
              validUntil: offer.offerEndDate,
              discount:
                offer.offerPrice && offer.storeProduct.price
                  ? Math.round(
                      100 - (offer.offerPrice / offer.storeProduct.price) * 100
                    )
                  : 0,
            };
          } catch {
            return null;
          }
        })
      );

      setOffers(offersWithInfo.filter(Boolean));

      //console.log("Productos cargados:", productsWithInfo.filter(Boolean)); // para pruebassss

      const { data: ratings } = await axios.get(
        `http://localhost:8080/api/v1/ratings/store/${id}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      const reviewsWithInfo = ratings.map((r: any) => ({
        id: r.id,
        score:
          r.score > 5
            ? Math.round((r.score / 2147483647) * 5 * 10) / 10
            : r.score,
        comment: r.comment,
        date: r.date,
        customerName: r.customerName,
      }));
      setReviews(reviewsWithInfo);

      const { data: favorites } = await axios.get(
        "http://localhost:8080/api/v1/preferences/favorite-stores",
        { headers: { Authorization: `Bearer ${token}` } }
      );

      const favoriteStoreId = favorites.favoriteStoreIds[0];

      if(favoriteStoreId == id) {
        setIsFavorite(true);
      } else {
        setIsFavorite(false);
      }
    } catch (error) {
      setBusinessData(null);
    } finally {
      setLoading(false);
    }
  };

  // metodo para marcar store como fav
  const handleToggleFavorite = async () => {
  try {
    if (isFavorite) {
      await axios.delete(
        `http://localhost:8080/api/v1/preferences/favorite-stores/${id}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setIsFavorite(false);
    } else {
      await axios.post(
        `http://localhost:8080/api/v1/preferences/favorite-stores/${id}`,
        {},
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setIsFavorite(true);
    }
  } catch (e) {
    console.log(e);
  }
};



  useEffect(() => {
    fetchBusinessData();
  }, [id]);

  if (loading) return <div className="p-4">Cargando información...</div>;
  if (!businessData)
    return <div className="p-4">No se encontró el supermercado.</div>;

  return (
    <div className="min-h-screen bg-white">
      <BusinessHeader
        {...businessData}
        isFavorite={isFavorite}
        onToggleFavorite={handleToggleFavorite}
      />
      <OffersSection offers={offers} />
      <ProductsSection products={products} storeId={id || ""}/>
      <ReviewSection
        reviews={reviews}
        storeId={id || ""}
        onReviewAdded={fetchBusinessData}
      />
      <StoreWish storeId={id || ""} />
    </div>
  );
};

export default Negociosinfo;
