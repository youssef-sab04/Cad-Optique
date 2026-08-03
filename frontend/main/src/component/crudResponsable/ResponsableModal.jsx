import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useDispatch } from "react-redux";
import toast from "react-hot-toast";
import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from "@headlessui/react";
import { FaTimes } from "react-icons/fa";
import { UserCog } from "lucide-react";
import InputField from "../shared/InputField";
import { addResponsable, updateResponsable } from "../../store/reducers/actions";

const ResponsableModal = ({ open, setOpen, responsable }) => {
    const dispatch = useDispatch();
    const [btnLoader, setBtnLoader] = useState(false);

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors },
    } = useForm({ mode: "onTouched" });

    useEffect(() => {
        if (responsable) {
            reset({
                nom: responsable.nom || "",
                prenom: responsable.prenom || "",
                userName: responsable.userName || "",
                email: responsable.email || "",
                phoneNumber: responsable.phoneNumber || "",
                cin: responsable.cin || "",
                password: "",
                role: (responsable.roles && responsable.roles[0]) || "ROLE_RESPONSABLE",
            });
        } else {
            reset({
                nom: "",
                prenom: "",
                userName: "",
                email: "",
                phoneNumber: "",
                cin: "",
                password: "",
                role: "ROLE_RESPONSABLE",
            });
        }
    }, [responsable, open, reset]);

    const onSubmit = (data) => {
        const payload = {
            nom: data.nom,
            prenom: data.prenom,
            userName: data.userName,
            email: data.email,
            phoneNumber: data.phoneNumber,
            cin: data.cin,
            roles: [data.role],
        };

        if (data.password) {
            payload.password = data.password;
        }

        if (responsable) {
            dispatch(updateResponsable(responsable.id, payload, toast, reset, setOpen, setBtnLoader));
        } else {
            dispatch(addResponsable(payload, toast, reset, setOpen, setBtnLoader));
        }
    };

    return (
        <Dialog open={open} onClose={() => setOpen(false)} className="relative z-50">
            <DialogBackdrop className="fixed inset-0 bg-slate-950/60 backdrop-blur-sm" />
            <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4">
                    <DialogPanel className="relative w-full max-w-xl overflow-hidden rounded-2xl bg-white shadow-2xl">
                        <button
                            onClick={() => setOpen(false)}
                            className="absolute right-4 top-4 z-10 flex h-8 w-8 items-center justify-center rounded-full text-slate-400 hover:bg-slate-100 hover:text-slate-700"
                        >
                            <FaTimes size={13} />
                        </button>

                        <div className="flex items-center gap-3 border-b border-slate-100 bg-linear-to-r from-violet-50 to-white px-6 py-5">
                            <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-violet-600 text-white shadow-md shadow-violet-200">
                                <UserCog size={18} />
                            </div>
                            <div>
                                <DialogTitle className="text-base font-bold text-slate-800">
                                    {responsable ? "Modifier le responsable" : "Nouveau responsable"}
                                </DialogTitle>
                                <p className="text-xs text-slate-400">
                                    Informations du responsable de la structure
                                </p>
                            </div>
                        </div>

                        <form onSubmit={handleSubmit(onSubmit)} className="grid gap-4 px-6 py-6 md:grid-cols-2">
                            <InputField
                                label="Nom"
                                id="nom"
                                type="text"
                                register={register}
                                errors={errors}
                                required
                                message="Le nom est requis"
                            />
                            <InputField
                                label="Prénom"
                                id="prenom"
                                type="text"
                                register={register}
                                errors={errors}
                                required
                                message="Le prénom est requis"
                            />
                            <div className="md:col-span-2">
                                <InputField
                                    label="Nom d'utilisateur"
                                    id="userName"
                                    type="text"
                                    register={register}
                                    errors={errors}
                                    required
                                    message="Le nom d'utilisateur est requis"
                                />
                            </div>
                            <div className="md:col-span-2">
                                <InputField
                                    label="Email"
                                    id="email"
                                    type="email"
                                    register={register}
                                    errors={errors}
                                    required
                                    message="L'email est requis"
                                />
                            </div>
                            <InputField
                                label="Téléphone"
                                id="phoneNumber"
                                type="text"
                                register={register}
                                errors={errors}
                                required
                                message="Le téléphone est requis"
                            />
                            <InputField
                                label="CIN"
                                id="cin"
                                type="text"
                                register={register}
                                errors={errors}
                                required
                                message="Le CIN est requis"
                            />

                            <div className="md:col-span-2 flex flex-col gap-1.5">
                                <label htmlFor="role" className="font-semibold text-sm text-slate-700">Rôle</label>
                                <select
                                    id="role"
                                    className={`px-3 py-2.5 border outline-none bg-white text-slate-900 rounded-lg transition-all duration-200 hover:border-slate-400 focus:ring-2 focus:ring-violet-900/20 ${errors.role ? "border-red-500" : "border-slate-300 focus:border-violet-700"}`}
                                    {...register("role", { required: { value: true, message: "Le rôle est requis" } })}
                                >
                                    <option value="ROLE_RESPONSABLE">Responsable</option>
                                    <option value="ROLE_ADMIN">Admin</option>
                                </select>
                                {errors.role?.message && (
                                    <p className="text-sm font-medium text-red-600">{errors.role.message}</p>
                                )}
                            </div>

                            <div className="md:col-span-2">
                                <InputField
                                label="Mot de passe"
                                id="password"
                                type="password"
                                register={register}
                                errors={errors}
                                required={!responsable}
                                message="Le mot de passe est requis"
                            />
                            </div>

                            <div className="md:col-span-2">
                                <button
                                    type="submit"
                                    disabled={btnLoader}
                                    className="mt-2 inline-flex w-full items-center justify-center rounded-xl bg-violet-600 px-4 py-3 text-sm font-semibold text-white shadow-md shadow-violet-200 transition hover:bg-violet-700 disabled:cursor-not-allowed disabled:opacity-70"
                                >
                                    {btnLoader ? "Enregistrement..." : "Enregistrer"}
                                </button>
                            </div>
                        </form>
                    </DialogPanel>
                </div>
            </div>
        </Dialog>
    );
};

export default ResponsableModal;
