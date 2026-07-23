// ExamenDetailModal.jsx
import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from "@headlessui/react";
import { FaTimes } from "react-icons/fa";
import { Calendar, Eye, FileText, CalendarClock } from "lucide-react";

const InfoRow = ({ icon: Icon, label, value }) => (
    <div className="flex items-start gap-3 py-2.5">
        <Icon size={16} className="text-slate-400 mt-0.5 shrink-0" />
        <div className="flex flex-col">
            <span className="text-xs text-slate-400">{label}</span>
            <span className="text-sm text-slate-800 font-medium">{value || "—"}</span>
        </div>
    </div>
);

const ExamenDetailModal = ({ open, setOpen, examen }) => {
    if (!examen) return null;

    return (
        <Dialog open={open} onClose={() => setOpen(false)} className="relative z-50">
            <DialogBackdrop className="fixed inset-0 bg-black/40" />
            <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4">
                    <DialogPanel className="relative bg-white rounded-xl shadow-xl w-full max-w-lg overflow-hidden">
                        <button
                            onClick={() => setOpen(false)}
                            className="absolute right-4 top-4 text-slate-400 hover:text-slate-600"
                        >
                            <FaTimes size={16} />
                        </button>

                        <div className="bg-slate-900 px-6 py-5">
                            <DialogTitle className="text-base font-bold text-white">
                                Examen de {examen.clientDTO?.nom} {examen.clientDTO?.prenom}
                            </DialogTitle>
                            <span className="text-xs text-slate-300">{examen.dateExamen}</span>
                        </div>

                        <div className="px-6 py-4">
                            <div className="grid grid-cols-2 gap-x-6">
                                <div>
                                    <span className="text-xs font-semibold text-slate-500 uppercase">Œil droit (OD)</span>
                                    <InfoRow icon={Eye} label="Sphère" value={examen.sphereOd} />
                                    <InfoRow icon={Eye} label="Cylindre" value={examen.cylindreOd} />
                                    <InfoRow icon={Eye} label="Axe" value={examen.axeOd} />
                                    <InfoRow icon={Eye} label="Écart" value={examen.ecartOd} />
                                </div>
                                <div>
                                    <span className="text-xs font-semibold text-slate-500 uppercase">Œil gauche (OG)</span>
                                    <InfoRow icon={Eye} label="Sphère" value={examen.sphereOg} />
                                    <InfoRow icon={Eye} label="Cylindre" value={examen.cylindreOg} />
                                    <InfoRow icon={Eye} label="Axe" value={examen.axeOg} />
                                    <InfoRow icon={Eye} label="Écart" value={examen.ecartOg} />
                                </div>
                            </div>

                            <div className="border-t border-slate-100 mt-2">
                                <InfoRow icon={FileText} label="Addition" value={examen.addition} />
                                <InfoRow icon={FileText} label="Remarques" value={examen.remarques} />
                                <InfoRow icon={CalendarClock} label="Prochaine visite" value={examen.prochaineVisite} />
                            </div>
                        </div>
                    </DialogPanel>
                </div>
            </div>
        </Dialog>
    );
};

export default ExamenDetailModal;