# GNO Application - SSO & Non-SSO Login Setup

## Quick Start

### Local Development (Non-SSO Authentication)

1. **Activate local profile:**
   ```bash
   set SPRING_PROFILES_ACTIVE=local
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Access the application:**
   - URL: `http://localhost:8080`
   - Username: `admin`
   - Password: `admin`

4. **Access H2 Console:**
   - URL: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:file:./data/gno`
   - User: `sa`
   - Password: (leave empty)

---

## Configuration Profiles Overview

| Profile | Auth Method | Database | Use Case | Default Credentials |
|---------|-------------|----------|----------|-------------------|
| `local` | Basic Auth (Non-SSO) | H2 | Local Dev | admin/admin |
| `dev` | Azure AD SSO | H2 | Development | Requires Azure creds |
| `test` | Azure AD SSO | H2 (test) | Testing | Requires Azure creds |
| `prod` | Azure AD SSO | PostgreSQL | Production | Requires Azure creds |

---

## File Structure

```
src/main/resources/
├── application.yaml              # Base configuration (common to all profiles)
├── application-local.yml         # Local development (Non-SSO)
├── application-dev.yml           # Development environment (SSO enabled)
├── application-test.yml          # Test environment (SSO enabled)
├── application-prod.yml          # Production environment (SSO enabled)
├── SSO_CONFIG_GUIDE.md           # Detailed SSO configuration guide
└── .env.example                  # Environment variables template
```

---

## Setup Instructions by Environment

### 1. Local Environment (Non-SSO)

**Best for:** Local development without Azure AD integration

**Files involved:**
- `application.yaml` (base configuration)
- `application-local.yml` (non-SSO specific)

**Steps:**

1. Set environment variable:
   ```bash
   set SPRING_PROFILES_ACTIVE=local
   ```

2. Run the application:
   ```bash
   mvn spring-boot:run
   ```

3. Login credentials:
   - Username: `admin` (can be customized in `application-local.yml`)
   - Password: `admin` (can be customized in `application-local.yml`)

**Configuration Features:**
- Basic HTTP authentication (no SSO required)
- H2 in-memory/file database
- H2 console enabled at `/h2-console`
- DEBUG logging for development
- No Azure AD configuration needed

---

### 2. Development Environment (SSO Enabled)

**Best for:** Development team using Azure AD

**Files involved:**
- `application.yaml` (base configuration)
- `application-dev.yml` (SSO specific)

**Prerequisites:**

1. **Azure AD Application Registration:**
   - Register a new app in [Azure Portal](https://portal.azure.com)
   - Note the following values:
     - `Tenant ID` (Directory ID)
     - `Client ID` (Application ID)
     - `Client Secret` (Create a new client secret)
   
2. **Configure Redirect URI in Azure:**
   - Add redirect URI: `http://localhost:8080/login/oauth2/code/azure`
   - Save the configuration

3. **Grant API Permissions:**
   - Add Microsoft Graph API permissions:
     - `email`
     - `profile`
     - `openid`
     - `https://graph.microsoft.com/.default`

**Setup Steps:**

1. Create `.env.local` from `.env.example`:
   ```bash
   copy .env.example .env.local
   ```

2. Edit `.env.local` and set:
   ```
   SPRING_PROFILES_ACTIVE=dev
   DEV_AZURE_TENANT_ID=your-actual-tenant-id
   DEV_AZURE_CLIENT_ID=your-actual-client-id
   DEV_AZURE_CLIENT_SECRET=your-actual-client-secret
   DEV_AZURE_REDIRECT_URI=http://localhost:8080/login/oauth2/code/azure
   ```

3. Set environment variables (PowerShell):
   ```powershell
   $env:SPRING_PROFILES_ACTIVE="dev"
   $env:DEV_AZURE_TENANT_ID="your-tenant-id"
   $env:DEV_AZURE_CLIENT_ID="your-client-id"
   $env:DEV_AZURE_CLIENT_SECRET="your-client-secret"
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

5. Access at: `http://localhost:8080`
   - Click "Login with Azure" button
   - Authenticate using your Azure AD account

---

### 3. Test Environment (SSO Enabled)

**Best for:** Automated testing with Azure AD integration

**Files involved:**
- `application.yaml` (base configuration)
- `application-test.yml` (test-specific SSO)

**Setup Steps:**

