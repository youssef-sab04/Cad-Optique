import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from "@headlessui/react";
import { FaTimes } from "react-icons/fa";
import { Mail, MapPin, Phone, CalendarDays, Truck } from "lucide-react";

const InfoRow = ({ icon: Icon, label, value }) => (
    <div className="flex items-center gap-3 rounded-xl border border-slate-100 bg-slate-50/60 px-4 py-3">
        <Icon size={15} className="shrink-0 text-indigo-500" />
        <div className="flex flex-col">
            <span className="text-[11px] text-slate-400">{label}</span>
            <span className="text-sm font-medium text-slate-800">{value || "—"}</span>
        </div>
    </div>
);

const FournisseurDetailModal = ({ open, setOpen, fournisseur }) => {
    if (!fournisseur) return null;

    return (
        <Dialog open={open} onClose={() => setOpen(false)} className="relative z-50">
            <DialogBackdrop className="fixed inset-0 bg-slate-950/60 backdrop-blur-sm" />
            <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4">
                    <DialogPanel className="relative w-full max-w-md overflow-hidden rounded-2xl bg-white shadow-2xl">
                        <button
                            onClick={() => setOpen(false)}
                            className="absolute right-4 top-4 z-10 flex h-8 w-8 items-center justify-center rounded-full text-white/80 hover:bg-white/10"
                        >
                            <FaTimes size={13} />
                        </button>

                        <div className="bg-gradient-to-br from-indigo-600 to-indigo-800 px-6 py-6 text-white">
                            <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-white/15">
                                <Truck size={20} />
                            </div>
                            <DialogTitle className="mt-3 text-lg font-bold">{fournisseur.nom}</DialogTitle>
                            <span className="text-xs text-indigo-100">Fournisseur #{fournisseur.id}</span>
                        </div>

                        <div className="flex flex-col gap-3 px-6 py-5">
                            <InfoRow icon={Phone} label="Téléphone" value={fournisseur.phoneNumber} />
                            <InfoRow icon={Mail} label="Email" value={fournisseur.email} />
                            <InfoRow icon={MapPin} label="Adresse" value={fournisseur.adresse} />
                            <InfoRow
                                icon={CalendarDays}
                                label="Ajouté le"
                                value={
                                    fournisseur.createdAt
                                        ? new Date(fournisseur.createdAt).toLocaleDateString("fr-FR")
                                        : "—"
                                }
                            />
                        </div>
                    </DialogPanel>
                </div>
            </div>
        </Dialog>
    );
};

export default FournisseurDetailModal;
