import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from "@headlessui/react";
import { FaTimes } from "react-icons/fa";
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

const MouvementStockDetailModal = ({ open, setOpen, mouvement }) => {
    if (!mouvement) return null;

    const isSortie = mouvement.type === "SORTIE";
    const accent = isSortie ? "text-red-600 bg-red-50" : "text-green-600 bg-green-50";
    const Icon = isSortie ? ArrowDownCircle : ArrowUpCircle;
    const unitPrice = getPrice(mouvement, ["prix_unit", "prixUnit", "prix_Unit"]);
    const totalPrice = getPrice(mouvement, ["prix_total", "prixTotal", "prix_Total"]);

    return (
        <Dialog open={open} onClose={() => setOpen(false)} className="relative z-50">
            <DialogBackdrop className="fixed inset-0 bg-black/50 backdrop-blur-sm" />
            <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4">
                    <DialogPanel className="relative w-full max-w-2xl overflow-hidden rounded-3xl bg-white shadow-2xl">
                        <div className="flex items-center justify-between border-b border-slate-200 bg-slate-950 px-6 py-5">
                            <DialogTitle className="text-base font-bold text-white">Mouvement #{mouvement.id}</DialogTitle>
                            <button onClick={() => setOpen(false)} className="text-slate-400 transition-colors hover:text-white">
                                <FaTimes size={16} />
                            </button>
                        </div>

                        <div className="flex flex-col gap-6 p-6">
                            <div className={`flex items-center gap-3 rounded-2xl p-4 ${accent}`}>
                                <Icon size={28} />
                                <div>
                                    <p className="text-sm font-bold">{mouvement.type}</p>
                                    <p className="text-xs opacity-80">
                                        {mouvement.createdAt ? new Date(mouvement.createdAt).toLocaleString() : "—"}
                                    </p>
                                </div>
                            </div>

                            <div className="flex items-center gap-4 rounded-2xl border border-slate-200 p-4">
                                <img
                                    src={mouvement.produitDTO?.image || pImage}
                                    alt={mouvement.produitDTO?.nom}
                                    className="h-16 w-16 rounded-2xl object-cover bg-slate-50"
                                />
                                <div>
                                    <p className="font-semibold text-slate-900">{mouvement.produitDTO?.nom || "Produit supprimé"}</p>
                                    <p className="text-xs text-slate-400">{mouvement.produitDTO?.code_barre}</p>
                                </div>
                            </div>

                            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
                                <div className="rounded-2xl border border-slate-200 p-4">
                                    <p className="text-xs font-semibold uppercase tracking-[0.18em] text-slate-400">Quantité</p>
                                    <p className="mt-2 text-2xl font-bold text-slate-900">{mouvement.quantity}</p>
                                </div>
                                <div className="rounded-2xl border border-slate-200 p-4">
                                    <p className="text-xs font-semibold uppercase tracking-[0.18em] text-slate-400">Stock actuel</p>
                                    <p className="mt-2 text-2xl font-bold text-slate-900">{mouvement.produitDTO?.quantity ?? "—"}</p>
                                </div>
                                <div className="rounded-2xl border border-slate-200 p-4">
                                    <p className="text-xs font-semibold uppercase tracking-[0.18em] text-slate-400">Prix unitaire</p>
                                    <p className="mt-2 text-2xl font-bold text-slate-900">{formatAmount(unitPrice)}</p>
                                </div>
                                <div className="rounded-2xl border border-slate-200 p-4">
                                    <p className="text-xs font-semibold uppercase tracking-[0.18em] text-slate-400">Prix total</p>
                                    <p className="mt-2 text-2xl font-bold text-slate-900">{formatAmount(totalPrice)}</p>
                                </div>
                            </div>

                            {mouvement.salesOrderItemDTO && (
                                <div className="rounded-2xl border border-slate-200 p-4">
                                    <p className="mb-1 text-xs font-semibold uppercase text-slate-400">Lié à la vente</p>
                                    <p className="text-sm text-slate-700">Item de vente #{mouvement.salesOrderItemDTO.id}</p>
                                </div>
                            )}

                            {mouvement.commandeItemDTO && (
                                <div className="rounded-2xl border border-slate-200 p-4">
                                    <p className="mb-1 text-xs font-semibold uppercase text-slate-400">Lié au bon de livraison</p>
                                    <p className="text-sm text-slate-700">Item de commande #{mouvement.commandeItemDTO.id}</p>
                                </div>
                            )}

                            {mouvement.description && (
                                <div className="rounded-2xl border border-slate-200 p-4">
                                    <p className="mb-1 text-xs font-semibold uppercase text-slate-400">Description</p>
                                    <p className="text-sm text-slate-700">{mouvement.description}</p>
                                </div>
                            )}
                        </div>
                    </DialogPanel>
                </div>
            </div>
        </Dialog>
    );
};

export default MouvementStockDetailModal;