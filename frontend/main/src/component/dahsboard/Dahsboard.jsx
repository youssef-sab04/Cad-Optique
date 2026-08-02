import { useEffect, useMemo, useState } from "react";
import api from "../../api/api";
import Loader from "../shared/Loader";
import { CalendarDays, ChartColumnBig, Coins, Package, RefreshCcw, TrendingUp, Users } from "lucide-react";
import { MONTH_LABELS, formatMoney, formatNumber, formatPercent } from "./format";
import KpiCard from "./KpiCard";
import DashboardHeader from "./DashboardHeader";
import MonthSelector from "./MonthSelector";
import RevenueVentesChart from "./RevenueVentesChart";
import ConversionChart from "./ConversionChart";
import TopProduitsChart from "./TopProduitsChart";
import StockPanel from "./StockPanel";

const MONTH_FULL_LABELS = [
    "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
    "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre",
];

const statCardTone = {
    ca: "from-sky-500 to-cyan-600",
    ventes: "from-emerald-500 to-teal-600",
    devis: "from-violet-500 to-fuchsia-600",
    stock: "from-amber-500 to-orange-600",
    clients: "from-slate-700 to-slate-900",
};

const Dahsboard = () => {
    const currentYear = new Date().getFullYear();
    const [year, setYear] = useState(currentYear);
    const [selectedMonth, setSelectedMonth] = useState(0); // 0 = année entière
    const [refreshIndex, setRefreshIndex] = useState(0);
    const [stats, setStats] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchStats = async () => {
            try {
                setLoading(true);
                setError("");
                const { data } = await api.get(`/admin/statistiques/${year}`);
                setStats(data);
            } catch (requestError) {
                setError(requestError?.response?.data?.message || "Impossible de charger le tableau de bord.");
            } finally {
                setLoading(false);
            }
        };

        fetchStats();
    }, [year, refreshIndex]);

    const monthlyData = useMemo(() => {
        const source = stats?.statsParMois ?? [];
        return Array.from({ length: 12 }, (_, index) => {
            const monthIndex = index + 1;
            const monthStats = source.find((item) => Number(item.mois) === monthIndex) ?? {};
            return {
                mois: MONTH_LABELS[index],
                ca: Number(monthStats.ca ?? 0),
                nombreVentes: Number(monthStats.nombreVentes ?? 0),
                nombreDevis: Number(monthStats.nombreDevis ?? 0),
                tauxConversion: Number(monthStats.tauxConversion ?? 0),
            };
        });
    }, [stats]);

    const topProductsData = useMemo(
        () => (stats?.topProduits ?? []).map((item) => ({ name: item.nom, value: Number(item.quantiteVendue ?? 0) })),
        [stats]
    );

    // Bascule automatique entre les valeurs annuelles (stats) et celles du mois choisi (monthlyData)
    const selectedMonthData = selectedMonth ? monthlyData[selectedMonth - 1] : null;
    const periodeLabel = selectedMonth ? MONTH_FULL_LABELS[selectedMonth - 1] : "l'année";

    const displayedCa = selectedMonthData ? selectedMonthData.ca : stats?.caTotal;
    const displayedVentes = selectedMonthData ? selectedMonthData.nombreVentes : stats?.nombreVentesTotal;
    const displayedDevis = selectedMonthData ? selectedMonthData.nombreDevis : stats?.nombreDevisTotal;
    const displayedConversion = selectedMonthData ? selectedMonthData.tauxConversion : stats?.tauxConversionGlobal;
    const displayedPanierMoyen = selectedMonthData
        ? (selectedMonthData.nombreVentes > 0 ? selectedMonthData.ca / selectedMonthData.nombreVentes : 0)
        : stats?.panierMoyen;

    const kpis = [
        {
            id: "ca",
            label: `Chiffre d'affaires (${periodeLabel})`,
            value: formatMoney(displayedCa),
            helper: selectedMonth
                ? `Panier moyen: ${formatMoney(displayedPanierMoyen)}`
                : `${formatMoney(stats?.montantResteCumule)} restant à encaisser`,
            icon: Coins,
            tone: statCardTone.ca,
        },
        {
            id: "ventes",
            label: `Ventes confirmées (${periodeLabel})`,
            value: formatNumber(displayedVentes),
            helper: `Panier moyen: ${formatMoney(displayedPanierMoyen)}`,
            icon: TrendingUp,
            tone: statCardTone.ventes,
        },
        {
            id: "devis",
            label: `Devis (${periodeLabel})`,
            value: formatNumber(displayedDevis),
            helper: `Conversion: ${formatPercent(displayedConversion)}`,
            icon: ChartColumnBig,
            tone: statCardTone.devis,
        },
        {
            id: "stock",
            label: "Stock & alertes (année)",
            value: formatNumber(stats?.produitsStockFaible),
            helper: `Stock total: ${formatMoney(stats?.valeurTotaleStock)}`,
            icon: Package,
            tone: statCardTone.stock,
        },
        {
            id: "clients",
            label: "Clients actifs (année)",
            value: formatNumber(stats?.clientsActifs),
            helper: `${formatNumber(stats?.nouveauxClients)} nouveaux clients`,
            icon: Users,
            tone: statCardTone.clients,
        },
    ];

    const handleYearChange = (value) => {
        const parsedYear = Number(value);
        if (Number.isFinite(parsedYear) && parsedYear > 2000) {
            setYear(parsedYear);
        }
    };

    const handleDownloadPdf = async () => {
        const response = await api.get(`/admin/statistiques/${year}/rapport-pdf`, { responseType: "blob" });
        const url = window.URL.createObjectURL(new Blob([response.data], { type: "application/pdf" }));
        const link = document.createElement("a");
        link.href = url;
        link.download = `rapport-${year}.pdf`;
        document.body.appendChild(link);
        link.click();
        link.remove();
        window.URL.revokeObjectURL(url);
    };

    if (loading) {
        return <Loader text="Chargement du tableau de bord..." />;
    }

    if (error) {
        return (
            <div className="rounded-3xl border border-rose-200 bg-rose-50 p-6 text-rose-700 shadow-sm">
                <div className="flex items-start gap-3">
                    <div className="mt-0.5 rounded-2xl bg-rose-100 p-2">
                        <CalendarDays className="h-5 w-5" />
                    </div>
                    <div>
                        <h2 className="text-lg font-semibold">Tableau de bord indisponible</h2>
                        <p className="mt-1 text-sm text-rose-600">{error}</p>
                    </div>
                </div>
                <button
                    type="button"
                    onClick={() => setRefreshIndex((value) => value + 1)}
                    className="mt-5 inline-flex items-center gap-2 rounded-full bg-rose-600 px-4 py-2 text-sm font-semibold text-white transition hover:bg-rose-700"
                >
                    <RefreshCcw className="h-4 w-4" />
                    Réessayer
                </button>
            </div>
        );
    }

    return (
        <div className="space-y-6">
            <DashboardHeader
                year={year}
                annee={stats?.annee}
                onYearChange={handleYearChange}
                onReset={() => setYear(currentYear)}
                onDownloadPdf={handleDownloadPdf}
            />

            <section className="grid gap-4 md:grid-cols-2 xl:grid-cols-5">
                {kpis.map((item) => (
                    <KpiCard key={item.id} {...item} />
                ))}
            </section>

            <MonthSelector selectedMonth={selectedMonth} onSelect={setSelectedMonth} />

            <section className="grid gap-6 xl:grid-cols-3">
                <RevenueVentesChart data={monthlyData} />
                <StockPanel
                    produitsStockFaible={stats?.produitsStockFaible ?? 0}
                    valeurTotaleStock={stats?.valeurTotaleStock ?? 0}
                    mouvementsStockDuJour={stats?.mouvementsStockDuJour ?? 0}
                />
            </section>

            <section className="grid gap-6 xl:grid-cols-2">
                <ConversionChart data={monthlyData} />
                <TopProduitsChart data={topProductsData} />
            </section>
        </div>
    );
};

export default Dahsboard;