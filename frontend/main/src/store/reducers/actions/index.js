import api from "../../../api/api";

export const authenticateSignInUser
    = (sendData, toast, reset, navigate, setLoader) => async (dispatch) => {
        try {
            setLoader(true);
            const { data } = await api.post("/auth/signin", sendData);
            dispatch({ type: "LOGIN_USER", payload: data });
            localStorage.setItem("auth", JSON.stringify(data));
            reset();
            toast.success("Login Success");
            const roles = data.roles || [];
            if (roles.includes("ROLE_ADMIN")) navigate("/admin-dashboard");
            else if (roles.includes("ROLE_RESPONSABLE")) navigate("/responsable-dashboard");
        } catch (error) {
            console.log(error);
            toast.error(error?.response?.data?.message || "Internal Server Error");
        } finally {
            setLoader(false);
        }
    }

export const logOutUser = (navigate) => (dispatch) => {
    dispatch({ type: "LOG_OUT" });
    localStorage.removeItem("auth");
    navigate("/login");
};

export const fetchClients = (queryStrin) => async (dispatch) => {
    try {
        dispatch({ type: "IS_FETCHING" });
        const { data } = await api.get(`/public/clients?${queryStrin}`);
        dispatch({
            type: "FETCH_CLIENTS",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    }

    catch (error) {
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch examens",
        });
    }

};

export const fetchClientById = (toast , id, setLoader, setClient) => async () => {
    try {
        setLoader(true);
        const { data } = await api.get(`/public/clients/${id}`);
        setClient(data);
    } catch (error) {
       toast.error(error?.response?.data?.message || "Erreur lors du chargement");
    } finally {
        setLoader(false);
    }
};

export const addClient = (clientData, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.post(`/admin/client`, clientData);
        dispatch({ type: "ADD_CLIENT", payload: data });
        toast.success("Client ajouté");
        reset();
        setOpen(false);
        dispatch(fetchClients());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de l'ajout");
    } finally {
        setBtnLoader(false);
    }
};

export const updateClient = (id, clientData, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.put(`/admin/clients/${id}`, clientData);
        dispatch({ type: "UPDATE_CLIENT", payload: data });
        toast.success("Client modifié");
        reset();
        setOpen(false);
        dispatch(fetchClients());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la modification");
    } finally {
        setBtnLoader(false);
    }
};

export const deleteClient = (id, toast, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        await api.delete(`/admin/clients/${id}`);
        dispatch({ type: "DELETE_CLIENT", payload: id });
        toast.success("Client supprimé");
        setOpen(false);
        dispatch(fetchClients());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la suppression");
    } finally {
        setBtnLoader(false);
    }
};

export const fetchExamens = (queryStrin) => async (dispatch) => {
    try {
        dispatch({ type: "IS_FETCHING" });
        const { data } = await api.get(`/public/examens?${queryStrin}`);
        dispatch({
            type: "FETCH_EXAMENS",
            payload: data.examenDTOS,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    }

    catch (error) {
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch examens",
        });
    }

};
export const fetchExamenById = (toast , id, setLoader, seExamen) => async () => {
    try {
        setLoader(true);
        const { data } = await api.get(`/admin/client/examen/${id}`);
        seExamen(data);
    } catch (error) {
       toast.error(error?.response?.data?.message || "Erreur lors du chargement");
    } finally {
        setLoader(false);
    }
};


export const updateExamen = (id, examenData, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.put(`/admin/examen/${id}`, examenData);
        dispatch({ type: "UPDATE_EXAMEN", payload: data });
        toast.success("Examen modifié");
        reset();
        setOpen(false);
        dispatch(fetchExamens());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la modification");
    } finally {
        setBtnLoader(false);
    }
};

export const deleteExamen = (id, toast, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        await api.delete(`/admin/examen/${id}`);
        dispatch({ type: "DELETE_EXAMEN", payload: id });
        toast.success("Examen supprimé");
        setOpen(false);
        dispatch(fetchExamens());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la suppression");
    } finally {
        setBtnLoader(false);
    }
};

