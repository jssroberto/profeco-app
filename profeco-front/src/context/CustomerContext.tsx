import { createContext, useContext, useEffect, useState } from 'react';
import axios from 'axios';
import { useAuth } from './AuthContext';

interface CustomerData {
  id: string;
  name: string;
  email: string;
  userId: string;
  preferenceId: string;
  ratingsIds: string[];
  inconsistenciesIds: string[];
  wishesIds: string[];
  rolesIds: string[];
}

interface CustomerContextType {
  customer: CustomerData | null;
  loading: boolean;
  refresh: () => Promise<void>;
}

const CustomerContext = createContext<CustomerContextType | null>(null);

export const CustomerProvider = ({ children }: { children: React.ReactNode }) => {
  const { token } = useAuth();
  const [customer, setCustomer] = useState<CustomerData | null>(null);
  const [loading, setLoading] = useState(true);

  const fetchCustomer = async () => {
    if (!token) return;
    
    setLoading(true);
    try {
      const response = await axios.get('http://localhost:8080/api/v1/customers/me', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setCustomer(response.data);
    } catch (error) {
      setCustomer(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCustomer();
  }, [token]);

  return (
    <CustomerContext.Provider value={{ customer, loading, refresh: fetchCustomer }}>
      {children}
    </CustomerContext.Provider>
  );
};

export const useCustomer = () => {
  const context = useContext(CustomerContext);
  if (!context) throw new Error('useCustomer must be used within CustomerProvider');
  return context;
};