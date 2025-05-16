import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.tsx'
import { BrowserRouter } from 'react-router-dom'

import './App.css'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <div className="min-h-screen w-full bg-gradient-to-br from-[#f8e6ef] via-[#f7f2fa] to-[#f3e6f7] animate-gradient-move transition-all duration-700 font-sans">
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </div>
  </StrictMode>
)
// NOTA: Asegúrate de tener en App.css lo siguiente para la animación y fuente:
// @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;700;900&display=swap');
// .font-sans { font-family: 'Inter', Arial, sans-serif; }
// @keyframes gradient-move { 0% {background-position:0% 50%} 100% {background-position:100% 50%} }
// .animate-gradient-move { background-size: 200% 200%; animation: gradient-move 10s ease-in-out infinite; }
