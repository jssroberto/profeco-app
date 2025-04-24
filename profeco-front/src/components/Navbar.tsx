import { Link, useLocation } from "react-router-dom";
import React from "react";

interface LinkItem {
  name: string;
  path: string;
}

const Navbar: React.FC = () => {
  const location = useLocation();
  const links: LinkItem[] = [
    { name: "Inicio", path: "/" },
    { name: "Productos", path: "/productos" },
    { name: "Negocios", path: "/negocios" },
  ];
  
  return (
    <nav className="fixed top-0 left-0 w-full z-50 backdrop-blur-md bg-opacity-30 border-b border-gray-300/20 bg-[#611232]">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          {/* Logo */}
          <div className="text-white text-xl font-bold">profecoApp</div>

          {/* Navigation Links */}
          <nav className="hidden md:flex space-x-6">
            {links.map((link) => (
                <Link
                    key={link.path}
                    to={link.path}
                    className={`text-white hover:text-[#F36464] transition duration-300 text-lg font-regular ${
                        location.pathname === link.path ? "font-bold" : ""
                    }`}
                >
                    {link.name}
                </Link>
            ))}

          </nav>

            <div>
            <div className="flex max-w-xs">
              <button className="flex-1 font-semibold text-sm bg-white px-4 py-2 rounded-lg cursor-pointer hover:bg-gray-100">
                Iniciar sesion
              </button>
            </div>
            </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
