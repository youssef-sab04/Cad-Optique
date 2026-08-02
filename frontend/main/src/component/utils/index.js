import {
  Users, Package, FileText, ShieldCheck, ChevronDown, Menu, X, ArrowRight, Glasses, LayoutDashboard, Receipt, SquarePen , ReceiptText , 
ShoppingCart  , Truck , ClipboardList , ShieldAlert} from "lucide-react";
// <ShieldAlert />

export const NAV_LINKS = [
  { label: "Accueil", href: "#accueil" },
  { label: "Fonctionnalités", href: "#fonctionnalites" },
  { label: "Tableau de bord", href: "#dashboard" },
  { label: "FAQ", href: "#faq" },
];

export const FEATURES = [
  {
    icon: Users,
    title: "Dossiers Patients",
    desc: "Historique complet des corrections, ordonnances et examens de vue centralisés.",
  },
  {
    icon: Package,
    title: "Gestion de Stock",
    desc: "Suivi des montures, verres et lentilles avec alertes de réapprovisionnement.",
  },
  {
    icon: FileText,
    title: "Devis & Facturation",
    desc: "Devis professionnels, TVA marocaine et calculs automatiques en un clic.",
  },
  {
    icon: ShieldCheck,
    title: "Mutuelles & AMO",
    desc: "Part patient, part mutuelle et suivi des remboursements CNOPS sans erreur.",
  },
];

export const FAQS = [
  {
    q: "Mes données sont-elles sécurisées ?",
    a: "Oui, chaque compte est protégé par une authentification par rôle (Admin / Vendeur) et vos données sont sauvegardées automatiquement.",
  },
  {
    q: "Puis-je consulter l'historique d'un client rapidement ?",
    a: "Chaque fiche client centralise ordonnances, examens et achats précédents pour un accès immédiat en magasin.",
  },
  {
    q: "L'application gère-t-elle la TVA marocaine ?",
    a: "Oui, le calcul des prix et de la TVA suit la réglementation marocaine selon la catégorie du produit.",
  },
  {
    q: "Puis-je suivre mon chiffre d'affaires en temps réel ?",
    a: "Le tableau de bord affiche vos ventes journalières et mensuelles ainsi que vos produits les plus vendus.",
  },
];

export const NAV_SECTIONS = {
  admin: [
    {
      label: "Aperçu",
      links: [{ label: "Tableau de bord", icon: LayoutDashboard, path: "/admin-dashboard/dash" }],
    },
    {
      label: "Achats",
      links: [
        { label: "Fournisseurs", icon: Truck, path: "/admin-dashboard/fournisseurs" },
        { label: "Commandes", icon: ClipboardList, path: "/admin-dashboard/commandes" },
      ],
    },
    {
      label: "Équipe",
      links: [{ label: "Responsables", icon: ShieldCheck, path: "/admin-dashboard/responsables" }],
    },
    
  ],
  responsable: [
    {
      label: "Aperçu",
      links: [{ label: "Tableau de bord", icon: LayoutDashboard, path: "/responsable-dashboard/dash" }],
    },
    {
      label: "Gestion",
      links: [
        { label: "Clients", icon: Users, path: "/responsable-dashboard/clients" },
        { label: "Examens", icon: ReceiptText, path: "/responsable-dashboard/examens" },
        { label: "Ordonances", icon: SquarePen, path: "/responsable-dashboard/ordonnance" },
        { label: "Produits", icon: ShoppingCart, path: "/responsable-dashboard/produits" },
        { label: "Ventes", icon: Receipt, path: "/responsable-dashboard/ventes" },
        { label: "Devis", icon: FileText, path: "/responsable-dashboard/devis" },
        { label: "Mouvements", icon: Package, path: "/responsable-dashboard/mouvements-stock" },
      ],
    },
    {
      label: "Suivi",
      links: [{ label: "Notications", icon: ShieldAlert, path: "/responsable-dashboard/notifications" }],
    },
  ],
};


// Notif
export  const typeMeta = {
    stock_bas: {
        label: "Stock bas",
        tone: "amber",
        icon: ShieldAlert,
    },
    stock_epuise: {
        label: "Stock épuisé",
        tone: "rose",
        icon: Package,
    },
};

 export  const toneStyles = {
    amber: {
        badge: "bg-amber-50 text-amber-700 ring-1 ring-amber-200",
        dot: "bg-amber-500",
    },
    rose: {
        badge: "bg-rose-50 text-rose-700 ring-1 ring-rose-200",
        dot: "bg-rose-500",
    },
    slate: {
        badge: "bg-slate-100 text-slate-700 ring-1 ring-slate-200",
        dot: "bg-slate-400",
    },
};