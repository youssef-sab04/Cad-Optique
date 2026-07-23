import { Mail, MapPin, Phone, Eye, Pencil, Trash2, Truck , ShoppingBasket } from "lucide-react";

const initials = (nom = "") =>
    nom
        .trim()
        .split(/\s+/)
        .slice(0, 2)
        .map((w) => w[0]?.toUpperCase())
        .join("") || "F";

const AVATAR_THEMES = [
    "bg-indigo-50 text-indigo-600",
    "bg-emerald-50 text-emerald-600",
    "bg-amber-50 text-amber-600",
    "bg-rose-50 text-rose-600",
    "bg-cyan-50 text-cyan-600",
];

const FournisseurTable = ({ fournisseurs, onEdit, onDelete, onView , onCOrder }) => {
    if (!fournisseurs.length) {
        return (
            <div className="flex flex-col items-center justify-center gap-2 h-[220px] rounded-2xl border border-dashed border-slate-200 text-slate-400">
                <Truck size={28} />
                <span>Aucun fournisseur trouvé.</span>
            </div>
        );
    }

    return (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
            {fournisseurs.map((fournisseur, idx) => (
                <div
                    key={fournisseur.id}
                    className="group relative flex flex-col rounded-2xl border border-slate-200 bg-white p-5 shadow-sm transition-all duration-200 hover:-translate-y-0.5 hover:shadow-lg hover:shadow-slate-200/60"
                >
                    <div className="flex items-start justify-between">
                        <div className="flex items-center gap-3">
                            <div
                                className={`flex h-12 w-12 shrink-0 items-center justify-center rounded-xl text-sm font-bold ${AVATAR_THEMES[idx % AVATAR_THEMES.length]
                                    }`}
                            >
                                {initials(fournisseur.nom)}
                            </div>
                            <div>
                                <h3 className="text-sm font-bold text-slate-800 leading-tight">
                                    {fournisseur.nom}
                                </h3>
                                <span className="text-[11px] font-medium text-slate-400">
                                    Fournisseur #{fournisseur.id}
                                </span>
                            </div>
                        </div>

                        <div className="flex items-center gap-1 opacity-0 transition-opacity duration-200 group-hover:opacity-100">
                            <button
                                onClick={() => onView(fournisseur)}
                                title="Voir"
                                className="flex h-8 w-8 items-center justify-center rounded-lg text-slate-500 hover:bg-slate-100 hover:text-slate-800"
                            >
                                <Eye size={15} />
                            </button>

                            <button onClick={() => onCOrder(fournisseur)} className="text-slate-600 hover:text-slate-800">
                                        <ShoppingBasket size={16} />
                                    </button>


                            <button
                                onClick={() => onEdit(fournisseur)}
                                title="Modifier"
                                className="flex h-8 w-8 items-center justify-center rounded-lg text-indigo-500 hover:bg-indigo-50"
                            >
                                <Pencil size={15} />
                            </button>
                            <button
                                onClick={() => onDelete(fournisseur.id)}
                                title="Supprimer"
                                className="flex h-8 w-8 items-center justify-center rounded-lg text-rose-500 hover:bg-rose-50"
                            >
                                <Trash2 size={15} />
                            </button>
                        </div>
                    </div>

                    <div className="mt-4 flex flex-col gap-2 border-t border-slate-100 pt-4">
                        <div className="flex items-center gap-2 text-xs text-slate-600">
                            <Phone size={13} className="text-slate-400" />
                            {fournisseur.phoneNumber || "—"}
                        </div>
                        <div className="flex items-center gap-2 text-xs text-slate-600">
                            <Mail size={13} className="text-slate-400" />
                            <span className="truncate">{fournisseur.email || "—"}</span>
                        </div>
                        <div className="flex items-center gap-2 text-xs text-slate-600">
                            <MapPin size={13} className="text-slate-400" />
                            <span className="truncate">{fournisseur.adresse || "—"}</span>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
};

export default FournisseurTable;
