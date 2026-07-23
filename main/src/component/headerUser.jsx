import { Bell, LogOut, Menu, Glasses , ChevronDown } from "lucide-react";
import { useDispatch, useSelector } from 'react-redux';
import { logOutUser } from "../store/reducers/actions";
import { useNavigate } from 'react-router-dom';





const HeaderUser = ({ setMobileOpen, userMenuOpen, setUserMenuOpen, role, userName }) => {

    const dispatch = useDispatch();
    const navigate = useNavigate();
        const { user } = useSelector((state) => state.auth);
        const isAdmin = user && user?.roles?.includes("ROLE_ADMIN");
        const isRespo= user && user?.roles.includes("ROLE_RESPONSABLE");
        //console.log(user)



    const logOutHandler = () => {
        dispatch(logOutUser(navigate));
    };




    return (
        <header className="sticky top-0 z-40 flex h-16 items-center justify-between gap-4 border-b border-slate-200 bg-white/90 px-4 backdrop-blur lg:px-8">
            <div className="flex items-center gap-4">
                <button
                    className="text-slate-600 lg:hidden"
                    onClick={() => setMobileOpen(true)}
                    aria-label="Ouvrir le menu"
                >
                    <Menu className="h-6 w-6" />
                </button>


            </div>

            <div className="flex items-center gap-4">
                <span className="hidden font-body text-xs font-semibold uppercase tracking-wide text-slate-400 sm:block">
                    { isAdmin ? "Espace Admin" : "Espace Responsable"}
                </span>

                <button className="relative text-slate-500 transition-colors hover:text-blue-900">
                    <Bell className="h-5 w-5" />
                    <span className="absolute -right-0.5 -top-0.5 h-2 w-2 rounded-full bg-red-500 ring-2 ring-white" />
                </button>

                <div className="relative">
                    <button
                        onClick={() => setUserMenuOpen((o) => !o)}
                        className="flex items-center gap-2 rounded-full border border-slate-200 py-1.5 pl-1.5 pr-3 transition-colors hover:bg-slate-50"
                    >
                        <span className="flex h-7 w-7 items-center justify-center rounded-full bg-blue-900 font-body text-xs font-semibold text-white">
                            {user?.username?.charAt(0).toUpperCase()}
                        </span>
                        <span className="hidden font-body text-sm font-medium text-slate-700 sm:block">
                            {user?.username}
                        </span>
                        <ChevronDown
                            className={`h-4 w-4 text-slate-400 transition-transform duration-200 ${userMenuOpen ? "rotate-180" : ""
                                }`}
                        />
                    </button>

                    {userMenuOpen && (
                        <div className="absolute right-0 mt-2 w-44 overflow-hidden rounded-lg border border-slate-200 bg-white shadow-lg">
                            <button className="flex w-full items-center gap-2 px-4 py-2.5 font-body text-sm text-slate-600 hover:bg-slate-50">
                                Mon profil
                            </button>
                            <button className="flex w-full items-center gap-2 px-4 py-2.5 font-body text-sm text-red-600 hover:bg-red-50"
                                                onClick={logOutHandler}>

                                <LogOut className="h-4 w-4" />
                                Déconnexion
                            </button>
                        </div>
                    )}
                </div>
            </div>
        </header>
    )
}

export default HeaderUser
