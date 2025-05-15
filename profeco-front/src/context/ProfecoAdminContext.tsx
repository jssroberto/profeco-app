import React, { createContext, useContext, useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "./AuthContext";

interface ProfecoAdmin {
  id: string;
  userId: string;
  email: string;
  name: string;
}

interface ProfecoAdminContextType {
  admin: ProfecoAdmin | null;
  isLoading: boolean;
  error: string | null;
  refresh: () => void;
}

const ProfecoAdminContext = createContext<ProfecoAdminContextType | undefined>(undefined);

export const ProfecoAdminProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { token } = useAuth();
  const [admin, setAdmin] = useState<ProfecoAdmin | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchAdmin = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const res = await axios.get("http://localhost:8080/api/v1/profeco-admins/me", {
        headers: { Authorization: `Bearer ${token}` }
      });
      setAdmin(res.data);
    } catch (err: any) {
      setError("No se pudo cargar el administrador.");
      setAdmin(null);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (token) fetchAdmin();
    else setIsLoading(false);
  }, [token]);

  return (
    <ProfecoAdminContext.Provider value={{ admin, isLoading, error, refresh: fetchAdmin }}>
      {children}
    </ProfecoAdminContext.Provider>
  );
};

export const useProfecoAdmin = () => {
  const ctx = useContext(ProfecoAdminContext);
  if (!ctx) throw new Error("useProfecoAdmin must be used within ProfecoAdminProvider");
  return ctx;
};