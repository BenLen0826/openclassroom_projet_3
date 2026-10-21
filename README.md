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
- **MySQL 8** ou plus : vérifier avec `mysql --version`.
  Sous Windows, si la commande `mysql` est introuvable, ajouter le dossier `bin` de MySQL
  (par exemple `C:\Program Files\MySQL\MySQL Server 8.0\bin`) à la variable d'environnement `PATH`,
  puis ouvrir un nouveau terminal
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
SOURCE src/main/resources/sql/script.sql;
CREATE USER 'chatop_user'@'localhost' IDENTIFIED BY 'mot_de_passe';
GRANT SELECT, INSERT, UPDATE, DELETE ON chatop_db.* TO 'chatop_user'@'localhost';
EXIT;
```

La première commande exécute le script SQL fourni, qui crée la base `chatop_db`
et les tables `USERS`, `RENTALS` et `MESSAGES`. Les deux suivantes créent l'utilisateur
`chatop_user` dédié à l'application. Le mot de passe n'est volontairement pas dans le script,
pour qu'aucun identifiant ne soit enregistré dans le dépôt.

> Si MySQL n'est pas installé sur la même machine que l'application (serveur distant, Docker…),
> remplacer `'localhost'` par `'%'` dans les commandes `CREATE USER` et `GRANT`.

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
DB_HOST=localhost
DB_PORT=3306
DB_USERNAME=chatop_user
DB_PASSWORD=mot_de_passe
JWT_SECRET=une_cle_secrete_d_au_moins_32_caracteres
```

| Variable | Description |
|---|---|
| `DB_HOST` | Adresse du serveur MySQL. `localhost` si MySQL est installé sur la même machine |
| `DB_PORT` | Port de MySQL. `3306` par défaut ; à adapter si besoin (WAMP ou MAMP utilisent parfois `3307` ou `8889`) |
| `DB_USERNAME` | Utilisateur MySQL créé à l'étape 2 (`chatop_user`) |
| `DB_PASSWORD` | Son mot de passe |
| `JWT_SECRET` | Clé utilisée pour signer les tokens JWT. **Au moins 32 caractères**, sinon l'inscription et la connexion renvoient une erreur 500 |

`DB_HOST` et `DB_PORT` sont facultatifs : s'ils sont absents du `.env`, l'application utilise `localhost` et `3306`.
Attention à ne pas les laisser vides (`DB_PORT=`) : une valeur vide ne remplace pas la valeur par défaut.

> Le `.env` est lu comme un fichier `.properties` : le caractère `\` y sert d'échappement.
> Éviter le `\` dans `DB_PASSWORD` et `JWT_SECRET`, ou l'écrire `\\`.

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

Les routes `register` et `login` sont publiques. Toutes les autres routes de l'API nécessitent
un token JWT dans l'en-tête `Authorization: Bearer <token>`.

Sont aussi accessibles sans token :
- la documentation Swagger (`/swagger-ui/index.html` et `/v3/api-docs`) ;
- les images des locations (`/uploads/...`), en lecture seule, pour qu'elles s'affichent dans le navigateur.

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
