import React, { useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";

const Login: React.FC = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState({ email: "", password: "" });

  const validateForm = () => {
    let isValid = true;
    const newErrors = { email: "", password: "" };

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

    try {
      const response = await axios.post("http://localhost:8080/api/v1/auth/login", {
        email,
        password,
      });
      
      localStorage.setItem("token", response.data.token);

      // navigate to main page!!!
    } catch (error) {
        console.log(error)
    }
  };

  return (
    <div className="flex min-h-screen">
      <div className="w-full md:w-1/2 flex items-center justify-center p-10">
        <div className="w-full max-w-md space-y-6">
          <h1 className="text-3xl font-bold mb-12">Inicio de Sesión</h1>
          <form className="space-y-4" onSubmit={handleSubmit}>
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                Email
              </label>
              <input
                type="email"
                id="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className={`mt-1 w-full border ${
                  errors.email ? "border-red-500" : "border-gray-300"
                } rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500`}
              />
              {errors.email && (
                <p className="mt-1 text-sm text-red-600">{errors.email}</p>
              )}
            </div>

            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                Contraseña
              </label>
              <input
                type="password"
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
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
              className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition cursor-pointer"
            >
              Iniciar sesión
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