export const fetchOLU = (queryStrin) => async (dispatch) => {
    try {
        dispatch({ type: "IS_FETCHING" });
        const { data } = await api.get(`/public/ordonances-lunette?${queryStrin}`);
        dispatch({
            type: "FETCH_ORDLU",
            payload: data.ordonnanceLunetteDTOS,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    }

    catch (error) {
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch ordonances",
        });
    }

};
//scan
export const addOrdLunScan = (clientId, imageFile, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const formData = new FormData();
        formData.append("image", imageFile);
        const { data } = await api.post(`/admin/ordonance-lunette/scan/${clientId}`, formData, {
            headers: { "Content-Type": "multipart/form-data" },
        });
        dispatch({ type: "ADD_ORDLU", payload: data });
        toast.success("Ordonnance ajoutée");
        reset();
        setOpen(false);
        dispatch(fetchOLU());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de l'ajout");
    } finally {
        setBtnLoader(false);
    }
};

export const previewScanOrdLun = (imageFile, toast, setPreviewData, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const formData = new FormData();
        formData.append("image", imageFile);
        const { data } = await api.post(`/admin/ordonance-lunette/scan-preview`, formData, {
            headers: { "Content-Type": "multipart/form-data" },
        });
        setPreviewData(data.dto);
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de l'extraction");
    } finally {
        setBtnLoader(false);
    }
};

export const addOrdLunAvecImage = (clientId, ordonnanceData, imageFile, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const formData = new FormData();
        formData.append("ordonnance", new Blob([JSON.stringify(ordonnanceData)], { type: "application/json" }));
        if (imageFile) formData.append("image", imageFile);
        const { data } = await api.post(`/admin/client/ordonance-lunette-with-image/${clientId}`, formData, {
            headers: { "Content-Type": "multipart/form-data" },
        });
        dispatch({ type: "ADD_ORDLU", payload: data });
        toast.success("Ordonnance ajoutée");
        reset();
        setOpen(false);
        dispatch(fetchOLU());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de l'ajout");
    } finally {
        setBtnLoader(false);
    }
};

export const fetchOLe = (queryStrin) => async (dispatch) => {
    try {
        dispatch({ type: "IS_FETCHING" });
        const { data } = await api.get(`/public/ordonnances-lentille?${queryStrin}`);
        dispatch({
            type: "FETCH_ORDLE",
            payload: data.ordonnanceLentilleDTOS,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    }

    catch (error) {
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch ordonances",
        });
    }

};

export const fetchOrdLunById = (toast, id, setLoader, setOrdonnance) => async () => {
    try {
        setLoader(true);
        const { data } = await api.get(`/public/ordonance-lunette/${id}`);
        setOrdonnance(data);
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors du chargement");
    } finally {
        setLoader(false);
    }
};

export const fetchOrdLenById = (toast, id, setLoader, setOrdonnance) => async () => {
    try {
        setLoader(true);
        const { data } = await api.get(`/public/ordonance-lentille/${id}`);
        setOrdonnance(data);
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors du chargement");
    } finally {
        setLoader(false);
    }
};

export const updateOrdLun = (id, ordonnanceData, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.put(`/admin/ordonnance-lunette/${id}`, ordonnanceData);
        dispatch({ type: "UPDATE_ORDLU", payload: data });
        toast.success("Ordonnance modifiée");
        reset();
        setOpen(false);
        dispatch(fetchOLU());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la modification");
    } finally {
        setBtnLoader(false);
    }
};

export const deleteOrdLun = (id, toast, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        await api.delete(`/admin/ordonnance-lunette/${id}`);
        dispatch({ type: "DELETE_ORDLU", payload: id });
        toast.success("Ordonnance supprimée");
        setOpen(false);
        dispatch(fetchOLU());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la suppression");
    } finally {
        setBtnLoader(false);
    }
};

export const updateOrdLen = (id, ordonnanceData, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.put(`/admin/ordonnance-lentille/${id}`, ordonnanceData);
        dispatch({ type: "UPDATE_ORDLE", payload: data });
        toast.success("Ordonnance modifiée");
        reset();
        setOpen(false);
        dispatch(fetchOLe());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la modification");
    } finally {
        setBtnLoader(false);
    }
};

export const deleteOrdLen = (id, toast, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        await api.delete(`/admin/ordonnance-lentille/${id}`);
        dispatch({ type: "DELETE_ORDLE", payload: id });
        toast.success("Ordonnance supprimée");
        setOpen(false);
        dispatch(fetchOLe());
    } catch (error) {
    toast.error(error?.response?.data?.message || "Erreur lors de la suppression");
    } finally {
        setBtnLoader(false);
    }
};

