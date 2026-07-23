import { useSelector, useDispatch } from "react-redux";
import { useState, useEffect } from "react";
import DevisTable from "./DevisTable";
import useDevisFilter from "./useDevisFilter";
import DeleteModal from "../shared/DeleteModal";
import Loader from "../shared/Loader";
import Paginations from "../shared/Paginations";
import { fetchDevis, deleteDevis, deleteDevisItem, confirmDevis, cancelDevis } from "../../store/reducers/actions";
import toast from "react-hot-toast";
import { FaExclamationTriangle } from "react-icons/fa";
import DevisItemModal from "./DevisItemModal";
import ConfirmModal from "../shared/ConfirmModal";
import { downloadDevisPdf } from "../../store/reducers/actions";

const Devis = () => {
    useDevisFilter();
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(fetchDevis());
    }, [dispatch]);

    const { devis, pagination } = useSelector((state) => state.devis);
    const { isLoading, errorMessage } = useSelector((state) => state.errors);

    const [openDelete, setOpenDelete] = useState(false);
    const [deleteId, setDeleteId] = useState(null);

    const [openDeleteItem, setOpenDeleteItem] = useState(false);
    const [deleteItemId, setDeleteItemId] = useState(null);

    const [confirmModal, setConfirmModal] = useState(false);
    const [confirmId, setConfirmId] = useState(null);

    const [cancelModal, setCancelModal] = useState(false);
    const [cancelId, setCancelId] = useState(null);

    const [btnLoader, setBtnLoader] = useState(false);
    const [openAddItem, setOpenAddItem] = useState(false);
    const [addItemDevisId, setAddItemDevisId] = useState(null);

    const handleAddItem = (devisId) => {
        setAddItemDevisId(devisId);
        setOpenAddItem(true);
    };

    const handleDelete = (id) => {
        setDeleteId(id);
        setOpenDelete(true);
    };

    const confirmDelete = () => {
        dispatch(deleteDevis(deleteId, toast, setOpenDelete, setBtnLoader));
    };

    const handleDeleteItem = (id) => {
        setDeleteItemId(id);
        setOpenDeleteItem(true);
    };

    const confirmDeleteItem = () => {
        dispatch(deleteDevisItem(deleteItemId, toast, setOpenDeleteItem, setBtnLoader));
    };

    const handleConfirmClick = (id) => {
        setConfirmId(id);
        setConfirmModal(true);
    };

    const handleConfirmDevis = () => {
        dispatch(confirmDevis(confirmId, toast, setConfirmModal, setBtnLoader));
    };

    const handleCancelClick = (id) => {
        setCancelId(id);
        setCancelModal(true);
    };

    const handleCancelDevis = () => {
        dispatch(cancelDevis(cancelId, toast, setCancelModal, setBtnLoader));
    };

   const handleReceipt = (id) => {
    dispatch(downloadDevisPdf(id, toast));
};

    return (
        <div className="px-6 py-8">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-slate-800">Devis</h1>
                {pagination?.totalElements != null && (
                    <span className="text-sm text-slate-500">
                        {pagination.totalElements} devis
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
                    <DevisTable
                        devisList={devis || []}
                        onAddItem={handleAddItem}
                        onDelete={handleDelete}
                        onDeleteItem={handleDeleteItem}
                        onConfirm={handleConfirmClick}
                        onCancel={handleCancelClick}
                        onReceipt={handleReceipt}
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
                title="Supprimer ce devis ?"
                onDeleteHandler={confirmDelete}
                loader={btnLoader}
            />

            <DeleteModal
                open={openDeleteItem}
                setOpen={setOpenDeleteItem}
                title="Supprimer cet item ?"
                onDeleteHandler={confirmDeleteItem}
                loader={btnLoader}
            />

            <ConfirmModal
                open={confirmModal}
                setOpen={setConfirmModal}
                title="Confirmer le devis ? Une vente sera créée."
                onConfirmHandler={handleConfirmDevis}
                loader={btnLoader}
            />

            <ConfirmModal
                open={cancelModal}
                setOpen={setCancelModal}
                title="Annuler ce devis ?"
                onConfirmHandler={handleCancelDevis}
                loader={btnLoader}
            />

            <DevisItemModal
                open={openAddItem}
                setOpen={setOpenAddItem}
                devisId={addItemDevisId}
                onSuccess={() => {}}
            />
        </div>
    );
};

export default Devis;