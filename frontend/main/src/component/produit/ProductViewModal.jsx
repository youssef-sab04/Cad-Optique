import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from "@headlessui/react";
import { FaTimes } from "react-icons/fa";
import { Barcode, Layers, Palette, Droplet, Package, ShieldAlert, CalendarPlus, Building2, CheckCircle2, XCircle } from "lucide-react";
import pImage from "../../assets/image/placeholder.png";

const Spec = ({ icon: Icon, label, value }) => (
    <div className="flex items-center gap-3 bg-slate-50 rounded-lg px-3 py-2.5">
        <div className="w-8 h-8 rounded-md bg-white shadow-sm flex items-center justify-center shrink-0">
            <Icon size={15} className="text-slate-500" />
        </div>
        <div className="flex flex-col min-w-0">
            <span className="text-[11px] text-slate-400 leading-none mb-1">{label}</span>
            <span className="text-sm text-slate-800 font-semibold truncate">{value || "—"}</span>
        </div>
    </div>
);

const ProductViewModal = ({ open, setOpen, product }) => {
    if (!product) return null;

    const {
        nom, description, image, code_barre, categoryDTO,
        quantity, prixHT, tva, discount, price, marque, couleur,
        modele, indice, diametre, seuilMin, traitement, createdAt,
    } = product;

    const isLowStock = seuilMin != null && quantity != null && quantity <= seuilMin;
    const isOutOfStock = !quantity || quantity <= 0;
    const finalPrice = discount ? Number(price) - (Number(price) * Number(discount)) / 100 : Number(price ?? 0);

    return (
        <Dialog open={open} onClose={() => setOpen(false)} className="relative z-50">
            <DialogBackdrop className="fixed inset-0 bg-black/50 backdrop-blur-sm" />
            <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4">
                    <DialogPanel className="relative bg-white rounded-2xl shadow-2xl w-full max-w-lg overflow-hidden">
                        <button
                            onClick={() => setOpen(false)}
                            className="absolute right-4 top-4 z-20 bg-black/30 hover:bg-black/50 text-white rounded-full p-1.5 transition-colors"
                        >
                            <FaTimes size={14} />
                        </button>

                        <div className="relative w-full aspect-[16/9] bg-slate-100">
                            <img src={image } alt={nom} className="w-full h-full object-cover" />
                            <div className="absolute inset-0 bg-gradient-to-t from-black/70 via-black/10 to-transparent" />

                            <div className="absolute bottom-0 left-0 right-0 px-6 py-4 flex items-end justify-between">
                                <div>
                                    <span className="inline-block bg-white/15 backdrop-blur text-white text-[11px] font-medium px-2 py-0.5 rounded-full mb-1.5">
                                        {categoryDTO?.nom ?? "Produit"}
                                    </span>
                                    <DialogTitle className="text-xl font-bold text-white leading-tight">
                                        {nom}
                                    </DialogTitle>
                                    {(marque || modele) && (
                                        <span className="text-xs text-slate-200">{marque} {modele}</span>
                                    )}
                                </div>
                            </div>
                        </div>

                        <div className="px-6 pt-4 flex items-center justify-between">
                            <div className="flex items-baseline gap-2">
                                {discount ? (
                                    <>
                                        <span className="text-2xl font-bold text-slate-900">{finalPrice.toFixed(2)} DH</span>
                                        <span className="text-sm text-slate-400 line-through">{Number(price).toFixed(2)} DH</span>
                                        <span className="text-xs font-semibold text-emerald-600 bg-emerald-50 px-1.5 py-0.5 rounded">-{discount}%</span>
                                    </>
                                ) : (
                                    <span className="text-2xl font-bold text-slate-900">{finalPrice.toFixed(2)} DH</span>
                                )}
                            </div>

                            {isOutOfStock ? (
                                <span className="flex items-center gap-1 text-xs font-semibold text-red-600 bg-red-50 px-2.5 py-1 rounded-full">
                                    <XCircle size={13} /> Rupture
                                </span>
                            ) : isLowStock ? (
                                <span className="flex items-center gap-1 text-xs font-semibold text-amber-700 bg-amber-50 px-2.5 py-1 rounded-full">
                                    <ShieldAlert size={13} /> Stock bas
                                </span>
                            ) : (
                                <span className="flex items-center gap-1 text-xs font-semibold text-emerald-700 bg-emerald-50 px-2.5 py-1 rounded-full">
                                    <CheckCircle2 size={13} /> En stock
                                </span>
                            )}
                        </div>

                        <div className="px-6 pt-4 pb-1 text-xs text-slate-400">
                            Prix HT {Number(prixHT ?? 0).toFixed(2)} DH · TVA {tva ?? 0}%
                        </div>

                        <div className="px-6 py-4 grid grid-cols-2 gap-2.5">
                            <Spec icon={Barcode} label="Code-barres" value={code_barre} />
                            <Spec icon={Package} label="Quantité" value={quantity} />
                            <Spec icon={Palette} label="Couleur" value={couleur} />
                            <Spec icon={Building2} label="Marque" value={marque} />
                            {traitement && <Spec icon={Layers} label="Traitement" value={traitement} />}
                            {(indice != null || diametre != null) && (
                                <Spec icon={Droplet} label="Indice / Diamètre" value={`${indice ?? "—"} / ${diametre ?? "—"}`} />
                            )}
                            <Spec icon={CalendarPlus} label="Ajouté le" value={createdAt?.split("T")[0]} />
                        </div>

                        {description && (
                            <div className="px-6 pb-6 pt-1 text-sm text-slate-600 leading-relaxed border-t border-slate-100">
                                <p className="pt-3">{description}</p>
                            </div>
                        )}
                    </DialogPanel>
                </div>
            </div>
        </Dialog>
    );
};

export default ProductViewModal;