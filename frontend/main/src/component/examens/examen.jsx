import { useSelector, useDispatch } from "react-redux";
import { useState, useEffect } from "react";
import Loader from "../shared/Loader";
import Paginations from "../shared/Paginations";
import toast from "react-hot-toast";
import { FaExclamationTriangle } from "react-icons/fa";
import { deleteSaleOrderItem, fetchExamens } from "../../store/reducers/actions";
import ExamenTable from "./ExamenTable";
import ExamenModal from "./ExamenModal";
import ExamenDetailModal from "./ExamenDetailModal";
import { fetchExamenById } from "../../store/reducers/actions";
import { deleteExamen } from "../../store/reducers/actions";
import DeleteModal from "../shared/DeleteModal";
import ClientFilter from "../client/ClientFilter";
import useExamenFilter from "../hooks/useExamenFilter";
const Examen = () => {

useExamenFilter()


    const dispatch = useDispatch();


    useEffect(() => {
        dispatch( fetchExamens())
    }, [dispatch]);



    const { examens, pagination } = useSelector((state) => state.examens);
    console.log("examens", examens)



    const { isLoading, errorMessage } = useSelector((state) => state.errors);

    const [openModal, setOpenModal] = useState(false);
    const [selectedExamen, setSelectedExamen] = useState(null);

    const [openDelete, setOpenDelete] = useState(false);
    const [deleteId, setDeleteId] = useState(null);
    const [btnLoader, setBtnLoader] = useState(false);



    const [openDetail, setOpenDetail] = useState(false);
    const [detailExamen, setDetailExamen] = useState(null);
    const [detailLoader, setDetailLoader] = useState(false);


    const handleAdd = () => {
        setSelectedExamen(null);
        setOpenModal(true);
    };

    const handleView = (id) => {
        setOpenDetail(true);
        dispatch(fetchExamenById (toast , id, setDetailLoader, setDetailExamen));
    };

    const handleEdit = (examen) => {
        setSelectedExamen(examen);
        setOpenModal(true);
    };

    const handleDelete = (id) => {
        setDeleteId(id);
        setOpenDelete(true);
    };

    const confirmDelete = () => {
         dispatch(deleteExamen(deleteId, toast, setOpenDelete, setBtnLoader));
    };



    return (
        <div className="px-6 py-8">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-slate-800">Examens</h1>
                
            </div>

            < ClientFilter /> 

            {isLoading ? (
                <Loader />
            ) : errorMessage ? (
                <div className="flex justify-center items-center h-[200px]">
                    <FaExclamationTriangle className="text-slate-800 text-3xl mr-2" />
                    <span className="text-slate-800 text-lg font-medium">{errorMessage}</span>
                </div>
            ) : (
                <>
                    
                    <ExamenTable
                        examens={examens || []}
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
         

            <ExamenModal open={openModal} setOpen={setOpenModal} examen={selectedExamen} />

            
            <ExamenDetailModal open={openDetail} setOpen={setOpenDetail} examen={detailExamen} />

            <DeleteModal
                open={openDelete}
                setOpen={setOpenDelete}
                title="Supprimer ce client ?"
                onDeleteHandler={confirmDelete}
                loader={btnLoader}
            />
             

        </div>
    );
};

export default Examen;