import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import HomePage from "./pages/HomePage";
import Productos from "./pages/Productos";
import Negocios from "./pages/Negocios";
import ProductInfo from "./pages/ProductInfo";
import NegocioInfo from "./pages/NegocioInfo";
import ReportInconsistency from "./pages/ReportInconsistency";
import Register from "./pages/Register";
import Login from "./pages/Login";
import ProtectedRoute from "./pages/ProtectedRoute";
import { AuthProvider } from "./context/AuthContext";

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
            <Route path="/productos/:id/reportar" element={<ReportInconsistency />}
            />
          </Route>

          {/* public routes */}
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />

        </Routes>
      </main>
    </div>
    </AuthProvider>
    
  );
}

export default App;
