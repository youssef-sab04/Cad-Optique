 

import { FaExclamationTriangle } from "react-icons/fa";
import { useSelector, useDispatch } from "react-redux";
import { useEffect } from "react";
import Loader from "../shared/Loader";
import Paginations from "../shared/Paginations";
import { deleteProd, fetchCategories } from "../../store/reducers/actions";
import ProductCard from "./ProductCard";
import ProductViewModal from "./ProductViewModal";
import useProductFilter from "./useProductFilter";
import Filter from "./Filter";
import { useState } from "react";
import ProductModal from "./ProductModal";
import DeleteModal from "../shared/DeleteModal";
import toast from "react-hot-toast";







const Produit = () => {

   useProductFilter();

     const { produits , categories, pagination } = useSelector(
        (state) => state.produits);

    const { isLoading, errorMessage } = useSelector(
        (state) => state.errors
    );
    const dispatch = useDispatch();

    useEffect(() => {
       dispatch(fetchCategories());
    }, [dispatch]);

   const[Produit , setProduit] = useState(null)
   const [open , setOpen] = useState(false)

   const [openDelete, setOpenDelete] = useState(false);
    const [deleteId, setDeleteId] = useState(null);
        const [btnLoader, setBtnLoader] = useState(false);


   const onEdit = (produit) =>{
    setOpen(true)
    setProduit(produit)
   }

       const handleDelete = (id) => {
        setDeleteId(id);
        setOpenDelete(true);
    };

    const confirmDelete = () => {
        dispatch(deleteProd(deleteId, toast, setOpenDelete, setBtnLoader));
    };

        const handleAdd = () => {
        setProduit(null);
        setOpen(true);
    };


    return (
        <div className="px-6 py-8">
            

            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-slate-800">Produits</h1>
             
                {pagination?.totalElements != null && (
                    <span className="text-sm text-slate-500">
                        {pagination.totalElements} produit(s)
                    </span>
                )} 
                <button
            onClick={handleAdd}
            className="bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium px-4 py-2 rounded-lg transition-colors"
        >
            + Ajouter un produit
        </button>
            </div>  

            <Filter categories={categories ? categories : []} /> 
            <ProductModal open={open} setOpen={setOpen} product={Produit} />


            {isLoading ? (
                <Loader />
            ) : errorMessage ? (
                <div className="flex justify-center items-center h-[200px]">
                    <FaExclamationTriangle className="text-slate-800 text-3xl mr-2" />
                    <span className="text-slate-800 text-lg font-medium">
                        {errorMessage}
                    </span>
                </div>
            ) : (
                <div className="min-h-[700px] pt-6">
                    {produits && produits.length ? (
                        <div className="grid 2xl:grid-cols-4 lg:grid-cols-3 sm:grid-cols-2 gap-6">
                            {produits.map((item, i) => (
                                <div
                                    key={i}
                                    className="bg-white rounded-xl border border-slate-200 shadow-sm hover:shadow-md transition-shadow duration-200"
                                >
                                     <ProductCard onEdit={onEdit}  onDelete={handleDelete} produit={Produit} open={open} setOpen={setOpen} {...item} /> 
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className="flex justify-center items-center h-[200px] text-slate-500">
                            Aucun produit trouvé
                        </div>
                    )}

                    <div className="flex justify-center pt-10">
                        
                        <Paginations
                            numberOfPage={pagination?.totalPages}
                            totalProducts={pagination?.totalElements}
                        /> 
                    </div>
                </div>
            )}
              
              
              <DeleteModal
                open={openDelete}
                setOpen={setOpenDelete}
                title="Supprimer ce produit ?"
                onDeleteHandler={confirmDelete}
                loader={btnLoader}
            />       
            
            
            
             </div>

        
    )
}

export default Produit