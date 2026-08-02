import { useState } from 'react'
import './App.css'
import React from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import Home from "./component/home/home"
import { Toaster } from 'react-hot-toast'
import LogIn from './component/auth/login'
import DashboardLayout from './component/responsable/responsableLayout'
import PrivateRoute from './component/PrivateRoute'
import Client from './component/client/client'
import Examen from './component/examens/examen'
import Ordonnance from './component/ordonance/Ordonance'
import Produit from './component/produit/Produit'
import SalesOrder from './component/salesOrder/SalesOrder'
import Devis from './component/devis/Devis'
import MouvementStock from './component/mouvementStock/MouvementStock'
import Fournisseur from './component/fournisseur/Fournisseur'
import Commande from './component/commande/Commande'
import Notification from './component/notification/Notification'
import Dahsboard from './component/dahsboard/Dahsboard'
import Responsable from './component/crudResponsable/Responsable'

function App() {

  return (
    <>
      <React.Fragment>
        <Router>
          <Routes>

            <Route element={<PrivateRoute publicPage />}>
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<LogIn />} />
            </Route>

            <Route element={<PrivateRoute allowedRoles={["ROLE_ADMIN"]} />}>
              <Route path="/admin-dashboard/*" element={<DashboardLayout role="admin" />}>
                <Route path="dash" element={<Dahsboard />} />
                <Route path="fournisseurs" element={<Fournisseur />} />
                <Route path="commandes" element={<Commande />} />
                <Route path="responsables" element={<Responsable />} />
              </Route>
            </Route>

            <Route element={<PrivateRoute allowedRoles={["ROLE_RESPONSABLE"]} />}>
              <Route path="/responsable-dashboard/*" element={<DashboardLayout role="responsable" />}>
                <Route path="clients" element={<Client />} />
                <Route path="examens" element={<Examen />} />
                <Route path="ordonnance" element={<Ordonnance />} />
                <Route path="produits" element={<Produit />} />
                <Route path="ventes" element={<SalesOrder />} />
                <Route path="devis" element={<Devis />} />
                <Route path="mouvements-stock" element={<MouvementStock />} />
                <Route path="notifications" element={<Notification />} />
                <Route path="dash" element={<Dahsboard />} />
              </Route>
            </Route>

          </Routes>
        </Router>
        <Toaster position='bottom-center' />
      </React.Fragment>
    </>
  )
}

export default App