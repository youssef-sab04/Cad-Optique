const initialState = {
    notificationsP: null,
    notificationsC: null,
    paginationP: {},
    paginationC: {},

};

export const notificationReducer = (state = initialState, action) => {
    switch (action.type) {
        case "FETCH_NOTIFICATIONS_PRODUITS":
            return {
                ...state,
                notificationsP: action.payload,
                paginationP: {
                    ...state.paginationP,
                    pageNumber: action.pageNumber,
                    pageSize: action.pageSize,
                    totalElements: action.totalElements,
                    totalPages: action.totalPages,
                    lastPage: action.lastPage,
                },
                
            };
        
        case "FETCH_NOTIFICATIONS_CLIENTS":
            return {
                ...state,
                notificationsC: action.payload,
                paginationP: {
                    ...state.paginationC,
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
           
