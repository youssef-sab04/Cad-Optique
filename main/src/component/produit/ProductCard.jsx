import { useState } from "react";
import { FiEdit2, FiTrash2 } from "react-icons/fi";
import { FaExclamationTriangle } from "react-icons/fa";
import ProductViewModal from "./ProductViewModal";
import pImage from "../../assets/image/placeholder.png";


const ProductCard = ({
    id,
    nom,
    description,
    image ,
    code_barre,
    categoryDTO,
    quantity,
    prixHT,
    tva,
    discount,
    price,
    marque,
    couleur,
    modele,
    indice,
    diametre,
    seuilMin,
    traitement,
    createdAt,
    onEdit ,
    onDelete ,
    open, setOpen
}) => {

    const [openView, setOpenView] = useState(false);

    const product = {
        id, nom, description, image, code_barre, categoryDTO,
        quantity, prixHT, tva, discount, price, marque, couleur,
        modele, indice, diametre, seuilMin, traitement, createdAt,
    };

    const isLowStock = seuilMin != null && quantity != null && quantity <= seuilMin;
    const ruptureStock = seuilMin != null && quantity != null && quantity == 0;

    return (
        <div className="group relative border border-slate-200 rounded-lg overflow-hidden bg-white">

           
                <div className="absolute top-2 right-2 z-10 flex gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                   
                        <button
                            onClick={(e) => { onEdit(product); }}
                            className="bg-white shadow p-1.5 rounded-md text-slate-600 hover:text-blue-600"
                        >
                            <FiEdit2 size={14} />
                        </button>
                 
                  
                        <button
                            onClick={(e) => { onDelete(id); }}
                            className="bg-white shadow p-1.5 rounded-md text-slate-600 hover:text-red-600"
                        >
                            <FiTrash2 size={14} />
                        </button>
                 
                </div>
          

            {isLowStock && (
                <div className="absolute top-2 left-2 z-10 bg-amber-100 text-amber-800 text-[11px] font-medium px-2 py-1 rounded-md flex items-center gap-1">
                    <FaExclamationTriangle size={10} />
                    Stock bas
                </div>
            )}
            {ruptureStock && (
                <div className="absolute top-2 left-2 z-10 bg-red-200 text-red-800 text-[11px] font-medium px-2 py-1 rounded-md flex items-center gap-1">
                    <FaExclamationTriangle size={10} />
                    Stock en rupture
                </div>
            )}

            <div
                className="w-full aspect-square bg-slate-50 cursor-pointer overflow-hidden"
                onClick={() => setOpenView(true)}
            >
                {image ? (
                    <img
                        src={image}
                        alt={nom}
                        className="w-full h-full object-cover transition-transform duration-300 group-hover:scale-105"
                    />
                ) : (
                    <div className="w-full h-full flex items-center justify-center text-slate-300 text-sm">
                        Pas d'image
                    </div>
                )}
            </div>

            <div className="p-4 cursor-pointer" onClick={() => setOpenView(true)}>
                <span className="text-xs text-slate-400">{categoryDTO?.nom}</span>
                <h2 className="text-sm font-semibold text-slate-800 truncate">{nom}</h2>
                <p className="text-xs text-slate-500 truncate">{marque} {modele}</p>

                <div className="flex items-center justify-between mt-3">
                    {discount ? (
                        <div className="flex flex-col">
                            <span className="text-xs text-slate-400 line-through">
                                {Number(price).toFixed(2)} DH
                            </span>
                            <span className="text-base font-bold text-slate-800">
                                {(Number(price) - (Number(price) * Number(discount)) / 100).toFixed(2)} DH
                            </span>
                        </div>
                    ) : (
                        <span className="text-base font-bold text-slate-800">
                            {Number(price ?? 0).toFixed(2)} DH
                        </span>
                    )}
                    <span className={`text-xs font-medium ${quantity > 0 ? "text-slate-500" : "text-red-600"}`}>
                        {quantity > 0 ? `${quantity} en stock` : "Rupture"}
                    </span>
                </div>
            </div>

            <ProductViewModal open={openView} setOpen={setOpenView} product={product} />
        </div>
    );
};

export default ProductCard;