const initialState = {
    user: JSON.parse(localStorage.getItem("auth")) || null,
}

export const authReducer = (state = initialState, action) => {
    switch (action.type) {     
        case "LOGIN_USER":
            return { ...state, user: action.payload };
        case "LOG_OUT":
            return { 
                user: null,
             };
            
            
        default:
            return state;
    }
};