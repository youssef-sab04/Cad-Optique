import { useSelector, useDispatch } from "react-redux";
import { useState, useEffect } from "react";
import Loader from "../shared/Loader";
import Paginations from "../shared/Paginations";
import toast from "react-hot-toast";
import { FaExclamationTriangle } from "react-icons/fa";
import OrdonnanceLunetteTable from "./OrdonnanceLunetteTable";
import OrdonnanceLentilleTable from "./OrdonnanceLentilleTable";
import { fetchOLU , fetchOLe } from "../../store/reducers/actions";
import { fetchOrdLunById , fetchOrdLenById } from "../../store/reducers/actions";
import OrdonnanceLentilleDetailModal from "./OrdonnanceLentilleDetailModal";
import OrdonnanceLunetteDetailModal from "./OrdonnanceLunetteDetailModal";
import { deleteOrdLen  , deleteOrdLun } from "../../store/reducers/actions";
import DeleteModal from "../shared/DeleteModal";
import OrdonnanceLunetteModal from "./OrdonnanceLunetteModal";
import OrdonnanceLentilleModal from "./OrdonnanceLentilleModal";
import useExamenFilter from "../hooks/useExamenFilter";
import ClientFilter from "../client/ClientFilter";

const Ordonance = () => {



    const dispatch = useDispatch();
    const [activeTab, setActiveTab] = useState("lunette");
    const [Olunette , setOlunette] = useState(true)



    useEffect(() => {
        if(activeTab == "lunette"){
            dispatch(fetchOLU())
            setOlunette(true)
        }
        else if(activeTab == "lentille"){
            dispatch(fetchOLe())
            setOlunette(false)

        }
        
    }, [dispatch , activeTab ]);
    
     useExamenFilter(Olunette)




    const { ordonancesU, paginationU } = useSelector((state) => state.ordonanceLu);
    const { ordonancesE, paginationE } = useSelector((state) => state.ordonanceLe);




    const { isLoading, errorMessage } = useSelector((state) => state.errors);

    console.log("OLU" , ordonancesU , paginationU)
    console.log("OLE" , ordonancesE , paginationE)



    const [openModal, setOpenModal] = useState(false);
    const [openModale, setOpenModale] = useState(false);


    const [selectedOrd, setSelectedOrd] = useState(null);

    const [openDelete, setOpenDelete] = useState(false);
    const [deleteId, setDeleteId] = useState(null);
    const [btnLoader, setBtnLoader] = useState(false);

    const [openDetail, setOpenDetail] = useState(false);
    const [openDetailE, setOpenDetailE] = useState(false);

    const [detailOrd, setDetailOrd] = useState(null);
    const [detailLoader, setDetailLoader] = useState(false);



    const handleAdd = () => {
      //  setSelectedExamen(null);
        setOpenModal(true);
    };

    const handleView = (id) => {
        Olunette ? setOpenDetail(true)  :  setOpenDetailE(true);
        Olunette ? dispatch(fetchOrdLunById(toast , id, setDetailLoader, setDetailOrd))  : dispatch(fetchOrdLenById(toast , id, setDetailLoader, setDetailOrd)) 

    };

    const handleEdit = (ordonance) => {
        setSelectedOrd(ordonance)
        console.log("ord transmis" , ordonance)

       Olunette ?  setOpenModal(true) : setOpenModale(true);
    };

    const handleDelete = (id) => {
        setDeleteId(id);
        setOpenDelete(true);
    };

    const confirmDelete = () => {
       Olunette ? dispatch(deleteOrdLun(deleteId, toast, setOpenDelete, setBtnLoader))  : dispatch(deleteOrdLen(deleteId, toast, setOpenDelete, setBtnLoader)) 
    };

    return (
        <div className="px-6 py-8">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-slate-800">Ordonnances</h1>

            </div>

            <div className="flex gap-2 mb-6 border-b border-slate-200">
    <button
        onClick={() => setActiveTab("lunette")}
        className={`px-4 py-2 text-sm font-medium rounded-t-lg transition-colors ${
            activeTab === "lunette"
                ? "bg-blue-600 text-white"
                : "text-slate-600 hover:bg-slate-50"
        }`}
    >
        Ordonnance Lunette
    </button>
    <button
        onClick={() => setActiveTab("lentille")}
        className={`px-4 py-2 text-sm font-medium rounded-t-lg transition-colors ${
            activeTab === "lentille"
                ? "bg-blue-600 text-white"
                : "text-slate-600 hover:bg-slate-50"
        }`}
    >
        Ordonnance Lentille
    </button>
</div>

           <ClientFilter/>

            {isLoading ? (
                <Loader />
            ) : errorMessage ? (
                <div className="flex justify-center items-center h-[200px]">
                    <FaExclamationTriangle className="text-slate-800 text-3xl mr-2" />
                    <span className="text-slate-800 text-lg font-medium">{errorMessage}</span>
                </div>
            ) : (
                <>

                {

                    Olunette ? (     
                    < OrdonnanceLunetteTable
                    ordonnances={ordonancesU || []}
                        onEdit={handleEdit}
                        onDelete={handleDelete}
                        onView={handleView}
                    />  )

                    :
            (

                    < OrdonnanceLentilleTable
                     ordonnances={ordonancesE || []}
                        onEdit={handleEdit}
                        onDelete={handleDelete}
                        onView={handleView}
                    /> 
            )
                    }




                    <div className="flex justify-center pt-6">
                        <Paginations
                            numberOfPage={Olunette ? paginationU.totalPages :  paginationE.totalPages}
                            totalProducts={Olunette ? paginationU.totalElements : paginationE.totalElements}
                        />
                    </div>
                </>
            )}
         
{/*
            <ExamenModal open={openModal} setOpen={setOpenModal} examen={selectedExamen} /

            */}

            <OrdonnanceLunetteModal open={openModal} setOpen={setOpenModal} ordonnance={selectedOrd}  />
            <OrdonnanceLentilleModal open={openModale} setOpen={setOpenModale} ordonnance={selectedOrd}  />

            <OrdonnanceLentilleDetailModal open={openDetailE} setOpen={setOpenDetailE}  ordonnance={detailOrd} />
            <OrdonnanceLunetteDetailModal   open={openDetail} setOpen={setOpenDetail}  ordonnance={detailOrd}  />
            <DeleteModal
                open={openDelete}
                setOpen={setOpenDelete}
                title="Supprimer cet ordonance ?"
                onDeleteHandler={confirmDelete}
                loader={btnLoader}
            />

        </div>
    );
};

export default Ordonance;