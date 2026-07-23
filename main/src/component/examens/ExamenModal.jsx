import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useDispatch } from "react-redux";
import toast from "react-hot-toast";
import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from "@headlessui/react";
import { FaTimes } from "react-icons/fa";
import { CalendarDays, Eye, EyeOff, Sparkles } from "lucide-react";
import InputField from "../shared/InputField";
import { updateExamen , addExamen } from "../../store/reducers/actions";

const SectionTitle = ({ icon: Icon, children, subtitle }) => (
    <div className="col-span-2 flex items-start gap-3 rounded-2xl border border-white/70 bg-white/80 px-4 py-3 shadow-[0_8px_30px_rgba(15,23,42,0.06)] backdrop-blur-sm">
        <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-slate-900 text-white shadow-lg shadow-slate-900/20">
            <Icon size={16} />
        </div>
        <div>
            <span className="block text-[11px] font-semibold uppercase tracking-[0.24em] text-slate-400">
                {children}
            </span>
            {subtitle && <p className="mt-1 text-sm text-slate-500">{subtitle}</p>}
        </div>
    </div>
);


const ExamenModal = ({ open, setOpen, examen, examenId , clienId }) => {
    const dispatch = useDispatch();
    const [btnLoader, setBtnLoader] = useState(false);

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors },
    } = useForm({ mode: "onTouched" });

    useEffect(() => {
        if (examen) {
            reset(examen);
        } else if(clienId) {
            reset({
                dateExamen: "",
                sphereOd: "",
                cylindreOd: "",
                axeOd: "",
                ecartOd: "",
                sphereOg: "",
                cylindreOg: "",
                axeOg: "",
                ecartOg: "",
                addition: "",
                remarques: "",
                prochaineVisite: "",
            });
            console.log("idclie" , clienId)
        }
        
    }, [examen, open , clienId]);

    const onSubmit = (data) => {
        if (examen) {
           dispatch(updateExamen(examen.id, data, toast, reset, setOpen, setBtnLoader));
        } else if(clienId) {
           dispatch(addExamen(clienId, data, toast, reset, setOpen, setBtnLoader));
        }
    };

    return (
        <Dialog open={open} onClose={() => setOpen(false)} className="relative z-50">
            <DialogBackdrop className="fixed inset-0 bg-slate-950/70 backdrop-blur-[6px]" />
            <div className="fixed inset-0 z-10 w-screen overflow-y-auto bg-[radial-gradient(circle_at_top,rgba(59,130,246,0.14),transparent_32%),radial-gradient(circle_at_bottom_right,rgba(15,23,42,0.08),transparent_28%)]">
                <div className="flex min-h-full items-center justify-center p-4 sm:p-6">
                    <DialogPanel className="relative w-full max-w-6xl overflow-hidden rounded-[28px] border border-white/60 bg-white shadow-[0_30px_100px_rgba(15,23,42,0.28)]">
                        <button
                            onClick={() => setOpen(false)}
                            className="absolute right-4 top-4 z-20 flex h-10 w-10 items-center justify-center rounded-full bg-slate-900/70 text-white shadow-lg shadow-slate-900/20 transition hover:bg-slate-900"
                        >
                            <FaTimes size={14} />
                        </button>

                        <div className="grid gap-0 lg:grid-cols-[0.95fr_1.15fr]">
                            <div className="relative overflow-hidden bg-slate-950 text-white">
                                <div className="absolute inset-0 bg-[radial-gradient(circle_at_top,rgba(56,189,248,0.32),transparent_34%),linear-gradient(135deg,#020617_0%,#0f172a_55%,#1e293b_100%)]" />
                                <div className="relative flex h-full min-h-80 flex-col justify-between p-6 sm:p-8">
                                    <div>
                                        <div className="inline-flex items-center gap-2 rounded-full border border-white/15 bg-white/10 px-3 py-1 text-[11px] font-semibold uppercase tracking-[0.24em] text-white/80 backdrop-blur">
                                            <Sparkles size={12} />
                                            Bilan visuel
                                        </div>
                                        <DialogTitle className="mt-4 max-w-md text-3xl font-black leading-tight sm:text-4xl">
                                            {examen ? "Modifier l'examen" : "Ajouter un examen"}
                                        </DialogTitle>
                                     
                                    </div>

                                    <div className="grid gap-3 sm:grid-cols-2">
                                        <div className="rounded-2xl border border-white/10 bg-white/10 p-4 backdrop-blur-sm sm:col-span-2">
                                            <p className="text-[11px] uppercase tracking-[0.2em] text-white/50">Client</p>
                                            <p className="mt-2 text-sm font-semibold text-white">
                                                {examen?.clientDTO
                                                    ? `${examen.clientDTO?.nom || ""} ${examen.clientDTO?.prenom || ""}`.trim() || "Client lié"
                                                    : clienId
                                                        ? `Client sélectionné #${clienId}`
                                                        : "Client à renseigner"}
                                            </p>
                                        </div>
                                        <div className="rounded-2xl border border-white/10 bg-white/10 p-4 backdrop-blur-sm">
                                            <p className="text-[11px] uppercase tracking-[0.2em] text-white/50">Date</p>
                                            <p className="mt-2 text-sm font-semibold text-white">{examen?.dateExamen || "À sélectionner"}</p>
                                        </div>
                                        <div className="rounded-2xl border border-white/10 bg-white/10 p-4 backdrop-blur-sm">
                                            <p className="text-[11px] uppercase tracking-[0.2em] text-white/50">Suivi</p>
                                            <p className="mt-2 text-sm font-semibold text-white">Prochaine visite et remarques</p>
                                        </div>
                                        <div className="rounded-2xl border border-white/10 bg-white/10 p-4 backdrop-blur-sm sm:col-span-2">
                                            <p className="text-[11px] uppercase tracking-[0.2em] text-white/50">Mesures</p>
                                            <p className="mt-2 text-sm font-semibold text-white">Œil droit, œil gauche et addition</p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <form onSubmit={handleSubmit(onSubmit)} className="max-h-[90vh] overflow-y-auto bg-slate-50/70 p-5 sm:p-6 lg:p-8">
                                <div className="grid grid-cols-2 gap-4">
                                    <SectionTitle icon={CalendarDays} subtitle="Sélection de la date d'examen">
                                        Date et suivi
                                    </SectionTitle>

                                    <div className="col-span-2 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
                                        <InputField
                                            label="Date de l'examen"
                                            id="dateExamen"
                                            type="date"
                                            register={register}
                                            errors={errors}
                                            required
                                            message="La date est requise"
                                        />
                                    </div>

                                    <SectionTitle icon={Eye} subtitle="Mesures pour l'œil droit">
                                        Œil droit (OD)
                                    </SectionTitle>

                                    <div className="col-span-2 grid gap-4 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm sm:grid-cols-2">
                                        <InputField label="Sphère" id="sphereOd" type="number" step="0.25" register={register} errors={errors} />
                                        <InputField label="Cylindre" id="cylindreOd" type="number" step="0.25" register={register} errors={errors} />
                                        <InputField label="Axe" id="axeOd" type="number" register={register} errors={errors} />
                                        <InputField label="Écart" id="ecartOd" type="number" step="0.25" register={register} errors={errors} />
                                    </div>

                                    <SectionTitle icon={EyeOff} subtitle="Mesures pour l'œil gauche">
                                        Œil gauche (OG)
                                    </SectionTitle>

                                    <div className="col-span-2 grid gap-4 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm sm:grid-cols-2">
                                        <InputField label="Sphère" id="sphereOg" type="number" step="0.25" register={register} errors={errors} />
                                        <InputField label="Cylindre" id="cylindreOg" type="number" step="0.25" register={register} errors={errors} />
                                        <InputField label="Axe" id="axeOg" type="number" register={register} errors={errors} />
                                        <InputField label="Écart" id="ecartOg" type="number" step="0.25" register={register} errors={errors} />
                                    </div>

                                    <SectionTitle icon={Sparkles} subtitle="Compléments et remarques">
                                        Informations complémentaires
                                    </SectionTitle>

                                    <div className="col-span-2 grid gap-4 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
                                        <InputField label="Addition" id="addition" type="number" step="0.25" register={register} errors={errors} />
                                        <InputField label="Remarques" id="remarques" type="text" register={register} errors={errors} />
                                        <InputField label="Prochaine visite" id="prochaineVisite" type="date" register={register} errors={errors} />
                                    </div>

                                    <div className="col-span-2 grid gap-3 sm:grid-cols-[1fr_auto]">
                                        <div className="hidden items-center gap-2 rounded-2xl border border-slate-200 bg-white px-4 py-3 text-sm text-slate-500 shadow-sm sm:flex">
                                            <CalendarDays size={15} className="text-slate-400" />
                                            Les valeurs optiques sont conservées telles quelles.
                                        </div>
                                        <button
                                            type="submit"
                                            disabled={btnLoader}
                                            className="inline-flex items-center justify-center rounded-2xl bg-slate-900 px-6 py-3.5 font-semibold text-white shadow-lg shadow-slate-900/20 transition hover:bg-slate-800 disabled:cursor-not-allowed disabled:opacity-70"
                                        >
                                            {btnLoader ? "Enregistrement..." : "Enregistrer"}
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </DialogPanel>
                </div>
            </div>
        </Dialog>
    );
};

export default ExamenModal;