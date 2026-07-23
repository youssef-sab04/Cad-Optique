import { useEffect, useRef, useState } from "react";
import { useForm } from "react-hook-form";
import { useDispatch } from "react-redux";
import toast from "react-hot-toast";
import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from "@headlessui/react";
import { FaTimes } from "react-icons/fa";
import { CalendarDays, Contact, Eye, EyeOff, Sparkles, Camera } from "lucide-react";
import InputField from "../shared/InputField";
import { updateOrdLun, addOrdLunAvecImage, previewScanOrdLun } from "../../store/reducers/actions";

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

const OrdonnanceLunetteModal = ({ open, setOpen, ordonnance, clienId }) => {
    const dispatch = useDispatch();
    const [btnLoader, setBtnLoader] = useState(false);
    const [scanFile, setScanFile] = useState(null);
    const [scanLoading, setScanLoading] = useState(false);
    const [camOpen, setCamOpen] = useState(false);
    const [captured, setCaptured] = useState(false);
    const [previewUrl, setPreviewUrl] = useState(null);
    const [cameraReady, setCameraReady] = useState(false);
    const videoRef = useRef(null);
    const canvasRef = useRef(null);
    const streamRef = useRef(null);

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors },
    } = useForm({ mode: "onTouched" });

    useEffect(() => {
        if (ordonnance) {
            reset(ordonnance);
        } else if (clienId) {
            reset({
                prescripteur: "",
                dateEmission: "",
                dateExpiration: "",
                sphereOd: "",
                cylindreOd: "",
                axeOd: "",
                additionOd: "",
                sphereOg: "",
                cylindreOg: "",
                axeOg: "",
                additionOg: "",
            });
        }
    }, [ordonnance, open, clienId, reset]);

    const handleScanImageChange = (e) => {
        const file = e.target.files?.[0];
        if (file) {
            setScanFile(file);
            setPreviewUrl(URL.createObjectURL(file));
            dispatch(previewScanOrdLun(file, toast, (dto) => reset(dto), setScanLoading));
        }
    };

    const openCamera = async () => {
        try {
            const stream = await navigator.mediaDevices.getUserMedia({ video: true });
            streamRef.current = stream;
            setCameraReady(false);
            setCaptured(false);
            setCamOpen(true);
        } catch {
            toast.error("Impossible d'accéder à la caméra");
        }
    };

    useEffect(() => {
        if (!camOpen || captured || !videoRef.current || !streamRef.current) return;
        const video = videoRef.current;
        video.srcObject = streamRef.current;

        const markReady = () => setCameraReady(true);
        video.addEventListener("loadedmetadata", markReady);
        video.addEventListener("loadeddata", markReady);
        video.addEventListener("canplay", markReady);
        video.play().catch(() => {});

        // Filet de sécurité : si aucun des évènements ci-dessus ne se déclenche
        // (arrive sur certains navigateurs), on vérifie nous-mêmes les dimensions.
        const poll = setInterval(() => {
            if (video.videoWidth > 0) {
                setCameraReady(true);
                clearInterval(poll);
            }
        }, 200);

        return () => {
            clearInterval(poll);
            video.removeEventListener("loadedmetadata", markReady);
            video.removeEventListener("loadeddata", markReady);
            video.removeEventListener("canplay", markReady);
        };
    }, [camOpen, captured]);

    const closeCamera = () => {
        streamRef.current?.getTracks().forEach((t) => t.stop());
        streamRef.current = null;
        setCamOpen(false);
        setCaptured(false);
        setCameraReady(false);
    };

    const capturePhoto = () => {
        const video = videoRef.current;
        const canvas = canvasRef.current;
        if (!video.videoWidth || !video.videoHeight) {
            toast.error("Caméra pas encore prête, réessayez dans une seconde");
            return;
        }
        canvas.width = video.videoWidth;
        canvas.height = video.videoHeight;
        canvas.getContext("2d").drawImage(video, 0, 0);
        setPreviewUrl(canvas.toDataURL("image/jpeg"));
        setCaptured(true);
    };

    const retakePhoto = () => {
        setCaptured(false);
    };

    const validatePhoto = () => {
        canvasRef.current.toBlob((blob) => {
            const file = new File([blob], "scan-webcam.jpg", { type: "image/jpeg" });
            setScanFile(file);
            setPreviewUrl(URL.createObjectURL(file));
            dispatch(previewScanOrdLun(file, toast, (dto) => reset(dto), setScanLoading));
            closeCamera();
        }, "image/jpeg");
    };

    const onSubmit = (data) => {
        if (ordonnance) {
            dispatch(updateOrdLun(ordonnance.id, data, toast, reset, setOpen, setBtnLoader));
        } else if (clienId) {
            dispatch(addOrdLunAvecImage(clienId, data, scanFile, toast, reset, setOpen, setBtnLoader));
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
                                <div className="absolute inset-0 bg-[radial-gradient(circle_at_top,rgba(59,130,246,0.34),transparent_34%),linear-gradient(135deg,#020617_0%,#0f172a_55%,#1e293b_100%)]" />
                                <div className="relative flex h-full min-h-80 flex-col justify-between p-6 sm:p-8">
                                    <div>
                                        <div className="inline-flex items-center gap-2 rounded-full border border-white/15 bg-white/10 px-3 py-1 text-[11px] font-semibold uppercase tracking-[0.24em] text-white/80 backdrop-blur">
                                            <Sparkles size={12} />
                                            Ordonnance lunette
                                        </div>
                                        <DialogTitle className="mt-4 max-w-md text-3xl font-black leading-tight sm:text-4xl">
                                            {ordonnance ? "Modifier l'ordonnance" : "Ajouter une ordonnance"}
                                        </DialogTitle>
                                    </div>

                                    <div className="grid gap-3 sm:grid-cols-2">
                                        <div className="rounded-2xl border border-white/10 bg-white/10 p-4 backdrop-blur-sm sm:col-span-2">
                                            <p className="text-[11px] uppercase tracking-[0.2em] text-white/50">Client</p>
                                            <p className="mt-2 text-sm font-semibold text-white">
                                                {ordonnance?.clientDTO
                                                    ? `${ordonnance.clientDTO?.nom || ""} ${ordonnance.clientDTO?.prenom || ""}`.trim()
                                                    : clienId
                                                        ? "Client sélectionné"
                                                        : "Client non lié"}
                                            </p>
                                        </div>
                                        <div className="rounded-2xl border border-white/10 bg-white/10 p-4 backdrop-blur-sm">
                                            <p className="text-[11px] uppercase tracking-[0.2em] text-white/50">Prescripteur</p>
                                            <p className="mt-2 text-sm font-semibold text-white">{ordonnance?.prescripteur || "À renseigner"}</p>
                                        </div>
                                        <div className="rounded-2xl border border-white/10 bg-white/10 p-4 backdrop-blur-sm">
                                            <p className="text-[11px] uppercase tracking-[0.2em] text-white/50">Validité</p>
                                            <p className="mt-2 text-sm font-semibold text-white">{ordonnance?.dateExpiration || "Date d'expiration"}</p>
                                        </div>
                                        <div className="rounded-2xl border border-white/10 bg-white/10 p-4 backdrop-blur-sm sm:col-span-2">
                                            <p className="text-[11px] uppercase tracking-[0.2em] text-white/50">Lunette</p>
                                            <p className="mt-2 text-sm font-semibold text-white">OD / OG, sphère, cylindre, axe et addition</p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <form onSubmit={handleSubmit(onSubmit)} className="max-h-[90vh] overflow-y-auto bg-slate-50/70 p-5 sm:p-6 lg:p-8">
                                <div className="grid grid-cols-2 gap-4">
                                    <SectionTitle icon={Contact} subtitle="Identité de la prescription">
                                        Informations générales
                                    </SectionTitle>

                                    <div className="col-span-2 grid gap-4 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm sm:grid-cols-3">
                                        <InputField label="Prescripteur" id="prescripteur" type="text" register={register} errors={errors} />
                                        <InputField label="Date émission" id="dateEmission" type="date" register={register} errors={errors} />
                                        <InputField label="Date expiration" id="dateExpiration" type="date" register={register} errors={errors} />
                                    </div>

                                    <SectionTitle icon={Eye} subtitle="Paramètres de l'œil droit">
                                        Œil droit (OD)
                                    </SectionTitle>

                                    <div className="col-span-2 grid gap-4 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm sm:grid-cols-2">
                                        <InputField label="Sphère" id="sphereOd" type="number"  register={register} errors={errors} />
                                        <InputField label="Cylindre" id="cylindreOd" type="number" register={register} errors={errors} />
                                        <InputField label="Axe" id="axeOd" type="number" register={register} errors={errors} />
                                        <InputField label="Addition" id="additionOd" type="number" register={register} errors={errors} />
                                    </div>

                                    <SectionTitle icon={EyeOff} subtitle="Paramètres de l'œil gauche">
                                        Œil gauche (OG)
                                    </SectionTitle>

                                    <div className="col-span-2 grid gap-4 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm sm:grid-cols-2">
                                        <InputField label="Sphère" id="sphereOg" type="number"  register={register} errors={errors} />
                                        <InputField label="Cylindre" id="cylindreOg" type="number"  register={register} errors={errors} />
                                        <InputField label="Axe" id="axeOg" type="number" register={register} errors={errors} />
                                        <InputField label="Addition" id="additionOg" type="number"  register={register} errors={errors} />
                                    </div>

                                    <div className="col-span-2 grid gap-3 sm:grid-cols-[1fr_auto_auto]">
                                        <div className="hidden items-center gap-2 rounded-2xl border border-slate-200 bg-white px-4 py-3 text-sm text-slate-500 shadow-sm sm:flex">
                                            <CalendarDays size={15} className="text-slate-400" />
                                            La structure du formulaire reste identique, seule la présentation change.
                                        </div>

                                        <label className="inline-flex cursor-pointer items-center justify-center gap-2 rounded-2xl border border-slate-300 bg-white px-5 py-3.5 font-semibold text-slate-700 shadow-sm transition hover:bg-slate-100">
                                            <Camera size={16} />
                                            Ajouter avec scan
                                            <input
                                                type="file"
                                                accept="image/*"
                                                capture="environment"
                                                onChange={handleScanImageChange}
                                                className="hidden"
                                            />
                                        </label>

                                        <button
                                            type="button"
                                            onClick={openCamera}
                                            className="inline-flex items-center justify-center gap-2 rounded-2xl border border-slate-300 bg-white px-5 py-3.5 font-semibold text-slate-700 shadow-sm transition hover:bg-slate-100"
                                        >
                                            <Camera size={16} />
                                            Scanner via webcam
                                        </button>

                                        {camOpen && (
                                            <div className="fixed inset-0 z-[60] flex items-center justify-center bg-black/80 p-4">
                                                <div className="w-full max-w-md rounded-2xl bg-white p-4">
                                                    {!captured ? (
                                                        <video ref={videoRef} autoPlay playsInline muted className="w-full rounded-xl" />
                                                    ) : (
                                                        <img src={previewUrl} alt="Photo capturée" className="w-full rounded-xl" />
                                                    )}
                                                    <canvas ref={canvasRef} className="hidden" />
                                                    <div className="mt-3 flex gap-2">
                                                        {!captured ? (
                                                            <>
                                                                <button
                                                                    type="button"
                                                                    onClick={capturePhoto}
                                                                    disabled={!cameraReady}
                                                                    className="flex-1 rounded-xl bg-slate-900 px-4 py-2.5 font-semibold text-white disabled:cursor-not-allowed disabled:opacity-50"
                                                                >
                                                                    {cameraReady ? "Capturer" : "Chargement de la caméra..."}
                                                                </button>
                                                                <button
                                                                    type="button"
                                                                    onClick={closeCamera}
                                                                    className="flex-1 rounded-xl border border-slate-300 px-4 py-2.5 font-semibold text-slate-700"
                                                                >
                                                                    Annuler
                                                                </button>
                                                            </>
                                                        ) : (
                                                            <>
                                                                <button
                                                                    type="button"
                                                                    onClick={validatePhoto}
                                                                    className="flex-1 rounded-xl bg-slate-900 px-4 py-2.5 font-semibold text-white"
                                                                >
                                                                    Valider
                                                                </button>
                                                                <button
                                                                    type="button"
                                                                    onClick={retakePhoto}
                                                                    className="flex-1 rounded-xl border border-slate-300 px-4 py-2.5 font-semibold text-slate-700"
                                                                >
                                                                    Reprendre
                                                                </button>
                                                            </>
                                                        )}
                                                    </div>
                                                </div>
                                            </div>
                                        )}

                                        <button
                                            type="submit"
                                            disabled={btnLoader || scanLoading}
                                            className="inline-flex items-center justify-center rounded-2xl bg-slate-900 px-6 py-3.5 font-semibold text-white shadow-lg shadow-slate-900/20 transition hover:bg-slate-800 disabled:cursor-not-allowed disabled:opacity-70"
                                        >
                                            {scanLoading ? "Extraction en cours..." : btnLoader ? "Enregistrement..." : "Enregistrer"}
                                        </button>
                                    </div>

                                    {scanLoading && (
                                        <p className="col-span-2 mt-1 text-xs text-blue-600">
                                            Extraction des données en cours...
                                        </p>
                                    )}

                                    {scanFile && !scanLoading && (
                                        <div className="col-span-2 mt-1 flex items-center gap-3">
                                            {previewUrl && (
                                                <img
                                                    src={previewUrl}
                                                    alt="Ordonnance scannée"
                                                    className="h-16 w-16 rounded-lg border border-slate-200 object-cover"
                                                />
                                            )}
                                            <p className="text-xs text-slate-500">
                                                Image scannée : <span className="font-medium">{scanFile.name}</span> — vérifiez les champs préremplis avant d'enregistrer.
                                            </p>
                                        </div>
                                    )}
                                </div>
                            </form>
                        </div>
                    </DialogPanel>
                </div>
            </div>
        </Dialog>
    );
};

export default OrdonnanceLunetteModal;