import { useSelector, useDispatch } from "react-redux";
import { useState, useEffect } from "react";
import SalesOrderTable from "./SalesOrderTable";
import useSalesOrderFilter from "./useSalesOrderFilter";
import DeleteModal from "../shared/DeleteModal";
import Loader from "../shared/Loader";
import Paginations from "../shared/Paginations";
import { fetchSalesOrders  /*, deleteSalesOrder */ } from "../../store/reducers/actions";
import toast from "react-hot-toast";
import { FaExclamationTriangle } from "react-icons/fa";
import { deleteSaleOrder, deleteSaleOrderItem, confirmSaleOrder, downloadSalesOrderPdf } from "../../store/reducers/actions";
import SalesOrderItemModal from "./SalesOrderItemModal";
import ConfirmModal from "../shared/ConfirmModal";

const SalesOrder = () => {
    useSalesOrderFilter();
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(fetchSalesOrders());
    }, [dispatch]);

    const { salesOrder, pagination } = useSelector((state) => state.salesOrders);
    const { isLoading, errorMessage } = useSelector((state) => state.errors);

    const [openDelete, setOpenDelete] = useState(false);
    const [deleteId, setDeleteId] = useState(null);


    const [openDeleteOI, setOpenDeleteOI] = useState(false);
    const [deleteIdOI, setDeleteIdOI] = useState(null);

    const [confirmModal, setconfirmModal] = useState(false);
const [confirmId, setConfirmId] = useState(null);





    const [btnLoader, setBtnLoader] = useState(false);
    const [openAddItem, setOpenAddItem] = useState(false);
    const [addItemOrderId, setAddItemOrderId] = useState(null);

    const handleAddItem = (orderId) => {
        setAddItemOrderId(orderId);
        setOpenAddItem(true);
    };




    const handleDelete = (id) => {
        setDeleteId(id);
        setOpenDelete(true);
    };

    const confirmDelete = () => {
        dispatch(deleteSaleOrder(deleteId, toast, setOpenDelete, setBtnLoader));
    };


    const handleDeleteOI = (id) => {
        setDeleteIdOI(id);
        setOpenDeleteOI(true);
    };

    const confirmDeleteOI = () => {
        dispatch(deleteSaleOrderItem(deleteIdOI, toast, setOpenDeleteOI, setBtnLoader));
    };
    const confirmOrder = (id) => {
        setConfirmId(id);
        setconfirmModal(true);
    };

    const handleConfirmOrder = () => {
        dispatch(confirmSaleOrder(confirmId, toast, setconfirmModal, setBtnLoader));
    };




    const handleReceipt = (id) => {
        dispatch(downloadSalesOrderPdf(id, toast));
    };

    return (
        <div className="px-6 py-8">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-slate-800">Ventes</h1>
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
                    <SalesOrderTable
                        salesOrders={salesOrder || []}
                        onAddItem={handleAddItem}
                        onDelete={handleDelete}
                        onDeleteOI={handleDeleteOI}
                        onConfirm={confirmOrder}
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
                title="Supprimer cette vente ?"
                onDeleteHandler={confirmDelete}
                loader={btnLoader}
            />

            <DeleteModal
                open={openDeleteOI}
                setOpen={setOpenDeleteOI}
                title="Supprimer cet item ?"
                onDeleteHandler={confirmDeleteOI}
                loader={btnLoader}
            />

            <ConfirmModal
                open={confirmModal}
                setOpen={setconfirmModal}
                title="Confirmer la vente?"
                onConfirmHandler={handleConfirmOrder}
                loader={btnLoader}

            />



            <SalesOrderItemModal
                open={openAddItem}
                setOpen={setOpenAddItem}
                orderId={addItemOrderId}
                onSuccess={(newItem) => {

                }}
            />

        </div>
    );
};

export default SalesOrder;