import { Mail, Phone, Eye, Pencil, Trash2, UserCog, BadgeCheck, IdCard } from "lucide-react";

const initials = (nom = "", prenom = "") => {
    const parts = [nom, prenom].filter(Boolean);
    return parts
        .slice(0, 2)
        .map((word) => word[0]?.toUpperCase())
        .join("") || "R";
};

const AVATAR_THEMES = [
    "bg-violet-50 text-violet-600",
    "bg-emerald-50 text-emerald-600",
    "bg-amber-50 text-amber-600",
    "bg-rose-50 text-rose-600",
    "bg-cyan-50 text-cyan-600",
];

const ResponsableTable = ({ responsables, onEdit, onDelete, onView }) => {
    if (!responsables?.length) {
        return (
            <div className="flex h-[220px] flex-col items-center justify-center gap-2 rounded-2xl border border-dashed border-slate-200 text-slate-400">
                <UserCog size={28} />
                <span>Aucun responsable trouvé.</span>
            </div>
        );
    }

    return (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
            {responsables.map((responsable, idx) => (
                <div
                    key={responsable.id}
                    className="group relative flex flex-col rounded-2xl border border-slate-200 bg-white p-5 shadow-sm transition-all duration-200 hover:-translate-y-0.5 hover:shadow-lg hover:shadow-slate-200/60"
                >
                    <div className="flex items-start justify-between">
                        <div className="flex items-center gap-3">
                            <div className={`flex h-12 w-12 shrink-0 items-center justify-center rounded-xl text-sm font-bold ${AVATAR_THEMES[idx % AVATAR_THEMES.length]}`}>
                                {initials(responsable.nom, responsable.prenom)}
                            </div>
                            <div>
                                <h3 className="text-sm font-bold text-slate-800 leading-tight">
                                    {responsable.nom} {responsable.prenom}
                                </h3>
                                <span className="text-[11px] font-medium text-slate-400">
                                    Responsable #{responsable.id}
                                </span>
                            </div>
                        </div>

                        <div className="flex items-center gap-1 opacity-0 transition-opacity duration-200 group-hover:opacity-100">
                            <button
                                onClick={() => onView(responsable)}
                                title="Voir"
                                className="flex h-8 w-8 items-center justify-center rounded-lg text-slate-500 hover:bg-slate-100 hover:text-slate-800"
                            >
                                <Eye size={15} />
                            </button>
                            <button
                                onClick={() => onEdit(responsable)}
                                title="Modifier"
                                className="flex h-8 w-8 items-center justify-center rounded-lg text-violet-500 hover:bg-violet-50"
                            >
                                <Pencil size={15} />
                            </button>
                            <button
                                onClick={() => onDelete(responsable.id)}
                                title="Supprimer"
                                className="flex h-8 w-8 items-center justify-center rounded-lg text-rose-500 hover:bg-rose-50"
                            >
                                <Trash2 size={15} />
                            </button>
                        </div>
                    </div>

                    <div className="mt-4 flex flex-col gap-2 border-t border-slate-100 pt-4">
                        <div className="flex items-center gap-2 text-xs text-slate-600">
                            <BadgeCheck size={13} className="text-slate-400" />
                            <span className="truncate">@{responsable.userName || "—"}</span>
                        </div>
                        <div className="flex items-center gap-2 text-xs text-slate-600">
                            <Mail size={13} className="text-slate-400" />
                            <span className="truncate">{responsable.email || "—"}</span>
                        </div>
                        <div className="flex items-center gap-2 text-xs text-slate-600">
                            <Phone size={13} className="text-slate-400" />
                            {responsable.phoneNumber || "—"}
                        </div>
                        <div className="flex items-center gap-2 text-xs text-slate-600">
                            <IdCard size={13} className="text-slate-400" />
                            <span className="truncate">CIN: {responsable.cin || "—"}</span>
                        </div>
                        <div className="flex items-center gap-2 text-xs text-slate-600">
                            <span className="text-slate-400">Rôles:</span>
                            <span className="truncate">{responsable.roles?.join(", ") || "—"}</span>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
};

export default ResponsableTable;
