import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useDispatch } from "react-redux";
import toast from "react-hot-toast";
import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from "@headlessui/react";
import { FaTimes } from "react-icons/fa";
import { Shapes } from "lucide-react";
import InputField from "../shared/InputField";
import { addCategory, updateCategory } from "../../store/reducers/actions";

const CategoryModal = ({ open, setOpen, category }) => {
    const dispatch = useDispatch();
    const [btnLoader, setBtnLoader] = useState(false);

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors },
    } = useForm({ mode: "onTouched" });

    useEffect(() => {
        if (category) {
            reset({
                nom: category.nom || "",
                description: category.description || "",
                tva: category.tva ?? 20,
            });
        } else {
            reset({ nom: "", description: "", tva: 20 });
        }
    }, [category, open, reset]);

    const onSubmit = (data) => {
        const payload = {
            ...data,
            tva: data?.tva === "" || data?.tva == null ? 20 : Number(data.tva),
        };

        if (category) {
            dispatch(updateCategory(category.id, payload, toast, reset, setOpen, setBtnLoader));
            return;
        }

        dispatch(addCategory(payload, toast, reset, setOpen, setBtnLoader));
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

                        <div className="flex items-center gap-3 border-b border-slate-100 bg-linear-to-r from-blue-50 to-white px-6 py-5">
                            <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-blue-600 text-white shadow-md shadow-blue-200">
                                <Shapes size={18} />
                            </div>
                            <div>
                                <DialogTitle className="text-base font-bold text-slate-800">
                                    {category ? "Modifier la catégorie" : "Nouvelle catégorie"}
                                </DialogTitle>
                                <p className="text-xs text-slate-400">
                                    Nom, description et TVA de la catégorie
                                </p>
                            </div>
                        </div>

                        <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-4 px-6 py-6">
                            <InputField
                                label="Nom"
                                id="nom"
                                type="text"
                                register={register}
                                errors={errors}
                                required
                                message="Le nom est requis"
                            />

                            <div className="flex flex-col gap-1.5">
                                <label htmlFor="description" className="font-semibold text-sm text-slate-700">
                                    Description
                                </label>
                                <textarea
                                    id="description"
                                    rows={3}
                                    className="px-3 py-2.5 border border-slate-300 outline-none bg-white text-slate-900 rounded-lg focus:ring-2 focus:ring-blue-900/20 focus:border-blue-900"
                                    {...register("description")}
                                />
                            </div>

                            <InputField
                                label="TVA (%)"
                                id="tva"
                                type="number"
                                register={register}
                                errors={errors}
                                required
                                message="La TVA est requise"
                            />

                            <p className="text-xs text-slate-500 -mt-2">
                                Valeur par défaut: 20 (modifiable)
                            </p>

                            <button
                                type="submit"
                                disabled={btnLoader}
                                className="mt-2 inline-flex w-full items-center justify-center rounded-xl bg-blue-600 px-4 py-3 text-sm font-semibold text-white shadow-md shadow-blue-200 transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-70"
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

export default CategoryModal;
