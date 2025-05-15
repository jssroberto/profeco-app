// contexts/StoreAdminContext.tsx
import { createContext, useContext, useEffect, useState } from 'react';
import axios from 'axios';
import { useAuth } from './AuthContext';

interface StoreAdminData {
  id: string;
  name: string;
  email: string;
  storeId: string;
}

interface StoreData {
  id: string;
  name: string;
  location: string;
  // other store fields
}

interface StoreAdminContextType {
  admin: StoreAdminData | null;
  store: StoreData | null;
  loading: boolean;
  refresh: () => Promise<void>;
}

const StoreAdminContext = createContext<StoreAdminContextType | null>(null);

export const StoreAdminProvider = ({ children }: { children: React.ReactNode }) => {
  const { token } = useAuth();
  const [admin, setAdmin] = useState<StoreAdminData | null>(null);
  const [store, setStore] = useState<StoreData | null>(null);
  const [loading, setLoading] = useState(true);

  const fetchData = async () => {
    if (!token) return;
    
    setLoading(true);
    try {
      // Fetch admin data
      const adminRes = await axios.get('http://localhost:8080/api/v1/store-admins/me', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setAdmin(adminRes.data);

      // Fetch store data
      const storeRes = await axios.get(`http://localhost:8080/api/v1/stores/${adminRes.data.storeId}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setStore(storeRes.data);
    } catch (error) {
      setAdmin(null);
      setStore(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [token]);

  return (
    <StoreAdminContext.Provider value={{ admin, store, loading, refresh: fetchData }}>
      {children}
    </StoreAdminContext.Provider>
  );
};

export const useStoreAdmin = () => {
  const context = useContext(StoreAdminContext);
  if (!context) throw new Error('useStoreAdmin must be used within StoreAdminProvider');
  return context;
};