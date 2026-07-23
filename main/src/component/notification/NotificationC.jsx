import { useState } from "react";
import {Bell, CalendarClock, CheckCircle2, Layers3, Package, Search, ShieldAlert} from "lucide-react";
import NotificationPModal from "./NotificationPModal";
import { typeMeta , toneStyles } from "../utils";
import { useDispatch } from "react-redux";
import { viewNotification } from "../../store/reducers/actions";
import toast from "react-hot-toast";



const formatDateTime = (value) => {
    if (!value) {
        return "—";
    }


   

    return new Intl.DateTimeFormat("fr-FR", {
        dateStyle: "medium",
        timeStyle: "short",
    }).format(new Date(value));
};

const NotificationC = ({ data, title = "Notifications clients" }) => {

    const dispatch = useDispatch();

    const [selectedNotification, setSelectedNotification] = useState(null);
    const rows = Array.isArray(data) ? data : [];


     const onNotificationClick  = (id  )  => {
        dispatch(viewNotification(id , toast))

    }

    const getTypeMeta = (type) =>
        typeMeta[type] || {
            label: type || "Inconnu",
            tone: "slate",
            icon: Package,
        };

    return (
        <>
            <div className="overflow-hidden rounded-3xl border border-slate-200 bg-white shadow-sm">
                <div className="flex flex-col gap-4 border-b border-slate-100 px-5 py-5 sm:flex-row sm:items-center sm:justify-between">
                    <div>
                        <div className="inline-flex items-center gap-2 rounded-full bg-emerald-50 px-3 py-1 text-xs font-semibold uppercase tracking-[0.2em] text-emerald-700">
                            <Layers3 className="h-3.5 w-3.5" />
                            {title}
                        </div>
                        <h3 className="mt-3 text-lg font-semibold text-slate-900">Alerte stock</h3>
                        <p className="mt-1 text-sm text-slate-500">Notifications produits récentes.</p>
                    </div>

                    <div className="grid grid-cols-2 gap-3 sm:w-auto sm:grid-cols-2">
                        <div className="rounded-2xl bg-slate-50 px-4 py-3">
                            <p className="text-xs font-medium uppercase tracking-[0.18em] text-slate-400">Total</p>
                            <p className="mt-1 text-2xl font-semibold text-slate-900">{rows.length}</p>
                        </div>
                        <div className="rounded-2xl bg-slate-50 px-4 py-3">
                            <p className="text-xs font-medium uppercase tracking-[0.18em] text-slate-400">Non lues</p>
                            <p className="mt-1 text-2xl font-semibold text-slate-900">
                                {rows.filter((item) => item.status !== "LUE").length}
                            </p>
                        </div>
                    </div>
                </div>

                <div className="overflow-x-auto">
                    <table className="min-w-full divide-y divide-slate-100">
                        <thead className="bg-slate-50/80">
                            <tr>
                                <th className="px-5 py-4 text-left text-xs font-semibold uppercase tracking-[0.18em] text-slate-500">
                                    Type
                                </th>
                                <th className="px-5 py-4 text-left text-xs font-semibold uppercase tracking-[0.18em] text-slate-500">
                                    Client
                                </th>
                                <th className="px-5 py-4 text-left text-xs font-semibold uppercase tracking-[0.18em] text-slate-500">
                                    Date
                                </th>
                                <th className="px-5 py-4 text-left text-xs font-semibold uppercase tracking-[0.18em] text-slate-500">
                                    État
                                </th>
                                <th className="px-5 py-4 text-right text-xs font-semibold uppercase tracking-[0.18em] text-slate-500">
                                    Détail
                                </th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-slate-100 bg-white">
                            {rows.map((notification) => {
                                const meta = getTypeMeta(notification.type);
                                const ToneIcon = meta.icon;
                                const tone = toneStyles[meta.tone] || toneStyles.slate;

                                return (
                                    <tr key={notification.id} className="group hover:bg-slate-50/70">
                                        <td className="px-5 py-4 align-top">
                                            <div className={`inline-flex items-center gap-2 rounded-full px-3 py-1.5 text-xs font-semibold ${tone.badge}`}>
                                                <span className={`h-2 w-2 rounded-full ${tone.dot}`} />
                                                <ToneIcon className="h-3.5 w-3.5" />
                                                {meta.label}
                                            </div>
                                        </td>
                                        <td className="px-5 py-4 align-top">
                                            <div className="max-w-xl">
                                                <p className="text-sm font-medium leading-6 text-slate-900">
                                                    {notification?.clientDTO?.nom} {notification?.clientDTO?.prenom}
                                                </p>
                                                <p className="mt-1 text-xs text-slate-500">
                                                    ID notification #{notification.id}
                                                </p>
                                            </div>
                                        </td>
                                        <td className="px-5 py-4 align-top text-sm text-slate-600">
                                            <div className="flex items-center gap-2">
                                                <CalendarClock className="h-4 w-4 text-slate-400" />
                                                {formatDateTime(notification.createdAt)}
                                            </div>
                                        </td>
                                        <td className="px-5 py-4 align-top">
                                            {notification._read == true ? (
                                                <span className="inline-flex items-center gap-1.5 rounded-full bg-emerald-50 px-3 py-1.5 text-xs font-semibold text-emerald-700 ring-1 ring-emerald-200">
                                                    <CheckCircle2 className="h-3.5 w-3.5" />
                                                    Lue
                                                </span>
                                            ) : (
                                                <span className="inline-flex items-center gap-1.5 rounded-full bg-sky-50 px-3 py-1.5 text-xs font-semibold text-sky-700 ring-1 ring-sky-200">
                                                    <Bell className="h-3.5 w-3.5" />
                                                    Non lue
                                                </span>
                                            )}
                                        </td>
                                        <td className="px-5 py-4 align-top text-right">
                                            <button
                                                type="button"
                                                onClick={() => {
                                                    onNotificationClick(notification.id);
                                                    setSelectedNotification(notification);
                                                }} className="inline-flex items-center gap-2 rounded-full border border-slate-200 bg-white px-4 py-2 text-sm font-medium text-slate-700 transition-colors hover:border-slate-300 hover:bg-slate-900 hover:text-white"
                                            >
                                                <Search className="h-4 w-4" />
                                                Voir détail
                                            </button>
                                        </td>
                                    </tr>
                                );
                            })}
                        </tbody>
                    </table>
                </div>
            </div>

            <NotificationPModal
                open={Boolean(selectedNotification)}
                notification={selectedNotification}
                getTypeMeta={getTypeMeta}
                onClose={() => setSelectedNotification(null)}
                formatDateTime={formatDateTime}
            />
        </>
    );
};

export default NotificationC;