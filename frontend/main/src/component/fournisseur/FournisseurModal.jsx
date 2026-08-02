import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useDispatch } from "react-redux";
import toast from "react-hot-toast";
import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from "@headlessui/react";
import { FaTimes } from "react-icons/fa";
import { Truck } from "lucide-react";
import InputField from "../shared/InputField";
import { addFournisseur, updateFournisseur } from "../../store/reducers/actions";

const FournisseurModal = ({ open, setOpen, fournisseur }) => {
    const dispatch = useDispatch();
    const [btnLoader, setBtnLoader] = useState(false);

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors },
    } = useForm({ mode: "onTouched" });

    useEffect(() => {
        if (fournisseur) {
            reset(fournisseur);
        } else {
            reset({ nom: "", phoneNumber: "", email: "", adresse: "" });
        }
    }, [fournisseur, open]);

    const onSubmit = (data) => {
        if (fournisseur) {
            dispatch(updateFournisseur(fournisseur.id, data, toast, reset, setOpen, setBtnLoader));
        } else {
            dispatch(addFournisseur(data, toast, reset, setOpen, setBtnLoader));
        }
    };

    return (
        <Dialog open={open} onClose={() => setOpen(false)} className="relative z-50">
            <DialogBackdrop className="fixed inset-0 bg-slate-950/60 backdrop-blur-sm" />
            <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4">
                    <DialogPanel className="relative w-full max-w-md overflow-hidden rounded-2xl bg-white shadow-2xl">
                        <button
                            onClick={() => setOpen(false)}
                            className="absolute right-4 top-4 z-10 flex h-8 w-8 items-center justify-center rounded-full text-slate-400 hover:bg-slate-100 hover:text-slate-700"
                        >
                            <FaTimes size={13} />
                        </button>

                        <div className="flex items-center gap-3 border-b border-slate-100 bg-gradient-to-r from-indigo-50 to-white px-6 py-5">
                            <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-indigo-600 text-white shadow-md shadow-indigo-200">
                                <Truck size={18} />
                            </div>
                            <div>
                                <DialogTitle className="text-base font-bold text-slate-800">
                                    {fournisseur ? "Modifier le fournisseur" : "Nouveau fournisseur"}
                                </DialogTitle>
                                <p className="text-xs text-slate-400">
                                    Coordonnées du partenaire d'approvisionnement
                                </p>
                            </div>
                        </div>

                        <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-4 px-6 py-6">
                            <InputField
                                label="Nom de l'entreprise"
                                id="nom"
                                type="text"
                                register={register}
                                errors={errors}
                                required
                                message="Le nom est requis"
                            />
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
                                label="Email"
                                id="email"
                                type="email"
                                register={register}
                                errors={errors}
                            />
                            <InputField
                                label="Adresse"
                                id="adresse"
                                type="text"
                                register={register}
                                errors={errors}
                            />

                            <button
                                type="submit"
                                disabled={btnLoader}
                                className="mt-2 inline-flex w-full items-center justify-center rounded-xl bg-indigo-600 px-4 py-3 text-sm font-semibold text-white shadow-md shadow-indigo-200 transition hover:bg-indigo-700 disabled:cursor-not-allowed disabled:opacity-70"
                            >
                                {btnLoader ? "Enregistrement..." : "Enregistrer"}
                            </button>
                        </form>
                    </DialogPanel>
                </div>
            </div>
        </Dialog>
    );
};

export default FournisseurModal;
