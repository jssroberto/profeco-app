import { createContext, useContext, useEffect, useState } from 'react';
import axios from 'axios';

interface UserData {
  id: string;
  userId: string;
  email: string;
  name: string;
  preferenceId?: string;
  ratingsIds?: string[];
  inconsistenciesIds?: string[];
  wishesIds?: string[];
  rolesIds?: string[];
}

interface UserContextType {
  user: UserData | null;
  isLoading: boolean;
  error: string | null;
  fetchUserData: () => Promise<void>;
  clearUserData: () => void;
}

const UserContext = createContext<UserContextType | null>(null);

export const UserProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = useState<UserData | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchUserData = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const token = localStorage.getItem('accessToken');
      if (!token) throw new Error('No access token found');

      const response = await axios.get('http://localhost:8080/api/v1/customers/me', {
        headers: { Authorization: `Bearer ${token}` },
      });
      setUser(response.data);
    } catch (err) {
      setError(axios.isAxiosError(err) ? err.message : 'UserCOntext:no se pudo fetchear info');
      console.error('Error:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const clearUserData = () => {
    setUser(null);
  };

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (token) fetchUserData();
  }, []);

  return (
    <UserContext.Provider value={{ user, isLoading, error, fetchUserData, clearUserData }}>
      {children}
    </UserContext.Provider>
  );
};

export const useUser = () => {
  const context = useContext(UserContext);
  if (!context) throw new Error('useUser must be used within UserProvider');
  return context;
};