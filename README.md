# uadb-microservices

**uadb-microservices** est une application de gestion de locations immobilières basée sur une architecture de microservices. Elle permet aux propriétaires de gérer leurs maisons, chambres, contrats et rendez-vous, tout en offrant aux locataires une interface pour consulter les annonces, prendre des rendez-vous et signaler des problèmes. Le projet utilise **Spring Boot** pour le backend, **React/Next.js** pour le frontend, et **MySQL** pour le stockage des données, avec **Eureka** pour la découverte des services et **JWT** pour l'authentification.

## Table des matières
- [Architecture](#architecture)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Exécution](#exécution)
- [Structure du projet](#structure-du-projet)
- [API Documentation](#api-documentation)
- [Technologies utilisées](#technologies-utilisées)
- [Tests](#tests)
- [Contribuer](#contribuer)
- [Licence](#licence)

## Architecture

Le projet est divisé en plusieurs microservices indépendants, chacun gérant un domaine spécifique :

1. **cdn_project** : Gère le stockage et la récupération des fichiers multimédias (photos, vidéos) associés aux chambres.
2. **contrat_project** : Gère les contrats, paiements et signalements de problèmes liés aux locations.
3. **eureka_project** : Serveur Eureka pour la découverte et l'enregistrement des microservices.
4. **frontend** : Interface utilisateur pour les locataires et propriétaires, développée avec React/Next.js et TypeScript.
5. **gateway_project** : Passerelle API avec authentification JWT et routage vers les autres microservices.
6. **logement_project** : Gère les entités principales (maisons, chambres, rendez-vous) avec des fonctionnalités CRUD.

Les services communiquent via des appels REST, et la base de données MySQL est utilisée pour persister les données.

## Prérequis

- **Java 17** ou supérieur (pour les microservices Spring Boot)
- **Maven 3.9.x** (pour la gestion des dépendances backend)
- **Node.js 16+** et **npm** (pour le frontend)
- **MySQL 8.x** (pour la base de données)
- **Docker** (optionnel, pour les tests avec Testcontainers)
- **Git** (pour cloner le dépôt)

## Installation

1. **Cloner le dépôt** :
   ```bash
   git clone https://github.com/nakir00/uadb-microservices.git
   cd uadb-microservices
   ```

2. **Configurer la base de données MySQL** :
   - Créez une base de données MySQL nommée `logement` :
     ```sql
     CREATE DATABASE logement;
     ```
   - Les scripts d'initialisation de la base de données se trouvent dans `logement_project/src/main/resources/db/migration/V1__init.sql` et `gateway_project/src/main/resources/db/migration/V1__init.sql`. Appliquez-les avec un client MySQL (par exemple, `mysql` ou phpMyAdmin).

3. **Configurer les variables d'environnement** :
   - Copiez les fichiers `application.properties` dans chaque module (`cdn_project`, `contrat_project`, etc.) et configurez les paramètres suivants :
     - `spring.datasource.url` : URL de la base de données (ex. `jdbc:mysql://localhost:3306/logement`)
     - `spring.datasource.username` : Nom d'utilisateur MySQL
     - `spring.datasource.password` : Mot de passe MySQL
     - `gateway.service.url` : URL du service gateway (ex. `http://localhost:8080`)
     - `cdn.service.url` : URL du service CDN (ex. `http://localhost:8083`)

4. **Installer les dépendances backend** :
   - Depuis la racine de chaque module (`cdn_project`, `contrat_project`, `eureka_project`, `gateway_project`, `logement_project`), exécutez :
     ```bash
     mvn clean install
     ```

5. **Installer les dépendances frontend** :
   - Depuis le dossier `frontend`, exécutez :
     ```bash
     npm install
     ```

## Exécution

1. **Démarrer le serveur Eureka** :
   - Allez dans `eureka_project` et exécutez :
     ```bash
     mvn spring-boot:run
     ```
   - Le serveur Eureka sera disponible sur `http://localhost:8761`.

2. **Démarrer les microservices** :
   - Dans chaque dossier de microservice (`cdn_project`, `contrat_project`, `gateway_project`, `logement_project`), exécutez :
     ```bash
     mvn spring-boot:run
     ```
   - Les ports par défaut sont :
     - `cdn_project` : 8083
     - `contrat_project` : 8082
     - `gateway_project` : 8080
     - `logement_project` : 8081

3. **Démarrer le frontend** :
   - Dans le dossier `frontend`, exécutez :
     ```bash
     npm run dev
     ```
   - L'application frontend sera disponible sur `http://localhost:3000` (ou un autre port configuré dans `vite.config.ts`).

4. **Accéder à l'application** :
   - Ouvrez un navigateur et accédez à `http://localhost:3000` pour l'interface utilisateur.
   - Consultez la documentation des APIs via Swagger UI, par exemple :
     - `http://localhost:8081/swagger-ui.html` (logement_project)
     - `http://localhost:8083/swagger-ui.html` (cdn_project)

## Structure du projet

```
uadb-microservices/
├── cdn_project/                # Service de gestion des fichiers multimédias
├── contrat_project/            # Service de gestion des contrats, paiements et problèmes
├── eureka_project/             # Serveur Eureka pour la découverte des services
├── frontend/                   # Interface utilisateur (React/Next.js, TypeScript)
├── gateway_project/            # Passerelle API avec authentification JWT
├── logement_project/           # Service de gestion des maisons, chambres et rendez-vous
└── .gitignore
```

### Détails des modules
- **cdn_project** : Gère les uploads de fichiers (images, vidéos) pour les chambres, avec endpoints comme `/api/cdn/{proprietaireId}/{chambreId}`.
- **contrat_project** : Fournit des fonctionnalités pour créer, lire, mettre à jour et supprimer des contrats, paiements et problèmes.
- **eureka_project** : Configure un serveur Eureka pour l'enregistrement et la découverte des microservices.
- **frontend** : Contient des composants React/Next.js pour les locataires et propriétaires, avec des formulaires, tableaux et un calendrier de rendez-vous.
- **gateway_project** : Gère l'authentification (JWT) et le routage des requêtes vers les autres services.
- **logement_project** : Gère les entités principales (maisons, chambres, rendez-vous) avec des critères de recherche avancés via JPA Specifications.

## API Documentation

Chaque microservice expose une documentation Swagger UI pour ses endpoints REST :
- **cdn_project** : `http://localhost:8083/swagger-ui.html`
- **contrat_project** : `http://localhost:8082/swagger-ui.html`
- **gateway_project** : `http://localhost:8080/swagger-ui.html`
- **logement_project** : `http://localhost:8081/swagger-ui.html`

Consultez ces URLs pour explorer les endpoints disponibles, leurs paramètres et les réponses attendues.

## Technologies utilisées

### Backend
- **Java 17** : Langage principal pour les microservices.
- **Spring Boot** : Framework pour le développement des services REST.
- **Spring Data JPA** : Gestion des entités et requêtes vers MySQL.
- **Spring Cloud Eureka** : Découverte des services.
- **Springdoc OpenAPI** : Documentation des APIs.
- **MySQL** : Base de données relationnelle.
- **Maven** : Gestion des dépendances.
- **Testcontainers** : Tests d'intégration avec MySQL.

### Frontend
- **React/Next.js** : Framework pour l'interface utilisateur.
- **TypeScript** : Typage statique pour le frontend.
- **TanStack Query** : Gestion des requêtes API.
- **ShadCN/UI** : Composants d'interface utilisateur réutilisables.
- **Vite** : Outil de build rapide pour le frontend.

### Sécurité
- **JWT** : Authentification et autorisation dans `gateway_project`.

## Tests

Le projet utilise **Testcontainers** pour les tests d'intégration avec MySQL (voir `TestcontainersConfiguration.java`). Cependant, les tests actuels sont limités à des vérifications de chargement de contexte (`contextLoads`). Pour ajouter des tests :
- Utilisez **JUnit** pour écrire des tests unitaires et d'intégration dans les modules backend.
- Utilisez **Jest** ou **Vitest** pour tester les composants frontend (`frontend/src/components`).

Exemple de commande pour exécuter les tests backend :
```bash
mvn test
```

Pour le frontend :
```bash
cd frontend
npm test
```

## Contribuer

1. Forkez le dépôt sur GitHub.
2. Créez une branche pour votre fonctionnalité ou correction :
   ```bash
   git checkout -b feature/nouvelle-fonctionnalite
   ```
3. Ajoutez vos modifications et committez :
   ```bash
   git commit -m "Ajout de nouvelle fonctionnalité"
   ```
4. Poussez votre branche et créez une pull request :
   ```bash
   git push origin feature/nouvelle-fonctionnalite
   ```
5. Assurez-vous que les tests passent et que le code respecte les conventions (utilisez `mvn checkstyle:check` pour le backend et `npm run lint` pour le frontend).

## Licence

Ce projet est sous licence **Apache 2.0**. Voir le fichier `LICENSE` pour plus de détails.
