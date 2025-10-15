# ===== Etapa 1: build con Node (glibc) compatible con Vite =====
FROM node:22.12-bookworm-slim AS build
WORKDIR /app

# Evita logs ruidosos y garantiza reproducibilidad
ENV npm_config_fund=false npm_config_audit=false

COPY package*.json ./
RUN npm ci

COPY . .
# Si usas variables de build para Vite:
# ARG VITE_API_BASE=/api
# ENV VITE_API_BASE=${VITE_API_BASE}
RUN npm run build

# ===== Etapa 2: servir estáticos con Nginx =====
FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
