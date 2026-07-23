import { useSelector, useDispatch } from "react-redux";
import { useState, useEffect } from "react";
import DeleteModal from "../shared/DeleteModal";
import Loader from "../shared/Loader";
import Paginations from "../shared/Paginations";
import toast from "react-hot-toast";
import { FaExclamationTriangle } from "react-icons/fa";
import ConfirmModal from "../shared/ConfirmModal";
import useCommandeFilter from "./useCommandeFilter";
import CommandeTable from "./CommandeTable";
import { deleteCommande, deleteCommandeItem , confirmCommande } from "../../store/reducers/actions";
import CommandeItemModal from "./CommandeItemModal";


const Commande = () => {

    useCommandeFilter();

    const dispatch = useDispatch();

    /* useEffect(() => {
       // dispatch(fetchSalesOrders());
    }, [dispatch]); */

    const { commande, pagination } = useSelector((state) => state.commandes);
    const { isLoading, errorMessage } = useSelector((state) => state.errors);

    const [openDelete, setOpenDelete] = useState(false);
    const [deleteId, setDeleteId] = useState(null);


    const [openDeleteCI, setOpenDeleteCI] = useState(false);
    const [deleteIdCI, setDeleteIdCI] = useState(null);

    const [confirmModal, setconfirmModal] = useState(false);
    const [confirmId, setConfirmId] = useState(null);

    const [openAddCom, setOpenAddCom] = useState(false);
    const [addItemComId, setAddItemComId] = useState(null);

    const [btnLoader, setBtnLoader] = useState(false);






    const handleAddItem = (comId) => {
        setAddItemComId(comId);
        setOpenAddCom(true);
    };



    const handleDelete = (id) => {
        setDeleteId(id);
        setOpenDelete(true);
    };
    const confirmDelete = () => {
        dispatch(deleteCommande(deleteId, toast, setOpenDelete, setBtnLoader));
    };




    // delete item

    const handleDeleteCI = (id) => {
        setDeleteIdCI(id);
        setOpenDeleteCI(true);
    };

    const confirmDeleteCI = () => {
        dispatch(deleteCommandeItem(deleteIdCI, toast, setOpenDeleteCI, setBtnLoader));
    };


   
    const confirmOrder = (id) => {
        setConfirmId(id);
        setconfirmModal(true);
    };

    const handleConfirmOrder = () => {
        dispatch(confirmCommande(confirmId, toast, setconfirmModal, setBtnLoader));
    };



    return (
        <div className="px-6 py-8">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-slate-800">Commandes</h1>
                {pagination?.totalElements != null && (
                    <span className="text-sm text-slate-500">
                        {pagination.totalElements} vente(s)
                    </span>
                )}
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
                    <CommandeTable
                        commandes={commande || []}
                        onAddItem={handleAddItem}
                        onDelete={handleDelete}
                        onDeleteCI={handleDeleteCI}
                        onConfirm={confirmOrder}
                    />
                    <div className="flex justify-center pt-6">
                        <Paginations
                            numberOfPage={pagination?.totalPages}
                            totalProducts={pagination?.totalElements}
                        />
                    </div>
                </>
            )}


            <DeleteModal
                open={openDelete}
                setOpen={setOpenDelete}
                title="Supprimer cette commande ?"
                onDeleteHandler={confirmDelete}
                loader={btnLoader}
            />



            <DeleteModal
                open={openDeleteCI}
                setOpen={setOpenDeleteCI}
                title="Supprimer cet item ?"
                onDeleteHandler={confirmDeleteCI}
                loader={btnLoader}
            />


        

            <ConfirmModal
                open={confirmModal}
                setOpen={setconfirmModal}
                title="Confirmer la vente?"
                onConfirmHandler={handleConfirmOrder}
                loader={btnLoader}

            />




            <CommandeItemModal
                open={openAddCom}
                setOpen={setOpenAddCom}
                commandeId={addItemComId}
                onSuccess={(newItem) => { }}

            />


        </div>
    );
};

export default Commande;