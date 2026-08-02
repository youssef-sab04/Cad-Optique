import { useSelector, useDispatch } from "react-redux";
import { useState  , useEffect} from "react";
import ClientFilter from "./ClientFilter";
import useClientFilter from "../hooks/useClientFilter";
import ClientTable from "./ClientTable";
import ClientModal from "./ClientModal";
import DeleteModal from "../shared/DeleteModal";
import Loader from "../shared/Loader";
import Paginations from "../shared/Paginations";
import { fetchClients } from "../../store/reducers/actions";
import toast from "react-hot-toast";
import { FaExclamationTriangle } from "react-icons/fa";
import ClientDetailModal from "./ClientDetailModal";
import { fetchClientById } from "../../store/reducers/actions";
import { deleteClient } from "../../store/reducers/actions";
import DocModal from "../DocModal";
import ExamenModal from "../examens/ExamenModal";
import OrdonnanceLunetteModal  from "../ordonance/OrdonnanceLunetteModal";
import OrdonnanceLentilleModal  from "../ordonance/OrdonnanceLentilleModal";
import SalesOrderModal from "../salesOrder/SalesOrderModal";
import DevisModal from '../devis/DevisModal';



const Client = () => {
    useClientFilter();
    const dispatch = useDispatch();


    useEffect(() => {
        dispatch(fetchClients());
    }, [dispatch]);



    const { clients, pagination } = useSelector((state) => state.clients);
    console.log("clients" , clients)



    const { isLoading, errorMessage } = useSelector((state) => state.errors);

    const [openModal, setOpenModal] = useState(false);

    const [openModalE, setOpenModalE] = useState(false);
    const [openModalLU, setOpenModalLU] = useState(false);
    const [openModalLE, setOpenModalLE] = useState(false);
    
    const [openModalO, setOpenModalO] = useState(false);

    const [openModalD, setOpenModalD] = useState(false);




    const [selectedClient, setSelectedClient] = useState(null);

    const [selectedClientid, setSelectedClientid] = useState(null);


    const [selectedDoc, setSelectedDoc] = useState(null);



    const [openModalDoc, setopenModalDoc] = useState(false);
    const [TypeDoc, setTypeDoc] = useState("");


    


    const [openDelete, setOpenDelete] = useState(false);
    const [deleteId, setDeleteId] = useState(null);
    const [btnLoader, setBtnLoader] = useState(false);

    const [openDetail, setOpenDetail] = useState(false);
    const [detailClient, setDetailClient] = useState(null);
    const [detailLoader, setDetailLoader] = useState(false);

    const handleAdd = () => {
        setSelectedClient(null);
        setOpenModal(true);
    };

    const HandleAdd = () =>{
        setSelectedDoc(null)
        setOpenModalLE(true)

    }

    const handleView = (id) => {
    setOpenDetail(true);
    dispatch(fetchClientById(toast , id, setDetailLoader, setDetailClient));
};

    const handleEdit = (client) => {
        setSelectedClient(client);
        setOpenModal(true);
    };

    const handleDelete = (id) => {
        setDeleteId(id);
        setOpenDelete(true);
    };

    const confirmDelete = () => {
      dispatch(deleteClient(deleteId, toast, setOpenDelete, setBtnLoader));
    };

    const handleDoc = (id) => {
        setopenModalDoc(true);
        console.log("id" , id , openModalDoc )
        setSelectedClientid(id)
    };

    const onCOrder = (client) => {
        setSelectedClient(client)
        setOpenModalO(true)

    } 

    const onCDevis = (client) => {
    setSelectedClient(client);
    setOpenModalD(true);
};

    
    return (
        <div className="px-6 py-8">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-slate-800">Clients</h1>
                <button
                    onClick={handleAdd}
                    className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg font-medium"
                >
                    + Ajouter client
                </button>
            </div>

            <ClientFilter />

            {isLoading ? (
                <Loader />
            ) : errorMessage ? (
                <div className="flex justify-center items-center h-[200px]">
                    <FaExclamationTriangle className="text-slate-800 text-3xl mr-2" />
                    <span className="text-slate-800 text-lg font-medium">{errorMessage}</span>
                </div>
            ) : (
                <>
                    <ClientTable
                        clients={clients || []}
                        onEdit={handleEdit}
                        onDelete={handleDelete}
                        onView={handleView}
                        handleDoc = {handleDoc}
                        HandleAdd = {HandleAdd}
                        onCOrder = {onCOrder}
                        onCDevis={onCDevis}

                    />
                    <div className="flex justify-center pt-6">
                        <Paginations
                            numberOfPage={pagination?.totalPages}
                            totalProducts={pagination?.totalElements}
                        />
                    </div>
                </>
            )}

            <ClientModal open={openModal} setOpen={setOpenModal} client={selectedClient} />


            <ExamenModal open={openModalE} setOpen={setOpenModalE} clienId={selectedClientid} />
            <OrdonnanceLunetteModal open={openModalLU} setOpen={setOpenModalLU} clienId={selectedClientid} />
            <OrdonnanceLentilleModal open={openModalLE} setOpen={setOpenModalLE} clienId={selectedClientid}  />
            <SalesOrderModal open={openModalO} setOpen={setOpenModalO} client={selectedClient}  />


            <ClientDetailModal open={openDetail} setOpen={setOpenDetail} client={detailClient} />
            <DocModal open={openModalDoc}  setOpen={setopenModalDoc}  setTypeDoc={setTypeDoc}  
            openE={openModalE} setOpenE={setOpenModalE}
            openLU={openModalLU} setOpenLU={setOpenModalLU}
            openLE={openModalLE} setOpenLE={setOpenModalLE}
            
            
            />

            <DevisModal 
            open={openModalD} 
            setOpen={setOpenModalD} 
            client={selectedClient} />




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

export default Client;