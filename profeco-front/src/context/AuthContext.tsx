import { createContext, useContext, useState, useEffect } from 'react';

interface AuthContextType {
  isAuthenticated: boolean;
  role: string | null;
  login: (token: string, role?: string) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [role, setRole] = useState<string | null>(null);

  // Initialize auth state from localStorage on mount
  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    const storedRole = localStorage.getItem('userRole');
    
    if (token) {
      setIsAuthenticated(true);
      setRole(storedRole);
      
      // If you need to extract role from token (JWT) when page refreshes:
      // try {
      //   const payload = JSON.parse(atob(token.split('.')[1]));
      //   setRole(payload.role || storedRole);
      // } catch (e) {
      //   console.error("Failed to parse token", e);
      // }
    }
  }, []);

  const login = (token: string, role?: string) => {
    localStorage.setItem('accessToken', token);
    setIsAuthenticated(true);
    
    if (role) {
      localStorage.setItem('userRole', role);
      setRole(role);
    } else {
      // Extract role from token if not provided
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const tokenRole = payload.role;
        if (tokenRole) {
          localStorage.setItem('userRole', tokenRole);
          setRole(tokenRole);
        }
      } catch (e) {
        console.error("Failed to extract role from token", e);
      }
    }
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('userRole');
    setIsAuthenticated(false);
    setRole(null);
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, role, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider');
  return context;
};