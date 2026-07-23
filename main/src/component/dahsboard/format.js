export const MONTH_LABELS = [
    "Jan", "Fév", "Mar", "Avr", "Mai", "Jun",
    "Jul", "Aoû", "Sep", "Oct", "Nov", "Déc",
];

export const formatNumber = (value, fractionDigits = 0) =>
    new Intl.NumberFormat("fr-FR", {
        minimumFractionDigits: fractionDigits,
        maximumFractionDigits: fractionDigits,
    }).format(Number.isFinite(value) ? value : 0);

export const formatMoney = (value) =>
    new Intl.NumberFormat("fr-FR", {
        style: "currency",
        currency: "MAD",
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
    }).format(Number.isFinite(value) ? value : 0);

export const formatPercent = (value) => `${formatNumber(value, 1)}%`;

export const chartTooltipStyle = {
    backgroundColor: "rgba(15, 23, 42, 0.96)",
    border: "1px solid rgba(148, 163, 184, 0.18)",
    borderRadius: "16px",
    boxShadow: "0 24px 60px rgba(15, 23, 42, 0.28)",
    color: "#e2e8f0",
};