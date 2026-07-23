import { CartesianGrid, Legend, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";
import { chartTooltipStyle, formatNumber, formatPercent } from "./format";

const ConversionChart = ({ data }) => (
    <article className="rounded-[28px] border border-slate-200 bg-white p-5 shadow-sm">
        <div className="mb-5 flex items-start justify-between gap-4">
            <div>
                <h2 className="text-lg font-bold text-slate-900">Conversion mensuelle</h2>
                <p className="mt-1 text-sm text-slate-500">Compare le nombre de devis et le taux de conversion par mois.</p>
            </div>
            <span className="rounded-full bg-emerald-50 px-3 py-1 text-xs font-semibold text-emerald-700 ring-1 ring-emerald-200">
                Devis / conversion
            </span>
        </div>

        <div className="h-80 w-full">
            <ResponsiveContainer width="100%" height="100%">
                <LineChart data={data}>
                    <CartesianGrid strokeDasharray="3 3" stroke="#e2e8f0" vertical={false} />
                    <XAxis dataKey="mois" tick={{ fill: "#64748b", fontSize: 12 }} axisLine={false} tickLine={false} />
                    <YAxis tick={{ fill: "#64748b", fontSize: 12 }} axisLine={false} tickLine={false} />
                    <Tooltip
                        contentStyle={chartTooltipStyle}
                        formatter={(value, name) => {
                            if (name === "tauxConversion") return [formatPercent(value), "Conversion"];
                            return [formatNumber(value), name === "nombreDevis" ? "Devis" : name];
                        }}
                    />
                    <Legend />
                    <Line type="monotone" dataKey="nombreDevis" name="Devis" stroke="#8b5cf6" strokeWidth={3} dot={{ r: 4 }} />
                    <Line type="monotone" dataKey="tauxConversion" name="Conversion %" stroke="#14b8a6" strokeWidth={3} dot={{ r: 4 }} />
                </LineChart>
            </ResponsiveContainer>
        </div>
    </article>
);

export default ConversionChart;