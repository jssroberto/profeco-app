import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import HomePage from "./pages/HomePage";
import Productos from "./pages/Productos";
import Negocios from "./pages/Negocios";
import ProductInfo from "./pages/ProductInfo";
import NegocioInfo from "./pages/NegocioInfo";
import ReportInconsistency from "./pages/ReportInconsistency";
import ProtectedRoute from "./pages/ProtectedRoute";
import { AuthProvider } from "./context/AuthContext";
import Register from "./pages/auth/Register";
import Login from "./pages/auth/Login";
import AdminRegister from "./pages/auth/AdminRegister";
import AdminVerification from "./pages/auth/AdminVerification";
import StoreAdminRegister from "./pages/auth/StoreAdminRegister";
import ProfecoAdminRegister from "./pages/auth/ProfecoAdminRegister";

function App() {
  return (
    <AuthProvider>
      <div className="flex flex-col h-screen w-screen">
        <Navbar />
        <main className="flex-1 bg-white">
          <Routes>
            {/* protected routes, only with token */}
            <Route element={<ProtectedRoute />}>
              <Route path="/" element={<HomePage />} />
              <Route path="/productos" element={<Productos />} />
              <Route path="/productos/:id" element={<ProductInfo />} />
              <Route path="/negocios" element={<Negocios />} />
              <Route path="/negocios/:id" element={<NegocioInfo />} />
              <Route
                path="/productos/:id/reportar"
                element={<ReportInconsistency />}
              />
            </Route>

            {/* public routes */}
            <Route path="/register" element={<Register />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register/customer" element={<Register />} />
            <Route path="/register/code-verification" element={<AdminVerification />} />
            <Route path="/register/store-admin" element={<StoreAdminRegister />} />
            <Route path="/register/profeco-admin" element={<ProfecoAdminRegister />} />
          </Routes>
        </main>
      </div>
    </AuthProvider>
  );
}

export default App;
