import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { useNavigate } from "react-router"

const Register: React.FC = () => {

  let navigate = useNavigate()
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    name: '',
    role: 'customer' 
  });
  const [errors, setErrors] = useState({
    email: '',
    password: '',
    name: '',
    role: ''
  });

  const validateForm = () => {

    let isValid = true;
    const newErrors = { email: '', password: '', name: '', role: '' };

    if (!formData.email) {
      newErrors.email = 'Email is required';
      isValid = false;
    } else if (!/^\S+@\S+\.\S+$/.test(formData.email)) {
      newErrors.email = 'Email is invalid';
      isValid = false;
    }

    if (!formData.password) {
      newErrors.password = 'Password is required';
      isValid = false;
    } else if (formData.password.length < 6) {
      newErrors.password = 'Password must be at least 6 characters';
      isValid = false;
    }

    if (!formData.name) {
      newErrors.name = 'Name is required';
      isValid = false;
    }

    setErrors(newErrors);
    return isValid;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validateForm()) return;

    try {
      const endpoint = formData.role === 'customer' 
        ? 'http://localhost:8080/api/v1/auth/register/customer' 
        : 'http://localhost:8080/api/v1/auth/register/market_admin';

      const response = await axios.post(endpoint, {
        email: formData.email,
        password: formData.password,
        name: formData.name
      });

      console.log('Registration successful:', response.data);

      // now redirect the user to the home with the access token hehe
      localStorage.setItem('accessToken', response.data.accessToken);

      navigate('/')
    } catch (error) {
      console.error('Registration failed:', error);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  return (
    <div className="flex min-h-screen">
      <div className="w-full md:w-1/2 flex items-center justify-center p-10">
        <div className="w-full max-w-md space-y-6">
          <h1 className="text-3xl font-bold mb-6">Create Account</h1>
          
          <div className="flex border border-gray-300 rounded-lg overflow-hidden mb-6">
            <button
              type="button"
              className={`flex-1 py-2 px-4 text-center font-medium cursor-pointer ${
                formData.role === 'customer' 
                  ? 'bg-blue-600 text-white' 
                  : 'bg-gray-100 hover:bg-gray-200'
              }`}
              onClick={() => setFormData(prev => ({ ...prev, role: 'customer' }))}
            >
              Soy un consumidor
            </button>
            <button
              type="button"
              className={`flex-1 py-2 px-4 text-center font-medium cursor-pointer ${
                formData.role === 'market_admin' 
                  ? 'bg-blue-600 text-white' 
                  : 'bg-gray-100 hover:bg-gray-200'
              }`}
              onClick={() => setFormData(prev => ({ ...prev, role: 'market_admin' }))}
            >
              Soy un supermercado
            </button>
          </div>

          <form className="space-y-4" onSubmit={handleSubmit}>
            {/* Name Field */}
            <div>
              <label htmlFor="name" className="block text-sm font-medium text-gray-700">
                Full Name
              </label>
              <input
                type="text"
                id="name"
                name="name"
                value={formData.name}
                onChange={handleChange}
                className={`mt-1 w-full border ${
                  errors.name ? 'border-red-500' : 'border-gray-300'
                } rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500`}
                placeholder="John Doe"
              />
              {errors.name && <p className="mt-1 text-sm text-red-600">{errors.name}</p>}
            </div>

            {/* Email Field */}
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                Email
              </label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                className={`mt-1 w-full border ${
                  errors.email ? 'border-red-500' : 'border-gray-300'
                } rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500`}
                placeholder="your@email.com"
              />
              {errors.email && <p className="mt-1 text-sm text-red-600">{errors.email}</p>}
            </div>

            {/* Password Field */}
            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                Contraseña
              </label>
              <input
                type="password"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                className={`mt-1 w-full border ${
                  errors.password ? 'border-red-500' : 'border-gray-300'
                } rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500`}
                placeholder="••••••••"
              />
              {errors.password && <p className="mt-1 text-sm text-red-600">{errors.password}</p>}
            </div>

            <button
              type="submit"
              className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition cursor-pointer"
            >
              Registrarme!
            </button>
          </form>

          <p className="text-sm text-gray-600 text-center">
            Ya tienes una cuenta?{' '}
            <Link to="/login" className="text-blue-600 hover:underline">
              Inicia sesión
            </Link>
          </p>
        </div>
      </div>

      <div className="hidden md:block md:w-1/2">
        <img
          src="/imgs/dih.jpg"
          alt="Register visual"
          className="w-full h-full object-cover"
        />
      </div>
    </div>
  );
};

export default Register;