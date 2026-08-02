const initialState = {
    devisI: null,
    pagination: {}
};

export const devisReducersItems = (state = initialState, action) => {
    switch (action.type) {
        case "FETCH_DEVIS_ITEMS":
            return {
                ...state,
                devisI: action.payload,
                pagination: {
                    ...state.pagination,
                    pageNumber: action.pageNumber,
                    pageSize: action.pageSize,
                    totalElements: action.totalElements,
                    totalPages: action.totalPages,
                    lastPage: action.lastPage,
                },
            };

        default:
            return state;
    }
};