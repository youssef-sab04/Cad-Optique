import { ArrowDownCircle, ArrowUpCircle } from "lucide-react";
import pImage from "../../assets/image/placeholder.png";

const formatAmount = (value) => {
    if (value == null || value === "") return "—";
    const number = Number(value);
    if (Number.isNaN(number)) return String(value);
    return number.toLocaleString("fr-FR", {
        minimumFractionDigits: 0,
        maximumFractionDigits: 2,
    });
};

const getPrice = (mvt, keys) => {
    for (const key of keys) {
        if (mvt?.[key] != null) return mvt[key];
    }
    return null;
};

const MouvementStockTable = ({ mouvements, onView }) => {
    if (!mouvements.length) {
        return (
            <div className="flex min-h-[220px] items-center justify-center rounded-2xl border border-dashed border-slate-200 bg-slate-50 text-slate-500">
                Aucun mouvement de stock trouvé.
            </div>
        );
    }

    return (
        <div className="flex flex-col gap-4">
            {mouvements.map((mvt) => {
                const isSortie = mvt.type === "SORTIE";
                const unitPrice = getPrice(mvt, ["prix_unit", "prixUnit", "prix_Unit"]);
                const totalPrice = getPrice(mvt, ["prix_total", "prixTotal", "prix_Total"]);
                return (
                    <div
                        key={mvt.id}
                        onClick={() => onView(mvt)}
                        className="group flex flex-col gap-4 rounded-2xl border border-slate-200 bg-white p-4 transition-all hover:-translate-y-0.5 hover:border-slate-300 hover:shadow-md sm:flex-row sm:items-center"
                    >
                        <div className="flex items-start gap-4">
                            <div
                                className={`flex h-12 w-12 shrink-0 items-center justify-center rounded-2xl ${
                                    isSortie ? "bg-rose-50 text-rose-600" : "bg-emerald-50 text-emerald-600"
                                }`}
                            >
                                {isSortie ? <ArrowDownCircle size={24} /> : <ArrowUpCircle size={24} />}
                            </div>

                            <img
                                src={mvt.produitDTO?.image || pImage}
                                alt={mvt.produitDTO?.nom}
                                className="h-12 w-12 shrink-0 rounded-2xl object-cover bg-slate-50"
                            />

                            <div className="min-w-0 flex-1">
                                <div className="flex flex-wrap items-center gap-2">
                                    <p className="truncate text-base font-semibold text-slate-900">
                                        {mvt.produitDTO?.nom || "Produit supprimé"}
                                    </p>
                                    <span
                                        className={`rounded-full px-2.5 py-1 text-xs font-semibold ${
                                            isSortie ? "bg-rose-100 text-rose-700" : "bg-emerald-100 text-emerald-700"
                                        }`}
                                    >
                                        {mvt.type}
                                    </span>
                                </div>

                                <p className="mt-1 text-sm text-slate-500">
                                    {mvt.createdAt ? new Date(mvt.createdAt).toLocaleString("fr-FR") : "—"}
                                </p>

                                <div className="mt-3 flex flex-wrap gap-2 text-xs text-slate-500">
                                    {mvt.produitDTO?.code_barre && (
                                        <span className="rounded-full bg-slate-100 px-3 py-1">{mvt.produitDTO.code_barre}</span>
                                    )}
                                    {unitPrice != null && (
                                        <span className="rounded-full bg-slate-100 px-3 py-1">
                                            Prix unitaire: {formatAmount(unitPrice)}
                                        </span>
                                    )}
                                    {totalPrice != null && (
                                        <span className="rounded-full bg-slate-100 px-3 py-1">
                                            Prix total: {formatAmount(totalPrice)}
                                        </span>
                                    )}
                                </div>
                            </div>
                        </div>

                        <div className="flex items-center justify-between gap-4 border-t border-slate-100 pt-4 sm:ml-auto sm:flex-col sm:items-end sm:border-0 sm:pt-0">
                            <span className={`text-sm font-semibold ${isSortie ? "text-rose-600" : "text-emerald-600"}`}>
                                {isSortie ? "-" : "+"}
                                {mvt.quantity}
                            </span>

                            {totalPrice != null && (
                                <span className="text-sm font-bold text-slate-900">{formatAmount(totalPrice)}</span>
                            )}
                        </div>
                    </div>
                );
            })}
        </div>
    );
};

export default MouvementStockTable;