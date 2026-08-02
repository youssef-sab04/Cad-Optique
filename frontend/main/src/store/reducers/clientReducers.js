const initialState = {
    clients: null,
    pagination: {},
};

export const clientReducer = (state = initialState, action) => {
    switch (action.type) {
        case "FETCH_CLIENTS":
            return {
                ...state,
                clients: action.payload,
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