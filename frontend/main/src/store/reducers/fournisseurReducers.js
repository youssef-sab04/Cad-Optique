const initialState = {
    fournisseurs: null,
    pagination: {},
};

export const fournisseurReducer = (state = initialState, action) => {
    switch (action.type) {
        case "FETCH_FOURNISSEURS":
            return {
                ...state,
                fournisseurs: action.payload,
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
