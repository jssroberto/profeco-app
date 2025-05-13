import { createContext, useContext, useState, useEffect } from 'react';

interface UserData {
  id: string;
  email: string;
  name: string;
  userEntityId: string;
}

interface AuthContextType {
  isAuthenticated: boolean;
  role: string | null;
  user: UserData | null;
  isLoading: boolean;
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
  const [state, setState] = useState<{
    isAuthenticated: boolean;
    role: string | null;
    user: UserData | null;
    isLoading: boolean;
  }>({
    isAuthenticated: false,
    role: null,
    user: null,
    isLoading: true
  });

  useEffect(() => {
    const initializeAuth = () => {
      const token = localStorage.getItem('accessToken');
      const storedRole = localStorage.getItem('userRole');
      const storedUser = localStorage.getItem('userData');

      if (token) {
        try {
          // Simple token check (you can add JWT decoding if needed)
          setState({
            isAuthenticated: true,
            role: storedRole,
            user: storedUser ? JSON.parse(storedUser) : null,
            isLoading: false
          });
        } catch (e) {
          console.error("Auth initialization failed", e);
          logout();
        }
      } else {
        setState(prev => ({
          ...prev,
          isLoading: false
        }));
      }
    };

    initializeAuth();
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

    setState({
      isAuthenticated: true,
      role: authData.roles[0],
      user: {
        id: authData.id,
        email: authData.email,
        name: authData.name,
        userEntityId: authData.userEntityId
      },
      isLoading: false
    });
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('userRole');
    localStorage.removeItem('userData');
    setState({
      isAuthenticated: false,
      role: null,
      user: null,
      isLoading: false
    });
  };

  return (
    <AuthContext.Provider value={{
      ...state,
      login,
      logout
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider');
  return context;
};