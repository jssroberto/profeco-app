import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import { useAuth } from "../../context/AuthContext";

const Login: React.FC = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState({
    email: "",
    password: "",
    form: "", // Added form-level error
  });
  const [isSubmitting, setIsSubmitting] = useState(false); // Loading state
  const { login } = useAuth();
  const navigate = useNavigate();

  const validateForm = () => {
    let isValid = true;
    const newErrors = { email: "", password: "", form: "" };

    if (!email) {
      newErrors.email = "El email es requerido";
      isValid = false;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      newErrors.email = "Email inválido";
      isValid = false;
    }

    if (!password) {
      newErrors.password = "La contraseña es requerida";
      isValid = false;
    } else if (password.length < 6) {
      newErrors.password = "Mínimo 6 caracteres";
      isValid = false;
    }

    setErrors(newErrors);
    return isValid;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validateForm()) return;

    setIsSubmitting(true);
    setErrors((prev) => ({ ...prev, form: "" }));

    try {
      const response = await axios.post(
        "http://localhost:8080/api/v1/auth/login",
        { email, password },
        { validateStatus: (status) => status < 500 }
      );

      // debug section lol
      const token = response.data.accessToken;
      const payload = JSON.parse(atob(token.split(".")[1]));
      console.log("JWT Payload:", payload);

      if (response.status === 200) {
        login(token);
        navigate("/");
      } else {
        if (response.status === 403) {
          setErrors((prev) => ({
            ...prev,
            form: "Email o contraseña incorrectos",
            password: " ",
          }));
        } else if (response.status === 404) {
          setErrors((prev) => ({
            ...prev,
            form: "Usuario no encontrado",
            email: " ",
          }));
        } else {
          setErrors((prev) => ({
            ...prev,
            form: `Error al iniciar sesión: ${
              response.data?.message || "Intente nuevamente"
            }`,
          }));
        }
      }
    } catch (error) {
      setErrors((prev) => ({
        ...prev,
        form: "Error de conexión. Intente nuevamente más tarde",
      }));
      console.error("Login error:", error);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="flex min-h-screen">
      <div className="w-full md:w-1/2 flex items-center justify-center p-10">
        <div className="w-full max-w-md space-y-6">
          <h1 className="text-3xl font-bold mb-12">Inicio de Sesión</h1>

          {/* Form-level error message */}
          {errors.form && (
            <div className="p-3 bg-red-50 text-red-600 rounded-lg text-sm">
              {errors.form}
            </div>
          )}

          <form className="space-y-4" onSubmit={handleSubmit}>
            <div>
              <label
                htmlFor="email"
                className="block text-sm font-medium text-gray-700"
              >
                Email
              </label>
              <input
                type="email"
                id="email"
                value={email}
                onChange={(e) => {
                  setEmail(e.target.value);
                  setErrors((prev) => ({ ...prev, email: "", form: "" }));
                }}
                className={`mt-1 w-full border ${
                  errors.email ? "border-red-500" : "border-gray-300"
                } rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500`}
              />
              {errors.email && (
                <p className="mt-1 text-sm text-red-600">{errors.email}</p>
              )}
            </div>

            <div>
              <label
                htmlFor="password"
                className="block text-sm font-medium text-gray-700"
              >
                Contraseña
              </label>
              <input
                type="password"
                id="password"
                value={password}
                onChange={(e) => {
                  setPassword(e.target.value);
                  setErrors((prev) => ({ ...prev, password: "", form: "" }));
                }}
                className={`mt-1 w-full border ${
                  errors.password ? "border-red-500" : "border-gray-300"
                } rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500`}
              />
              {errors.password && (
                <p className="mt-1 text-sm text-red-600">{errors.password}</p>
              )}
            </div>

            <button
              type="submit"
              disabled={isSubmitting}
              className={`w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition ${
                isSubmitting
                  ? "opacity-70 cursor-not-allowed"
                  : "cursor-pointer"
              }`}
            >
              {isSubmitting ? "Iniciando sesión..." : "Iniciar sesión"}
            </button>
          </form>

          <p className="text-sm text-gray-600 text-center">
            No tienes una cuenta?{" "}
            <Link to="/register" className="text-blue-600 hover:underline">
              Registrarme
            </Link>
          </p>
        </div>
      </div>

      <div className="hidden md:block md:w-1/2">
        <img
          src="/imgs/dih.jpg"
          alt="Login visual"
          className="w-full h-full object-cover"
        />
      </div>
    </div>
  );
};

export default Login;
