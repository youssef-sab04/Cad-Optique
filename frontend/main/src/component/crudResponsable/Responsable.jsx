import { useSelector, useDispatch } from "react-redux";
import { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import toast from "react-hot-toast";
import { FaExclamationTriangle } from "react-icons/fa";
import { Plus } from "lucide-react";
import Loader from "../shared/Loader";
import Paginations from "../shared/Paginations";
import DeleteModal from "../shared/DeleteModal";
import ResponsableTable from "./ResponsableTable";
import ResponsableModal from "./ResponsableModal";
import ResponsableDetailModal from "./ResponsableDetailModal";
import { fetchResponsables, deleteResponsable } from "../../store/reducers/actions";

const Responsable = () => {
    const dispatch = useDispatch();
    const [searchParams] = useSearchParams();

    useEffect(() => {
        const params = new URLSearchParams();
        const currentPage = searchParams.get("page") ? Number(searchParams.get("page")) : 1;
        params.set("pageNumber", currentPage - 1);
        dispatch(fetchResponsables(params.toString()));
    }, [dispatch, searchParams]);

    const { responsables, pagination } = useSelector((state) => state.responsables);
    const { isLoading, errorMessage } = useSelector((state) => state.errors);

    const [openModal, setOpenModal] = useState(false);
    const [selectedResponsable, setSelectedResponsable] = useState(null);
    const [openDetail, setOpenDetail] = useState(false);
    const [detailResponsable, setDetailResponsable] = useState(null);
    const [openDelete, setOpenDelete] = useState(false);
    const [deleteId, setDeleteId] = useState(null);
    const [btnLoader, setBtnLoader] = useState(false);

    const handleAdd = () => {
        setSelectedResponsable(null);
        setOpenModal(true);
    };

    const handleEdit = (responsable) => {
        setSelectedResponsable(responsable);
        setOpenModal(true);
    };

    const handleView = (responsable) => {
        setDetailResponsable(responsable);
        setOpenDetail(true);
    };

    const handleDelete = (id) => {
        setDeleteId(id);
        setOpenDelete(true);
    };

    const confirmDelete = () => {
        dispatch(deleteResponsable(deleteId, toast, setOpenDelete, setBtnLoader));
    };

    return (
        <div className="px-6 py-8">
            <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between mb-6">
                <div>
                    <h1 className="text-2xl font-bold text-slate-800">Responsables</h1>
                    <p className="text-sm text-slate-500">Gérez les responsables de l&apos;établissement</p>
                </div>
                <button
                    onClick={handleAdd}
                    className="inline-flex items-center gap-2 rounded-xl bg-violet-600 px-4 py-2.5 text-sm font-semibold text-white shadow-md shadow-violet-200 transition hover:bg-violet-700"
                >
                    <Plus size={16} />
                    Ajouter un responsable
                </button>
            </div>

            {isLoading ? (
                <Loader />
            ) : errorMessage ? (
                <div className="flex h-[200px] items-center justify-center">
                    <FaExclamationTriangle className="mr-2 text-slate-800 text-3xl" />
                    <span className="text-lg font-medium text-slate-800">{errorMessage}</span>
                </div>
            ) : (
                <>
                    <ResponsableTable
                        responsables={responsables || []}
                        onEdit={handleEdit}
                        onDelete={handleDelete}
                        onView={handleView}
                    />
                    <div className="flex justify-center pt-6">
                        <Paginations
                            numberOfPage={pagination?.totalPages}
                            totalProducts={pagination?.totalElements}
                        />
                    </div>
                </>
            )}

            <ResponsableModal open={openModal} setOpen={setOpenModal} responsable={selectedResponsable} />
            <ResponsableDetailModal open={openDetail} setOpen={setOpenDetail} responsable={detailResponsable} />
            <DeleteModal
                open={openDelete}
                setOpen={setOpenDelete}
                title="Supprimer ce responsable ?"
                onDeleteHandler={confirmDelete}
                loader={btnLoader}
            />
        </div>
    );
};

export default Responsable;
