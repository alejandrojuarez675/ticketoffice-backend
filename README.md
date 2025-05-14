## Como crear la documentación

Para crear la documentación, se debe ejecutar el siguiente comando para levantar la aplicación:

```bash
./gradlew clean build
./gradlew bootRun
```

Y en otra terminal:
```bash
curl http://localhost:8080/v3/api-docs -o docs/api-docs.json
```

Para acceder a la documentación, abrir el archivo `docs/api-docs.json` en un navegador web. O simplemente acceder a la siguiente URL: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) cuando esté el app levantada.

Cada vez que se haga un push a la rama `main`, la documentación se actualizará automáticamente en la siguiente URL:
https://alejandrojuarez675.github.io/ticketoffice-backend/docs/ con lo que haya en el archivo `docs/api-docs.json` en la rama `main`.
