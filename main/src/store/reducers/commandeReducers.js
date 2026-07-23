const initialState = {
    commande: null,
    pagination : {}
};

export const commandeReducers = (state = initialState, action) => {
    switch (action.type) {
        case "FETCH_COMMANDES":
            return {
                ...state,
                commande : action.payload,
                pagination: {
                    ...state.pagination,
                    pageNumber: action.pageNumber,
                    pageSize: action.pageSize,
                    totalElements: action.totalElements,
                    totalPages: action.totalPages,
                    lastPage: action.lastPage,
                },
                
            };

        case "UPDATE_COMMANDE_TOTAL":
    return {
        ...state,
        commande: state.commande.map((commande) =>
            commande.id === action.payload.id
                ? { ...commande, totalprice: action.payload.totalprice }
                : commande
        ),
    };

    case "ADD_COMMANDE_TOTAL":
    return {
        ...state,
        commande: state.commande.map((commande) =>
            commande.id === action.payload.id
                ? { ...commande, totalprice: Number(commande.totalprice ?? 0) + action.payload.amount }
                : commande
        ),
    };

        
        default:
            return state;
    }
};