1. Set environment variables for test profile:
   ```bash
   set SPRING_PROFILES_ACTIVE=test
   set TEST_AZURE_TENANT_ID=your-test-tenant-id
   set TEST_AZURE_CLIENT_ID=your-test-client-id
   set TEST_AZURE_CLIENT_SECRET=your-test-client-secret
   ```

2. Run tests:
   ```bash
   mvn clean test -Dspring.profiles.active=test
   ```

3. Or run the application in test mode:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=test"
   ```

**Features:**
- Separate H2 database instance (gno-test)
- Reduced logging (WARN level for root)
- Test-specific Azure AD credentials
- Schema updates enabled for test setup

---

### 4. Production Environment (SSO Enabled)

**Best for:** Production deployment with PostgreSQL

**Files involved:**
- `application.yaml` (base configuration)
- `application-prod.yml` (production-specific)

**Prerequisites:**

1. **PostgreSQL Database:**
   - Main application database (gno)
   - Camunda engine database (camunda)
   - Both databases must be accessible

2. **Azure AD Production App:**
   - Register a separate prod Azure AD application
   - Ensure redirect URI is set to production URL (e.g., `https://prod.example.com/login/oauth2/code/azure`)

3. **Environment Variables:**
   ```bash
   set SPRING_PROFILES_ACTIVE=prod
   set PROD_AZURE_TENANT_ID=your-prod-tenant-id
   set PROD_AZURE_CLIENT_ID=your-prod-client-id
   set PROD_AZURE_CLIENT_SECRET=your-prod-client-secret
   set PROD_AZURE_REDIRECT_URI=https://prod.example.com/login/oauth2/code/azure
   set PROD_DB_URL=jdbc:postgresql://prod-db-host:5432/gno
   set PROD_DB_USERNAME=gno_user
   set PROD_DB_PASSWORD=your-secure-password
   set PROD_DB_ENGINE_URL=jdbc:postgresql://prod-db-host:5432/camunda
   set PROD_DB_ENGINE_USERNAME=camunda_user
   set PROD_DB_ENGINE_PASSWORD=your-secure-password
   ```

**Deployment:**

1. Build the application:
   ```bash
   mvn clean package -DskipTests
   ```

2. Run with production profile:
   ```bash
   java -Dspring.profiles.active=prod -jar target/gno-0.0.1-SNAPSHOT.jar
   ```

3. Access at: `https://prod.example.com`
   - Login via Azure AD SSO

**Features:**
- PostgreSQL database (production-ready)
- Minimal logging (WARN for root)
- Schema validation only (no auto-updates)
- Connection pooling (HikariCP) configured
- H2 console disabled

---

## Switching Between SSO and Non-SSO

### To use Non-SSO (Local):
```bash
# Set local profile
set SPRING_PROFILES_ACTIVE=local

# Run application
mvn spring-boot:run

# Login with username/password (admin/admin)
```

### To use SSO (Dev/Test/Prod):
```bash
# Set desired profile
set SPRING_PROFILES_ACTIVE=dev

# Set Azure AD credentials
set DEV_AZURE_TENANT_ID=your-tenant-id
set DEV_AZURE_CLIENT_ID=your-client-id
set DEV_AZURE_CLIENT_SECRET=your-client-secret

# Run application
mvn spring-boot:run

# Login via Azure AD button
```

---

## Environment Variables

All environment variables are documented in `.env.example`. 

### Variable Fallback Chain

The application uses a fallback chain for configuration:

```
${ENVIRONMENT_SPECIFIC} → ${GENERIC} → ${DEFAULT}
```

**Example:**
```
${DEV_AZURE_TENANT_ID} → ${AZURE_TENANT_ID} → dev-tenant-id
```

This allows flexibility:
1. Use environment-specific variables (DEV_, TEST_, PROD_)
2. Or use generic variables (AZURE_) for shared configuration
3. Or rely on default values in YAML files

---

## Troubleshooting

### Issue: "OAuth2 Login Not Working"
**Cause:** Missing or incorrect Azure AD credentials
**Solution:**
1. Verify Tenant ID, Client ID, Client Secret in Azure Portal
2. Ensure redirect URI matches exactly
3. Check environment variables are set correctly
4. View logs for OAuth2 errors

