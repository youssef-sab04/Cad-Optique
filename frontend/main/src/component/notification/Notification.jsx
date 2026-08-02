import { useSelector , useDispatch } from "react-redux";
import { useState, useEffect } from "react";

import { AlertTriangle, Bell, Users } from "lucide-react";
import Loader from "../shared/Loader";
import Paginations from "../shared/Paginations";
import NotificationP from "./NotificationP";
import NotificationC from "./NotificationC";
import useNotificationsFilter from "./useNotificationsFilter";
import { fetchNotifClients, fetchNotifProd } from "../../store/reducers/actions";
import NotificationFilter from "./NotificationFilter";
const Notification = () => {
    const [activeTab, setActiveTab] = useState("prod");
    const { isLoading, errorMessage } = useSelector((state) => state.errors);
    const { notificationsP, paginationP } = useSelector((state) => state.notifications);
    const { notificationsC, paginationC } = useSelector((state) => state.notifications);


    useNotificationsFilter();
    const dispatch = useDispatch();

       useNotificationsFilter(activeTab === "prod" ? "produit" : "client");



    const tabs = [
        {
            key: "prod",
            label: "Notifications Produits",
            description: "Suivi des alertes liées au catalogue et aux stocks.",
            icon: Bell,
        },
        {
            key: "client",
            label: "Notifications Clients",
            description: "Messages et événements importants côté relation client.",
            icon: Users,
        },
    ];

    const currentTab = tabs.find((tab) => tab.key === activeTab) || tabs[0];

    return (
        <div className="px-4 py-6 sm:px-6 sm:py-8">
            
            <div className="mb-6 rounded-3xl border border-slate-200 bg-white p-2 shadow-sm">
                <div className="grid gap-2 sm:grid-cols-2">
                    {tabs.map((tab) => {
                        const Icon = tab.icon;
                        const isActive = activeTab === tab.key;

                        return (
                            <button
                                key={tab.key}
                                onClick={() => setActiveTab(tab.key)}
                                className={`group flex items-center gap-3 rounded-2xl px-4 py-4 text-left transition-all duration-200 ${
                                    isActive
                                        ? "bg-slate-900 text-white shadow-lg shadow-slate-900/20"
                                        : "bg-slate-50 text-slate-600 hover:bg-slate-100 hover:text-slate-900"
                                }`}
                            >
                                <span
                                    className={`flex h-11 w-11 items-center justify-center rounded-xl transition-colors ${
                                        isActive ? "bg-white/10" : "bg-white"
                                    }`}
                                >
                                    <Icon className={`h-5 w-5 ${isActive ? "text-white" : "text-slate-500"}`} />
                                </span>
                                <span className="min-w-0 flex-1">
                                    <span className="block text-sm font-semibold">{tab.label}</span>
                                    <span className={`block text-xs leading-5 ${isActive ? "text-slate-200" : "text-slate-500"}`}>
                                        {tab.description}
                                    </span>
                                </span>
                            </button>
                        );
                    })}
                </div>
            </div>

              <div className="pd-9 mt-9">
                                            <NotificationFilter/>
    
                    </div>
                    

            {isLoading ? (
                <Loader />
            ) : errorMessage ? (
                <div className="rounded-3xl border border-amber-200 bg-amber-50/80 px-6 py-10 text-center shadow-sm">
                    <div className="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-full bg-amber-100 text-amber-700">
                        <AlertTriangle className="h-6 w-6" />
                    </div>
                    <p className="text-base font-semibold text-slate-900">Une erreur est survenue</p>
                    <p className="mt-2 text-sm text-slate-600">{errorMessage}</p>
                </div>
            ) : (
                <div className="rounded-3xl border border-slate-200 bg-linear-to-br from-white to-slate-50 p-8 shadow-sm">
                    <div className="flex flex-col gap-3 sm:flex-row sm:items-end sm:justify-between">
                        <div>
                            <p className="text-sm font-medium uppercase tracking-[0.2em] text-slate-400">
                                Vue active
                            </p>
                            <h2 className="mt-1 text-xl font-semibold text-slate-900">
                                {currentTab.label}
                            </h2>
                        </div>
                        <span className="inline-flex w-fit items-center rounded-full bg-slate-900 px-3 py-1 text-xs font-semibold text-white">
                            {currentTab.key === "prod" ? "Produits" : "Clients"}
                        </span>
                    </div>
                  

                   

                    <div className="mt-8">
                        {activeTab === "prod" ? 
                        <NotificationP
                        data={notificationsP} /> 
                        : 
                        <NotificationC 
                        data={notificationsC}/>}
                    </div>

                    <div className="mt-8 flex justify-center">
                        <Paginations
                            numberOfPage={activeTab === "client" ? paginationC.totalPages : paginationP.totalPages}
                            totalProducts={activeTab === "client" ? paginationC.totalElements : paginationP.totalElements}
                        />
                    </div>
                </div>
            )}
        </div>
    );
};

export default Notification;
