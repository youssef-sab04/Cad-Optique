import { FiEdit2, FiTrash2, FiEye } from "react-icons/fi";

const OrdonnanceLentilleTable = ({ ordonnances, onEdit, onDelete, onView }) => {

    if (!ordonnances.length) {
        return (
            <div className="flex justify-center items-center h-[200px] text-slate-500">
                Aucune ordonnance trouvée.
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
                        <th className="px-4 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Date émission</th>
                        <th className="px-4 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Prescripteur</th>
                        <th className="px-4 py-3 text-right text-xs font-semibold text-slate-600 uppercase">Actions</th>
                    </tr>
                </thead>
                <tbody className="divide-y divide-slate-100 bg-white">
                    {ordonnances.map((ord) => (
                        <tr key={ord.id} className="hover:bg-slate-50 transition-colors">
                            <td className="px-4 py-3 text-sm text-slate-800 font-medium">{ord?.clientDTO?.nom}</td>
                            <td className="px-4 py-3 text-sm text-slate-700">{ord?.clientDTO?.prenom}</td>
                            <td className="px-4 py-3 text-sm text-slate-700">{ord.dateEmission || "—"}</td>
                            <td className="px-4 py-3 text-sm text-slate-700">{ord.prescripteur || "—"}</td>
                            <td className="px-4 py-3 text-right">
                                <div className="flex justify-end gap-3">
                                    <button onClick={() => onEdit(ord)} className="text-blue-600 hover:text-blue-800">
                                        <FiEdit2 size={16} />
                                    </button>
                                    <button onClick={() => onDelete(ord.id)} className="text-red-600 hover:text-red-800">
                                        <FiTrash2 size={16} />
                                    </button>
                                    <button onClick={() => onView(ord.id)} className="text-slate-600 hover:text-slate-800">
                                        <FiEye size={16} />
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

export default OrdonnanceLentilleTable;