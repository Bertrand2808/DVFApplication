#!/bin/bash

# Décompresser le fichier CSV
gunzip /app/doc/full.csv.gz

# Exécuter l'application
java -jar app.jar