export const addExamen = (clientId, examenData, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.post(`/admin/client/examen/${clientId}`, examenData);
        dispatch({ type: "ADD_EXAMEN", payload: data });
        toast.success("Examen ajouté");
        reset();
        setOpen(false);
        dispatch(fetchExamens());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de l'ajout");
    } finally {
        setBtnLoader(false);
    }
};

export const addOrdLun = (clientId, ordonnanceData, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.post(`/admin/client/ordonance-lunette/${clientId}`, ordonnanceData);
        dispatch({ type: "ADD_ORDLU", payload: data });
        toast.success("Ordonnance ajoutée");
        reset();
        setOpen(false);
        dispatch(fetchOLU());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de l'ajout");
    } finally {
        setBtnLoader(false);
    }
};

export const addOrdLen = (clientId, ordonnanceData, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.post(`/admin/client/ordonnance-lentille/${clientId}`, ordonnanceData);
        dispatch({ type: "ADD_ORDLE", payload: data });
        toast.success("Ordonnance ajoutée");
        reset();
        setOpen(false);
        dispatch(fetchOLe());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de l'ajout");
    } finally {
        setBtnLoader(false);
    }
};

export const fetchProducts = (queryStrin) => async (dispatch) => {
    try {
        dispatch({ type: "IS_FETCHING" });
        const { data } = await api.get(`/public/products?${queryStrin}`);
        dispatch({
            type: "FETCH_PRODUCTS",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    }

    catch (error) {
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch products",
        });
    }

};
export const fetchCategories = () => async (dispatch) => {
    try {
        dispatch({ type: "CATEGORY_LOADER" });
        const { data } = await api.get(`/public/categories`);
        dispatch({
            type: "FETCH_CATEGORIES",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "CATEGORY_SUCCESS" });
    } catch (error) {
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch categories",
        });
    }
};

export const addCategory = (categoryData, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const payload = {
            ...categoryData,
            tva: categoryData?.tva === "" || categoryData?.tva == null ? 20 : Number(categoryData.tva),
        };
        const { data } = await api.post(`/admin/category`, payload);
        dispatch({ type: "ADD_CATEGORY", payload: data });
        toast.success("Catégorie ajoutée");
        reset({ nom: "", description: "", tva: 20 });
        setOpen(false);
        dispatch(fetchCategories());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de l'ajout de la catégorie");
    } finally {
        setBtnLoader(false);
    }
};

export const updateCategory = (categoryId, categoryData, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const payload = {
            ...categoryData,
            tva: categoryData?.tva === "" || categoryData?.tva == null ? 20 : Number(categoryData.tva),
        };
        const { data } = await api.put(`/admin/categorys/${categoryId}`, payload);
        dispatch({ type: "UPDATE_CATEGORY", payload: data });
        toast.success("Catégorie modifiée");
        reset({ nom: "", description: "", tva: 20 });
        setOpen(false);
        dispatch(fetchCategories());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la modification de la catégorie");
    } finally {
        setBtnLoader(false);
    }
};

export const deleteCategory = (categoryId, toast, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        await api.delete(`/admin/categorys/${categoryId}`);
        dispatch({ type: "DELETE_CATEGORY", payload: categoryId });
        toast.success("Catégorie supprimée");
        setOpen(false);
        dispatch(fetchCategories());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la suppression de la catégorie");
    } finally {
        setBtnLoader(false);
    }
};

export const updateProd = (id, produitData, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.put(`/admin/products/${id}`, produitData);
        dispatch({ type: "UPDATE_PROD", payload: data });
        toast.success("produit modifiée");
        reset();
        setOpen(false);
        dispatch(fetchProducts());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la modification");
    } finally {
        setBtnLoader(false);
    }
};

export const deleteProd = (id, toast, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        await api.delete(`/admin/products/${id}`);
        dispatch({ type: "DELETE_PRDOF", payload: id });
        toast.success("Produit supprimé");
        setOpen(false);
        dispatch(fetchProducts());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la suppression");
    } finally {
        setBtnLoader(false);
    }
};
export const addProduct = (categoryId, produitData, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.post(`/admin/produits/${categoryId}`, produitData);
        dispatch({ type: "ADD_PROD", payload: data });
        toast.success("Produit ajouté");
        reset();
        setOpen(false);
        dispatch(fetchProducts());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de l'ajout");
    } finally {
        setBtnLoader(false);
    }
};

