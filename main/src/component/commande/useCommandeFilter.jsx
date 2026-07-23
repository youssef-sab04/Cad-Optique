import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useSearchParams } from "react-router-dom";
import { fetchCommandes } from "../../store/reducers/actions";
const useCommandeFilter = () => {
    const [searchParams] = useSearchParams();
    const dispatch = useDispatch();

    useEffect(() => {
        const params = new URLSearchParams();

        const currentPage = searchParams.get("page")
            ? Number(searchParams.get("page"))
            : 1;

        const sortOrder = searchParams.get("sortby") || "desc";

        params.set("pageNumber", currentPage - 1);
        params.set("sortOrder", sortOrder);

        const queryString = params.toString();
        dispatch(fetchCommandes(queryString));

    }, [dispatch, searchParams]);
};

export default useCommandeFilter;