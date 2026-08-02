const KpiCard = ({ label, value, helper, icon: Icon, tone }) => (
    <article className="group overflow-hidden rounded-3xl border border-slate-200 bg-white p-5 shadow-sm transition hover:-translate-y-1 hover:shadow-xl">
        <div className="flex items-start justify-between gap-3">
            <div className="min-w-0">
                <p className="text-sm font-medium text-slate-500">{label}</p>
                <h2 className="mt-2 break-words text-2xl font-bold tracking-tight text-slate-900">{value}</h2>
            </div>
            <div className={`shrink-0 rounded-2xl bg-linear-to-br ${tone} p-3 text-white shadow-lg shadow-slate-900/10`}>
                <Icon className="h-5 w-5" />
            </div>
        </div>
        <div className="mt-4 h-1.5 overflow-hidden rounded-full bg-slate-100">
            <div className={`h-full w-full rounded-full bg-linear-to-r ${tone}`} />
        </div>
        <p className="mt-3 text-sm text-slate-600">{helper}</p>
    </article>
);

export default KpiCard;