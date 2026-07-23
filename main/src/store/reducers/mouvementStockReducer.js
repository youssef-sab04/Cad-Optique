const initialState = {
    mouvements: null,
    pagination: {}
};

export const mouvementStockReducer = (state = initialState, action) => {
    switch (action.type) {
        case "FETCH_MOUVEMENTS_STOCK":
            return {
                ...state,
                mouvements: action.payload,
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