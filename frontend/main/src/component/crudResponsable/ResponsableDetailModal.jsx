import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from "@headlessui/react";
import { FaTimes } from "react-icons/fa";
import { Mail, MapPin, Phone, UserCog } from "lucide-react";

const ResponsableDetailModal = ({ open, setOpen, responsable }) => {
    if (!responsable) return null;

    return (
        <Dialog open={open} onClose={() => setOpen(false)} className="relative z-50">
            <DialogBackdrop className="fixed inset-0 bg-slate-950/60 backdrop-blur-sm" />
            <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4">
                    <DialogPanel className="relative w-full max-w-lg overflow-hidden rounded-2xl bg-white shadow-2xl">
                        <button
                            onClick={() => setOpen(false)}
                            className="absolute right-4 top-4 z-10 flex h-8 w-8 items-center justify-center rounded-full text-slate-400 hover:bg-slate-100 hover:text-slate-700"
                        >
                            <FaTimes size={13} />
                        </button>

                        <div className="flex items-center gap-3 border-b border-slate-100 bg-gradient-to-r from-violet-50 to-white px-6 py-5">
                            <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-violet-600 text-white shadow-md shadow-violet-200">
                                <UserCog size={18} />
                            </div>
                            <div>
                                <DialogTitle className="text-base font-bold text-slate-800">
                                    Détails du responsable
                                </DialogTitle>
                                <p className="text-xs text-slate-400">
                                    Profil complet du responsable
                                </p>
                            </div>
                        </div>

                        <div className="space-y-4 px-6 py-6 text-sm text-slate-700">
                            <div className="rounded-xl border border-slate-100 bg-slate-50 p-4">
                                <p className="text-xs font-semibold uppercase tracking-[0.2em] text-slate-400">Nom complet</p>
                                <p className="mt-1 text-lg font-semibold text-slate-800">
                                    {responsable.nom} {responsable.prenom}
                                </p>
                            </div>

                            <div className="grid gap-3 sm:grid-cols-2">
                                <div className="rounded-xl border border-slate-100 p-4">
                                    <div className="flex items-center gap-2 text-slate-500">
                                        <Mail size={14} />
                                        <span className="text-xs font-semibold uppercase tracking-[0.2em]">Email</span>
                                    </div>
                                    <p className="mt-2 break-all text-sm text-slate-700">{responsable.email || "—"}</p>
                                </div>
                                <div className="rounded-xl border border-slate-100 p-4">
                                    <div className="flex items-center gap-2 text-slate-500">
                                        <Phone size={14} />
                                        <span className="text-xs font-semibold uppercase tracking-[0.2em]">Téléphone</span>
                                    </div>
                                    <p className="mt-2 text-sm text-slate-700">{responsable.phoneNumber || "—"}</p>
                                </div>
                            </div>

                            <div className="rounded-xl border border-slate-100 p-4">
                                <div className="flex items-center gap-2 text-slate-500">
                                    <MapPin size={14} />
                                    <span className="text-xs font-semibold uppercase tracking-[0.2em]">Adresse</span>
                                </div>
                                <p className="mt-2 text-sm text-slate-700">{responsable.adresse || "—"}</p>
                            </div>
                        </div>
                    </DialogPanel>
                </div>
            </div>
        </Dialog>
    );
};

export default ResponsableDetailModal;
