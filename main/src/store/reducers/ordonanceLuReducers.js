const initialState = {
    ordonancesU: null,
    paginationU: {},
};

export const ordonanceLuReducer = (state = initialState, action) => {
    switch (action.type) {
        case "FETCH_ORDLU":
            return {
                ...state,
                ordonancesU : action.payload,
                paginationU: {
                    ...state.paginationU,
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