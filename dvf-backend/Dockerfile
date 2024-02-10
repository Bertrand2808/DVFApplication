# Étape 1 : Utiliser une image de base officielle Maven pour construire l'application
FROM maven:3.8.4-openjdk-17 AS builder

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier pom.xml dans le conteneur
COPY pom.xml .

# Télécharger toutes les dépendances et les stocker dans le cache Docker
RUN mvn dependency:go-offline

# Copier le code source dans le conteneur
COPY src ./src

# Créer le répertoire doc dans le conteneur
RUN mkdir -p /app/doc

# Compiler l'application
RUN mvn clean install

# Étape 2 : Utiliser une image de base officielle Java 17 pour l'image finale
FROM openjdk:17-jdk-alpine

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Télécharger le fichier CSV compressé depuis l'URL spécifiée
RUN wget -O doc/full.csv.gz https://files.data.gouv.fr/geo-dvf/latest/csv/2023/full.csv.gz

# Décompresser le fichier CSV
RUN gunzip doc/full.csv.gz

# Copier le fichier .jar compilé depuis l'étape précédente
COPY --from=builder /app/target/spring-boot-jpa-h2-0.0.1-SNAPSHOT.jar app.jar

# Exécuter l'application
CMD ["java", "-jar", "app.jar"]
