const initialState = {
    salesOrderI: null,
    pagination :  {}
};

export const salesOrderReducersItems = (state = initialState, action) => {
    switch (action.type) {
        case "FETCH_SALES_ORDER_ITEMS":
            return {
                ...state,
                salesOrderI : action.payload,
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