export const addSalesOrder = (id, dataS, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.post(`/admin/salesorder/clients/${id}`, dataS);
        dispatch({ type: "ADD_SALES_ORDER", payload: data });
toast.success(`Sales Order Ajouté `);        reset();
        setOpen(false);
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de l'ajout");
    } finally {
        setBtnLoader(false);
    }
};
export const downloadSalesOrderPdf = (id, toast) => async () => {
    try {
        const response = await api.get(`/admin/salesorder/${id}/pdf`, { responseType: "blob" });
        const url = window.URL.createObjectURL(new Blob([response.data], { type: "application/pdf" }));
        window.open(url, "_blank");
    } catch (error) {
        toast.error("Erreur lors de la génération du PDF");
    }
};

export const fetchSalesOrders = (queryStrin) => async (dispatch) => {
    try {
        dispatch({ type: "IS_FETCHING" });
        const { data } = await api.get(`/public/salesorders?${queryStrin}`);
        dispatch({
            type: "FETCH_SALES_ORDERS",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    } catch (error) {
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch sales orders",
        });
    }
};

export const fetchSalesOrderItems = (queryStrin) => async (dispatch) => {
    try {
        dispatch({ type: "IS_FETCHING" });
        const { data } = await api.get(`/public/salesorderitems?${queryStrin}`);
        dispatch({
            type: "FETCH_SALES_ORDER_ITEMS",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    } catch (error) {
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch sales order items",
        });
    }
};

export const fetchSalesOrderItemsID = (toast, id, setLoader, setSOI) => async () => {
    try {
        setLoader(true);
        const { data } = await api.get(`/public/salesorderitems/salesorders/${id}`);
        setSOI(data);
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors du chargement");
    } finally {
        setLoader(false);
    }
};
export const deleteSaleOrder = (id, toast, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        await api.delete(`/admin/salesorders/${id}`);
        dispatch({ type: "DELETE_OR", payload: id });
        toast.success("Ordre supprime");
        setOpen(false);
        dispatch(fetchSalesOrders());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la suppression");
    } finally {
        setBtnLoader(false);
    }
};

export const deleteSaleOrderItem = (id, toast, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        await api.delete(`/admin/salesorderitems/${id}`);
        dispatch({ type: "DELETE_ORI", payload: id });
        toast.success("Ordre item supprime");
        dispatch(fetchSalesOrderItems());
        setOpen(false);
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la suppression");
    } finally {
        setBtnLoader(false);
    }
};
export const updateSaleOrderItemQTE = (id, qte, toast, setSOI, orderId) => async (dispatch) => {
    try {
        const { data } = await api.put(`/admin/salesorderitems/${id}`, { quantity: qte });
        dispatch({ type: "UPDATE_QTE_OI", payload: data });

        setSOI((prev) => {
            const updated = prev.map((item) =>
                item.id === id ? { ...item, ...data, produitDTO: item.produitDTO } : item
            );
            const newTotal = updated.reduce(
                (sum, item) => sum + Number(item.quantity ?? 0) * Number(item.price ?? 0),
                0
            );
            dispatch({ type: "UPDATE_ORDER_TOTAL", payload: { id: orderId, totalprice: newTotal } });
            return updated;
        });

       // toast.success("Qte updated");
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la modification");
    }
};

export const addSalesOrderItem = (
    salesOrderId,
    produitId,
    quantity,
    toast,
    setOpen,
    setBtnLoader,
    reset,
    onSuccess
) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.post(
            `/admin/salesorderitems/salesorders/${salesOrderId}/produits/${produitId}`,
            { quantity }
        );
        dispatch({ type: "ADD_OI", payload: data });
        toast.success("Produit ajouté");
        reset();
        setOpen(false);
        if (onSuccess) onSuccess(data);
        dispatch(fetchSalesOrders())
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de l'ajout");
    } finally {
        setBtnLoader(false);
    }
};

export const confirmSaleOrder = (id, toast, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.post(`/admin/ordre/${id}`);
        dispatch({ type: "CONFIRM_OR", payload: data });
        toast.success("Ordre validé");
        setOpen(false);
        dispatch(fetchSalesOrders())
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la validation tets");
    } finally {
        setBtnLoader(false);
    }
};

