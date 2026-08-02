import { useState, useEffect, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchProducts } from "../../store/reducers/actions";
import { FiSearch, FiX } from "react-icons/fi";

const ProductSearchSelect = ({ value, onChange, placeholder = "Rechercher un produit..." ,comm = false }) => {
    const dispatch = useDispatch();
    const { produits } = useSelector((state) => state.produits);

    const [query, setQuery] = useState("");
    const [open, setOpen] = useState(false);
    const debounceRef = useRef(null);
     console.log(comm ? "commande" : "sales")

    useEffect(() => {
        if (debounceRef.current) clearTimeout(debounceRef.current);

        if (!query) return;

        debounceRef.current = setTimeout(() => {
            const params = new URLSearchParams();
            params.set("pageNumber", 0);
            params.set("sortBy", "price");
            params.set("sortOrder", "asc");
            params.set("keyword", query);

            dispatch(fetchProducts(params.toString()));
        }, 300);

        return () => clearTimeout(debounceRef.current);
    }, [query, dispatch]);

    const handleSelect = (produit) => {
        onChange(produit);
        setQuery(produit.nom);
        setOpen(false);
    };

    const handleClear = () => {
        onChange(null);
        setQuery("");
    };

    return (
        <div className="relative">
            <div className="relative">
                <FiSearch className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={16} />
                <input
                    type="text"
                    value={value ? value.nom : query}
                    onChange={(e) => {
                        setQuery(e.target.value);
                        setOpen(true);
                        if (value) onChange(null);
                    }}
                    onFocus={() => setOpen(true)}
                    placeholder={placeholder}
                    className="w-full rounded-lg border border-slate-300 pl-9 pr-9 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-600"
                />
                {(value || query) && (
                    <button
                        type="button"
                        onClick={handleClear}
                        className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600"
                    >
                        <FiX size={16} />
                    </button>
                )}
            </div>

            {open && query && !value && (
                <div className="absolute z-10 mt-1 w-full rounded-lg border border-slate-200 bg-white shadow-lg max-h-60 overflow-y-auto">
                    {produits?.length ? (
                        produits.map((produit) => (
                            <button
                                key={produit.id}
                                type="button"
                                onClick={() => handleSelect(produit)}
                                className="w-full text-left px-4 py-2 text-sm hover:bg-slate-50 flex justify-between items-center"
                            >
                                <span className="text-slate-700">{produit.nom}</span>
<span className="text-slate-400 text-xs">{Number((comm ? produit.prixAchat : produit.price) ?? 0).toFixed(2)} DH</span>                            </button>
                        ))
                    ) : (
                        <div className="px-4 py-3 text-sm text-slate-400">Aucun résultat</div>
                    )}
                </div>
            )}
        </div>
    );
};

export default ProductSearchSelect;