# SSO & Non-SSO Configuration Checklist

## Pre-Setup Requirements

- [ ] Java 21+ installed
- [ ] Maven installed and configured
- [ ] Git installed (if working with version control)
- [ ] Code editor/IDE (VS Code, IntelliJ IDEA, etc.)

---

## Local Development Setup (Non-SSO)

### Step 1: Prepare Environment
- [ ] Navigate to project root: `cd C:\Users\kulkarniS37\K\src1`
- [ ] Verify `pom.xml` exists
- [ ] Verify `src/main/resources/application.yaml` exists
- [ ] Verify `src/main/resources/application-local.yml` exists

### Step 2: Configure Profile
- [ ] Set environment variable: `set SPRING_PROFILES_ACTIVE=local`
- [ ] Verify by running: `echo %SPRING_PROFILES_ACTIVE%`

### Step 3: Build & Run
- [ ] Clean previous builds: `mvn clean`
- [ ] Build project: `mvn compile`
- [ ] Run application: `mvn spring-boot:run`

### Step 4: Verify Local Setup
- [ ] Application starts successfully
- [ ] No errors in logs
- [ ] Accessible at: `http://localhost:8080`
- [ ] Can access: `http://localhost:8080/swagger-ui/index.html`
- [ ] Can access: `http://localhost:8080/h2-console`

### Step 5: Test Login
- [ ] Navigate to login page
- [ ] Use credentials: admin / admin
- [ ] Successfully logged in
- [ ] Can access main application

### Step 6: Verify Database
- [ ] Access H2 Console: `http://localhost:8080/h2-console`
- [ ] JDBC URL: `jdbc:h2:file:./data/gno`
- [ ] User: `sa`
- [ ] Password: (empty)
- [ ] Can view tables in H2 console

---

## Development Setup with Azure AD SSO

