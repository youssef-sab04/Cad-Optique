const initialState = {
    devis: null,
    pagination: {}
};

export const devisReducers = (state = initialState, action) => {
    switch (action.type) {
        case "FETCH_DEVIS":
            return {
                ...state,
                devis: action.payload,
                pagination: {
                    ...state.pagination,
                    pageNumber: action.pageNumber,
                    pageSize: action.pageSize,
                    totalElements: action.totalElements,
                    totalPages: action.totalPages,
                    lastPage: action.lastPage,
                },
            };

        case "UPDATE_DEVIS_TOTAL":
            return {
                ...state,
                devis: state.devis.map((d) =>
                    d.id === action.payload.id
                        ? { ...d, totalprice: action.payload.totalprice }
                        : d
                ),
            };

        default:
            return state;
    }
};