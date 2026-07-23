import { Dialog } from "@headlessui/react";
import { useForm, Controller } from "react-hook-form";
import { FiX, FiPackage } from "react-icons/fi";
import toast from "react-hot-toast";
import { useDispatch } from "react-redux";
import { addCommandeItem } from "../../store/reducers/actions";
import { useState, useEffect } from "react";
import ProductSearchSelect from "../shared/ProductSearchSelect";

const CommandeItemModal = ({ open, setOpen, commandeId, onSuccess }) => {
    const dispatch = useDispatch();
    const [btnLoader, setBtnLoader] = useState(false);
    const { control, register, handleSubmit, watch, reset, setValue, formState: { errors } } = useForm();

    const selectedProduct = watch("produit");
    const quantity = watch("quantity") || 1;
    const price = watch("price") || 0;

    const subtotal = (price * quantity).toFixed(2);

    useEffect(() => {
        if (selectedProduct) {
            setValue("price", selectedProduct.prixAchat ?? 0);
        }
    }, [selectedProduct, setValue]);

    const onSubmit = (formData) => {
        setBtnLoader(true);
        dispatch(
            addCommandeItem(
                commandeId,
                formData.produit.id,
                formData.quantity,
                formData.price,
                toast,
                setOpen,
                setBtnLoader,
                reset,
                onSuccess
            )
        );
    };

    return (
        <Dialog open={open} onClose={() => setOpen(false)} className="relative z-50">
            <div className="fixed inset-0 bg-black/40" aria-hidden="true" />
            <div className="fixed inset-0 flex items-center justify-center p-4">
                <Dialog.Panel className="w-full max-w-md rounded-2xl bg-white shadow-xl overflow-hidden">
                    <div className="flex items-center justify-between bg-slate-900 px-6 py-4">
                        <div className="flex items-center gap-2 text-white">
                            <FiPackage size={18} />
                            <Dialog.Title className="text-base font-semibold">
                                Ajouter un produit
                            </Dialog.Title>
                        </div>
                        <button onClick={() => setOpen(false)} className="text-slate-300 hover:text-white">
                            <FiX size={20} />
                        </button>
                    </div>

                    <form onSubmit={handleSubmit(onSubmit)} className="px-6 py-5 space-y-4">
                        <div>
                            <label className="block text-sm font-medium text-slate-700 mb-1">
                                Produit
                            </label>
                            <Controller
                                name="produit"
                                control={control}
                                rules={{ required: true }}
                                render={({ field }) => (
                                    <ProductSearchSelect
                                        value={field.value}
                                        onChange={field.onChange}
                                        placeholder="Rechercher un produit..."
                                        comm={true}

                                    />
                                )}
                            />
                            {errors.produit && (
                                <p className="text-xs text-red-500 mt-1">Produit requis</p>
                            )}
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-slate-700 mb-1">
                                Quantité
                            </label>
                            <input
                                type="number"
                                min={1}
                                defaultValue={1}
                                {...register("quantity", { required: true, min: 1, valueAsNumber: true })}
                                className="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-600"
                            />
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-slate-700 mb-1">
                                Price
                            </label>
                            <input
                                type="number"
                                defaultValue={0}
                                {...register("price", { required: true ,  valueAsNumber: true })}
                                className="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-600"
                            />
                        </div>

                        <div className="flex items-center justify-between rounded-lg bg-slate-50 px-4 py-3 text-sm">
                            <span className="text-slate-500">Sous-total</span>
                            <span className="font-semibold text-slate-800">{subtotal} DH</span>
                        </div>

                        <div className="flex justify-end gap-3 pt-2">
                            <button
                                type="button"
                                onClick={() => setOpen(false)}
                                className="px-4 py-2 text-sm font-medium text-slate-600 hover:bg-slate-100 rounded-lg"
                            >
                                Annuler
                            </button>
                            <button
                                type="submit"
                                disabled={btnLoader}
                                className="px-4 py-2 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-lg disabled:opacity-50"
                            >
                                {btnLoader ? "Ajout..." : "Ajouter"}
                            </button>
                        </div>
                    </form>
                </Dialog.Panel>
            </div>
        </Dialog>
    );
};

export default CommandeItemModal;