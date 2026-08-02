import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from "@headlessui/react";
import { FaTimes } from "react-icons/fa";
import { FileText, Eye, Glasses, ChevronRight } from "lucide-react";

const docOptions = [
    {
        key: "examen",
        label: "Examen de vue",
        description: "Sphère, cylindre, axe, addition...",
        icon: Eye,
    },
    {
        key: "ordonnance-lunette",
        label: "Ordonnance lunette",
        description: "Prescription verres correcteurs",
        icon: Glasses,
    },
    {
        key: "ordonnance-lentille",
        label: "Ordonnance lentille",
        description: "Prescription lentilles de contact",
        icon: FileText,
    },
];

const DocModal = ({ open, setOpen , setTypeDoc  , setOpenLU , setOpenLE , setOpenE  }) => {
    //setTypeDoc

    const handleDoc = (key) => {
        console.log("Methode" , key)
        setOpen(false)
        if(key == "examen") setOpenE(true)
        else if (key == "ordonnance-lunette")  setOpenLU(true)
        else if (key == "ordonnance-lentille")  setOpenLE(true)

    }
    return (
        <Dialog open={open} onClose={() => setOpen(false)} className="relative z-50">
            <DialogBackdrop className="fixed inset-0 bg-black/40" />
            <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4">
                    <DialogPanel className="relative bg-white rounded-xl shadow-xl w-full max-w-md overflow-hidden">
                        <div className="bg-slate-900 px-6 py-5 flex items-center justify-between">
                            <div className="flex items-center gap-3">
                                <div className="bg-white/10 p-2 rounded-lg">
                                    <FileText size={18} className="text-white" />
                                </div>
                                <DialogTitle className="text-base font-bold text-white">
                                    Ajouter un document
                                </DialogTitle>
                            </div>
                            <button
                                onClick={() => setOpen(false)}
                                className="text-slate-400 hover:text-white transition-colors"
                            >
                                <FaTimes size={16} />
                            </button>
                        </div>

                        <div className="px-4 py-4 flex flex-col gap-2">
                            {docOptions.map(({ key, label, description, icon: Icon }) => (
                                <button
                                    key={key}
                                   onClick={() => handleDoc(key)}
                                    className="group flex items-center gap-4 w-full text-left px-4 py-3 rounded-lg border border-slate-100 hover:border-blue-200 hover:bg-blue-50 transition-colors"
                                >
                                    <div className="bg-slate-100 group-hover:bg-blue-100 p-2.5 rounded-lg transition-colors">
                                        <Icon size={20} className="text-slate-600 group-hover:text-blue-600" />
                                    </div>
                                    <div className="flex-1">
                                        <p className="text-sm font-semibold text-slate-800">{label}</p>
                                        <p className="text-xs text-slate-500">{description}</p>
                                    </div>
                                    <ChevronRight size={16} className="text-slate-300 group-hover:text-blue-500" />
                                </button>
                            ))}
                        </div>
                    </DialogPanel>
                </div>
            </div>
        </Dialog>
    );
};

export default DocModal;