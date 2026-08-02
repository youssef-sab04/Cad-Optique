import { configureStore } from '@reduxjs/toolkit';
import { authReducer } from "./authReducer";
import { errorReducer } from './errorReducer';
import { clientReducer } from './clientReducers';
import { examenReducer } from './examenReducers';
import { ordonanceLeReducer } from './ordonanceLeReducers';
import { ordonanceLuReducer } from './ordonanceLuReducers';
import { productReducer } from './productReducer';
import { salesOrderReducers } from './salesOrderReducers';
import { salesOrderReducersItems } from './salesOrderReducersItems';
import { devisReducers } from './devisReducers';
import { devisReducersItems } from './devisReducersItems';
import { mouvementStockReducer } from './mouvementStockReducer';
import {fournisseurReducer} from './fournisseurReducers'
import  {commandeReducers} from './commandeReducers'
import  {commandeReducersItems} from './commandeReducersItems'
import { notificationReducer } from './notificationReducer';





const user = localStorage.getItem("auth")
    ? JSON.parse(localStorage.getItem("auth"))
    : null;


    const initialState = {
    auth: { user : user },
};

export const store = configureStore({
    reducer: {
        auth  :  authReducer,
        errors   :  errorReducer,
        clients : clientReducer,
        examens : examenReducer,
        ordonanceLe : ordonanceLeReducer ,
        ordonanceLu : ordonanceLuReducer,
        produits : productReducer,
        salesOrders : salesOrderReducers ,
        salesOrdersI : salesOrderReducersItems,
        devis:  devisReducers,
        devisI:  devisReducersItems,
        mouvements: mouvementStockReducer,
        fournisseurs :fournisseurReducer,
        commandes : commandeReducers ,
        commandeIt : commandeReducersItems,
        notifications : notificationReducer

        




}});

export default store;
