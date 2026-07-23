import { AlertTriangle, PackageCheck, RefreshCcw } from "lucide-react";
import { formatMoney, formatNumber } from "./format";

const StockPanel = ({ produitsStockFaible, valeurTotaleStock, mouvementsStockDuJour }) => {
    const hasAlert = produitsStockFaible > 0;

    return (
        <article className="rounded-[28px] border border-slate-200 bg-white p-5 shadow-sm">
            <div className="mb-5">
                <h2 className="text-lg font-bold text-slate-900">État du stock</h2>
                <p className="mt-1 text-sm text-slate-500">Alertes de réapprovisionnement et valeur immobilisée.</p>
            </div>

            <div className={`flex items-center gap-4 rounded-3xl p-5 ${hasAlert ? "bg-amber-50 ring-1 ring-amber-200" : "bg-emerald-50 ring-1 ring-emerald-200"}`}>
                <div className={`rounded-2xl p-3 text-white ${hasAlert ? "bg-amber-500" : "bg-emerald-500"}`}>
                    {hasAlert ? <AlertTriangle className="h-6 w-6" /> : <PackageCheck className="h-6 w-6" />}
                </div>
                <div>
                    <p className={`text-xs font-semibold uppercase tracking-wide ${hasAlert ? "text-amber-700" : "text-emerald-700"}`}>
                        {hasAlert ? "Produits à réapprovisionner" : "Aucune alerte de stock"}
                    </p>
                    <p className="mt-1 text-2xl font-bold text-slate-900">{formatNumber(produitsStockFaible)}</p>
                </div>
            </div>

            <div className="mt-4 grid grid-cols-2 gap-3 text-sm">
                <div className="min-w-0 rounded-2xl bg-sky-50 p-4 text-sky-700 ring-1 ring-sky-200">
                    <p className="text-xs font-semibold uppercase tracking-wide">Valeur du stock</p>
                    <p className="mt-2 break-words text-lg font-bold sm:text-xl">{formatMoney(valeurTotaleStock)}</p>
                </div>
                <div className="min-w-0 rounded-2xl bg-slate-50 p-4 text-slate-700 ring-1 ring-slate-200">
                    <p className="flex items-center gap-1 text-xs font-semibold uppercase tracking-wide">
                        <RefreshCcw className="h-3 w-3 shrink-0" /> Mouvements du jour
                    </p>
                    <p className="mt-2 break-words text-lg font-bold sm:text-xl">{formatNumber(mouvementsStockDuJour)}</p>
                </div>
            </div>
        </article>
    );
};

export default StockPanel;