import { createContext, useContext, useState, useEffect } from 'react';

// Helper function to safely decode JWT
const decodeJWT = (token: string) => {
  try {
    // Base64Url decode
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const decodedPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    return JSON.parse(decodedPayload);
  } catch (e) {
    console.error("Invalid token format", e);
    return null;
  }
};

interface AuthContextType {
  isAuthenticated: boolean;
  role: string | null;
  user: {
    id: string;
    email: string;
    name: string;
    userEntityId: string;
  } | null;
  login: (authData: {
    accessToken: string;
    id: string;
    email: string;
    name: string;
    roles: string[];
    userEntityId: string;
  }) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [role, setRole] = useState<string | null>(null);
  const [user, setUser] = useState<AuthContextType['user']>(null);

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    const storedRole = localStorage.getItem('userRole');
    const storedUser = localStorage.getItem('userData');
    
    if (token) {
      setIsAuthenticated(true);
      setRole(storedRole);
      if (storedUser) {
        setUser(JSON.parse(storedUser));
      } else {
        try {
          const payload = decodeJWT(token);
          if (payload?.sub) {
            setUser({
              id: payload.sub,
              email: payload.email || payload.sub,
              name: payload.name || '',
              userEntityId: payload.userEntityId || ''
            });
          }
        } catch (e) {
          console.error("Failed to parse token", e);
        }
      }
    }
  }, []);

  const login = (authData: {
    accessToken: string;
    id: string;
    email: string;
    name: string;
    roles: string[];
    userEntityId: string;
  }) => {
    localStorage.setItem('accessToken', authData.accessToken);
    localStorage.setItem('userRole', authData.roles[0]); 
    localStorage.setItem('userData', JSON.stringify({
      id: authData.id,
      email: authData.email,
      name: authData.name,
      userEntityId: authData.userEntityId
    }));
    
    setIsAuthenticated(true);
    setRole(authData.roles[0]); 
    setUser({
      id: authData.id,
      email: authData.email,
      name: authData.name,
      userEntityId: authData.userEntityId
    });
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('userRole');
    localStorage.removeItem('userData');
    setIsAuthenticated(false);
    setRole(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, role, user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider');
  return context;
};