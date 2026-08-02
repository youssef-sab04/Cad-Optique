import { useEffect, useState } from "react";
import { useLocation, useNavigate, useSearchParams } from "react-router-dom";
import { FiSearch, FiRefreshCw } from "react-icons/fi";

const ClientFilter = () => {
    const [searchParams] = useSearchParams();
    const pathname = useLocation().pathname;
    const navigate = useNavigate();

    const [searchTerm, setSearchTerm] = useState("");

    useEffect(() => {
        setSearchTerm(searchParams.get("keyword") || "");
    }, [searchParams]);

    useEffect(() => {
        const handler = setTimeout(() => {
            const params = new URLSearchParams(searchParams);
            if (searchTerm) params.set("keyword", searchTerm);
            else params.delete("keyword");
            navigate(`${pathname}?${params.toString()}`);
        }, 600);

        return () => clearTimeout(handler);
    }, [searchTerm]);

    const handleClear = () => {
        setSearchTerm("");
        navigate({ pathname });
    };

    return (
        <div className="flex items-center gap-4 mb-4">
            <div className="relative flex items-center w-full sm:w-[350px]">
                <input
                    type="text"
                    placeholder="Rechercher un client..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="border border-gray-300 text-slate-800 rounded-lg py-2 pl-10 pr-4 w-full focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                <FiSearch className="absolute left-3 text-gray-400" size={18} />
            </div>

            <button
                onClick={handleClear}
                className="flex items-center gap-2 bg-slate-100 hover:bg-slate-200 text-slate-700 px-3 py-2 rounded-lg"
            >
                <FiRefreshCw size={16} />
                <span className="text-sm font-medium">Réinitialiser</span>
            </button>
        </div>
    );
};

export default ClientFilter;