### Issue: "Basic Auth Login Not Working (Local Profile)"
**Cause:** Non-SSO excludes might not be working
**Solution:**
1. Verify `application-local.yml` is active
2. Check `spring.profiles.active=local`
3. Verify Spring Security basic auth is configured in YAML
4. Try restarting the application

### Issue: "Database Connection Failed"
**Cause:** Wrong database URL or credentials
**Solution:**
1. Verify database URL in environment variables
2. Check database username and password
3. Ensure database server is running and accessible
4. For PostgreSQL: verify PostgreSQL driver is available
5. For H2: check file system permissions

### Issue: "JIRA Integration Not Working"
**Cause:** Missing or incorrect JIRA credentials
**Solution:**
1. Verify JIRA email in environment variables
2. Check JIRA base URL is correct
3. Ensure API token is valid and not expired
4. Regenerate API token if needed in JIRA account settings

### View Application Logs
```bash
# Increase logging level
set LOGGING_LEVEL_ROOT=DEBUG
set LOGGING_LEVEL_COM_VODAFONE_GNO=DEBUG

# Run application
mvn spring-boot:run
```

---

## Security Best Practices

### Credentials Management

1. **Never commit secrets to version control:**
   - Add `.env.local` to `.gitignore`
   - Don't commit `.env` files with real credentials

2. **Use environment variables for secrets:**
   ```bash
   set PROD_DB_PASSWORD=your-secure-password
   set PROD_AZURE_CLIENT_SECRET=your-client-secret
   ```

3. **For CI/CD pipelines:**
   - Use CI/CD platform's secret management
   - Set secrets as environment variables in pipeline configuration
   - Never log sensitive values

4. **Rotate credentials regularly:**
   - Update Azure AD client secrets periodically
   - Rotate database passwords quarterly
   - Refresh JIRA API tokens annually

### Azure AD Security

1. **Use separate Azure AD apps per environment:**
   - Dev app for development
   - Test app for testing
   - Prod app for production

2. **Restrict application permissions:**
   - Only request required API permissions
   - Use least privilege principle

3. **Enable MFA on accounts:**
   - Require multi-factor authentication for prod access
   - Use conditional access policies

4. **Monitor access:**
   - Review Azure AD sign-in logs
   - Set up alerts for suspicious activities

---

## Additional Resources

### Documentation Files
- `SSO_CONFIG_GUIDE.md` - Detailed SSO/Non-SSO configuration
- `.env.example` - All available environment variables

### External References
- [Spring Boot Profiles](https://spring.io/blog/2015/02/13/boot-strap-your-spring-boot-application)
- [Azure AD OAuth2 Flow](https://learn.microsoft.com/en-us/azure/active-directory/develop/v2-oauth2-auth-code-flow)
- [Spring Security OAuth2 Client](https://spring.io/projects/spring-security-oauth2-client)
- [Camunda BPM Documentation](https://docs.camunda.org)
- [KramaX BPM Documentation](https://vodafone.github.io/kramax-bpm/)

---

## Support & Questions

For configuration issues or questions:

1. Check `SSO_CONFIG_GUIDE.md` for detailed configuration
2. Review `.env.example` for all available variables
3. Check application logs for error messages
4. Verify all environment variables are set correctly
5. Ensure Azure AD app is correctly configured in Azure Portal

---

## Quick Command Reference

```bash
# Local development (Non-SSO)
set SPRING_PROFILES_ACTIVE=local
mvn spring-boot:run

# Development (SSO)
set SPRING_PROFILES_ACTIVE=dev
set DEV_AZURE_TENANT_ID=...
set DEV_AZURE_CLIENT_ID=...
set DEV_AZURE_CLIENT_SECRET=...
mvn spring-boot:run

# Testing (SSO)
set SPRING_PROFILES_ACTIVE=test
mvn clean test

# Production Build
mvn clean package -DskipTests
java -Dspring.profiles.active=prod -jar target/gno-0.0.1-SNAPSHOT.jar

# Build with specific profile
mvn clean package -Dspring.profiles.active=prod

# View H2 Console (local only)
# http://localhost:8080/h2-console

# View Swagger UI
# http://localhost:8080/swagger-ui/index.html

# View Camunda Cockpit (if configured)
# http://localhost:8080/camunda/app/cockpit/default
```

---

**Last Updated:** October 2024
**Spring Boot Version:** 4.0.5
**Java Version:** 21

