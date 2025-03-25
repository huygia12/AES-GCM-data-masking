# Demo data masking

<img src="./assets/logokma.png" align="left"
width="150" hspace="10" vspace="10">

||

This project uses Springboot, Docker.

**_CT6_**

||

---

## Getting Started
- First, checkout this [.env template file](./.env.template)
- Pull images and run BE,DB:

    ```bash
    docker-compose up -d
    ```

- Stop all containers:
    ```bash
    docker-compose down
    ```

## Apis list
- [Swagger ui](http://localhost:8080/swagger-ui/index.html#/)

## Database management
- [adminer](http://localhost:8081/)
- **Notice**:
  - Server: mysql-container
  - Username, password, database: is defined in `.env` file
