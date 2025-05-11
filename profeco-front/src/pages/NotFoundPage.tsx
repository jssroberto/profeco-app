const NotFoundPage = () => {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center text-center text-gray-700">
      <h1 className="text-4xl font-bold mb-4">404</h1>
      <p className="text-lg mb-4">Página no encontrada.</p>
      <a href="/" className="text-blue-600 hover:underline">
        Volver al inicio
      </a>
    </div>
  );
};

export default NotFoundPage;