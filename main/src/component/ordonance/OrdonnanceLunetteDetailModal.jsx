import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from "@headlessui/react";
import { FaTimes } from "react-icons/fa";
import { Eye, FileText, CalendarClock } from "lucide-react";

const InfoRow = ({ icon: Icon, label, value }) => (
    <div className="flex items-start gap-3 py-2.5">
        <Icon size={16} className="text-slate-400 mt-0.5 shrink-0" />
        <div className="flex flex-col">
            <span className="text-xs text-slate-400">{label}</span>
            <span className="text-sm text-slate-800 font-medium">{value || "—"}</span>
        </div>
    </div>
);

const OrdonnanceLunetteDetailModal = ({ open, setOpen, ordonnance }) => {
    if (!ordonnance) return null;

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
                                Ordonnance lunette de {ordonnance.clientDTO?.nom} {ordonnance.clientDTO?.prenom}
                            </DialogTitle>
                            <span className="text-xs text-slate-300">{ordonnance.prescripteur}</span>
                        </div>

                        <div className="px-6 py-4">
                            <div className="grid grid-cols-2 gap-x-6">
                                <div>
                                    <span className="text-xs font-semibold text-slate-500 uppercase">Œil droit (OD)</span>
                                    <InfoRow icon={Eye} label="Sphère" value={ordonnance.sphereOd} />
                                    <InfoRow icon={Eye} label="Cylindre" value={ordonnance.cylindreOd} />
                                    <InfoRow icon={Eye} label="Axe" value={ordonnance.axeOd} />
                                    <InfoRow icon={Eye} label="Addition" value={ordonnance.additionOd} />
                                </div>
                                <div>
                                    <span className="text-xs font-semibold text-slate-500 uppercase">Œil gauche (OG)</span>
                                    <InfoRow icon={Eye} label="Sphère" value={ordonnance.sphereOg} />
                                    <InfoRow icon={Eye} label="Cylindre" value={ordonnance.cylindreOg} />
                                    <InfoRow icon={Eye} label="Axe" value={ordonnance.axeOg} />
                                    <InfoRow icon={Eye} label="Addition" value={ordonnance.additionOg} />
                                </div>
                            </div>

                            <div className="border-t border-slate-100 mt-2">
                                <InfoRow icon={FileText} label="Date émission" value={ordonnance.dateEmission} />
                                <InfoRow icon={CalendarClock} label="Date expiration" value={ordonnance.dateExpiration} />
                                <InfoRow icon={CalendarClock} label="Created at" value={ordonnance.createdAt} />

                                
                            </div>
                        </div>
                    </DialogPanel>
                </div>
            </div>
        </Dialog>
    );
};

export default OrdonnanceLunetteDetailModal;