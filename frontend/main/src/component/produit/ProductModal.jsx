import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useDispatch, useSelector } from "react-redux";
import toast from "react-hot-toast";
import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from "@headlessui/react";
import { FaTimes } from "react-icons/fa";
import { Tag, ScanBarcode, Boxes, DollarSign } from "lucide-react";
import InputField from "../shared/InputField";
import { updateProd  ,  addProduct  } from "../../store/reducers/actions";

const SectionCard = ({ icon: Icon, title, children }) => (
    <div className="col-span-2 border border-slate-200 rounded-xl p-4 bg-white transition-all duration-300 hover:-translate-y-0.5 hover:shadow-md hover:border-blue-200">
        <div className="flex items-center gap-2 mb-3">
            <div className="w-7 h-7 rounded-md bg-blue-50 flex items-center justify-center transition-colors duration-300 group-hover:bg-blue-100">
                <Icon size={14} className="text-blue-600" />
            </div>
            <span className="text-sm font-semibold text-slate-700">{title}</span>
        </div>
        <div className="grid grid-cols-2 gap-4">{children}</div>
    </div>
);

const ProductModal = ({ open, setOpen, product }) => {
    const dispatch = useDispatch();
    const [btnLoader, setBtnLoader] = useState(false);
    const [imageFile, setImageFile] = useState(null);
    const [imagePreview, setImagePreview] = useState(null);
    const { categories } = useSelector((state) => state.produits);

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors },
    } = useForm({ mode: "onTouched" });

    useEffect(() => {
        if (product) {
            reset({ ...product, categoryId: product.categoryDTO?.id });
        } else {
            reset({
                nom: "", description: "", image: "", code_barre: "",
                categoryId: "", quantity: "", prixHT: "",
                discount: "", marque: "", couleur: "",
                modele: "", indice: "", diametre: "", seuilMin: "", traitement: "",
            });
        }
        setImagePreview(product?.image || null);
        setImageFile(null);
    }, [product, open]);

    const onSubmit = (data) => {
        const { ...productData } = data;

        const formData = new FormData();
        formData.append("produit", new Blob([JSON.stringify(productData)], { type: "application/json" }));
        if (imageFile) {
            formData.append("image", imageFile);
        }

        if (product) {
            dispatch(updateProd(product.id, formData, toast, reset, setOpen, setBtnLoader));
        } else {
            dispatch(addProduct(productData.categoryId, formData, toast, reset, setOpen, setBtnLoader));
        }
    };

    return (
        <Dialog open={open} onClose={() => setOpen(false)} className="relative z-50">
            <DialogBackdrop className="fixed inset-0 bg-black/45 backdrop-blur-sm transition-opacity duration-300" />
            <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4">
                    <DialogPanel className="relative bg-white rounded-2xl shadow-2xl ring-1 ring-slate-200/70 w-full max-w-3xl max-h-[90vh] overflow-y-auto">

                        <div className="sticky top-0 z-10 bg-slate-900 px-6 py-5 flex items-center justify-between border-b border-slate-700">
                            <div>
                                <DialogTitle className="text-base font-bold text-white">
                                    {product ? "Modifier le produit" : "Ajouter un produit"}
                                </DialogTitle>
                                <span className="text-xs text-slate-300">
                                    {product ? `Produit #${product.id}` : "Nouvelle fiche produit"}
                                </span>
                            </div>
                            <button onClick={() => setOpen(false)} className="text-slate-400 hover:text-white hover:bg-slate-800 rounded-lg p-1.5 transition-all duration-200">
                                <FaTimes size={16} />
                            </button>
                        </div>

                        <form onSubmit={handleSubmit(onSubmit)} className="p-6 grid grid-cols-2 gap-5">

                            <SectionCard icon={Tag} title="Informations générales">
                                <InputField label="Nom" id="nom" type="text" register={register} errors={errors} required message="Le nom est requis" className="col-span-2" />

                                <div className="flex flex-col gap-1.5 col-span-2">
                                    <label htmlFor="categoryId" className="font-semibold text-sm text-slate-700">Catégorie</label>
                                    <select
                                        id="categoryId"
                                        className={`px-3 py-2.5 border outline-none bg-white text-slate-900 rounded-lg transition-all duration-200 hover:border-slate-400 focus:ring-2 focus:ring-blue-900/20 ${errors.categoryId ? "border-red-500" : "border-slate-300 focus:border-blue-900"}`}
                                        {...register("categoryId", { required: { value: true, message: "La catégorie est requise" } })}
                                    >
                                        <option value="">Sélectionner...</option>
                                        {categories && categories.map((c) => (
                                            <option key={c.id} value={c.id}>{c.nom}</option>
                                        ))}
                                    </select>
                                    {errors.categoryId?.message && (
                                        <p className="text-sm font-medium text-red-600">{errors.categoryId.message}</p>
                                    )}
                                </div>

                                <InputField label="Marque" id="marque" type="text" register={register} errors={errors} />
                                <InputField label="Modèle" id="modele" type="text" register={register} errors={errors} />
                                <InputField label="Couleur" id="couleur" type="text" register={register} errors={errors} />
                                <InputField label="Traitement" id="traitement" type="text" register={register} errors={errors} />

                                <div className="flex flex-col gap-1.5 col-span-2">
                                    <label htmlFor="description" className="font-semibold text-sm text-slate-700">Description</label>
                                    <textarea
                                        id="description"
                                        rows={3}
                                        className="px-3 py-2.5 border border-slate-300 outline-none bg-white text-slate-900 rounded-lg transition-all duration-200 hover:border-slate-400 focus:ring-2 focus:ring-blue-900/20 focus:border-blue-900"
                                        {...register("description")}
                                    />
                                </div>
                            </SectionCard>

                            <SectionCard icon={ScanBarcode} title="Identification">
                                <InputField label="Code-barres" id="code_barre" type="text" register={register} errors={errors} required message="Le code-barres est requis" />

                                <div className="flex flex-col gap-1.5">
                                    <label className="font-semibold text-sm text-slate-700">Image</label>
                                    <input
                                        type="file"
                                        accept="image/*"
                                        onChange={(e) => {
                                            const file = e.target.files[0];
                                            setImageFile(file);
                                            if (file) setImagePreview(URL.createObjectURL(file));
                                        }}
                                        className="text-sm file:mr-3 file:rounded-md file:border-0 file:bg-blue-50 file:px-3 file:py-1.5 file:font-medium file:text-blue-700 hover:file:bg-blue-100"
                                    />
                                    {imagePreview && (
                                        <img src={imagePreview} alt="preview" className="w-24 h-24 object-cover rounded-lg mt-2 ring-1 ring-slate-200 transition-transform duration-200 hover:scale-105" />
                                    )}
                                </div>
                            </SectionCard>

                            <SectionCard icon={Boxes} title="Stock & caractéristiques">
                                <InputField label="Quantité" id="quantity" type="number" register={register} errors={errors} required message="La quantité est requise" />
                                <InputField label="Seuil minimum" id="seuilMin" type="number" register={register} errors={errors} />
                                <InputField label="Indice" id="indice" type="number" register={register} errors={errors} />
                                <InputField label="Diamètre" id="diametre" type="number" register={register} errors={errors} />
                            </SectionCard>

                            <SectionCard icon={DollarSign} title="Tarification">
                                <InputField label="Prix HT" id="prixHT" type="number" register={register} errors={errors} required message="Le prix HT est requis" />
                                <InputField label="Remise (%)" id="discount" type="number" register={register} errors={errors} />
                            </SectionCard>

                            <div className="col-span-2 flex items-center gap-3 justify-end pt-1">
                                <button
                                    type="button"
                                    onClick={() => setOpen(false)}
                                    className="px-4 py-2.5 rounded-lg border border-slate-300 text-slate-700 font-medium hover:bg-slate-50 transition-colors"
                                >
                                    Annuler
                                </button>
                                <button type="submit" disabled={btnLoader} className="min-w-36 bg-blue-600 hover:bg-blue-700 hover:shadow-lg hover:shadow-blue-200 text-white py-2.5 px-5 rounded-lg font-medium transition-all duration-200 disabled:opacity-70 disabled:cursor-not-allowed">
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

export default ProductModal;