import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from "@headlessui/react";
import { FaTimes } from "react-icons/fa";
import { Phone, Mail, MapPin, Cake, ShieldCheck, CalendarPlus } from "lucide-react";

const InfoRow = ({ icon: Icon, label, value }) => (
    <div className="flex items-start gap-3 py-2.5">
        <Icon size={16} className="text-slate-400 mt-0.5 shrink-0" />
        <div className="flex flex-col">
            <span className="text-xs text-slate-400">{label}</span>
            <span className="text-sm text-slate-800 font-medium">{value || "—"}</span>
        </div>
    </div>
);

const ClientDetailModal = ({ open, setOpen, client }) => {
    if (!client) return null;

    const initials = `${client.nom?.[0] ?? ""}${client.prenom?.[0] ?? ""}`.toUpperCase();

    return (
        <Dialog open={open} onClose={() => setOpen(false)} className="relative z-50">
            <DialogBackdrop className="fixed inset-0 bg-black/40" />
            <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4">
                    <DialogPanel className="relative bg-white rounded-xl shadow-xl w-full max-w-md overflow-hidden">
                        <button
                            onClick={() => setOpen(false)}
                            className="absolute right-4 top-4 text-slate-400 hover:text-slate-600"
                        >
                            <FaTimes size={16} />
                        </button>

                        <div className="bg-slate-900 px-6 py-5 flex items-center gap-3">
                            <div className="w-11 h-11 rounded-full bg-white/10 flex items-center justify-center text-white font-semibold">
                                {initials}
                            </div>
                            <div>
                                <DialogTitle className="text-base font-bold text-white">
                                    {client.nom} {client.prenom}
                                </DialogTitle>
                                <span className="text-xs text-slate-300">Client #{client.id}</span>
                            </div>
                        </div>

                        <div className="px-6 py-4 divide-y divide-slate-100">
                            <InfoRow icon={Phone} label="Téléphone" value={client.phoneNumber} />
                            <InfoRow icon={Mail} label="Email" value={client.email} />
                            <InfoRow icon={MapPin} label="Adresse" value={client.adresse} />
                            <InfoRow icon={Cake} label="Date de naissance" value={client.dateNaissance} />
                            <InfoRow icon={ShieldCheck} label="Mutuelle" value={client.mutuelle} />
                            <InfoRow icon={CalendarPlus} label="Client depuis" value={client.createdAt?.split("T")[0]} />
                        </div>
                    </DialogPanel>
                </div>
            </div>
        </Dialog>
    );
};

export default ClientDetailModal;