### Step 1: Azure Portal Setup
- [ ] Log in to [Azure Portal](https://portal.azure.com)
- [ ] Navigate to Azure Active Directory
- [ ] Go to App registrations
- [ ] Click "New registration"
- [ ] Enter application name: "GNO-Dev"
- [ ] Select "Accounts in this organizational directory only"
- [ ] Click Register

### Step 2: Capture Azure Credentials
From the app overview page, record:
- [ ] **Tenant ID**: Copy from "Directory (tenant) ID" field
  ```
  Value: ___________________________________
  ```
- [ ] **Client ID**: Copy from "Application (client) ID" field
  ```
  Value: ___________________________________
  ```

### Step 3: Create Client Secret
- [ ] Go to "Certificates & secrets"
- [ ] Click "New client secret"
- [ ] Enter description: "GNO Dev Secret"
- [ ] Select expiration: Recommended 6-12 months
- [ ] Click "Add"
- [ ] **Client Secret**: Copy the secret value immediately (won't be visible later)
  ```
  Value: ___________________________________
  ```

### Step 4: Configure Redirect URI
- [ ] Go to "Authentication" in left menu
- [ ] Under "Redirect URIs", click "Add URI"
- [ ] Enter: `http://localhost:8080/login/oauth2/code/azure`
- [ ] Click "Save"

### Step 5: Grant API Permissions
- [ ] Go to "API permissions"
- [ ] Click "Add a permission"
- [ ] Select "Microsoft Graph"
- [ ] Choose "Delegated permissions"
- [ ] Search and add:
  - [ ] `email`
  - [ ] `profile`
  - [ ] `openid`
  - [ ] `https://graph.microsoft.com/.default`
- [ ] Click "Grant admin consent"

### Step 6: Environment Setup
- [ ] Create or edit `.env.local` file in project root
- [ ] Set the following values:
  ```
  SPRING_PROFILES_ACTIVE=dev
  DEV_AZURE_TENANT_ID=<tenant-id-from-step-2>
  DEV_AZURE_CLIENT_ID=<client-id-from-step-2>
  DEV_AZURE_CLIENT_SECRET=<secret-from-step-3>
  DEV_AZURE_REDIRECT_URI=http://localhost:8080/login/oauth2/code/azure
  ```
- [ ] **IMPORTANT**: Do NOT commit `.env.local` to git
- [ ] Add `.env.local` to `.gitignore` if not already present

### Step 7: Set Environment Variables in PowerShell
```powershell
$env:SPRING_PROFILES_ACTIVE="dev"
$env:DEV_AZURE_TENANT_ID="your-tenant-id"
$env:DEV_AZURE_CLIENT_ID="your-client-id"
$env:DEV_AZURE_CLIENT_SECRET="your-client-secret"
```
- [ ] Verify: `echo $env:SPRING_PROFILES_ACTIVE`

### Step 8: Build & Run
- [ ] Clean previous builds: `mvn clean`
- [ ] Build project: `mvn compile`
- [ ] Run application: `mvn spring-boot:run`

### Step 9: Verify SSO Setup
- [ ] Application starts successfully
- [ ] Logs show OAuth2 configuration loaded
- [ ] No OAuth2 initialization errors
- [ ] Accessible at: `http://localhost:8080`
- [ ] Login page shows "Sign in with Azure AD" button (or similar)

### Step 10: Test Azure AD Login
- [ ] Click "Sign in with Azure AD" button
- [ ] Redirected to Microsoft login page
- [ ] Log in with Azure AD account (corporate email)
- [ ] Redirected back to application
- [ ] Successfully logged in
- [ ] User information displayed from Azure AD

---

## Testing Environment Setup (SSO)

### Step 1: Prepare Test Environment
- [ ] Same Azure Portal setup as Dev (create separate "GNO-Test" app)
  - [ ] Tenant ID recorded
  - [ ] Client ID recorded
  - [ ] Client Secret recorded
  - [ ] Redirect URI: `http://localhost:8080/login/oauth2/code/azure`

### Step 2: Set Environment Variables
```powershell
$env:SPRING_PROFILES_ACTIVE="test"
$env:TEST_AZURE_TENANT_ID="your-test-tenant-id"
$env:TEST_AZURE_CLIENT_ID="your-test-client-id"
$env:TEST_AZURE_CLIENT_SECRET="your-test-client-secret"
```
- [ ] Verify variables are set

### Step 3: Run Tests
- [ ] Run: `mvn clean test -Dspring.profiles.active=test`
- [ ] All tests pass successfully
- [ ] No OAuth2 configuration errors

### Step 4: Verify Test Configuration
- [ ] Separate H2 database created (gno-test)
- [ ] Test database isolated from development
- [ ] Schema updates work correctly

---

## Production Environment Setup (SSO + PostgreSQL)

### Step 1: Azure Portal Production Setup
- [ ] Create new "GNO-Prod" Azure AD application
- [ ] Tenant ID recorded
  ```
  Value: ___________________________________
  ```
- [ ] Client ID recorded
  ```
  Value: ___________________________________
  ```
- [ ] Client Secret created and recorded
  ```
  Value: ___________________________________
  ```
- [ ] Redirect URI set to: `https://prod.example.com/login/oauth2/code/azure`

### Step 2: PostgreSQL Database Setup
- [ ] PostgreSQL server running and accessible
- [ ] Create main application database: `gno`
  - [ ] Database name: `gno`
  - [ ] Owner: `gno_user`
  - [ ] Password set and secured
- [ ] Create Camunda engine database: `camunda`
  - [ ] Database name: `camunda`
  - [ ] Owner: `camunda_user`
  - [ ] Password set and secured
- [ ] Record connection details:
  ```
  Main DB URL:    jdbc:postgresql://[host]:[port]/gno
  Main DB User:   gno_user
  Main DB Pass:   ___________________________________
  
  Engine DB URL:  jdbc:postgresql://[host]:[port]/camunda
  Engine DB User: camunda_user
  Engine DB Pass: ___________________________________
  ```

### Step 3: Environment Variables for Production
- [ ] Set all production variables:
  ```powershell
  $env:SPRING_PROFILES_ACTIVE="prod"
  $env:PROD_AZURE_TENANT_ID="prod-tenant-id"
  $env:PROD_AZURE_CLIENT_ID="prod-client-id"
  $env:PROD_AZURE_CLIENT_SECRET="prod-client-secret"
  $env:PROD_AZURE_REDIRECT_URI="https://prod.example.com/login/oauth2/code/azure"
  $env:PROD_DB_URL="jdbc:postgresql://prod-host:5432/gno"
  $env:PROD_DB_USERNAME="gno_user"
  $env:PROD_DB_PASSWORD="secure-password"
  $env:PROD_DB_ENGINE_URL="jdbc:postgresql://prod-host:5432/camunda"
  $env:PROD_DB_ENGINE_USERNAME="camunda_user"
  $env:PROD_DB_ENGINE_PASSWORD="secure-password"
  ```

### Step 4: PostgreSQL Driver
- [ ] Uncomment PostgreSQL dependency in `pom.xml` (if needed):
  ```xml
  <dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
  </dependency>
  ```
- [ ] If commented, uncomment lines 77-81 in pom.xml

### Step 5: Build for Production
- [ ] Clean: `mvn clean`
- [ ] Build: `mvn package -DskipTests`
- [ ] JAR created: `target/gno-0.0.1-SNAPSHOT.jar`

### Step 6: Deploy
- [ ] Copy JAR to production server
- [ ] Set all environment variables on production server
- [ ] Run: `java -Dspring.profiles.active=prod -jar gno-0.0.1-SNAPSHOT.jar`

### Step 7: Verify Production
- [ ] Application starts successfully
- [ ] Connected to PostgreSQL successfully
- [ ] Azure AD login working
- [ ] All integrations (JIRA, etc.) functioning
- [ ] Logs show INFO/WARN level (no DEBUG)

---

## Common Issues & Solutions

### Issue: Module Not Found
```
[ERROR] COMPILATION ERROR
[ERROR] Cannot find symbol: class XYZ
```
**Solution:**
- [ ] Run: `mvn clean install`
- [ ] Check all dependencies in pom.xml
- [ ] Verify kramax-bpm version is available

### Issue: Port Already in Use
```
Port 8080 is already in use
```
**Solution:**
- [ ] Change port in application.yaml:
  ```yaml
  server:
    port: 8081
  ```
- [ ] Or kill process using port 8080

### Issue: OAuth2 Not Loading
```
Caused by: org.springframework.beans.factory.NoSuchBeanDefinitionException
```
**Solution:**
- [ ] Verify Azure credentials are correct
- [ ] Check profile is set to dev/test/prod (not local)
- [ ] Verify all required environment variables are set
- [ ] Check logs for OAuth2 initialization errors

### Issue: Database Connection Refused
```
Cannot connect to database: Connection refused
```
**Solution for H2:**
- [ ] Check file permissions on `./data/` directory
- [ ] Ensure file path is accessible
- [ ] Try with different path (absolute vs relative)

**Solution for PostgreSQL:**
- [ ] Verify PostgreSQL is running
- [ ] Check host, port, username, password
- [ ] Verify database exists
- [ ] Check firewall rules

### Issue: JIRA Integration Failing
```
JIRA connection failed
```
**Solution:**
- [ ] Verify JIRA URL is correct
- [ ] Check JIRA email/username
- [ ] Regenerate API token in JIRA
- [ ] Verify proxy settings if behind corporate proxy

---

## Switching Between Profiles

### From Local to Dev
- [ ] Stop current application (Ctrl+C)
- [ ] Unset or change profile: `set SPRING_PROFILES_ACTIVE=dev`
- [ ] Set Azure credentials
- [ ] Run: `mvn spring-boot:run`

### From Dev to Local
- [ ] Stop current application (Ctrl+C)
- [ ] Set profile: `set SPRING_PROFILES_ACTIVE=local`
- [ ] Run: `mvn spring-boot:run`

### From Dev to Test
- [ ] Stop current application (Ctrl+C)
- [ ] Set profile: `set SPRING_PROFILES_ACTIVE=test`
- [ ] Set test credentials
- [ ] Run: `mvn clean test -Dspring.profiles.active=test`

---

## Security Checklist

- [ ] No credentials committed to Git
- [ ] `.env.local` in `.gitignore`
- [ ] Environment variables set securely (not in code)
- [ ] Production credentials different from dev/test
- [ ] API tokens and secrets not shared
- [ ] MFA enabled on Azure AD accounts
- [ ] Database passwords are strong
- [ ] Client secrets rotated regularly (every 6-12 months)
- [ ] Access logs reviewed periodically

---

## After First Successful Setup

1. **Document your setup:**
   - [ ] Record Azure AD Tenant ID (for your reference)
   - [ ] Document database connection details
   - [ ] Note any custom configurations

2. **Prepare team documentation:**
   - [ ] Share this checklist with team members
   - [ ] Provide sample `.env.example` values
   - [ ] Document any environment-specific settings

3. **Set up CI/CD (if applicable):**
   - [ ] Configure build pipeline with prod profile
   - [ ] Set secrets in CI/CD platform
   - [ ] Test deployment process

4. **Ongoing maintenance:**
   - [ ] Schedule credential rotation
   - [ ] Review access logs monthly
   - [ ] Update dependencies quarterly
   - [ ] Monitor application logs for errors

---

## Need Help?

1. **Check configuration files:**
   - Review `SSO_CONFIG_GUIDE.md`
   - Review `.env.example`
   - Review `SSO_SETUP_README.md`

2. **Review logs:**
   - Set `LOGGING_LEVEL_ROOT=DEBUG` for verbose logging
   - Look for OAuth2 initialization messages
   - Check for database connection errors

3. **Verify credentials:**
   - Recheck Azure Portal values
   - Verify all environment variables are set
   - Test database connectivity separately

4. **Common resources:**
   - Azure AD documentation
   - Spring Security OAuth2 documentation
   - Camunda BPM documentation
   - KramaX BPM documentation

---

**Document Version:** 1.0  
**Last Updated:** October 2024  
**Applicable to:** GNO Application v0.0.1+

