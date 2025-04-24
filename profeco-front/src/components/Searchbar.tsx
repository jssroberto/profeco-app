import { useState, FormEvent } from "react";
import { Search } from "lucide-react";
import { useNavigate } from "react-router-dom";

const burgundy = "#681837";

type SearchContext = "productos" | "negocios" | "global";

interface SearchBarProps {
  context?: SearchContext;
  placeholder?: string;
  className?: string;
}

const SearchBar = ({
  context = "global",
  placeholder = "Buscar productos, supermercados y más...",
  className = ""
}: SearchBarProps) => {
  const [query, setQuery] = useState("");
  const navigate = useNavigate();

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    if (!query.trim()) return;

    console.log(`Searching ${context} for:`, query);

    switch(context) {
      case "productos":
        navigate(`/productos?search=${encodeURIComponent(query)}`);
        break;
      case "negocios":
        navigate(`/negocios?search=${encodeURIComponent(query)}`);
        break;
      case "global":
      default:
        navigate(`/search?q=${encodeURIComponent(query)}`);
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      className={`w-full bg-[#f6f6f6] rounded-2xl shadow-sm flex items-center px-6 py-3 gap-4 max-w-3xl ${className}`}
      aria-label="Buscador"
    >
      <Search
        size={28}
        color={burgundy}
        strokeWidth={2.5}
        className="flex-shrink-0"
        aria-hidden="true"
      />
      <input
        type="text"
        value={query}
        onChange={e => setQuery(e.target.value)}
        placeholder={placeholder}
        className="flex-1 bg-transparent outline-none text-lg text-zinc-700 placeholder:text-zinc-500"
        aria-label={placeholder}
      />
      <button
        type="submit"
        className="bg-[#681837] text-white px-7 py-2 rounded-xl text-base font-medium shadow-none transition hover:bg-[#4d122b] focus:outline-none cursor-pointer"
      >
        Buscar
      </button>
    </form>
  );
};

export default SearchBar;