import { useState } from "react";
import { FiChevronDown, FiChevronUp, FiPackage, FiTrash2 } from "react-icons/fi";
import { fetchSalesOrderItemsID, updateSaleOrderItemQTE } from "../../store/reducers/actions";
import { useDispatch } from "react-redux";
import Loader from "../shared/Loader";
import toast from "react-hot-toast";
import ProductionQuantityLimitsIcon from '@mui/icons-material/ProductionQuantityLimits';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import React from "react";
import { fetchCommandeItemsID , updateCommandeItemQTE } from "../../store/reducers/actions";

const statusStyles = {
    "LIVREE": "bg-green-100 text-green-700",
    "EN_COURS": "bg-slate-100 text-slate-600",
    "Annulee": "bg-red-100 text-red-700",
};



const CommandeTable = ({ commandes, onAddItem, onDelete, onDeleteCI, onConfirm }) => {

    const dispatch = useDispatch();

    const [CI, setCI] = useState(null);
    const [btnLoader, setBtnLoader] = useState(false);

    const [expandedId, setExpandedId] = useState(null);

    if (!commandes.length) {
        return (
            <div className="flex justify-center items-center h-[200px] text-slate-500">
                Aucune vente trouvée.
            </div>
        );
    }

    const toggleExpand = (id) => {
        if (expandedId === id) {
            setExpandedId(null);
            return;
        }
        setExpandedId(id);
        setCI(null);
        dispatch(fetchCommandeItemsID (toast, id, setBtnLoader, setCI));
    };

    const updateQte = (id, qte) => {
       dispatch(updateCommandeItemQTE(id, qte, toast, setCI, expandedId));
    };

    return (
        <div className="overflow-x-auto rounded-xl border border-slate-200 shadow-sm">
            <table className="min-w-full divide-y divide-slate-200">
                <thead className="bg-slate-50">
                    <tr>
                        <th className="px-4 py-3 text-left text-xs font-semibold text-slate-600 uppercase">#</th>
                        <th className="px-4 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Fournisseur</th>
                        <th className="px-4 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Statut</th>
                        <th className="px-4 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Date</th>
                        <th className="px-4 py-3 text-right text-xs font-semibold text-slate-600 uppercase">Total</th>
                        <th className="px-4 py-3 text-right text-xs font-semibold text-slate-600 uppercase">Actions</th>
                    </tr>
                </thead>
                <tbody className="divide-y divide-slate-100 bg-white">
                    {commandes.map((c) => (
                        <React.Fragment key={c.id}>
                            <tr
                                key={c.id}
                                onClick={() => toggleExpand(c.id)}
                                className="hover:bg-slate-50 transition-colors cursor-pointer"
                            >
                                <td className="px-4 py-3 text-sm text-slate-500 font-mono">#{c.id}</td>
                                <td className="px-4 py-3 text-sm text-slate-800 font-medium">
                                    {c.fournisseurDTO?.nom} 
                                </td>
                                <td className="px-4 py-3">
                                    <span className={`text-xs font-medium px-2 py-1 rounded-md ${statusStyles[c.status] || "bg-slate-100 text-slate-600"}`}>
                                        {c?.status || "en cours"}
                                    </span>
                                </td>
                                <td className="px-4 py-3 text-sm text-slate-700">
                                    {c.createdAt ? new Date(c.createdAt).toLocaleDateString() : "—"}
                                </td>
                                <td className="px-4 py-3 text-sm text-slate-800 font-semibold text-right">
                                    {Number(c?.totalprice ?? 0).toFixed(2)} DH
                                </td>
                                <td className="px-4 py-3">
                                    <div className="flex justify-end items-center gap-3">
                                        <button
                                            onClick={(e) => { e.stopPropagation(); onAddItem(c.id); }}
                                            disabled={c.status === "LIVREE"}
                                            className="w-6 h-6 flex items-center justify-center rounded-lg bg-blue-600 hover:bg-blue-700 text-white disabled:opacity-40 disabled:cursor-not-allowed"
                                            title="Ajouter un produit"
                                        >
                                            <ProductionQuantityLimitsIcon sx={{ fontSize: 16 }} />
                                        </button>
                                        <button
                                            onClick={(e) => { e.stopPropagation(); onDelete(c.id); }}
                                            className="text-red-600 hover:text-red-800"
                                        >
                                            <FiTrash2 size={16} />
                                        </button>

                                        <button
                                            onClick={(e) => { e.stopPropagation(); onConfirm(c.id); }}
                                            disabled={c.status === "LIVREE"}
                                            className="w-8 h-8 flex items-center justify-center rounded-lg bg-green-50 text-green-600 hover:bg-green-100"
                                            title="Valider la commande"

                                        >
                                            <CheckCircleIcon sx={{ fontSize: 16 }} />
                                        </button>
                                        {expandedId === c.id ? <FiChevronUp size={16} className="text-slate-400" /> : <FiChevronDown size={16} className="text-slate-400" />}
                                    </div>
                                </td>
                            </tr>

                            {expandedId === c.id && (
                                <tr key={`${c.id}-items`}>
                                    <td colSpan={6} className="bg-slate-50 px-6 py-4">
                                        {btnLoader ? (
                                            <div className="flex justify-center py-4">
                                                <Loader />
                                            </div>
                                        ) : CI?.length ? (
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
                                                    {CI.map((item) => (
                                                        <tr key={item.id} className="border-t border-slate-200">
                                                            <td className="py-2 text-slate-700">{item.produitDTO?.nom}</td>
                                                            <td className="py-2 text-slate-700">
                                                                <div className="flex items-center gap-2">
                                                                    <button
                                                                        onClick={() => { updateQte(item.id, -1) }}
                                                                        disabled={c.status === "LIVREE"}
                                                                        className="w-6 h-6 flex items-center justify-center rounded bg-slate-200 hover:bg-slate-300 text-slate-700 disabled:opacity-40 disabled:cursor-not-allowed">-</button>
                                                                    <span>{item.quantity}</span>
                                                                    <button
                                                                        onClick={() => { updateQte(item.id, 1) }}
                                                                        disabled={c.status === "LIVREE"}
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
                                                                        onClick={(e) => { e.stopPropagation(); onDeleteCI(item.id) }}
                                                                        disabled={c.status === "LIVREE"}
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

export default CommandeTable;