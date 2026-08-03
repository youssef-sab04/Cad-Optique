 

import { FaExclamationTriangle } from "react-icons/fa";
import { FiEdit2, FiTrash2 } from "react-icons/fi";
import { useSelector, useDispatch } from "react-redux";
import { useEffect } from "react";
import Loader from "../shared/Loader";
import Paginations from "../shared/Paginations";
import { deleteCategory, deleteProd, fetchCategories } from "../../store/reducers/actions";
import ProductCard from "./ProductCard";
import useProductFilter from "./useProductFilter";
import Filter from "./Filter";
import { useState } from "react";
import ProductModal from "./ProductModal";
import DeleteModal from "../shared/DeleteModal";
import toast from "react-hot-toast";
import CategoryModal from "./CategoryModal";







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

    const [openCategoryModal, setOpenCategoryModal] = useState(false);
    const [selectedCategory, setSelectedCategory] = useState(null);
    const [openDeleteCategory, setOpenDeleteCategory] = useState(false);
    const [deleteCategoryId, setDeleteCategoryId] = useState(null);


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

    const handleAddCategory = () => {
        setSelectedCategory(null);
        setOpenCategoryModal(true);
    };

    const handleEditCategory = (category) => {
        setSelectedCategory(category);
        setOpenCategoryModal(true);
    };

    const handleDeleteCategory = (categoryId) => {
        setDeleteCategoryId(categoryId);
        setOpenDeleteCategory(true);
    };

    const confirmDeleteCategory = () => {
        dispatch(deleteCategory(deleteCategoryId, toast, setOpenDeleteCategory, setBtnLoader));
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

            <div className="mb-6 rounded-xl border border-slate-200 bg-white p-4">
                <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                    <div>
                        <h2 className="text-base font-semibold text-slate-800">Catégories</h2>
                        <p className="text-xs text-slate-500">Gérez les catégories produit (nom, description, TVA)</p>
                    </div>
                    <button
                        onClick={handleAddCategory}
                        className="bg-slate-800 hover:bg-slate-900 text-white text-sm font-medium px-4 py-2 rounded-lg transition-colors"
                    >
                        + Ajouter une catégorie
                    </button>
                </div>

                <div className="mt-4 flex flex-wrap gap-2">
                    {(categories || []).length ? (
                        categories.map((category) => (
                            <div
                                key={category.id}
                                className="flex items-center gap-2 rounded-lg border border-slate-200 bg-slate-50 px-3 py-2"
                            >
                                <div>
                                    <p className="text-sm font-semibold text-slate-700">{category.nom}</p>
                                    <p className="text-xs text-slate-500">TVA: {category.tva ?? 20}%</p>
                                </div>
                                <button
                                    onClick={() => handleEditCategory(category)}
                                    className="rounded-md p-1 text-slate-500 hover:bg-white hover:text-blue-600"
                                    title="Modifier"
                                >
                                    <FiEdit2 size={14} />
                                </button>
                                <button
                                    onClick={() => handleDeleteCategory(category.id)}
                                    className="rounded-md p-1 text-slate-500 hover:bg-white hover:text-red-600"
                                    title="Supprimer"
                                >
                                    <FiTrash2 size={14} />
                                </button>
                            </div>
                        ))
                    ) : (
                        <p className="text-sm text-slate-500">Aucune catégorie trouvée</p>
                    )}
                </div>
            </div>

            <Filter categories={categories ? categories : []} /> 
            <ProductModal open={open} setOpen={setOpen} product={Produit} />
            <CategoryModal open={openCategoryModal} setOpen={setOpenCategoryModal} category={selectedCategory} />


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

                        <DeleteModal
                                open={openDeleteCategory}
                                setOpen={setOpenDeleteCategory}
                                title="Supprimer cette catégorie ?"
                                onDeleteHandler={confirmDeleteCategory}
                                loader={btnLoader}
                        />
            
            
            
             </div>

        
    )
}

export default Produit