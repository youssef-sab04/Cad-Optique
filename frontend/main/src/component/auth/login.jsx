import { useState } from "react";
import { useForm } from "react-hook-form";
import { AiOutlineLogin } from "react-icons/ai";
import { Link, useNavigate, Navigate } from "react-router-dom";
import toast from "react-hot-toast";
import Spinners from "../shared/Spinners";
import InputField from "../shared/InputField";
import { useDispatch, useSelector } from "react-redux";
import { authenticateSignInUser } from "../../store/reducers/actions";
import { FiEye, FiEyeOff } from "react-icons/fi";

import {
    Glasses
} from "lucide-react";

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
        console.log("Login Click");
        dispatch(authenticateSignInUser(data, toast, reset, navigate, setLoader))
    };

    console.log(user)
    return (
        <div className="min-h-screen bg-slate-950 font-body text-slate-900">
            <header className="border-b border-white/10 bg-slate-950">
                <div className="mx-auto flex h-[76px] max-w-7xl items-center justify-between px-5 sm:px-6">
                    <Link to="/"
                        className="group flex items-center gap-3 font-display text-xl font-bold tracking-tight text-white"
                    >
                        <span className="flex h-10 w-10 items-center justify-center rounded-xl bg-cyan-400 text-slate-950 shadow-lg shadow-cyan-400/20 transition-transform duration-300 group-hover:-rotate-3 group-hover:scale-105">
                            <Glasses className="h-5 w-5" />
                        </span>
                        Cad-Optique
                    </Link>
                </div>
            </header>

            <div className="relative flex min-h-[calc(100vh-76px)] items-center justify-center overflow-hidden px-4 py-10 sm:px-6 lg:py-14">
                <div className="pointer-events-none absolute -left-24 top-20 h-72 w-72 rounded-full bg-blue-600/20 blur-3xl" />
                <div className="pointer-events-none absolute -right-24 bottom-10 h-80 w-80 rounded-full bg-cyan-400/15 blur-3xl" />
                <div className="relative grid w-full max-w-5xl overflow-hidden rounded-[28px] border border-white/10 bg-white shadow-2xl shadow-black/30 lg:grid-cols-[0.9fr_1.1fr]">
                    <section className="relative overflow-hidden bg-blue-900 px-6 py-9 text-white sm:px-10 sm:py-12 lg:flex lg:min-h-[560px] lg:flex-col lg:justify-between lg:px-12">
                        <div className="pointer-events-none absolute -right-20 -top-20 h-64 w-64 rounded-full border-[36px] border-cyan-300/20" />
                        <div className="pointer-events-none absolute -bottom-24 -left-16 h-56 w-56 rounded-full border-[28px] border-white/10" />
                        <div className="relative">
                            <span className="mb-8 inline-flex h-12 w-12 items-center justify-center rounded-2xl bg-cyan-400 text-slate-950 shadow-lg shadow-cyan-400/20">
                                <Glasses className="h-6 w-6" />
                            </span>
                            <p className="mb-3 text-xs font-semibold uppercase tracking-[0.22em] text-cyan-200">Espace professionnel</p>
                            <h1 className="max-w-sm font-display text-3xl font-bold leading-tight sm:text-4xl">
                                La gestion de votre magasin, en un regard.
                            </h1>
                            <p className="mt-5 max-w-sm text-sm leading-7 text-blue-100">
                                Retrouvez vos clients, vos stocks et votre activité dans un espace simple et centralisé.
                            </p>
                        </div>
                        <p className="relative mt-10 text-xs font-medium text-blue-200">Cad-Optique · Gestion optique intelligente</p>
                    </section>

                    <form
                    onSubmit={handleSubmit(loginHandler)}
                    className="px-5 py-9 sm:px-12 sm:py-12">
                    <div className="flex flex-col items-center justify-center space-y-3 text-center">
                        <span className="flex h-14 w-14 items-center justify-center rounded-2xl bg-blue-50 text-blue-900">
                            <AiOutlineLogin className="text-3xl" />
                        </span>
                        <h1 className="font-display text-2xl font-bold text-slate-900 sm:text-3xl">
                            Connexion Utilisateur
                        </h1>
                        <p className="max-w-sm text-sm leading-6 text-slate-500">Entrez vos identifiants pour accéder à votre espace de gestion.</p>
                    </div>
                    <div className="my-7 h-px bg-slate-200" />
                    <div className="flex flex-col gap-5">
                        <InputField
                            label="Nom d'utilisateur ou e-mail"
                            required
                            id="username"
                            type="text"
                            message="Le nom d'utilisateur est requis"
                            placeholder="Entrez votre nom d'utilisateur"
                            register={register}
                            errors={errors}
                        />

                        <div className="relative">
                            <InputField
                                label="Mot de passe"
                                required
                                id="password"
                                type={showPassword ? "text" : "password"}
                                message="Le mot de passe est requis"
                                placeholder="Entrez votre mot de passe"
                                register={register}
                                errors={errors}
                            />
                            <button
                                type="button"
                                onClick={() => setShowPassword(!showPassword)}
                                className="absolute right-3 top-9 rounded-md p-1 text-slate-400 transition-colors hover:text-blue-900"
                            >
                                {showPassword ? <FiEyeOff /> : <FiEye />}
                            </button>
                        </div>
                    </div>

                    <button
                        disabled={loader}
                        className="mt-7 flex w-full items-center justify-center gap-2 rounded-xl bg-blue-900 py-3.5 font-semibold text-white shadow-lg shadow-blue-900/20 transition-all duration-300 hover:-translate-y-0.5 hover:bg-blue-800 hover:shadow-blue-900/35 disabled:cursor-not-allowed disabled:opacity-60"
                        type="submit">
                        {loader ? (
                            <>
                                <Spinners /> Connexion...
                            </>
                        ) : (
                            <>Se connecter</>
                        )}
                    </button>

                    </form>
                </div>
            </div>
        </div>
    );
}

export default LogIn;