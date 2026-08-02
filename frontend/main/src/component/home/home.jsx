import { useState, useEffect } from "react";
import {
  ChevronDown, Menu, X, ArrowRight, Glasses,
} from "lucide-react";
import AOS from "aos";
import "aos/dist/aos.css";
import heroImg from "../../assets/image/image1.webp";
import { NAV_LINKS, FEATURES, FAQS } from "../utils";
import { Link } from "react-router-dom";



const Home = () => {
  const [menuOpen, setMenuOpen] = useState(false);
  const [openFaq, setOpenFaq] = useState(0);

  useEffect(() => {
    AOS.init({ duration: 700, once: true, easing: "ease-out-cubic" });
  }, []);

  return (
    <div className="min-h-screen bg-white text-slate-900 antialiased">

      {/* NAVBAR */}
      <header className="sticky top-0 z-50 border-b border-slate-200 bg-white/90 backdrop-blur">
        <div className="mx-auto flex max-w-7xl items-center justify-between px-6 py-4">
          <Link to="/"
                        className="group flex items-center gap-2 font-display text-xl font-bold text-slate-900"
                    >
                        <span className="flex h-9 w-9 items-center justify-center rounded-full bg-blue-900 text-white transition-transform duration-300 group-hover:rotate-12 group-hover:scale-110">
                            <Glasses className="h-5 w-5" />
                        </span>
                        Cad-Optique
                    </Link>

          <nav className="hidden items-center gap-8 font-body text-sm font-medium text-slate-600 md:flex">
            {NAV_LINKS.map((link) => (
              <a
                key={link.href}
                href={link.href}
                className="relative transition-colors hover:text-blue-900 after:absolute after:-bottom-1 after:left-0 after:h-0.5 after:w-0 after:bg-blue-700 after:transition-all after:duration-300 hover:after:w-full"
              >
                {link.label}
              </a>
            ))}
          </nav>

          <div className="hidden items-center gap-3 md:flex">
            <Link to="/login" className="hidden items-center gap-3 md:flex">
              <span className="rounded-full bg-blue-900 px-5 py-2.5 font-body text-sm font-semibold text-white transition-all duration-300 hover:-translate-y-0.5 hover:bg-blue-800 hover:shadow-lg hover:shadow-blue-900/30">
                Connexion
              </span>
            </Link>
          </div>


          <button
            className="md:hidden"
            onClick={() => setMenuOpen((o) => !o)}
            aria-label="Ouvrir le menu"
          >
            {menuOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
          </button>
        </div>

        {menuOpen && (
          <div className="border-t border-slate-200 bg-white px-6 py-4 md:hidden">
            <nav className="flex flex-col gap-4 font-body text-sm font-medium text-slate-600">
              {NAV_LINKS.map((link) => (
                <a key={link.href} href={link.href} onClick={() => setMenuOpen(false)}>
                  {link.label}
                </a>
              ))}
              <a
                href="/login"
                className="w-fit rounded-full bg-blue-900 px-5 py-2.5 text-sm font-semibold text-white"
              >
                Connexion
              </a>
              
            </nav>
          </div>
        )}
      </header>

      {/* HERO */}
      <section id="accueil" className="relative overflow-hidden bg-slate-50">
        <div className="pointer-events-none absolute -top-24 -right-24 h-72 w-72 rounded-full bg-blue-200/40 blur-3xl" />
        <div className="pointer-events-none absolute -bottom-24 -left-24 h-72 w-72 rounded-full bg-blue-100/60 blur-3xl" />

        <div className="mx-auto grid max-w-7xl items-center gap-12 px-6 py-24 md:grid-cols-2">
          <div data-aos="fade-right">
            <span className="mb-4 inline-block rounded-full border border-blue-200 bg-blue-50 px-4 py-1 font-body text-xs font-semibold uppercase tracking-wide text-blue-900">
              Gestion interne opticien
            </span>
            <h1 className="font-display text-5xl font-bold leading-tight text-slate-900">
              Votre magasin,
              <br />
              <span className="text-blue-700">au point.</span>
            </h1>
            <p className="mt-6 max-w-md font-body text-lg text-slate-600">
              Clients, stock, devis et facturation réunis dans une seule
              interface pensée pour le rythme d'un magasin d'optique.
            </p>
            <div className="mt-8 flex items-center gap-4">
              <a
                href="#fonctionnalites"
                className="group inline-flex items-center gap-2 rounded-full bg-blue-900 px-6 py-3 font-body text-sm font-semibold text-white transition-all duration-300 hover:-translate-y-0.5 hover:bg-blue-800 hover:shadow-lg hover:shadow-blue-900/30"
              >
                Découvrir
                <ArrowRight className="h-4 w-4 transition-transform duration-300 group-hover:translate-x-1" />
              </a>
              <a
                href="#dashboard"
                className="font-body text-sm font-semibold text-slate-700 transition-colors hover:text-blue-900"
              >
                Voir le tableau de bord
              </a>
            </div>
          </div>

          <div className="relative flex items-center justify-center" data-aos="fade-left" data-aos-delay="150">
            <div className="group relative overflow-hidden rounded-3xl border border-slate-200 shadow-xl shadow-slate-900/10 transition-all duration-500 hover:-translate-y-1 hover:shadow-2xl hover:shadow-blue-900/20">
              <img
                src={heroImg}
                alt="Examen de vue en magasin d'optique"
                className="h-[420px] w-full object-cover transition-transform duration-700 group-hover:scale-105"
              />
              <div className="absolute inset-0 bg-gradient-to-t from-slate-900/40 via-transparent to-transparent" />
            </div>
            <div className="absolute -bottom-6 -right-6 h-28 w-28 rounded-full bg-blue-600/10 blur-2xl" />
          </div>
        </div>
      </section>

      {/* FEATURES */}
      <section id="fonctionnalites" className="mx-auto max-w-7xl px-6 py-24">
        <div className="mb-14 text-center" data-aos="fade-up">
          <h2 className="font-display text-3xl font-bold text-slate-900 md:text-4xl">
            Tout votre magasin, en clair
          </h2>
          <div className="mx-auto mt-3 h-1 w-16 rounded-full bg-blue-700" />
        </div>

        <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-4">
          {FEATURES.map(({ icon: Icon, title, desc }, i) => (
            <div
              key={title}
              data-aos="fade-up"
              data-aos-delay={i * 100}
              className="group rounded-2xl border border-slate-200 bg-white p-8 text-center shadow-sm transition-all duration-300 hover:-translate-y-1.5 hover:border-blue-200 hover:shadow-xl hover:shadow-blue-900/10"
            >
              <span className="mx-auto mb-5 flex h-14 w-14 items-center justify-center rounded-full bg-blue-50 text-blue-900 transition-all duration-300 group-hover:scale-110 group-hover:bg-blue-900 group-hover:text-white">
                <Icon className="h-7 w-7" />
              </span>
              <h3 className="font-display text-lg font-semibold text-slate-900">{title}</h3>
              <p className="mt-2 font-body text-sm leading-relaxed text-slate-600">{desc}</p>
            </div>
          ))}
        </div>
      </section>

      {/* DASHBOARD PREVIEW */}
      <section id="dashboard" className="bg-slate-900 py-24 text-white">
        <div className="mx-auto max-w-7xl px-6">
          <div className="mx-auto max-w-2xl text-center" data-aos="fade-up">
            <h2 className="font-display text-3xl font-bold md:text-4xl">
              Un dashboard pensé pour la <span className="text-blue-400">clarté</span>
            </h2>
            <p className="mt-4 font-body text-slate-300">
              Visualisez votre chiffre d'affaires, vos meilleures ventes et vos
              niveaux de stock en un coup d'œil.
            </p>
            <div className="mt-6 flex flex-wrap justify-center gap-6 font-body text-sm text-slate-300">
              <span>✓ Alertes de stock</span>
              <span>✓ Suivi des devis</span>
              <span>✓ Rappels examens</span>
            </div>
          </div>

          <div
            className="mx-auto mt-14 max-w-4xl overflow-hidden rounded-2xl border border-slate-700 bg-slate-800 shadow-2xl transition-shadow duration-500 hover:shadow-blue-900/30"
            data-aos="zoom-in"
            data-aos-delay="150"
          >
            <div className="flex items-center gap-2 border-b border-slate-700 bg-slate-900 px-4 py-3">
              <span className="h-3 w-3 rounded-full bg-red-500/70" />
              <span className="h-3 w-3 rounded-full bg-yellow-500/70" />
              <span className="h-3 w-3 rounded-full bg-green-500/70" />
            </div>
            <div className="grid grid-cols-1 gap-4 p-6 sm:grid-cols-3">
              {[
                { label: "CA du jour", value: "4 250 MAD" },
                { label: "Devis en attente", value: "12" },
                { label: "Stock faible", value: "3 réfs", accent: true },
              ].map((stat) => (
                <div
                  key={stat.label}
                  className="rounded-xl bg-slate-700/50 p-5 transition-all duration-300 hover:-translate-y-1 hover:bg-slate-700"
                >
                  <p className="font-body text-xs uppercase tracking-wide text-slate-400">{stat.label}</p>
                  <p className={`mt-2 font-display text-2xl font-bold ${stat.accent ? "text-blue-400" : ""}`}>
                    {stat.value}
                  </p>
                </div>
              ))}
            </div>
          </div>
        </div>
      </section>

      {/* FAQ */}
      <section id="faq" className="mx-auto max-w-3xl px-6 py-24">
        <h2 className="text-center font-display text-3xl font-bold text-slate-900" data-aos="fade-up">
          Questions Fréquentes
        </h2>
        <div className="mx-auto mt-3 h-1 w-16 rounded-full bg-blue-700" data-aos="fade-up" />

        <div className="mt-12 divide-y divide-slate-200 rounded-2xl border border-slate-200" data-aos="fade-up" data-aos-delay="150">
          {FAQS.map((faq, i) => (
            <div key={faq.q}>
              <button
                className="flex w-full items-center justify-between px-6 py-5 text-left font-body font-medium text-slate-900 transition-colors hover:bg-slate-50"
                onClick={() => setOpenFaq(openFaq === i ? -1 : i)}
              >
                {faq.q}
                <ChevronDown
                  className={`h-5 w-5 shrink-0 text-blue-900 transition-transform duration-300 ${openFaq === i ? "rotate-180" : ""
                    }`}
                />
              </button>
              <div
                className={`grid overflow-hidden transition-all duration-300 ${openFaq === i ? "grid-rows-[1fr] opacity-100" : "grid-rows-[0fr] opacity-0"
                  }`}
              >
                <p className="overflow-hidden px-6 pb-5 font-body text-sm leading-relaxed text-slate-600">
                  {faq.a}
                </p>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* FOOTER */}
      <footer className="bg-slate-950 text-slate-300">
        <div className="mx-auto grid max-w-7xl grid-cols-1 gap-10 px-6 py-16 md:grid-cols-3">
          <div>
            <p className="font-display text-lg font-bold text-white">Cad-Optique</p>
            <p className="mt-3 font-body text-sm leading-relaxed text-slate-400">
              Solution interne de gestion pour magasins d'optique au Maroc.
            </p>
          </div>
          <div>
            <p className="font-body text-sm font-semibold uppercase tracking-wide text-white">Navigation</p>
            <ul className="mt-4 space-y-2 font-body text-sm text-slate-400">
              {NAV_LINKS.map((link) => (
                <li key={link.href}>
                  <a href={link.href} className="transition-colors hover:text-white">{link.label}</a>
                </li>
              ))}
            </ul>
          </div>
          <div>
            <p className="font-body text-sm font-semibold uppercase tracking-wide text-white">Contact</p>
            <ul className="mt-4 space-y-2 font-body text-sm text-slate-400">
              <li>Fès, Maroc</li>
            </ul>
          </div>
        </div>
        <div className="border-t border-slate-800 px-6 py-6 text-center font-body text-xs text-slate-500">
          © 2026 Cad-Optique. Tous droits réservés.
        </div>
      </footer>
    </div>
  );
};

export default Home;