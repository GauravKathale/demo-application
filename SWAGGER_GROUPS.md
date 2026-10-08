# Swagger Group URLs

This project exposes two Swagger groups:

- **Technical APIs** (all APIs in the application)
- **Demo APIs** (only selected demo endpoints)

## Swagger UI

Open:

`http://localhost:8080/swagger-ui/index.html`

In the Swagger UI group dropdown, use:

- `technical-apis`
- `demo-apis`

## Direct OpenAPI JSON URLs

- Technical APIs: `http://localhost:8080/v3/api-docs/technical-apis`
- Demo APIs: `http://localhost:8080/v3/api-docs/demo-apis`

## Current Demo API Endpoints

Currently, `demo-apis` includes:

- `GET /api/jira/issues/status/details`

## How to add more Demo APIs

Update `src/main/java/com/vodafone/gno/config/swagger/SwaggerOpenApiConfig.java` and add new paths to `DEMO_API_PATHS`.

Example:

```java
private static final String[] DEMO_API_PATHS = {
    "/api/jira/issues/status/details",
    "/api/jira/issues/recent"
};
```

