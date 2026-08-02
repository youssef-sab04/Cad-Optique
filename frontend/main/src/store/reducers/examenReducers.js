const initialState = {
    examens: null,
    pagination: {},
};

export const examenReducer = (state = initialState, action) => {
    switch (action.type) {
        case "FETCH_EXAMENS":
            return {
                ...state,
                examens: action.payload,
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