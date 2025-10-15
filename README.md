ProyectoFinalAutomatas

Sistema de máquina de estados para controlar el flujo de producción de materia prima en una fábrica de muebles.
Incluye Backend (Spring Boot + Postgres) y Frontend (Vite + React + Chakra UI), con despliegue sencillo vía Docker Compose.



✅ Requisitos previos

Solo necesitas Docker Desktop.

🚀 Arranque RÁPIDO con Docker (recomendado)

Clona el repositorio

git clone <URL_DEL_REPO>
cd ProyectoFinalAutomatas/BACKEND   # carpeta donde está docker-compose.yml

Construye y levanta todo
docker compose up --build

La primera vez descargará imágenes y compilará. Verás logs de:

db (PostgreSQL)

app (Spring Boot)

frontend (Nginx sirviendo tu app)

Abre el navegador

Frontend: http://localhost:5173

Backend  http://localhost:8080

⚠️ Si el frontend no muestra datos aún, espera a que el app termine de iniciar (verás el banner de Spring) y refresca la página.