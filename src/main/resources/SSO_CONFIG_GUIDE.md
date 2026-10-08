# SSO and Non-SSO Login Configuration Guide

## Overview

This project supports both **Azure AD (Entra) SSO login** and **Basic Authentication (non-SSO)** login. Environment-based profile files allow you to switch between these authentication methods depending on your deployment environment.

## Environment Profiles

### 1. **application-local.yml** (Local Development - Non-SSO)
- **Authentication**: Basic Authentication (username/password)
- **Database**: H2 in-file database
- **SSO Enabled**: `false`
- **Use Case**: Local development without Azure AD
- **Default Credentials**:
  - Username: `admin`
  - Password: `admin`

**To use this profile:**
```bash
java -Dspring.profiles.active=local -jar gno-0.0.1-SNAPSHOT.jar
# OR
set SPRING_PROFILES_ACTIVE=local
mvn spring-boot:run
```

### 2. **application-dev.yml** (Development - SSO Enabled)
- **Authentication**: Azure AD (Entra) SSO
- **Database**: H2 in-file database
- **SSO Enabled**: `true`
- **Use Case**: Development environment with Azure AD integration
- **Configuration**: Requires Azure AD credentials

**Environment Variables Required:**
```
DEV_AZURE_TENANT_ID        - Azure AD Tenant ID
DEV_AZURE_CLIENT_ID        - Azure AD Application Client ID
DEV_AZURE_CLIENT_SECRET    - Azure AD Application Client Secret
DEV_AZURE_REDIRECT_URI     - OAuth2 Redirect URI (default: http://localhost:8080/login/oauth2/code/azure)
DEV_DB_URL                 - Database URL
DEV_DB_USERNAME            - Database Username
DEV_DB_PASSWORD            - Database Password
DEV_JIRA_EMAIL             - JIRA Email
DEV_JIRA_BASE_URL          - JIRA Base URL
DEV_JIRA_API_TOKEN         - JIRA API Token
```

**To use this profile:**
```bash
set SPRING_PROFILES_ACTIVE=dev
java -Dspring.profiles.active=dev -jar gno-0.0.1-SNAPSHOT.jar
```

### 3. **application-test.yml** (Testing - SSO Enabled)
- **Authentication**: Azure AD (Entra) SSO
- **Database**: H2 in-file database (test-specific)
- **SSO Enabled**: `true`
- **Use Case**: Automated testing with Azure AD
- **Logging Level**: Reduced for testing (WARN for root, INFO for app)

**Environment Variables Required:**
```
TEST_AZURE_TENANT_ID       - Azure AD Tenant ID
TEST_AZURE_CLIENT_ID       - Azure AD Application Client ID
TEST_AZURE_CLIENT_SECRET   - Azure AD Application Client Secret
TEST_AZURE_REDIRECT_URI    - OAuth2 Redirect URI
TEST_DB_URL                - Database URL
TEST_DB_USERNAME           - Database Username
TEST_DB_PASSWORD           - Database Password
TEST_JIRA_EMAIL            - JIRA Email
TEST_JIRA_BASE_URL         - JIRA Base URL
TEST_JIRA_API_TOKEN        - JIRA API Token
```

**To use this profile:**
```bash
set SPRING_PROFILES_ACTIVE=test
mvn clean test -Dspring.profiles.active=test
```

### 4. **application-prod.yml** (Production - SSO Enabled)
- **Authentication**: Azure AD (Entra) SSO
- **Database**: PostgreSQL (external database)
- **SSO Enabled**: `true`
- **Use Case**: Production environment with high availability
- **Logging Level**: Minimal (WARN for root, INFO for app)
- **DDL Mode**: `validate` (no automatic schema updates)

**Environment Variables Required:**
```
PROD_AZURE_TENANT_ID       - Azure AD Tenant ID
PROD_AZURE_CLIENT_ID       - Azure AD Application Client ID
PROD_AZURE_CLIENT_SECRET   - Azure AD Application Client Secret
PROD_AZURE_REDIRECT_URI    - OAuth2 Redirect URI (https://prod.example.com/login/oauth2/code/azure)
PROD_DB_URL                - PostgreSQL Database URL
PROD_DB_USERNAME           - Database Username
PROD_DB_PASSWORD           - Database Password
PROD_DB_ENGINE_URL         - Camunda Engine Database URL
PROD_DB_ENGINE_USERNAME    - Engine Database Username
PROD_DB_ENGINE_PASSWORD    - Engine Database Password
PROD_JIRA_EMAIL            - JIRA Email
PROD_JIRA_BASE_URL         - JIRA Base URL
PROD_JIRA_API_TOKEN        - JIRA API Token
```

**To use this profile:**
```bash
set SPRING_PROFILES_ACTIVE=prod
java -Dspring.profiles.active=prod -jar gno-0.0.1-SNAPSHOT.jar
```

## Base Configuration (application.yaml)

The `application.yaml` file contains common configuration shared across all profiles:

### Key Settings:

1. **Plugin Identity (Entra/AAD)**:
   ```yaml
   plugin.identity.entra:
     use-email-as-camunda-user-id: true
     administrator-group-name: gno-admin
     cache-enabled: true
     fetch-groups-from-graph: true
   ```

