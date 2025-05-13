import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const AdminVerification: React.FC = () => {
  const navigate = useNavigate();
  const [code, setCode] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleVerify = async () => {
    if (!code.trim()) {
      setError('Verification code is required');
      return;
    }

    setIsLoading(true);
    setError('');

    try {
      const response = await axios.get(
        `http://localhost:8080/api/v1/invitation-codes/check/${code}`
      );

      // Navigate to the appropriate registration form based on role
      if (response.data.roleName === 'PROFECO_ADMIN') {
        navigate('/register/profeco-admin', { 
          state: { 
            invitationCode: response.data.code
          } 
        });
      } else {
        navigate('/register/store-admin', { 
          state: { 
            invitationCode: response.data.code,
            roleId: response.data.roleId // 
          } 
        });
      }
    } catch (err) {
      setError('El codigo no es valido');
      console.error('Verification failed:', err);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="flex min-h-screen">
      <div className="w-full md:w-1/2 flex items-center justify-center p-10">
        <div className="w-full max-w-md space-y-6">
          <h1 className="text-3xl font-bold mb-6">Verifiación de administrador </h1>
          
          <div className="space-y-4">
            <div>
              <label htmlFor="code" className="block text-sm font-medium text-gray-700">
                Código de Invitación
              </label>
              <input
                type="text"
                id="code"
                value={code}
                onChange={(e) => {
                  setCode(e.target.value);
                  setError('');
                }}
                className={`mt-1 w-full border ${
                  error ? 'border-red-500' : 'border-gray-300'
                } rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500`}
                placeholder="Ingresa el código"
              />
              {error && <p className="mt-1 text-sm text-red-600">{error}</p>}
            </div>

            <button
              onClick={handleVerify}
              disabled={isLoading}
              className={`w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition ${
                isLoading ? 'opacity-70 cursor-not-allowed' : 'cursor-pointer'
              }`}
            >
              {isLoading ? 'Verificando...' : 'Verificar código'}
            </button>

            <button
              onClick={() => navigate('/register')}
              className="w-full bg-gray-200 text-gray-800 py-2 rounded-lg hover:bg-gray-300 transition cursor-pointer"
            >
              Regresar
            </button>
          </div>
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

export default AdminVerification;