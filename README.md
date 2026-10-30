# Product Footprint Review - Backend

A small Spring Boot API for reviewing product carbon footprint submissions. It uses Java 21, PostgreSQL, Spring Security and JDBC.

## Run with Docker

You'll need Docker Desktop.

```bash
git clone https://github.com/caffeine-coder/footprint-review-backend.git
cd footprint-review-backend
docker compose up --build -d
```

The API runs at `http://localhost:8080`. PostgreSQL tables, 50 sample submissions and three demo users are created while docker creaets the container

The demo password is `footprint_demo_2026`. You can change it using `DEMO_PASSWORD` in a local `.env` file before the first startup. The default credentials are only for running the app locally.

## Demo accounts

| Role | Email | Password |
| --- | --- | --- |
| Admin | admin@demo.local | footprint_demo_2026 |
| Editor | editor@demo.local | footprint_demo_2026 |
| Viewer | viewer@demo.local | footprint_demo_2026 |

Admin and Editor can review submissions. Viewer has read-only access.

## API

- `POST /api/auth/login` - login (form data)
- `GET /api/auth/me` - current user
- `GET /api/auth/csrf` - CSRF token for login and changes
- `POST /api/auth/logout` - logout
- `GET /api/footprints` - list, search and filter
- `GET /api/footprints/{id}` - submission details
- `PATCH /api/footprints/{id}/review` - approve or reject
- `GET /api/footprints/{id}/history` - review history

The API uses session cookies. Requests that change data require a CSRF token.

## AI usage

- Took help of AI to setup spring security config as it is a repetitive code.
- Used AI to understand the emissions data model because this domain was new to me, and to help set up the initial app skeleton.
- Core application logic and data handling were written and adjusted by completely by me
- Clean readme file
