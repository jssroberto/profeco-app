import './App.css'
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from './components/Navbar'
import HomePage from './pages/HomePage';
import Productos from './pages/Productos';
import Negocios from './pages/Negocios';
import ProductInfo from './pages/ProductInfo';
import NegocioInfo from './pages/NegocioInfo';
import ReportInconsistency from './pages/ReportInconsistency';


function App() {

  return (
    <div className="flex flex-col h-screen w-screen">
      <Navbar />
      <main className="flex-1 bg-white">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/productos" element={<Productos />} />
          <Route path="/productos/:id" element={<ProductInfo />} />
          <Route path="/negocios" element={<Negocios />} />
          <Route path='/negocios/:id' element={<NegocioInfo />} />
          <Route path='productos/:id/reportar' element={<ReportInconsistency />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;