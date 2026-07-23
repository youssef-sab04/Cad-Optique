import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useSearchParams } from "react-router-dom";
import { fetchDevis } from "../../store/reducers/actions";

const useDevisFilter = () => {
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
        dispatch(fetchDevis(queryString));

    }, [dispatch, searchParams]);
};

export default useDevisFilter;