2. **KramaX BPM Configuration**:
   ```yaml
   kramax:
     bpm:
       identity-provider:
         enabled: true
       oauth2:
         azure-sso-enabled: true  # Overridden by profile-specific configs
   ```

3. **Camunda BPM**:
   - Schema updates enabled
   - History time-to-live: 30 days
   - Cleanup strategy: removalTimeBased

## Switching Between SSO and Non-SSO

### Non-SSO (Local Development):
```bash
# application-local.properties configuration
# Excludes:
# - OAuth2ClientWebSecurityAutoConfiguration
# - KramaxSpringSecurityOAuth2AutoConfiguration
# Uses: Basic Authentication (username/password)
spring.profiles.active=local
```

### SSO (Dev/Test/Prod):
```bash
# application-dev.yml, application-test.yml, application-prod.yml
# Includes: Azure AD OAuth2 authentication
# Requires: Azure AD credentials in environment variables
spring.profiles.active=dev|test|prod
```

## Environment Variable Fallback Chain

The configurations use a fallback chain for flexibility:

```
${ENVIRONMENT_SPECIFIC_VAR} → ${GENERIC_VAR} → ${DEFAULT_VALUE}

Example:
${DEV_AZURE_TENANT_ID} → ${AZURE_TENANT_ID} → dev-tenant-id
```

This allows you to either:
1. Set environment-specific variables (e.g., `DEV_AZURE_TENANT_ID`)
2. Set generic variables (e.g., `AZURE_TENANT_ID`) used across all environments
3. Use default values (useful for development)

## Setting Up Local Environment

### Option 1: Using application-local.yml (Recommended for Local Dev)

1. Set active profile:
   ```bash
   set SPRING_PROFILES_ACTIVE=local
   ```

2. Run the application:
   ```bash
   mvn spring-boot:run
   ```

3. Access at: `http://localhost:8080`

4. Login with:
   - Username: `admin`
   - Password: `admin`

### Option 2: Using application-dev.yml with Local Azure AD

1. Create an Azure AD application:
   - Register in Azure Portal
   - Note: Tenant ID, Client ID, Client Secret
   - Configure redirect URI: `http://localhost:8080/login/oauth2/code/azure`

2. Set environment variables:
   ```bash
   set DEV_AZURE_TENANT_ID=your-tenant-id
   set DEV_AZURE_CLIENT_ID=your-client-id
   set DEV_AZURE_CLIENT_SECRET=your-client-secret
   set SPRING_PROFILES_ACTIVE=dev
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Access at: `http://localhost:8080`

5. Click "Login with Azure" to authenticate via SSO

## Database Configuration

### H2 (Development/Testing)
```yaml
spring:
  datasource:
    url: jdbc:h2:file:./data/gno
    driver-class-name: org.h2.Driver
  h2:
    console:
      enabled: true
      path: /h2-console
```
- Access H2 Console: `http://localhost:8080/h2-console`

### PostgreSQL (Production)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://host:5432/database
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
```

## Logging Configuration

### Local (DEBUG)
```yaml
logging:
  level:
    root: INFO
    com.vodafone.gno: DEBUG
```

### Test (INFO)
```yaml
logging:
  level:
    root: WARN
    com.vodafone.gno: INFO
```

### Production (WARN)
```yaml
logging:
  level:
    root: WARN
    com.vodafone.gno: INFO
```

## Troubleshooting

### Issue: OAuth2 Login Not Working
**Solution**: Verify Azure AD credentials and redirect URI match exactly in Azure Portal

### Issue: Basic Auth Not Working (Local)
**Solution**: Ensure `application-local.yml` is active and excludes OAuth2AutoConfiguration

### Issue: Database Connection Failed
**Solution**: Check database URL, username, and password in environment variables

### Issue: JIRA Integration Not Working
**Solution**: Verify JIRA email, base URL, and API token are correct

## Security Considerations

1. **Never commit sensitive credentials** (client-secret, API tokens, passwords) to version control
2. **Use environment variables** for all sensitive configuration
3. **Secure redirect URI** in production (use HTTPS)
4. **Rotate API tokens** regularly
5. **Use environment-specific Azure AD applications** (dev, test, prod)
6. **Enable MFA** on production Azure AD accounts

## Configuration Priority

Spring Boot configuration priority (highest to lowest):
1. Command-line arguments: `java -D`
2. Environment variables: `SET VAR=value`
3. application-{profile}.yml files
4. application.yaml file
5. Default values hardcoded in properties

Example:
```bash
# This will override everything
java -Dspring.datasource.url=jdbc:postgresql://prod:5432/gno \
     -Dspring.profiles.active=prod \
     -jar gno-0.0.1-SNAPSHOT.jar
```

## Additional Resources

- [Spring Boot Profiles Documentation](https://spring.io/blog/2015/02/13/boot-strap-your-spring-boot-application)
- [Azure AD OAuth2 Configuration](https://learn.microsoft.com/en-us/azure/active-directory/develop/v2-oauth2-auth-code-flow)
- [Camunda BPM Properties](https://docs.camunda.org/manual/latest/reference/deployment-descriptors/tags/properties/)
- [Spring Security OAuth2](https://spring.io/projects/spring-security-oauth2-client)

