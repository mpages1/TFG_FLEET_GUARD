# FleetGuard – Sistema de Detecció de Fatiga i Somnolència

FleetGuard és un sistema modular dissenyat per monitorar l’estat de fatiga i somnolència dels conductors en temps real, integrant models de detecció avançats amb una interfície de gestió i visualització de dades. L’arquitectura es basa en microserveis conteniretzats mitjançant Docker, amb integració externa de models de detecció basats en visió per computador i dades del tacògraf.

---

## Mòduls del Sistema

### Backend (Spring Boot)
- Proporciona una API REST per la gestió d’usuaris, autenticació, informes i integració amb els models de detecció.
- Gestiona la persistència mitjançant PostgreSQL.
- Exposa serveis REST que poden ser consumits per la interfície web i per Flask.

### Frontend (React + Vite)
- Interfície web per a administradors i conductors.
- Permet visualitzar l’estat del conductor, informes històrics i alertes.
- Està dissenyat per ser responsive i visualment intuïtiu.

### PostgreSQL + pgAdmin
- Conté totes les dades relacionades amb usuaris, rols, prediccions i informes.
- PgAdmin permet una gestió visual i directa de la base de dades.

### Models de Detecció (Python + Flask)
- Implementats amb OpenCV i models estadístics / ML.
- Funcionen fora de Docker per accedir directament a la càmera del sistema host.
- Exposen rutes REST a través de Flask per iniciar, aturar i consultar els resultats de detecció.

---

## Posada en Marxa

### 1. Clona el repositori

```bash
https://github.com/mpages1/TFG_FLEET_GUARD.git
```

### 2. Executa el sistema conteniretzat (backend, frontend, base de dades)
```bash
docker-compose up --build
````
Ports exposats:
http://localhost:5173 → Frontend

http://localhost:8080 → Backend API

http://localhost:5050 → PgAdmin (usuari: admin@admin.com, contrasenya: admin)

localhost:33333 → PostgreSQL (usuari: postgres, contrasenya: admin123)


### 3. Inicia els models de detecció (fora de Docker)
Aquest pas és essencial. Sense els models de detecció en funcionament, el sistema no pot detectar fatiga ni somnolència, per tant tampoc pot enviar alertes.

```bash
cd fatigue_drowsiness_detection_models/
python flask_server.py
````
Això exposa el servei Flask a http://localhost:5002 amb les següents rutes:

/init_camera_detection

/start_camera_detection

/stop_camera_detection

/init_tachograph_detection

/start_tachograph_detection

/stop_tachograph_detection

/start_combined_detection

Important: Cal assegurar que la càmera està connectada i disponible per a accés nadiu. En entorns Windows, Docker no permet accés directe a càmeres, per això aquest mòdul s'executa externament.

### Consideracions Tècniques

La base de dades es configura per permetre connexions externes mitjançant la configuració del fitxer pg_hba.conf i exposant PostgreSQL al port 33333.

Si s'executa docker-compose down -v, totes les dades es perden (incloent usuaris i informes). Utilitza volums persistents per evitar-ho.

Els models han d’executar-se abans d’interactuar amb la detecció des de l’UI o API.

PgAdmin no guarda les connexions per defecte si no es persisteix pgadmin_data com a volum.


