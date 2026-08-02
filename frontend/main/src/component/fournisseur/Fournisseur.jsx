import { useSelector, useDispatch } from "react-redux";
import { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import toast from "react-hot-toast";
import { FaExclamationTriangle } from "react-icons/fa";
import { Plus } from "lucide-react";
import Loader from "../shared/Loader";
import Paginations from "../shared/Paginations";
import DeleteModal from "../shared/DeleteModal";
import FournisseurTable from "./FournisseurTable";
import FournisseurModal from "./FournisseurModal";
import FournisseurDetailModal from "./FournisseurDetailModal";
import { fetchFournisseurs, deleteFournisseur } from "../../store/reducers/actions";
import CommandeModal from "../commande/CommandeModal";

const Fournisseur = () => {
    const dispatch = useDispatch();
    const [searchParams] = useSearchParams();

    useEffect(() => {
        const params = new URLSearchParams();
        const currentPage = searchParams.get("page") ? Number(searchParams.get("page")) : 1;
        params.set("pageNumber", currentPage - 1);
        dispatch(fetchFournisseurs(params.toString()));
    }, [dispatch, searchParams]);

    const { fournisseurs, pagination } = useSelector((state) => state.fournisseurs);
    const { isLoading, errorMessage } = useSelector((state) => state.errors);

    const [openModal, setOpenModal] = useState(false);
    const [selectedFournisseur, setSelectedFournisseur] = useState(null);



    const [openDetail, setOpenDetail] = useState(false);
    const [detailFournisseur, setDetailFournisseur] = useState(null);
    const [openModalC, setOpenModalC] = useState(false);


    const [openDelete, setOpenDelete] = useState(false);
    const [deleteId, setDeleteId] = useState(null);
    const [btnLoader, setBtnLoader] = useState(false);

    const handleAdd = () => {
        setSelectedFournisseur(null);
        setOpenModal(true);
    };

    const handleEdit = (fournisseur) => {
        setSelectedFournisseur(fournisseur);
        setOpenModal(true);
    };

    const handleView = (fournisseur) => {
        setDetailFournisseur(fournisseur);
        setOpenDetail(true);
    };

    const handleDelete = (id) => {
        setDeleteId(id);
        setOpenDelete(true);
    };

    const confirmDelete = () => {
        dispatch(deleteFournisseur(deleteId, toast, setOpenDelete, setBtnLoader));
    };

    const onCOrder = (fournisseur) =>{

        setOpenModalC(true)
                setSelectedFournisseur(fournisseur)

        console.log("selectedFournisseur",selectedFournisseur)
        
    }

    return (
        <div className="px-6 py-8">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-slate-800">Fournisseurs</h1>
                <button
                    onClick={handleAdd}
                    className="inline-flex items-center gap-2 rounded-xl bg-indigo-600 px-4 py-2.5 text-sm font-semibold text-white shadow-md shadow-indigo-200 transition hover:bg-indigo-700"
                >
                    <Plus size={16} />
                    Ajouter un fournisseur
                </button>
            </div>

            {isLoading ? (
                <Loader />
            ) : errorMessage ? (
                <div className="flex justify-center items-center h-[200px]">
                    <FaExclamationTriangle className="text-slate-800 text-3xl mr-2" />
                    <span className="text-slate-800 text-lg font-medium">{errorMessage}</span>
                </div>
            ) : (
                <>
                    <FournisseurTable
                        fournisseurs={fournisseurs || []}
                        onEdit={handleEdit}
                        onDelete={handleDelete}
                        onView={handleView}
                        onCOrder={onCOrder}
                    />
                    <div className="flex justify-center pt-6">
                        <Paginations
                            numberOfPage={pagination?.totalPages}
                            totalProducts={pagination?.totalElements}
                        />
                    </div>
                </>
            )}

            <FournisseurModal open={openModal} setOpen={setOpenModal} fournisseur={selectedFournisseur} />

            <FournisseurDetailModal open={openDetail} setOpen={setOpenDetail} fournisseur={detailFournisseur} />

            <DeleteModal
                open={openDelete}
                setOpen={setOpenDelete}
                title="Supprimer ce fournisseur ?"
                onDeleteHandler={confirmDelete}
                loader={btnLoader}
            />      
                        
            <CommandeModal 
            open={openModalC} 
            setOpen={setOpenModalC} 
            fournisseur={selectedFournisseur}  />


        </div>
    );
};

export default Fournisseur;
