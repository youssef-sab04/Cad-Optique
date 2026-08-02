import { useState } from "react";
import { FiChevronDown, FiChevronUp, FiTrash2, FiPrinter } from "react-icons/fi";
import { fetchDevisItemsID, updateDevisItemQTE } from "../../store/reducers/actions";
import { useDispatch } from "react-redux";
import Loader from "../shared/Loader";
import toast from "react-hot-toast";
import ProductionQuantityLimitsIcon from '@mui/icons-material/ProductionQuantityLimits';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import CancelIcon from '@mui/icons-material/Cancel';
import React from "react";

const statusStyles = {
    "Valide": "bg-green-100 text-green-700",
    "en cours": "bg-slate-100 text-slate-600",
    "Annulee": "bg-red-100 text-red-700",
};

const DevisTable = ({ devisList, onAddItem, onDelete, onDeleteItem, onConfirm, onCancel, onReceipt }) => {

    const dispatch = useDispatch();

    const [DI, setDI] = useState(null);
    const [btnLoader, setBtnLoader] = useState(false);

    const [expandedId, setExpandedId] = useState(null);

    if (!devisList.length) {
        return (
            <div className="flex justify-center items-center h-[200px] text-slate-500">
                Aucun devis trouvé.
            </div>
        );
    }

    const toggleExpand = (id) => {
        if (expandedId === id) {
            setExpandedId(null);
            return;
        }
        setExpandedId(id);
        setDI(null);
        dispatch(fetchDevisItemsID(toast, id, setBtnLoader, setDI));
    };

    const updateQte = (id, currentQty, delta) => {
        const newQty = currentQty + delta;
        if (newQty < 1) return;
        dispatch(updateDevisItemQTE(id, newQty, toast, setDI, expandedId));
    };

    const isLocked = (status) => status === "Valide" || status === "Annulee";

    return (
        <div className="overflow-x-auto rounded-xl border border-slate-200 shadow-sm">
            <table className="min-w-full divide-y divide-slate-200">
                <thead className="bg-slate-50">
                    <tr>
                        <th className="px-4 py-3 text-left text-xs font-semibold text-slate-600 uppercase">#</th>
                        <th className="px-4 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Client</th>
                        <th className="px-4 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Statut</th>
                        <th className="px-4 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Date</th>
                        <th className="px-4 py-3 text-right text-xs font-semibold text-slate-600 uppercase">Total</th>
                        <th className="px-4 py-3 text-right text-xs font-semibold text-slate-600 uppercase">Actions</th>
                    </tr>
                </thead>
                <tbody className="divide-y divide-slate-100 bg-white">
                    {devisList.map((devis) => (
                        <React.Fragment key={devis.id}>
                            <tr
                                onClick={() => toggleExpand(devis.id)}
                                className="hover:bg-slate-50 transition-colors cursor-pointer"
                            >
                                <td className="px-4 py-3 text-sm text-slate-500 font-mono">#{devis.id}</td>
                                <td className="px-4 py-3 text-sm text-slate-800 font-medium">
                                    {devis.clientDTO?.nom} {devis.clientDTO?.prenom}
                                </td>
                                <td className="px-4 py-3">
                                    <span className={`text-xs font-medium px-2 py-1 rounded-md ${statusStyles[devis.status] || "bg-slate-100 text-slate-600"}`}>
                                        {devis?.status || "en cours"}
                                    </span>
                                </td>
                                <td className="px-4 py-3 text-sm text-slate-700">
                                    {devis.createdAt ? new Date(devis.createdAt).toLocaleDateString() : "—"}
                                </td>
                                <td className="px-4 py-3 text-sm text-slate-800 font-semibold text-right">
                                    {Number(devis.totalprice ?? 0).toFixed(2)} DH
                                </td>
                                <td className="px-4 py-3">
                                    <div className="flex justify-end items-center gap-3">
                                        <button
                                            onClick={(e) => { e.stopPropagation(); onAddItem(devis.id); }}
                                            disabled={isLocked(devis.status)}
                                            className="w-6 h-6 flex items-center justify-center rounded-lg bg-blue-600 hover:bg-blue-700 text-white disabled:opacity-40 disabled:cursor-not-allowed"
                                            title="Ajouter un produit"
                                        >
                                            <ProductionQuantityLimitsIcon sx={{ fontSize: 16 }} />
                                        </button>

                                        <button
                                            onClick={(e) => { e.stopPropagation(); onReceipt(devis.id); }}
                                            className="w-6 h-6 flex items-center justify-center rounded-lg bg-slate-100 hover:bg-slate-200 text-slate-700"
                                            title="Générer le devis"
                                        >
                                            <FiPrinter size={14} />
                                        </button>

                                        <button
                                            onClick={(e) => { e.stopPropagation(); onDelete(devis.id); }}
                                            className="text-red-600 hover:text-red-800"
                                            title="Supprimer"
                                        >
                                            <FiTrash2 size={16} />
                                        </button>

                                        <button
                                            onClick={(e) => { e.stopPropagation(); onCancel(devis.id); }}
                                            disabled={isLocked(devis.status)}
                                            className="w-8 h-8 flex items-center justify-center rounded-lg bg-orange-50 text-orange-600 hover:bg-orange-100 disabled:opacity-40 disabled:cursor-not-allowed"
                                            title="Annuler le devis"
                                        >
                                            <CancelIcon sx={{ fontSize: 16 }} />
                                        </button>

                                        <button
                                            onClick={(e) => { e.stopPropagation(); onConfirm(devis.id); }}
                                            disabled={isLocked(devis.status)}
                                            className="w-8 h-8 flex items-center justify-center rounded-lg bg-green-50 text-green-600 hover:bg-green-100 disabled:opacity-40 disabled:cursor-not-allowed"
                                            title="Confirmer le devis (crée la vente)"
                                        >
                                            <CheckCircleIcon sx={{ fontSize: 16 }} />
                                        </button>

                                        {expandedId === devis.id ? <FiChevronUp size={16} className="text-slate-400" /> : <FiChevronDown size={16} className="text-slate-400" />}
                                    </div>
                                </td>
                            </tr>

                            {expandedId === devis.id && (
                                <tr key={`${devis.id}-items`}>
                                    <td colSpan={6} className="bg-slate-50 px-6 py-4">
                                        {btnLoader ? (
                                            <div className="flex justify-center py-4">
                                                <Loader />
                                            </div>
                                        ) : DI?.length ? (
                                            <table className="w-full text-sm">
                                                <thead>
                                                    <tr className="text-xs text-slate-500 uppercase">
                                                        <th className="text-left py-1">Produit</th>
                                                        <th className="text-left py-1">Quantité</th>
                                                        <th className="text-right py-1">Prix unitaire</th>
                                                        <th className="text-right py-1">Sous-total</th>
                                                        <th className="px-4 py-3 text-right text-xs font-semibold text-slate-600 uppercase">Actions</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    {DI.map((item) => (
                                                        <tr key={item.id} className="border-t border-slate-200">
                                                            <td className="py-2 text-slate-700">{item.produitDTO?.nom}</td>
                                                            <td className="py-2 text-slate-700">
                                                                <div className="flex items-center gap-2">
                                                                    <button
                                                                        onClick={() => { updateQte(item.id, item.quantity, -1) }}
                                                                        disabled={isLocked(devis.status)}
                                                                        className="w-6 h-6 flex items-center justify-center rounded bg-slate-200 hover:bg-slate-300 text-slate-700 disabled:opacity-40 disabled:cursor-not-allowed">-</button>
                                                                    <span>{item.quantity}</span>
                                                                    <button
                                                                        onClick={() => { updateQte(item.id, item.quantity, 1) }}
                                                                        disabled={isLocked(devis.status)}
                                                                        className="w-6 h-6 flex items-center justify-center rounded bg-slate-200 hover:bg-slate-300 text-slate-700 disabled:opacity-40 disabled:cursor-not-allowed">+</button>
                                                                </div>
                                                            </td>
                                                            <td className="py-2 text-right text-slate-700">{Number(item.price ?? 0).toFixed(2)} DH</td>
                                                            <td className="py-2 text-right font-medium text-slate-800">
                                                                {(Number(item.quantity ?? 0) * Number(item.price ?? 0)).toFixed(2)} DH
                                                            </td>
                                                            <td className="px-4 py-3">
                                                                <div className="flex justify-end items-center gap-3">
                                                                    <button
                                                                        onClick={(e) => { e.stopPropagation(); onDeleteItem(item.id) }}
                                                                        disabled={isLocked(devis.status)}
                                                                        className="text-red-400 hover:text-red-800 disabled:opacity-40 disabled:cursor-not-allowed"
                                                                    >
                                                                        <FiTrash2 size={16} />
                                                                    </button>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    ))}
                                                </tbody>
                                            </table>
                                        ) : (
                                            <p className="text-sm text-slate-400 text-center py-2">Aucun produit ajouté</p>
                                        )}
                                    </td>
                                </tr>
                            )}
                        </React.Fragment>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default DevisTable;