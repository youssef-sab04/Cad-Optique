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
        <div className="min-h-screen flex flex-col">
            <header className="sticky top-0 z-50 border-b border-slate-200 bg-white/90 backdrop-blur">
                <div className="mx-auto flex max-w-7xl items-center justify-between px-6 py-4">
                    <Link to="/"
                        className="group flex items-center gap-2 font-display text-xl font-bold text-slate-900"
                    >
                        <span className="flex h-9 w-9 items-center justify-center rounded-full bg-blue-900 text-white transition-transform duration-300 group-hover:rotate-12 group-hover:scale-110">
                            <Glasses className="h-5 w-5" />
                        </span>
                        Cad-Optique
                    </Link>
                </div>
            </header>

            <div className="flex flex-1 items-center justify-center px-4 py-10">
                <form
                    onSubmit={handleSubmit(loginHandler)}
                    className="w-full max-w-[500px] shadow-custom py-8 sm:px-8 px-4 rounded-md">
                    <div className="flex flex-col items-center justify-center space-y-4">
                        <AiOutlineLogin className="text-slate-800 text-5xl" />
                        <h1 className="text-slate-800 text-center font-montserrat lg:text-3xl text-2xl font-bold">
                            Connexion Utilisateur
                        </h1>
                        <p className="text-sm">Entrez vos identifiants (Admin/Responsable) pour accéder au système</p>
                    </div>
                    <hr className="mt-2 mb-5 text-black" />
                    <div className="flex flex-col gap-3">
                        <InputField
                            label="UserName or email "
                            required
                            id="username"
                            type="text"
                            message="*UserName is required"
                            placeholder="Enter your username"
                            register={register}
                            errors={errors}
                        />

                        <div className="relative">
                            <InputField
                                label="Password"
                                required
                                id="password"
                                type={showPassword ? "text" : "password"}
                                message="*Password is required"
                                placeholder="Enter your password"
                                register={register}
                                errors={errors}
                            />
                            <button
                                type="button"
                                onClick={() => setShowPassword(!showPassword)}
                                className="absolute right-3 top-9 text-slate-500"
                            >
                                {showPassword ? <FiEyeOff /> : <FiEye />}
                            </button>
                        </div>
                    </div>

                    <button
                        disabled={loader}
                        className="w-full flex items-center justify-center gap-2 bg-blue-900 text-white font-semibold py-2.5 rounded-lg my-3 transition-all duration-300 hover:bg-blue-800 hover:shadow-lg hover:shadow-blue-900/30 disabled:opacity-60 disabled:cursor-not-allowed"
                        type="submit">
                        {loader ? (
                            <>
                                <Spinners /> Loading...
                            </>
                        ) : (
                            <>Login</>
                        )}
                    </button>

                </form>
            </div>
        </div>
    );
}

export default LogIn;