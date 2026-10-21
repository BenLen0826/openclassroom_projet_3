# ChâTop – Back-end (openclassroom_projet_3)

API REST du portail de location saisonnière ChâTop.
Elle est utilisée par le front-end Angular fourni par OpenClassrooms (https://github.com/OpenClassrooms-Student-Center/Mod-lisez-et-impl-mentez-le-back-end-en-utilisant-du-code-Java-maintenable).

## Fonctionnalités

- Inscription et connexion des utilisateurs (authentification par token JWT)
- Consultation, création et modification des locations (avec upload d'une image)
- Envoi de messages aux propriétaires
- Documentation interactive de l'API avec Swagger

## Technologies

- Java 27
- Spring Boot 4.1.1 (Spring Web, Spring Security, Spring Data JPA, Validation)
- MySQL
- springdoc-openapi (Swagger)

## Prérequis

Avant d'installer le projet, il faut avoir installé :

- **Java 27** (JDK) : vérifier avec `java -version`
- **MySQL 8** ou plus : vérifier avec `mysql --version`
- **Git**, pour cloner le projet

Maven n'est pas nécessaire : le projet contient le Maven Wrapper (`mvnw`), qui le télécharge automatiquement.

## Installation

### 1. Cloner le projet

```bash
git clone https://github.com/BenLen0826/openclassroom_projet_3.git
cd openclassroom_projet_3
```

### 2. Créer la base de données et les tables

Depuis la racine du projet, se connecter à MySQL (le mot de passe de l'utilisateur `root` est demandé) :

```bash
mysql -u root -p
```

> Le compte `root` est utilisé **uniquement pour cette installation**, car c'est le seul compte
> autorisé à créer une base et un utilisateur. L'application, elle, ne se connecte jamais avec `root` :
> elle utilise l'utilisateur `chatop_user` créé ci-dessous.

Puis exécuter les commandes suivantes, en remplaçant `mot_de_passe` par le mot de passe de votre choix :

```sql
CREATE DATABASE chatop_db;
CREATE USER 'chatop_user'@'localhost' IDENTIFIED BY 'mot_de_passe';
GRANT SELECT, INSERT, UPDATE, DELETE ON chatop_db.* TO 'chatop_user'@'localhost';
USE chatop_db;
SOURCE src/main/resources/sql/script.sql;
EXIT;
```

Ces commandes créent la base `chatop_db`, un utilisateur `chatop_user` dédié à l'application,
puis exécutent le script SQL fourni, qui crée les tables `USERS`, `RENTALS` et `MESSAGES`.

`chatop_user` reçoit seulement les droits nécessaires à l'application (principe du moindre privilège) :
lire, ajouter, modifier et supprimer des lignes, uniquement dans la base `chatop_db`.
Il ne peut ni supprimer ni modifier la structure des tables.

> L'application ne crée pas les tables elle-même : au démarrage, elle vérifie seulement
> qu'elles existent et qu'elles correspondent au code. Si le script n'a pas été exécuté,
> l'application refuse de démarrer.

### 3. Configurer les variables d'environnement

Les informations secrètes (identifiants de la base, clé JWT) ne sont pas écrites dans le code.
Elles sont lues dans un fichier `.env`, à la racine du projet, qui n'est jamais envoyé sur GitHub.

Copier le fichier d'exemple :

```bash
cp .env.example .env
```

Puis remplir le fichier `.env` :

```properties
DB_USERNAME=chatop_user
DB_PASSWORD=mot_de_passe
JWT_SECRET=une_cle_secrete_d_au_moins_32_caracteres
```

| Variable | Description |
|---|---|
| `DB_USERNAME` | Utilisateur MySQL créé à l'étape 2 (`chatop_user`) |
| `DB_PASSWORD` | Son mot de passe |
| `JWT_SECRET` | Clé utilisée pour signer les tokens JWT. **Au moins 32 caractères**, sinon l'inscription et la connexion renvoient une erreur 500 |

## Lancement

Depuis la racine du projet :

```bash
# Windows (PowerShell)
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

L'API est disponible sur **http://localhost:3001**.

Les images envoyées lors de la création d'une location sont enregistrées dans le dossier
`uploads/` à la racine du projet. Il est créé automatiquement au premier envoi.

## Documentation de l'API (Swagger)

Une fois l'application lancée, la documentation est accessible sans connexion à l'adresse :
**http://localhost:3001/swagger-ui/index.html**

Elle liste toutes les routes et permet de les tester directement.

Pour tester les routes protégées :

1. Créer un compte avec `POST /api/auth/register`, ou se connecter avec `POST /api/auth/login`
2. Copier la valeur du `token` renvoyée (sans les guillemets)
3. Cliquer sur le bouton **Authorize** en haut de la page et coller le token (sans écrire `Bearer` devant)
4. Toutes les routes peuvent maintenant être testées

## Routes de l'API

Toutes les routes, sauf `register` et `login`, nécessitent un token JWT dans l'en-tête
`Authorization: Bearer <token>`.

| Méthode | Route | Description |
|---|---|---|
| POST | `/api/auth/register` | Créer un compte et récupérer un token |
| POST | `/api/auth/login` | Se connecter et récupérer un token |
| GET | `/api/auth/me` | Récupérer l'utilisateur connecté |
| GET | `/api/rentals` | Récupérer toutes les locations |
| GET | `/api/rentals/{id}` | Récupérer une location |
| POST | `/api/rentals` | Créer une location (formulaire avec image) |
| PUT | `/api/rentals/{id}` | Modifier une location (uniquement son propriétaire) |
| GET | `/api/user/{id}` | Récupérer un utilisateur |
| POST | `/api/messages` | Envoyer un message au propriétaire d'une location |

## Utilisation avec le front-end

Le front-end Angular appelle l'API sur `http://localhost:3001/api`.
Lancer d'abord ce back-end, puis le front-end (`npm install` puis `npm run start`),
et ouvrir **http://localhost:4200**.
