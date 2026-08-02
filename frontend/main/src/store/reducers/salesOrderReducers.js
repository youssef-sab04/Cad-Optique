const initialState = {
    salesOrder: null,
    pagination : {}
};

export const salesOrderReducers = (state = initialState, action) => {
    switch (action.type) {
        case "FETCH_SALES_ORDERS":
            return {
                ...state,
                salesOrder : action.payload,
                pagination: {
                    ...state.pagination,
                    pageNumber: action.pageNumber,
                    pageSize: action.pageSize,
                    totalElements: action.totalElements,
                    totalPages: action.totalPages,
                    lastPage: action.lastPage,
                },
                
            };

        case "UPDATE_ORDER_TOTAL":
    return {
        ...state,
        salesOrder: state.salesOrder.map((order) =>
            order.id === action.payload.id
                ? { ...order, totalprice: action.payload.totalprice }
                : order
        ),
    };

        
        default:
            return state;
    }
};