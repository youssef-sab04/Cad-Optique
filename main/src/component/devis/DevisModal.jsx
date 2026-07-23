import { useState } from "react";
import { useDispatch } from "react-redux";
import { useForm } from "react-hook-form";
import toast from "react-hot-toast";
import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from "@headlessui/react";
import { FaTimes } from "react-icons/fa";
import { FileText } from "lucide-react";
import { addDevis } from "../../store/reducers/actions";
const DevisModal = ({ open, setOpen, client }) => {
    const dispatch = useDispatch();
    const [btnLoader, setBtnLoader] = useState(false);

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors },
    } = useForm({ mode: "onTouched" });

    const onSubmit = (data) => {
        dispatch(addDevis(client.id, data, toast, reset, setOpen, setBtnLoader));
    };

    return (
        <Dialog open={open} onClose={() => setOpen(false)} className="relative z-50">
            <DialogBackdrop className="fixed inset-0 bg-black/50 backdrop-blur-sm" />
            <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4">
                    <DialogPanel className="relative bg-white rounded-2xl shadow-2xl w-full max-w-md max-h-[90vh] overflow-y-auto">

                        <div className="sticky top-0 z-10 bg-slate-900 px-6 py-5 flex items-center justify-between">
                            <div>
                                <DialogTitle className="text-base font-bold text-white">
                                    Nouveau devis
                                </DialogTitle>
                                <span className="text-xs text-slate-300">
                                    {client?.nom} {client?.prenom}
                                </span>
                            </div>
                            <button onClick={() => setOpen(false)} className="text-slate-400 hover:text-white transition-colors">
                                <FaTimes size={16} />
                            </button>
                        </div>

                        <form onSubmit={handleSubmit(onSubmit)} className="p-6 flex flex-col gap-5">

                            <div className="border border-slate-200 rounded-xl p-4">
                                <div className="flex items-center gap-2 mb-3">
                                    <div className="w-7 h-7 rounded-md bg-blue-50 flex items-center justify-center">
                                        <FileText size={14} className="text-blue-600" />
                                    </div>
                                    <span className="text-sm font-semibold text-slate-700">Détails</span>
                                </div>

                                <div className="flex flex-col gap-1.5">
                                    <label htmlFor="description" className="font-semibold text-sm text-slate-700">
                                        Description
                                    </label>
                                    <textarea
                                        id="description"
                                        rows={4}
                                        placeholder="Détails du devis..."
                                        className={`px-3 py-2.5 border outline-none bg-white text-slate-900 rounded-lg focus:ring-2 focus:ring-blue-900/20 ${errors.description ? "border-red-500" : "border-slate-300 focus:border-blue-900"}`}
                                        {...register("description", { required: { value: true, message: "La description est requise" } })}
                                    />
                                    {errors.description?.message && (
                                        <p className="text-sm font-medium text-red-600">{errors.description.message}</p>
                                    )}
                                </div>
                            </div>

                            <button type="submit" disabled={btnLoader} className="bg-blue-600 hover:bg-blue-700 text-white py-2.5 rounded-lg font-medium transition-colors">
                                {btnLoader ? "Enregistrement..." : "Créer le devis"}
                            </button>
                        </form>
                    </DialogPanel>
                </div>
            </div>
        </Dialog>
    );
};

export default DevisModal;