//   DEVIS

export const addDevis = (id, dataS, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.post(`/admin/devis/clients/${id}`, dataS);
        dispatch({ type: "ADD_DEVIS", payload: data });
        toast.success(`Devis ajouté`);
        reset();
        setOpen(false);
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de l'ajout");
    } finally {
        setBtnLoader(false);
    }
};

export const fetchDevis = (queryStrin) => async (dispatch) => {
    try {
        dispatch({ type: "IS_FETCHING" });
        const { data } = await api.get(`/public/devis?${queryStrin}`);
        dispatch({
            type: "FETCH_DEVIS",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    } catch (error) {
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch devis",
        });
    }
};

export const fetchDevisItemsID = (toast, id, setLoader, setDI) => async () => {
    try {
        setLoader(true);
        const { data } = await api.get(`/public/devisitems/devis/${id}`);
        setDI(data);
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors du chargement");
    } finally {
        setLoader(false);
    }
};

export const deleteDevis = (id, toast, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        await api.delete(`/admin/devis/${id}`);
        dispatch({ type: "DELETE_DEVIS", payload: id });
        toast.success("Devis supprimé");
        setOpen(false);
        dispatch(fetchDevis());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la suppression");
    } finally {
        setBtnLoader(false);
    }
};

export const deleteDevisItem = (id, toast, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        await api.delete(`/admin/devisitems/${id}`);
        dispatch({ type: "DELETE_DI", payload: id });
        toast.success("Item supprimé");
        dispatch(fetchDevis());
        setOpen(false);
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la suppression");
    } finally {
        setBtnLoader(false);
    }
};

export const updateDevisItemQTE = (id, qte, toast, setDI, devisId) => async (dispatch) => {
    try {
        const { data } = await api.put(`/admin/devisitems/${id}`, { quantity: qte });
        dispatch({ type: "UPDATE_QTE_DI", payload: data });

        setDI((prev) => {
            const updated = prev.map((item) =>
                item.id === id ? { ...item, ...data, produitDTO: item.produitDTO } : item
            );
            const newTotal = updated.reduce(
                (sum, item) => sum + Number(item.quantity ?? 0) * Number(item.price ?? 0),
                0
            );
            dispatch({ type: "UPDATE_DEVIS_TOTAL", payload: { id: devisId, totalprice: newTotal } });
            return updated;
        });
        dispatch(fetchDevis())

        toast.success("Qte updated");
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la modification");
    }
};

export const addDevisItem = (
    devisId,
    produitId,
    quantity,
    toast,
    setOpen,
    setBtnLoader,
    reset,
    onSuccess
) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.post(
            `/admin/devisitems/devis/${devisId}/produits/${produitId}`,
            { quantity }
        );
        dispatch({ type: "ADD_DI", payload: data });
        toast.success("Produit ajouté");
        reset();
        setOpen(false);
        if (onSuccess) onSuccess(data);
                dispatch(fetchDevis())

    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de l'ajout");
    } finally {
        setBtnLoader(false);
    }
};

export const cancelDevis = (id, toast, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.post(`/admin/devis/${id}/cancel`);
        dispatch({ type: "CANCEL_DEVIS", payload: data });
        toast.success("Devis annulé");
        setOpen(false);
        dispatch(fetchDevis());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de l'annulation");
    } finally {
        setBtnLoader(false);
    }
};

export const confirmDevis = (id, toast, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.post(`/admin/devis/${id}/confirm`);
        dispatch({ type: "CONFIRM_DEVIS", payload: data });
        toast.success("Devis confirmé, vente créée");
        setOpen(false);
        dispatch(fetchDevis());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la confirmation");
    } finally {
        setBtnLoader(false);
    }
};

export const downloadDevisPdf = (id, toast) => async () => {
    try {
        const response = await api.get(`/admin/devis/${id}/pdf`, { responseType: "blob" });
        const url = window.URL.createObjectURL(new Blob([response.data], { type: "application/pdf" }));
        window.open(url, "_blank");
    } catch (error) {
        toast.error("Erreur lors de la génération du PDF");
    }
};

