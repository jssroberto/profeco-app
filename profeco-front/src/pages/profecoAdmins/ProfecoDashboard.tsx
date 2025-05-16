import { FileWarning } from 'lucide-react';
import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useProfecoAdmin } from '../../context/ProfecoAdminContext';

const ProfecoDashboard: React.FC = () => {
    const navigate = useNavigate();
    const { admin } = useProfecoAdmin();


    const cards = [
    {
      icon: <FileWarning className="w-6 h-6" />,
      title: "Ver Reportes de Inconsistencias",
      path: "/reportes",
    }
  ];

    return (
        <div className="p-8 mt-20 max-w-7xl mx-auto">
      <header className="mb-8">
        <h1 className="text-3xl font-bold mb-2 text-gray-800">Bienvenido, {admin?.name}</h1>
      </header>

      <div className="grid grid-cols-1 md:grid-cols-1 gap-6">
        {cards.map(({ icon, title, path }, index) => (
          <button
            key={index}
            onClick={() => navigate(path)}
            className="flex items-center gap-4 p-10 bg-[#902E56] text-white rounded-xl shadow-md transition cursor-pointer"
          >
            {icon}
            <h1 className="text-2xl font-medium">{title}</h1>
          </button>
        ))}
      </div>
    </div>
    );
};

export default ProfecoDashboard;