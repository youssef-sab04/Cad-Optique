const MONTHS = [
    "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
    "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre",
];

const MonthSelector = ({ selectedMonth, onSelect }) => (
    <section className="grid grid-cols-2 gap-3 sm:grid-cols-3 lg:grid-cols-6">
        {MONTHS.map((label, index) => {
            const monthNumber = index + 1;
            const isActive = selectedMonth === monthNumber;

            return (
                <button
                    key={label}
                    type="button"
                    onClick={() => onSelect(isActive ? 0 : monthNumber)}
                    className={`rounded-2xl border p-4 text-left shadow-sm transition ${
                        isActive
                            ? "border-slate-900 bg-slate-900 text-white"
                            : "border-slate-200 bg-white text-slate-900 hover:border-slate-300"
                    }`}
                >
                    <p className={`text-[11px] font-semibold uppercase tracking-wide ${isActive ? "text-slate-300" : "text-slate-400"}`}>
                        Période
                    </p>
                    <p className="mt-1 text-sm font-bold">{label}</p>
                </button>
            );
        })}
    </section>
);

export default MonthSelector;