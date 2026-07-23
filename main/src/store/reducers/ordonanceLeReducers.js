const initialState = {
    ordonancesE: null,
    paginationE: {},
};

export const ordonanceLeReducer = (state = initialState, action) => {
    switch (action.type) {
        case "FETCH_ORDLE":
            return {
                ...state,
                ordonancesE: action.payload,
                paginationE: {
                    ...state.paginationE,
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