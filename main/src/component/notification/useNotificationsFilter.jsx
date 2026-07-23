import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useSearchParams } from "react-router-dom";
import { fetchNotifProd, fetchNotifClients } from "../../store/reducers/actions";

const useNotificationsFilter = (type) => {
    const [searchParams] = useSearchParams();
    const dispatch = useDispatch();

    useEffect(() => {
        const params = new URLSearchParams();

        const currentPage = searchParams.get("page") ? Number(searchParams.get("page")) : 1;
        const keyword = searchParams.get("keyword") || null;
        const sortOrder = searchParams.get("sortby") || "desc";

        params.set("pageNumber", currentPage - 1);
        params.set("sortOrder", sortOrder);
        if (keyword) params.set("keyword", keyword);

        type === "produit" ? dispatch(fetchNotifProd(params.toString())) : dispatch(fetchNotifClients(params.toString()));
    }, [dispatch, searchParams, type]);
};

export default useNotificationsFilter;