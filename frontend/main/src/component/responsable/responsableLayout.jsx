import { useState } from "react";
import { NavLink, Outlet , useNavigate } from "react-router-dom";
import { useSelector , useDispatch} from 'react-redux'
import { logOutUser } from "../../store/reducers/actions";
import {
    Bell, LogOut, Menu, X, Glasses, ChevronDown, ChevronsLeft, Search,
} from "lucide-react";
import HeaderUser from "../headerUser";
import { NAV_SECTIONS } from "../utils";

import SidebarContent from "../SidebarContent";

export default function DashboardLayout({ role = "admin", userName = "Utilisateur" }) {
    const [mobileOpen, setMobileOpen] = useState(false);
    const [collapsed, setCollapsed] = useState(false);
    const [userMenuOpen, setUserMenuOpen] = useState(false);
    const sections = NAV_SECTIONS[role] ?? NAV_SECTIONS.admin;
    const { user } = useSelector((state) => state.auth);

   const dispatch = useDispatch();
    const navigate = useNavigate();


    const logOutHandler = () => {
        console.log("5rg ")
        dispatch(logOutUser(navigate));
    };


    return (
        <div className="flex min-h-screen bg-slate-50">
            {/* SIDEBAR - desktop */}
            <aside
                className={`relative hidden shrink-0 flex-col bg-slate-950 transition-all duration-300 lg:flex ${collapsed ? "w-[76px]" : "w-64"
                    }`}
            >
                <div className="flex h-16 items-center gap-2 border-b border-white/5 px-5">
                    <span className="flex h-9 w-9 shrink-0 items-center justify-center rounded-full bg-gradient-to-br from-blue-500 to-blue-800 text-white shadow-lg shadow-blue-900/40">
                        <Glasses className="h-5 w-5" />
                    </span>
                    {!collapsed && (
                        <span className="font-display text-lg font-bold text-white">Cad-Optique</span>
                    )}
                </div>

                <SidebarContent sections={sections} collapsed={collapsed} />

                <div className="border-t border-white/5 p-3">
                    <button className="flex w-full items-center gap-3 rounded-lg px-3 py-2.5 font-body text-sm font-medium text-slate-500 transition-colors duration-200 hover:bg-red-500/10 hover:text-red-400">
                        <LogOut className="h-[18px] w-[18px] shrink-0" />
                        {!collapsed && "Déconnexion"}
                    </button>
                </div>

                <button
                    onClick={() => setCollapsed((c) => !c)}
                    className="absolute -right-3 top-16 flex h-6 w-6 items-center justify-center rounded-full border border-slate-700 bg-slate-900 text-slate-400 shadow-md transition-colors hover:text-white"
                >
                    <ChevronsLeft
                        className={`h-3.5 w-3.5 transition-transform duration-300 ${collapsed ? "rotate-180" : ""}`}
                    />
                </button>
            </aside>

            {/* SIDEBAR - mobile drawer */}
            {mobileOpen && (
                <div className="fixed inset-0 z-50 lg:hidden">
                    <div
                        className="absolute inset-0 bg-slate-900/50 backdrop-blur-sm"
                        onClick={() => setMobileOpen(false)}
                    />
                    <aside className="relative flex h-full w-64 flex-col bg-slate-950 shadow-2xl">
                        <div className="flex h-16 items-center justify-between border-b border-white/5 px-5">
                            <div className="flex items-center gap-2">
                                <span className="flex h-9 w-9 items-center justify-center rounded-full bg-gradient-to-br from-blue-500 to-blue-800 text-white">
                                    <Glasses className="h-5 w-5" />
                                </span>
                                <span className="font-display text-lg font-bold text-white">Cad-Optique</span>
                            </div>
                            <button onClick={() => setMobileOpen(false)} aria-label="Fermer le menu">
                                <X className="h-5 w-5 text-slate-400" />
                            </button>
                        </div>
                        <SidebarContent sections={sections} collapsed={false} onNavigate={() => setMobileOpen(false)} />
                        <div className="border-t border-white/5 p-3">
                            <button className="flex w-full items-center gap-3 rounded-lg px-3 py-2.5 font-body text-sm font-medium text-slate-500 hover:bg-red-500/10 hover:text-red-400"
                             onClick={logOutHandler}>

                                <LogOut className="h-[18px] w-[18px]" />
                                Déconnexion
                            </button>
                        </div>
                    </aside>
                </div>
            )}

            {/* MAIN */}
            <div className="flex min-w-0 flex-1 flex-col">
                {/* NAVBAR */}
                <HeaderUser
                    setMobileOpen={setMobileOpen}
                    userMenuOpen={userMenuOpen}
                    setUserMenuOpen={setUserMenuOpen}
                    role={role}
                    userName={userName}
                />


                {/* PAGE CONTENT */}
                <main className="flex-1 p-4 lg:p-8">
                    <Outlet />
                </main>
            </div>
        </div>
    );
}