// Mvt 
export const fetchMouvementsStock = (queryStrin) => async (dispatch) => {
    try {
        dispatch({ type: "IS_FETCHING" });
        const { data } = await api.get(`/public/mouvements-stock?${queryStrin}`);
        dispatch({
            type: "FETCH_MOUVEMENTS_STOCK",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    } catch (error) {
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch mouvements",
        });
    }
};

/// Fournisseurs
export const fetchFournisseurs = (queryStrin) => async (dispatch) => {
    try {
        dispatch({ type: "IS_FETCHING" });
        const { data } = await api.get(`/public/fournisseurs?${queryStrin || ""}`);
        dispatch({
            type: "FETCH_FOURNISSEURS",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    } catch (error) {
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch fournisseurs",
        });
    }
};

export const addFournisseur = (fournisseurData, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.post(`/admin/fournisseur`, fournisseurData);
        dispatch({ type: "ADD_FOURNISSEUR", payload: data });
        toast.success("Fournisseur ajouté");
        reset();
        setOpen(false);
        dispatch(fetchFournisseurs());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de l'ajout");
    } finally {
        setBtnLoader(false);
    }
};

export const updateFournisseur = (id, fournisseurData, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.put(`/admin/fournisseurs/${id}`, fournisseurData);
        dispatch({ type: "UPDATE_FOURNISSEUR", payload: data });
        toast.success("Fournisseur modifié");
        reset();
        setOpen(false);
        dispatch(fetchFournisseurs());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la modification");
    } finally {
        setBtnLoader(false);
    }
};

export const deleteFournisseur = (id, toast, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        await api.delete(`/admin/fournisseurs/${id}`);
        dispatch({ type: "DELETE_FOURNISSEUR", payload: id });
        toast.success("Fournisseur supprimé");
        setOpen(false);
        dispatch(fetchFournisseurs());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la suppression");
    } finally {
        setBtnLoader(false);
    }
};



// Commande

export const fetchCommandes = (queryStrin) => async (dispatch) => {
    try {
        dispatch({ type: "IS_FETCHING" });
        const { data } = await api.get(`/public/commandes?${queryStrin}`);
        dispatch({
            type: "FETCH_COMMANDES",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    } catch (error) {
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch commandes",
        });
    }
};

export const fetchCommandeItemsID = (toast, id, setLoader, setCI) => async () => {
    try {
        setLoader(true);
        const { data } = await api.get(`/public/commandeItems/commandes/${id}`);
        setCI(data);
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors du chargement");
    } finally {
        setLoader(false);
    }
};

export const addCommande = (id, dataS, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.post(`/admin/commande/fournisseurs/${id}`, dataS);
        dispatch({ type: "ADD_COMMANDE", payload: data });
        toast.success(`Commande Ajouté `);        
        reset();
        setOpen(false);
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de l'ajout");
    } finally {
        setBtnLoader(false);
    }
};

export const deleteCommande = (id, toast, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        await api.delete(`/admin/commandes/${id}`);
        dispatch({ type: "DELETE_COMMANDE", payload: id });
        toast.success("Ordre supprime");
        setOpen(false);
        dispatch(fetchCommandes());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la suppression");
    } finally {
        setBtnLoader(false);
    }
};

export const addCommandeItem = (commandeId, produitId, quantity, price, toast, setOpen, 
    setBtnLoader, reset, onSuccess) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.post(
            `/admin/commandeItem/commandes/${commandeId}/produits/${produitId}`,
            { quantity  , price }
        );
       dispatch({ type: "ADD_CI", payload: data });
dispatch({ type: "ADD_COMMANDE_TOTAL", payload: { id: commandeId, amount: Number(quantity) * Number(price) } });
toast.success("Produit ajouté");
        reset();
        setOpen(false);
        if (onSuccess) onSuccess(data);
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de l'ajout");
    } finally {
        setBtnLoader(false);
    }
};


export const deleteCommandeItem = (id, toast, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        await api.delete(`/admin/commandeItems/${id}`);
        dispatch({ type: "DELETE_CI", payload: id });
        dispatch(fetchCommandes());
        toast.success("Ordre item supprime");
        setOpen(false);
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la suppression");
    } finally {
        setBtnLoader(false);
    }
};

