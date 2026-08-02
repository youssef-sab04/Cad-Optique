import { useSelector, useDispatch } from "react-redux";
import { useState, useEffect } from "react";
import MouvementStockTable from "./MouvementStockTable";
import MouvementStockDetailModal from "./MouvementStockDetailModal";
import useMouvementStockFilter from "./useMouvementStockFilter";
import Loader from "../shared/Loader";
import Paginations from "../shared/Paginations";
import { fetchMouvementsStock } from "../../store/reducers/actions";
import { FaExclamationTriangle } from "react-icons/fa";

const MouvementStock = () => {
    useMouvementStockFilter();
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(fetchMouvementsStock());
    }, [dispatch]);

    const { mouvements, pagination } = useSelector((state) => state.mouvements);
    const { isLoading, errorMessage } = useSelector((state) => state.errors);

    const [openView, setOpenView] = useState(false);
    const [selected, setSelected] = useState(null);

    const totalMouvements = pagination?.totalElements ?? mouvements?.length ?? 0;

    const handleView = (mvt) => {
        setSelected(mvt);
        setOpenView(true);
    };

    return (
        <div className="min-h-screen bg-slate-50 px-4 py-6 sm:px-6 lg:px-8">
            <div className="mx-auto flex w-full max-w-7xl flex-col gap-6">
                <section className="overflow-hidden rounded-3xl border border-slate-200 bg-white shadow-sm">
                    <div className="flex flex-col gap-6 bg-gradient-to-r from-slate-950 via-slate-900 to-slate-800 px-6 py-8 text-white sm:px-8 lg:flex-row lg:items-end lg:justify-between">
                        <div className="max-w-2xl">
                            <p className="text-xs font-semibold uppercase tracking-[0.3em] text-slate-400">
                                Gestion du stock
                            </p>
                            <h1 className="mt-2 text-3xl font-bold sm:text-4xl">Mouvements de stock</h1>
                            <p className="mt-3 max-w-xl text-sm leading-6 text-slate-300 sm:text-base">
                                Consultez rapidement les entrées et sorties de produits, avec les quantités et les montants associés.
                            </p>
                        </div>

                        <div className="grid grid-cols-1 gap-3 sm:grid-cols-3 lg:w-[520px]">
                            <div className="rounded-2xl border border-white/10 bg-white/5 px-4 py-3 backdrop-blur-sm">
                                <p className="text-xs uppercase tracking-[0.2em] text-slate-400">Total</p>
                                <p className="mt-2 text-2xl font-bold">{totalMouvements}</p>
                                <p className="text-xs text-slate-300">mouvement(s) enregistrés</p>
                            </div>
                            <div className="rounded-2xl border border-white/10 bg-white/5 px-4 py-3 backdrop-blur-sm">
                                <p className="text-xs uppercase tracking-[0.2em] text-slate-400">Navigation</p>
                                <p className="mt-2 text-2xl font-bold">{pagination?.totalPages ?? 0}</p>
                                <p className="text-xs text-slate-300">page(s) disponible(s)</p>
                            </div>
                            <div className="rounded-2xl border border-white/10 bg-white/5 px-4 py-3 backdrop-blur-sm">
                                <p className="text-xs uppercase tracking-[0.2em] text-slate-400">Vue</p>
                                <p className="mt-2 text-2xl font-bold">Liste</p>
                                <p className="text-xs text-slate-300">lecture rapide des détails</p>
                            </div>
                        </div>
                    </div>
                </section>

                <div className="flex justify-end text-sm text-slate-500">
                    {pagination?.totalElements != null && <span>{pagination.totalElements} mouvement(s)</span>}
                </div>

                <section className="rounded-3xl border border-slate-200 bg-white p-4 shadow-sm sm:p-6">
                    {isLoading ? (
                        <Loader text="Chargement des mouvements de stock..." />
                    ) : errorMessage ? (
                        <div className="flex min-h-[220px] flex-col items-center justify-center gap-3 rounded-2xl border border-dashed border-rose-200 bg-rose-50 px-6 text-center">
                            <FaExclamationTriangle className="text-rose-500 text-3xl" />
                            <div>
                                <p className="text-lg font-semibold text-rose-800">Impossible de charger les mouvements</p>
                                <p className="mt-1 text-sm text-rose-700">{errorMessage}</p>
                            </div>
                        </div>
                    ) : (
                        <>
                            <MouvementStockTable mouvements={mouvements || []} onView={handleView} />
                            <div className="flex justify-center pt-6">
                                <Paginations
                                    numberOfPage={pagination?.totalPages}
                                    totalProducts={pagination?.totalElements}
                                />
                            </div>
                        </>
                    )}
                </section>
            </div>

            <MouvementStockDetailModal open={openView} setOpen={setOpenView} mouvement={selected} />
        </div>
    );
};

export default MouvementStock;