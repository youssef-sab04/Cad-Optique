import { Bar, BarChart, CartesianGrid, Cell, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";
import { chartTooltipStyle, formatNumber } from "./format";

const TopProduitsChart = ({ data }) => (
    <article className="rounded-[28px] border border-slate-200 bg-white p-5 shadow-sm">
        <div className="mb-5 flex items-start justify-between gap-4">
            <div>
                <h2 className="text-lg font-bold text-slate-900">Top produits vendus</h2>
                <p className="mt-1 text-sm text-slate-500">Les références les plus performantes sur la période.</p>
            </div>
            <span className="rounded-full bg-violet-50 px-3 py-1 text-xs font-semibold text-violet-700 ring-1 ring-violet-200">
                Top ventes
            </span>
        </div>

        <div className="h-80 w-full">
            <ResponsiveContainer width="100%" height="100%">
                <BarChart data={data} layout="vertical" margin={{ left: 12, right: 16 }}>
                    <CartesianGrid strokeDasharray="3 3" stroke="#e2e8f0" horizontal={false} />
                    <XAxis type="number" tick={{ fill: "#64748b", fontSize: 12 }} axisLine={false} tickLine={false} />
                    <YAxis dataKey="name" type="category" width={140} tick={{ fill: "#334155", fontSize: 12 }} axisLine={false} tickLine={false} />
                    <Tooltip contentStyle={chartTooltipStyle} formatter={(value) => [formatNumber(value), "Quantité vendue"]} />
                    <Bar dataKey="value" radius={[0, 14, 14, 0]} fill="#0ea5e9">
                        {data.map((entry, index) => (
                            <Cell key={`top-product-cell-${entry.name}`} fill={index % 2 === 0 ? "#0ea5e9" : "#14b8a6"} />
                        ))}
                    </Bar>
                </BarChart>
            </ResponsiveContainer>
        </div>
    </article>
);

export default TopProduitsChart;