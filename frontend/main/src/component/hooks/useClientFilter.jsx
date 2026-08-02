import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useSearchParams } from "react-router-dom";
import { fetchClients } from "../../store/reducers/actions";

const useClientFilter = () => {
    const [searchParams] = useSearchParams();
    const dispatch = useDispatch();

    useEffect(() => {
        const params = new URLSearchParams();

        const currentPage = searchParams.get("page") ? Number(searchParams.get("page")) : 1;
        const keyword = searchParams.get("keyword") || null;

        params.set("pageNumber", currentPage - 1);
        if (keyword) params.set("keyword", keyword);
        dispatch(fetchClients(params.toString()));
        console.log("sP" , searchParams)
    }, [dispatch, searchParams]);
};

export default useClientFilter;