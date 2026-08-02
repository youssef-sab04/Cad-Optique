import { FiEdit2, FiTrash2, FiEye , FiUpload  } from "react-icons/fi";
import { ShoppingBasket , FileText } from "lucide-react";



const ClientTable = ({ clients, onEdit, onDelete, onView , handleDoc  , onCOrder , onCDevis}) => {



    if (!clients.length) {
        return (
            <div className="flex justify-center items-center h-[200px] text-slate-500">
                Aucun client trouvé.
            </div>
        );
    }

    return (
        <div className="overflow-x-auto rounded-xl border border-slate-200 shadow-sm">
            <table className="min-w-full divide-y divide-slate-200">
                <thead className="bg-slate-50">
                    <tr>
                        <th className="px-4 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Nom</th>
                        <th className="px-4 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Prénom</th>
                        <th className="px-4 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Téléphone</th>
                        <th className="px-4 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Dernier examen</th>
                        <th className="px-4 py-3 text-right text-xs font-semibold text-slate-600 uppercase">Actions</th>
                    </tr>
                </thead>
                <tbody className="divide-y divide-slate-100 bg-white">
                    {clients.map((client) => (
                        <tr key={client.id} className="hover:bg-slate-50 transition-colors">
                            <td className="px-4 py-3 text-sm text-slate-800 font-medium">{client.nom}</td>
                            <td className="px-4 py-3 text-sm text-slate-700">{client.prenom}</td>
                            <td className="px-4 py-3 text-sm text-slate-700">{client.phoneNumber}</td>
                            <td className="px-4 py-3 text-sm text-slate-700">
                                {client.dernierExamen || "—"}
                            </td>
                            <td className="px-4 py-3 text-right">
                                <div className="flex justify-end gap-3">
                                    <button
                                        onClick={() => onEdit(client)}
                                        className="text-blue-600 hover:text-blue-800"
                                    >
                                        <FiEdit2 size={16} />
                                    </button>
                                    <button
                                        onClick={() => onDelete(client.id)}
                                        className="text-red-600 hover:text-red-800"
                                    >
                                        <FiTrash2 size={16} />
                                    </button>

                                    <button onClick={() => onView(client.id)} className="text-slate-600 hover:text-slate-800">
                                        <FiEye size={16} />
                                    </button>

                                    <button onClick={() => onCOrder(client)} className="text-slate-600 hover:text-slate-800">
                                        <ShoppingBasket size={16} />
                                    </button>
                                    <button onClick={() => onCDevis(client)} className="text-slate-600 hover:text-slate-800">
    <FileText size={16} />
</button>

                                    <button
                                      onClick={()=> handleDoc(client.id)}
                                        className="w-9 h-9 flex items-center justify-center rounded-lg bg-blue-600 hover:bg-blue-700 text-white"
                                        title="Ajouter document"
                                    >
                                        <FiUpload size={16} />
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default ClientTable;