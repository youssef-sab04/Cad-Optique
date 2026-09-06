# Cad-Optique

Application full-stack de gestion interne pour un magasin d'optique au Maroc — utilisée par l'opticien et ses collaborateurs, pas par les clients finaux. Couvre l'ensemble du cycle métier : de la prise en charge du client (examen, ordonnance) jusqu'à la vente, le suivi de stock et la facturation, avec prise en compte des spécificités réglementaires marocaines (TVA, AMO/CNOPS).

🔗 **Démo en ligne** : [cad-optique.vercel.app](https://cad-optique.vercel.app)

## Contexte

Projet  (PFA) réalisé à l'ENSA Fès, en collaboration avec **CAD Digital Agency** (Fès). Le magasin d'optique est un client potentiel de l'agence ; l'application est pensée pour un usage en production réelle, pas uniquement comme livrable académique.

## Stack technique

**Backend**
- Java / Spring Boot
- Spring Security + JWT (cookie httpOnly)
- PostgreSQL (hébergé sur Neon)
- ModelMapper, pagination, gestion centralisée des erreurs

**Frontend**
- React + Redux
- Tailwind CSS, Headless UI, lucide-react
- react-hook-form, react-hot-toast

**Infrastructure**
- Backend : Render (Docker)
- Frontend : Vercel
- Base de données : Neon (PostgreSQL)
- Stockage images : Cloudinary
- Notifications : WhatsApp Business Cloud API (Meta Graph API)

## Fonctionnalités

### Gestion métier
- **Clients** : fiches clients, historique
- **Examens** : suivi des examens de vue
- **Ordonnances** (lunettes/lentilles) : saisie assistée par **OCR** — extraction automatique des champs SPH/CYL/AXE/ADD à partir d'une photo/scan, avec relecture par l'opticien avant validation
- **Produits** : catalogue avec gestion d'images
- **Fournisseurs** : fiches fournisseurs
- **Commandes fournisseurs** : cycle de vie EN_COURS → Validé/Annulé
- **Devis** : création, conversion en bon de vente (SalesOrder)
- **Ventes (SalesOrder)** : gestion des lignes, calcul TVA/prix, statuts
- **Mouvements de stock** : traçabilité des entrées/sorties (déclenchées automatiquement par les commandes et ventes)

### Autres
- **Génération de PDF** : devis et reçus de vente (mise en page A4)
- **Notifications** : alertes de stock bas, rappels clients automatiques par WhatsApp (job planifié quotidien)
- **Tableau de bord / Statistiques** : chiffre d'affaires par mois, panier moyen, taux de conversion des devis, produits les plus vendus, alertes stock, valeur du stock

## Authentification & rôles

- Connexion via JWT (endpoint `/api/auth/signin`), token stocké en cookie sécurisé
- Deux rôles : **Admin** (gestion des comptes, fournisseurs, commandes) et **Responsable** (clients, examens, ordonnances, produits, ventes, devis, stock)
- Routage frontend conditionné par le rôle après connexion

## Architecture

**Backend** — architecture en couches classique :
```
Controller → Service → ServiceImpl → Repository
```
avec DTO + ModelMapper pour le mapping, réponses paginées, et exceptions métier centralisées (`ResourceNotFoundException`).

**Frontend** — un pattern homogène répété pour chaque module :
```
reducer + actions + hook de filtre + Filter / Table / Modal
```

## Tests

Démarche de tests structurée en plusieurs couches :
1. **Unitaires (Mockito)** — logique métier des services (TVA, calculs de stock, conversion devis→vente, statistiques)
2. **Mapping** — un test par entité pour vérifier le ModelMapper
3. **Controller (@WebMvcTest)** — validations, codes de retour
4. **Repository (@DataJpaTest + Testcontainers/PostgreSQL)** — requêtes personnalisées, cascades
5. **Sécurité (@SpringBootTest)** — accès par rôle sur les endpoints `/admin/**` et `/public/**`
6. **End-to-end (RestAssured)** — scénarios complets (login → création de vente → impact stock, confirmation de devis → vente créée)

## Structure du repo

```
Cad-Optique/
├── backend/    # API Spring Boot
└── frontend/   # Application React
```

