import { useState } from "react";
import { useForm } from "react-hook-form";
import { AiOutlineLogin } from "react-icons/ai";
import { Link, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import Spinners from "../shared/Spinners";
import InputField from "../shared/InputField";
import { useDispatch, useSelector } from "react-redux";
import { authenticateSignInUser } from "../../store/reducers/actions";
import { FiEye, FiEyeOff } from "react-icons/fi";
import { Glasses } from "lucide-react";

const LogIn = () => {
    const [loader, setLoader] = useState(false);
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { user } = useSelector((state) => state.auth);
    const [showPassword, setShowPassword] = useState(false);

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors },
    } = useForm({
        mode: "onTouched",
    });

    const loginHandler = async (data) => {
        dispatch(authenticateSignInUser(data, toast, reset, navigate, setLoader));
    };

    return (
        <div className="min-h-screen bg-[#0B1220] font-body">
            {/* Header */}
            <header className="border-b border-white/10">
                <div className="mx-auto flex h-[72px] max-w-7xl items-center px-5 sm:px-6">
                    <Link
                        to="/"
                        className="group flex items-center gap-2.5 font-display text-lg font-bold tracking-tight text-white"
                    >
                        <span className="flex h-9 w-9 items-center justify-center rounded-lg bg-cyan-400 text-slate-950 transition-transform duration-300 group-hover:-rotate-6">
                            <Glasses className="h-4.5 w-4.5" strokeWidth={2.2} />
                        </span>
                        Cad-Optique
                    </Link>
                </div>
            </header>

            {/* Main */}
            <div className="relative flex min-h-[calc(100vh-72px)] items-center justify-center overflow-hidden px-4 py-10">
                {/* Ambient glow */}
                <div className="pointer-events-none absolute left-1/2 top-0 h-[500px] w-[900px] -translate-x-1/2 rounded-full bg-cyan-500/10 blur-[120px]" />

                <div className="relative grid w-full max-w-4xl overflow-hidden rounded-3xl border border-white/10 bg-white shadow-2xl shadow-black/40 lg:grid-cols-[1fr_1.05fr]">
                    {/* Brand panel */}
                    <section className="relative hidden flex-col items-center justify-center overflow-hidden bg-[#0B1220] px-10 py-12 text-center text-white lg:flex">
                        {/* Lens motif signature — sits behind the copy, clear of the text */}
                        <svg
                            viewBox="0 0 240 100"
                            className="pointer-events-none absolute left-1/2 top-1/2 h-auto w-[320px] -translate-x-1/2 -translate-y-1/2 opacity-25"
                            fill="none"
                        >
                            <circle cx="60" cy="50" r="46" stroke="#22D3EE" strokeWidth="1.5" />
                            <circle cx="178" cy="50" r="46" stroke="#22D3EE" strokeWidth="1.5" />
                            <path d="M106 50 H132" stroke="#22D3EE" strokeWidth="1.5" />
                        </svg>

                        <div className="relative">
                            <p className="mb-4 text-[11px] font-semibold uppercase tracking-[0.25em] text-cyan-300">
                                Espace pro
                            </p>
                            <h1 className="mx-auto max-w-[300px] font-display text-3xl font-bold leading-[1.15]">
                                Votre magasin, en un regard
                            </h1>
                        </div>

                        <p className="absolute bottom-12 text-xs font-medium text-white/40">
                            Cad-Optique
                        </p>
                    </section>

                    {/* Form panel */}
                    <form onSubmit={handleSubmit(loginHandler)} className="px-6 py-10 sm:px-10 sm:py-12">
                        <div className="mb-8 flex items-center gap-3">
                            <span className="flex h-11 w-11 shrink-0 items-center justify-center rounded-xl bg-blue-50 text-blue-900">
                                <AiOutlineLogin className="text-xl" />
                            </span>
                            <div>
                                <h2 className="font-display text-xl font-bold text-slate-900">
                                    Connexion
                                </h2>
                                <p className="text-sm text-slate-400">Accédez à votre espace</p>
                            </div>
                        </div>

                        <div className="flex flex-col gap-4">
                            <InputField
                                label="Identifiant"
                                required
                                id="username"
                                type="text"
                                message="Champ requis"
                                placeholder="Nom d'utilisateur ou e-mail"
                                register={register}
                                errors={errors}
                            />

                            <div className="relative">
                                <InputField
                                    label="Mot de passe"
                                    required
                                    id="password"
                                    type={showPassword ? "text" : "password"}
                                    message="Champ requis"
                                    placeholder="••••••••"
                                    register={register}
                                    errors={errors}
                                />
                                <button
                                    type="button"
                                    onClick={() => setShowPassword(!showPassword)}
                                    className="absolute right-3 top-9 rounded-md p-1 text-slate-400 transition-colors hover:text-blue-900"
                                    aria-label={showPassword ? "Masquer le mot de passe" : "Afficher le mot de passe"}
                                >
                                    {showPassword ? <FiEyeOff /> : <FiEye />}
                                </button>
                            </div>
                        </div>

                        <button
                            disabled={loader}
                            type="submit"
                            className="mt-7 flex w-full items-center justify-center gap-2 rounded-xl bg-blue-900 py-3.5 font-semibold text-white transition-all duration-200 hover:-translate-y-0.5 hover:bg-blue-800 hover:shadow-lg hover:shadow-blue-900/25 disabled:cursor-not-allowed disabled:opacity-60 disabled:hover:translate-y-0"
                        >
                            {loader ? (
                                <>
                                    <Spinners /> Connexion...
                                </>
                            ) : (
                                "Se connecter"
                            )}
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default LogIn;