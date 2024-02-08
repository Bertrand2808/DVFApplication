# Étape de construction
FROM node:16 AS build

WORKDIR /app

COPY package.json package-lock.json ./

RUN npm install

COPY . ./

RUN npm run build

# Étape d'exécution
FROM nginx:stable-alpine

COPY --from=build /app/dist /usr/share/nginx/html

# Rediriger toutes les requêtes vers index.html pour le support du Single Page Application
RUN echo 'server { listen 5173; location / { root /usr/share/nginx/html; try_files $uri /index.html; } }' > /etc/nginx/conf.d/default.conf

EXPOSE 5173

CMD ["nginx", "-g", "daemon off;"]
