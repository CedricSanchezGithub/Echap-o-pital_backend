# Kotlin_Back

Backend Spring Boot

## Lancer l'application et la base de données avec Docker Compose
Deux options sont disponibles :

- Environnement complet (application + base MySQL + phpMyAdmin) au niveau racine:
  1. Construire le jar localement (si non présent): `./gradlew build`
  2. Démarrer les services: `docker compose up --build`
  3. Accès:
     - API: http://localhost:8081
     - phpMyAdmin: http://localhost:8080 (host: db, user: root, mdp: root)

- Environnement base de données seule (dossier dev):
  1. `cd dev`
  2. `docker compose up`

Par défaut, l'application utilise MySQL avec les variables d'environnement injectées par Docker Compose:
- SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/openfood_db?serverTimezone=UTC
- SPRING_DATASOURCE_USERNAME=root
- SPRING_DATASOURCE_PASSWORD=root

En local (sans Docker), la configuration par défaut pointe sur `localhost:3306` (voir `src/main/resources/application.properties`).
# Echap-o-pital_IA
# Echap-o-pital_IA
# Echap-o-pital_IA
