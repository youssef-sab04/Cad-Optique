import {
    Area, Bar, CartesianGrid, ComposedChart, Legend,
    ResponsiveContainer, Tooltip, XAxis, YAxis,
} from "recharts";
import { chartTooltipStyle, formatMoney, formatNumber } from "./format";

const RevenueVentesChart = ({ data }) => (
    <article className="xl:col-span-2 rounded-[28px] border border-slate-200 bg-white p-5 shadow-sm">
        <div className="mb-5 flex items-start justify-between gap-4">
            <div>
                <h2 className="text-lg font-bold text-slate-900">Chiffre d'affaires et ventes mensuelles</h2>
                <p className="mt-1 text-sm text-slate-500">Lecture rapide de la tendance mensuelle sur l'année.</p>
            </div>
            <span className="rounded-full bg-sky-50 px-3 py-1 text-xs font-semibold text-sky-700 ring-1 ring-sky-200">
                CA / Ventes
            </span>
        </div>

        <div className="h-90 w-full">
            <ResponsiveContainer width="100%" height="100%">
                <ComposedChart data={data}>
                    <CartesianGrid strokeDasharray="3 3" stroke="#e2e8f0" vertical={false} />
                    <XAxis dataKey="mois" tick={{ fill: "#64748b", fontSize: 12 }} axisLine={false} tickLine={false} />
                    <YAxis yAxisId="left" tick={{ fill: "#64748b", fontSize: 12 }} axisLine={false} tickLine={false} />
                    <YAxis yAxisId="right" orientation="right" tick={{ fill: "#64748b", fontSize: 12 }} axisLine={false} tickLine={false} />
                    <Tooltip
                        contentStyle={chartTooltipStyle}
                        formatter={(value, name) => {
                            if (name === "ca") return [formatMoney(value), "CA"];
                            if (name === "nombreVentes") return [formatNumber(value), "Ventes"];
                            return [formatNumber(value), name];
                        }}
                    />
                    <Legend />
                    <defs>
                        <linearGradient id="caGradient" x1="0" y1="0" x2="0" y2="1">
                            <stop offset="5%" stopColor="#0ea5e9" stopOpacity={0.35} />
                            <stop offset="95%" stopColor="#0ea5e9" stopOpacity={0.02} />
                        </linearGradient>
                    </defs>
                    <Area yAxisId="left" type="monotone" dataKey="ca" name="CA" stroke="#0ea5e9" fill="url(#caGradient)" strokeWidth={3} />
                    <Bar yAxisId="right" dataKey="nombreVentes" name="Ventes" radius={[10, 10, 0, 0]} fill="#22c55e" />
                </ComposedChart>
            </ResponsiveContainer>
        </div>
    </article>
);

export default RevenueVentesChart;