export const updateCommandeItemQTE = (id, qte, toast, setCI, orderId) => async (dispatch) => {
    try {
        const { data } = await api.put(`/admin/commandeItems/${id}`, { quantity: qte });
        dispatch({ type: "UPDATE_QTE_CI", payload: data });

        setCI((prev) => {
            const updated = prev.map((item) =>
                item.id === id ? { ...item, ...data, produitDTO: item.produitDTO } : item
            );
            const newTotal = updated.reduce(
                (sum, item) => sum + Number(item.quantity ?? 0) * Number(item.price ?? 0),
                0
            );
            dispatch({ type: "UPDATE_COMMANDE_TOTAL", payload: { id: orderId, totalprice: newTotal } });
            return updated;
        });

        toast.success("Qte updated");
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la modification");
    }
};

export const fetchCommandesItems = (queryStrin) => async (dispatch) => {
    try {
        dispatch({ type: "IS_FETCHING" });
        const { data } = await api.get(`/public/commandeItems?${queryStrin}`);
        dispatch({
            type: "FETCH_COMMANDE_ITEMS",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    } catch (error) {
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch commande items",
        });
    }
};


export const confirmCommande = (id, toast, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.post(`/admin/commande/${id}`);
        dispatch({ type: "COMM_OR", payload: data });
        toast.success("Commande validé");
        setOpen(false);
        dispatch(fetchCommandes())
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la validation");
    } finally {
        setBtnLoader(false);
    }
};

// Notif Prod
export const fetchNotifProd = (queryStrin) => async (dispatch) => {
    try {
        dispatch({ type: "IS_FETCHING" });
        const { data } = await api.get(`/admin/notifications?${queryStrin}`);
        dispatch({
            type: "FETCH_NOTIFICATIONS_PRODUITS",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    } catch (error) {
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch Notications",
        });
    }
};

export const viewNotification = (id , toast) => async (dispatch) => {
    try {
        const { data } = await api.put(`/admin/notifications/${id}`);
       // dispatch({ type: "VIEW_NOTIF" });
        toast.success("Notification consulte");
       // dispatch(fetchNotifProd());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la modification");
    } finally {
    }
};

export const fetchNotifClients = (queryStrin) => async (dispatch) => {
    try {
        dispatch({ type: "IS_FETCHING" });
        const { data } = await api.get(`/public/notifications/clients?${queryStrin}`);
        dispatch({
            type: "FETCH_NOTIFICATIONS_CLIENTS",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    } catch (error) {
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch Notications",
        });
    }
};

/// Responsables
export const fetchResponsables = (queryStrin) => async (dispatch) => {
    try {
        dispatch({ type: "IS_FETCHING" });
        const { data } = await api.get(`/auth/admin/responsables?${queryStrin || ""}`);
        dispatch({
            type: "FETCH_RESPONSABLES",
            payload: data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage,
        });
        dispatch({ type: "IS_SUCCESS" });
    } catch (error) {
        console.log(error);
        dispatch({
            type: "IS_ERROR",
            payload: error?.response?.data?.message || "Failed to fetch responsables",
        });
    }
};

export const addResponsable = (respoData, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.post(`/auth/admin/add_responsable`, respoData);
        dispatch({ type: "ADD_RESPONSABLE", payload: data });
        toast.success("Responsable ajouté");
        reset();
        setOpen(false);
        dispatch(fetchResponsables());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de l'ajout");
    } finally {
        setBtnLoader(false);
    }
};

export const updateResponsable = (id, respoData, toast, reset, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        const { data } = await api.put(`/auth/admin/responsables/${id}`, respoData);
        dispatch({ type: "UPDATE_RESPONSABLE", payload: data });
        toast.success("Responsable modifié");
        reset();
        setOpen(false);
        dispatch(fetchResponsables());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la modification");
    } finally {
        setBtnLoader(false);
    }
};

export const deleteResponsable = (id, toast, setOpen, setBtnLoader) => async (dispatch) => {
    try {
        setBtnLoader(true);
        await api.delete(`/auth/admin/responsables/${id}`);
        dispatch({ type: "DELETE_RESPONSABLE", payload: id });
        toast.success("Responsable supprimé");
        setOpen(false);
        dispatch(fetchResponsables());
    } catch (error) {
        toast.error(error?.response?.data?.message || "Erreur lors de la suppression");
    } finally {
        setBtnLoader(false);
    }
};

