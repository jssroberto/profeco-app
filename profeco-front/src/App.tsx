import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import { AuthProvider } from "./context/AuthContext";
import Register from "./pages/auth/Register";
import Login from "./pages/auth/Login";
import AdminVerification from "./pages/auth/AdminVerification";
import StoreAdminRegister from "./pages/auth/StoreAdminRegister";
import ProfecoAdminRegister from "./pages/auth/ProfecoAdminRegister";
import UnauthorizedPage from "./pages/UnauthorizedPage";
import RoleProtectedRoute from "./pages/auth/RoleProtectedRoute";
import HomePage from "./pages/customers/HomePage";
import Productos from "./pages/customers/Productos";
import ProductInfo from "./pages/customers/ProductInfo";
import ReportInconsistency from "./pages/customers/ReportInconsistency";
import Negocios from "./pages/customers/Negocios";
import Negociosinfo from "./pages/customers/NegocioInfo";

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
              <Route path="/" element={<HomePage />} />
              <Route path="/productos" element={<Productos />} />
              <Route path="/productos/:id" element={<ProductInfo />} />
              <Route
                path="/productos/:id/reportar"
                element={<ReportInconsistency />}
              />
              <Route path="/negocios" element={<Negocios />} />
              <Route path="/negocios/:id" element={<Negociosinfo />} />
            </Route>

            {/* routes for store admin */}
            <Route
              element={<RoleProtectedRoute allowedRoles={["STORE_ADMIN"]} />}
            ></Route>

            {/* routes for admin only */}
            <Route
              element={<RoleProtectedRoute allowedRoles={["PROFECO_ADMIN"]} />}
            ></Route>
          </Routes>
        </main>
      </div>
    </AuthProvider>
  );
}

export default App;
