const initialState = {
    commandeI: null,
    pagination :  {}
};

export const commandeReducersItems = (state = initialState, action) => {
    switch (action.type) {
        case "FETCH_COMMANDE_ITEMS":
            return {
                ...state,
                commandeI : action.payload,
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