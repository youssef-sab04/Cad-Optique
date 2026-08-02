import {
    Dialog,
    DialogBackdrop,
    DialogPanel,
    DialogTitle,
} from "@headlessui/react";
import { Clock3, Info, Package, X } from "lucide-react";

const NotificationPModal = ({
    open,
    notification,
    getTypeMeta,
    onClose,
    formatDateTime,
}) => {
    if (!notification) {
        return null;
    }

    const meta = getTypeMeta(notification.type);

    return (
        <Dialog open={open} onClose={onClose} className="relative z-50">
            <DialogBackdrop
                transition
                className="fixed inset-0 bg-slate-950/55 backdrop-blur-sm transition-opacity data-closed:opacity-0 data-enter:duration-300 data-leave:duration-200"
            />

            <div className="fixed inset-0 z-10 overflow-y-auto px-4 py-8 sm:px-6 lg:px-8">
                <div className="flex min-h-full items-center justify-center">
                    <DialogPanel
                        transition
                        className="w-full max-w-xl overflow-hidden rounded-3xl bg-white shadow-2xl transition-all data-closed:translate-y-4 data-closed:opacity-0 data-enter:duration-300 data-leave:duration-200 data-closed:sm:scale-95"
                    >
                        <div className="flex items-start justify-between border-b border-slate-100 px-6 py-5">
                            <div>
                                <DialogTitle className="text-lg font-semibold text-slate-900">
                                    Détail de la notification
                                </DialogTitle>
                                <p className="mt-1 text-sm text-slate-500">
                                    {meta.label}
                                </p>
                            </div>
                            <button
                                type="button"
                                onClick={onClose}
                                className="rounded-full p-2 text-slate-400 transition-colors hover:bg-slate-100 hover:text-slate-700"
                            >
                                <span className="sr-only">Fermer</span>
                                <X className="h-5 w-5" />
                            </button>
                        </div>

                        <div className="space-y-4 px-6 py-6">
                            <div className="rounded-2xl border border-slate-200 bg-slate-50 p-4">
                                <div className="flex items-center gap-2 text-xs font-semibold uppercase tracking-[0.18em] text-slate-400">
                                    <Info className="h-4 w-4" />
                                    Message
                                </div>
                                <p className="mt-3 text-sm leading-7 text-slate-700">
                                    {notification.message}
                                </p>
                            </div>

                            <div className="grid gap-4 sm:grid-cols-2">
                                <div className="rounded-2xl bg-white p-4 ring-1 ring-slate-200">
                                    <p className="text-xs font-semibold uppercase tracking-[0.18em] text-slate-400">
                                        Type
                                    </p>
                                    <p className="mt-2 text-sm font-medium text-slate-900">
                                        {meta.label}
                                    </p>
                                </div>
                                <div className="rounded-2xl bg-white p-4 ring-1 ring-slate-200">
                                    <p className="text-xs font-semibold uppercase tracking-[0.18em] text-slate-400">
                                        Statut
                                    </p>
                                    <p className="mt-2 text-sm font-medium text-slate-900">
                                        {notification._read ? "Lue" : "Non lue"}
                                    </p>
                                </div>
                            </div>

                            <div className="grid gap-4 sm:grid-cols-2">
                                <div className="rounded-2xl border border-slate-200 bg-slate-50 p-4">
                                    <div className="flex items-center gap-2 text-xs font-semibold uppercase tracking-[0.18em] text-slate-400">
                                        <Clock3 className="h-4 w-4" />
                                        Date
                                    </div>
                                    <p className="mt-2 text-sm font-medium text-slate-900">
                                        {formatDateTime(notification.createdAt)}
                                    </p>
                                </div>
                                <div className="rounded-2xl border border-slate-200 bg-slate-50 p-4">
                                    <div className="flex items-center gap-2 text-xs font-semibold uppercase tracking-[0.18em] text-slate-400">
                                        <Package className="h-4 w-4" />
                                        Identifiant
                                    </div>
                                    <p className="mt-2 text-sm font-medium text-slate-900">
                                        #{notification.id}
                                    </p>
                                </div>
                            </div>
                        </div>
                    </DialogPanel>
                </div>
            </div>
        </Dialog>
    );
};

export default NotificationPModal;
