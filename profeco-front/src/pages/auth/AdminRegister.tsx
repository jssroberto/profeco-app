import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';

const AdminRegister: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { adminType, verificationCode, roleId } = location.state || {};
  
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    name: '',
    code: verificationCode || '',
    roleId: roleId || ''
  });
  
  const [errors, setErrors] = useState({
    email: '',
    password: '',
    name: ''
  });
  const [isLoading, setIsLoading] = useState(false);

  // Redirect if missing verification data
  if (!adminType || !verificationCode || !roleId) {
    navigate('/register/code-verification');
    return null;
  }

  const validateForm = () => {
    let isValid = true;
    const newErrors = { email: '', password: '', name: '' };

    if (!formData.email.trim()) {
      newErrors.email = 'Email is required';
      isValid = false;
    } else if (!/^\S+@\S+\.\S+$/.test(formData.email)) {
      newErrors.email = 'Email is invalid';
      isValid = false;
    }

    if (!formData.password) {
      newErrors.password = 'Password is required';
      isValid = false;
    } else if (formData.password.length < 8) {
      newErrors.password = 'Password must be at least 8 characters';
      isValid = false;
    }

    if (!formData.name.trim()) {
      newErrors.name = 'Name is required';
      isValid = false;
    }

    setErrors(newErrors);
    return isValid;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validateForm()) return;

    setIsLoading(true);

    try {
      const endpoint = 'http://localhost:8080/api/v1/auth/register/admin';
      
      const requestData = {
        email: formData.email,
        password: formData.password,
        name: formData.name,
        invitationCode: formData.code,
        roleId: formData.roleId
      };

      const response = await axios.post(endpoint, requestData);

      console.log('Admin registration successful:', response.data);
      localStorage.setItem('accessToken', response.data.accessToken);
      navigate('/');
    } catch (error) {
      console.error('Admin registration failed:', error);
      alert('Registration failed. Please try again.');
    } finally {
      setIsLoading(false);
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
          <h1 className="text-3xl font-bold mb-6">
            {adminType === 'profeco_admin' ? 'PROFECO Admin' : 'Market Admin'} Registration
          </h1>
          
          <div className="p-4 bg-blue-50 rounded-lg mb-4">
            <p className="text-blue-700 font-medium">
              Registering as {adminType === 'profeco_admin' ? 'PROFECO Administrator' : 'Market Administrator'}
            </p>
            <p className="text-sm text-blue-600 mt-1">
              Invitation code: {verificationCode}
            </p>
          </div>

          <form className="space-y-4" onSubmit={handleSubmit}>
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

            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                Password
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
              <p className="mt-1 text-sm text-gray-500">
                Must be at least 8 characters
              </p>
            </div>

            <button
              type="submit"
              disabled={isLoading}
              className={`w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition cursor-pointer ${
                isLoading ? 'opacity-70 cursor-not-allowed' : ''
              }`}
            >
              {isLoading ? 'Registering...' : 'Complete Registration'}
            </button>
          </form>

          <button
            onClick={() => navigate('/register/code-verification')}
            className="w-full bg-gray-200 text-gray-800 py-2 rounded-lg hover:bg-gray-300 transition cursor-pointer"
          >
            Back to Verification
          </button>
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

export default AdminRegister;