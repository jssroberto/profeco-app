import "./App.css";
import { BrowserRouter as Router, Routes, Route, Outlet } from "react-router-dom";
import Navbar from "./components/Navbar";
import { AuthProvider } from "./context/AuthContext";
import Register from "./pages/auth/Register";
import Login from "./pages/auth/Login";
import AdminVerification from "./pages/auth/AdminVerification";
import StoreAdminRegister from "./pages/auth/StoreAdminRegister";
import ProfecoAdminRegister from "./pages/auth/ProfecoAdminRegister";
import UnauthorizedPage from "./pages/UnauthorizedPage";
import HomePage from "./pages/customers/HomePage";
import Productos from "./pages/customers/Productos";
import BaseProductInfo from "./pages/customers/BaseProductInfo";
import ReportInconsistency from "./pages/customers/ReportInconsistency";
import Negocios from "./pages/customers/Negocios";
import Negociosinfo from "./pages/customers/NegocioInfo"; 

import { AdminInconsistencias } from "./pages/storeAdmins/AdminInconsistencias";
import { AdminComentarios } from "./pages/storeAdmins/AdminComentarios";
import StoreAdminDashboard from "./pages/storeAdmins/StoreAdminDashboard";
import { StoreAdminProvider } from "./context/StoreAdminContext";
import { CustomerProvider } from "./context/CustomerContext";
import { RoleProtectedRoute } from "./pages/auth/RoleProtectedRoute";
import { ProfecoAdminProvider } from "./context/ProfecoAdminContext";

import ProfecoDashboard from "./pages/profecoAdmins/ProfecoDashboard";
import Reportes from "./pages/profecoAdmins/Reportes";
import AdminPrecios from "./pages/storeAdmins/AdminPrecios";
import ProductInfo from "./pages/customers/ProductInfo";

function App() {
  return (
    <AuthProvider>
      <div className="flex flex-col h-screen w-screen">
        <Navbar />
        <main className="flex-1 bg-white">
          <Routes>
            {/* public routes */}
            <Route path="/register" element={<Register />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register/customer" element={<Register />} />
            
            <Route
              path="/register/code-verification"
              element={<AdminVerification />}
            />
            <Route
              path="/register/store-admin"
              element={<StoreAdminRegister />}
            />
            <Route
              path="/register/profeco-admin"
              element={<ProfecoAdminRegister />}
            />
            <Route path="/unauthorized" element={<UnauthorizedPage />} />

            {/* routes for customer */}
            <Route element={<RoleProtectedRoute allowedRoles={["CUSTOMER"]} />}>
              <Route element={
                <CustomerProvider>
                  <Outlet />
                </CustomerProvider>
              }>
                <Route path="/" element={<HomePage />} />
                <Route path="/productos" element={<Productos />} />
                <Route path="/productos/:id" element={<BaseProductInfo />} />
                <Route
                  path="/productos/:id/reportar"
                  element={<ReportInconsistency />}
                />
                <Route path="/negocios" element={<Negocios />} />
                <Route path="/negocios/:id" element={<Negociosinfo />} />
                <Route path="/negocios/:id/productos/:id" element={<ProductInfo /> } />
              </Route>
            </Route>

            {/* routes for store admin */}
            <Route element={<RoleProtectedRoute allowedRoles={["STORE_ADMIN"]} />}>
              <Route element={
                <StoreAdminProvider>
                  <Outlet />
                </StoreAdminProvider>
              }>
                <Route path="/store-dashboard" element={<StoreAdminDashboard />} />
                <Route path="/inconsistencias" element={<AdminInconsistencias />} />
                <Route path="/comentarios" element={<AdminComentarios />} />
                <Route path="/precios" element={<AdminPrecios />} />
              </Route>
            </Route>

            {/* routes for profeco admin */}
            <Route element={<RoleProtectedRoute allowedRoles={["PROFECO_ADMIN"]} />}>
              <Route element={
                <ProfecoAdminProvider>
                  <Outlet />
                </ProfecoAdminProvider>
              }>
                <Route path="/reportes" element={<Reportes />} />
                <Route path="/admin-dashboard" element={<ProfecoDashboard />} />
              </Route>
            </Route>
          </Routes>
        </main>
      </div>
    </AuthProvider>
  );
}

export default App;
