import { CalendarDays, Download, RefreshCcw } from "lucide-react";

const DashboardHeader = ({ year, annee, onYearChange, onReset, onDownloadPdf }) => (
    <section className="overflow-hidden rounded-[28px] border border-slate-200 bg-white shadow-[0_24px_80px_rgba(15,23,42,0.08)]">
        <div className="relative isolate overflow-hidden bg-[radial-gradient(circle_at_top_right,rgba(59,130,246,0.22),transparent_35%),radial-gradient(circle_at_bottom_left,rgba(14,165,233,0.14),transparent_30%),linear-gradient(135deg,#0f172a_0%,#111827_42%,#1e293b_100%)] px-6 py-7 text-white sm:px-8 sm:py-8">
            <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.03)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.03)_1px,transparent_1px)] bg-size-[32px_32px] opacity-35" />
            <div className="relative flex flex-col gap-6 lg:flex-row lg:items-end lg:justify-between">
                <div className="max-w-3xl space-y-4">
                    <span className="inline-flex items-center gap-2 rounded-full border border-white/15 bg-white/10 px-3 py-1 text-xs font-semibold uppercase tracking-[0.24em] text-sky-100 backdrop-blur">
                        <CalendarDays className="h-3.5 w-3.5" />
                        Tableau de bord annuel
                    </span>
                    <div>
                        <h1 className="font-display text-3xl font-bold tracking-tight sm:text-5xl">
                            Vue claire de l'activité, des ventes et du stock
                        </h1>
                        <p className="mt-3 max-w-2xl text-sm leading-6 text-slate-200 sm:text-base">
                            Suivez le chiffre d'affaires, le taux de conversion, les clients actifs et les produits les plus vendus pour l'année {annee ?? year}.
                        </p>
                    </div>
                </div>

                <div className="flex flex-col gap-3 rounded-3xl border border-white/10 bg-white/10 p-4 backdrop-blur-xl sm:min-w-70">
                    <label className="text-xs font-semibold uppercase tracking-[0.2em] text-slate-200">
                        Année analysée
                    </label>
                    <div className="flex items-center gap-2">
                        <input
                            type="number"
                            min="2000"
                            max="2100"
                            value={year}
                            onChange={(event) => onYearChange(event.target.value)}
                            className="h-12 w-full rounded-2xl border border-white/10 bg-slate-950/40 px-4 text-lg font-semibold text-white outline-none ring-0 placeholder:text-slate-400 focus:border-sky-400"
                        />
                        <button
                            type="button"
                            onClick={onReset}
                            className="inline-flex h-12 items-center justify-center rounded-2xl border border-white/10 bg-white/10 px-4 text-sm font-semibold text-white transition hover:bg-white/20"
                        >
                            <RefreshCcw className="h-4 w-4" />
                        </button>
                        <button
                            type="button"
                            onClick={onDownloadPdf}
                            className="inline-flex h-12 items-center justify-center rounded-2xl border border-white/10 bg-white/10 px-4 text-sm font-semibold text-white transition hover:bg-white/20"
                            title="Télécharger le rapport PDF"
                        >
                            <Download className="h-4 w-4" />
                        </button>
                    </div>
                    <p className="text-sm text-slate-200/90">Les données sont rechargées automatiquement à chaque changement d'année.</p>
                </div>
            </div>
        </div>
    </section>
);

export default DashboardHeader;