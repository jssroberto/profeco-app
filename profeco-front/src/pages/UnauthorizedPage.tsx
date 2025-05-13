import { Link } from "react-router-dom";

const UnauthorizedPage = () => {
  return (
    <div className="flex items-center justify-center h-full">
      <div className="text-center">
        <h1 className="text-2xl font-bold mb-4">403 - Acceso no autorizado</h1>
        <p>No tienes permiso para acceder a esta página.</p>
        <Link to="/" className="text-blue-600 mt-4 inline-block">
          Volver al inicio
        </Link>
      </div>
    </div>
  );
};

export default UnauthorizedPage