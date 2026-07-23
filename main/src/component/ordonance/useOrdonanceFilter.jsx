import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useSearchParams } from "react-router-dom";
import { fetchOLU , fetchOLe } from "../../store/reducers/actions";

const useOrdonanceFilter = () => {
    const [searchParams] = useSearchParams();
    const dispatch = useDispatch();

    useEffect(() => {
        const params = new URLSearchParams();

        const currentPage = searchParams.get("page") ? Number(searchParams.get("page")) : 1;
        const keyword = searchParams.get("keyword") || null;

        params.set("pageNumber", currentPage - 1);
        if (keyword) params.set("keyword", keyword);
        dispatch(fetchExamens(params.toString()));
    }, [dispatch, searchParams]);
};

export default useOrdonanceFilter;