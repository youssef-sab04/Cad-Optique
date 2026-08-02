const initialState = {
    responsables: null,
    pagination: {},
};

export const responsableReducer = (state = initialState, action) => {
    switch (action.type) {
        case "FETCH_RESPONSABLES":
            return {
                ...state,
                responsables: action.payload,
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