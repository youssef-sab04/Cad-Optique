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
                email: responsable.email || "",
                phoneNumber: responsable.phoneNumber || "",
                adresse: responsable.adresse || "",
            });
        } else {
            reset({ nom: "", prenom: "", email: "", phoneNumber: "", adresse: "" });
        }
    }, [responsable, open, reset]);

    const onSubmit = (data) => {
        if (responsable) {
            dispatch(updateResponsable(responsable.id, data, toast, reset, setOpen, setBtnLoader));
        } else {
            dispatch(addResponsable(data, toast, reset, setOpen, setBtnLoader));
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

                        <div className="flex items-center gap-3 border-b border-slate-100 bg-gradient-to-r from-violet-50 to-white px-6 py-5">
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
                                label="Adresse"
                                id="adresse"
                                type="text"
                                register={register}
                                errors={errors}
                                required
                                message="L'adresse est requise"
                            />

                             <InputField
                                label="Password"
                                id="password1"
                                type="password"
                                register={register}
                                errors={errors}
                                required
                                message="Le nom est requis"
                            />

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
