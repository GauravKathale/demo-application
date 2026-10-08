# Quick Reference Card - SSO & Non-SSO Configuration

## 🚀 Quick Start (Copy & Paste)

### Local Development (Non-SSO)
```powershell
# Set profile
set SPRING_PROFILES_ACTIVE=local

# Run application
mvn spring-boot:run

# Access application
# URL: http://localhost:8080
# Username: admin
# Password: admin

# H2 Console
# URL: http://localhost:8080/h2-console
# JDBC: jdbc:h2:file:./data/gno
# User: sa
```

### Development with Azure AD (SSO)
```powershell
# Set profile
set SPRING_PROFILES_ACTIVE=dev

# Set Azure credentials (replace with your actual values)
set DEV_AZURE_TENANT_ID=your-tenant-id
set DEV_AZURE_CLIENT_ID=your-client-id
set DEV_AZURE_CLIENT_SECRET=your-client-secret

# Run application
mvn spring-boot:run

# Access application
# URL: http://localhost:8080
# Click "Sign in with Azure AD"
# Use your organization email
```

### Testing (SSO)
```powershell
# Set profile
set SPRING_PROFILES_ACTIVE=test

# Set test credentials
set TEST_AZURE_TENANT_ID=your-test-tenant-id
set TEST_AZURE_CLIENT_ID=your-test-client-id
set TEST_AZURE_CLIENT_SECRET=your-test-client-secret

# Run tests
mvn clean test -Dspring.profiles.active=test
```

### Production Build
```powershell
# Clean build
mvn clean package -DskipTests

# Run with production profile
$env:SPRING_PROFILES_ACTIVE="prod"
$env:PROD_AZURE_TENANT_ID="prod-tenant-id"
$env:PROD_AZURE_CLIENT_ID="prod-client-id"
$env:PROD_AZURE_CLIENT_SECRET="prod-client-secret"
$env:PROD_DB_URL="jdbc:postgresql://host:5432/gno"
$env:PROD_DB_USERNAME="user"
$env:PROD_DB_PASSWORD="password"

java -Dspring.profiles.active=prod -jar target/gno-0.0.1-SNAPSHOT.jar
```

---

## 📁 Key Configuration Files

| File | Location | Purpose |
|------|----------|---------|
| `application.yaml` | `src/main/resources/` | Base config (all profiles) |
| `application-local.yml` | `src/main/resources/` | Local Non-SSO config |
| `application-dev.yml` | `src/main/resources/` | Dev SSO config |
| `application-test.yml` | `src/main/resources/` | Test SSO config |
| `application-prod.yml` | `src/main/resources/` | Prod SSO config |
| `.env.example` | Project root | Environment variables template |
| `pom.xml` | Project root | Maven dependencies |

---

## 🔐 Switching Between Authentication Methods

### To Use Local (Non-SSO)
```bash
set SPRING_PROFILES_ACTIVE=local
# No Azure credentials needed
# Uses: admin/admin
```

### To Use Azure AD (SSO)
```bash
set SPRING_PROFILES_ACTIVE=dev
# Requires Azure credentials
# Uses: Azure AD login
```

---

## 🗄️ Database Connections

### H2 Console Access (Local Development)
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:file:./data/gno
Username: sa
Password: (leave empty)
```

### PostgreSQL Connection (Production)
```
Host: prod-db-host
Port: 5432
Database: gno
Username: gno_user
Password: (secure password)
```

---

## 🌍 Environment Variables Needed

### For Local (Non-SSO)
```
SPRING_PROFILES_ACTIVE=local
```

### For Dev (SSO)
```
SPRING_PROFILES_ACTIVE=dev
DEV_AZURE_TENANT_ID=<tenant-id>
DEV_AZURE_CLIENT_ID=<client-id>
DEV_AZURE_CLIENT_SECRET=<secret>
DEV_AZURE_REDIRECT_URI=http://localhost:8080/login/oauth2/code/azure
DEV_DB_URL=jdbc:h2:file:./data/gno
DEV_DB_USERNAME=sa
DEV_DB_PASSWORD=
DEV_JIRA_EMAIL=dev@example.com
DEV_JIRA_BASE_URL=https://dev-jira.atlassian.net
DEV_JIRA_API_TOKEN=<token>
```

### For Test (SSO)
```
SPRING_PROFILES_ACTIVE=test
TEST_AZURE_TENANT_ID=<tenant-id>
TEST_AZURE_CLIENT_ID=<client-id>
TEST_AZURE_CLIENT_SECRET=<secret>
TEST_AZURE_REDIRECT_URI=http://localhost:8080/login/oauth2/code/azure
TEST_DB_URL=jdbc:h2:file:./data/gno-test
TEST_DB_USERNAME=sa
TEST_DB_PASSWORD=
```

### For Prod (SSO)
```
SPRING_PROFILES_ACTIVE=prod
PROD_AZURE_TENANT_ID=<tenant-id>
PROD_AZURE_CLIENT_ID=<client-id>
PROD_AZURE_CLIENT_SECRET=<secret>
PROD_AZURE_REDIRECT_URI=https://prod.example.com/login/oauth2/code/azure
PROD_DB_URL=jdbc:postgresql://host:5432/gno
PROD_DB_USERNAME=<user>
PROD_DB_PASSWORD=<password>
PROD_DB_ENGINE_URL=jdbc:postgresql://host:5432/camunda
PROD_DB_ENGINE_USERNAME=<user>
PROD_DB_ENGINE_PASSWORD=<password>
```

---

## 🔗 Access URLs

| Resource | Local | Dev/Test | Prod |
|----------|-------|----------|------|
| Application | `http://localhost:8080` | `http://localhost:8080` | `https://prod.example.com` |
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` | Same | Same |
| H2 Console | `http://localhost:8080/h2-console` | `http://localhost:8080/h2-console` | Not available |
| Camunda Cockpit | `http://localhost:8080/camunda/app/cockpit/default` | Same | Same |

---

## 🎯 Login Credentials

### Local (Non-SSO)
```
Username: admin
Password: admin
```

### Dev/Test/Prod (SSO)
```
Use: Azure AD Account (Corporate Email)
Auth Flow: OAuth2 Authorization Code
Provider: https://login.microsoftonline.com/{tenant-id}/oauth2/v2.0/...
```

---

## 📋 Profile Comparison

```
┌─────────┬──────────────┬───────────────┬──────────────┬─────────────────┐
│ Profile │ Auth Method  │ Database      │ Best For     │ Setup Complexity│
├─────────┼──────────────┼───────────────┼──────────────┼─────────────────┤
│ local   │ Basic Auth   │ H2 File       │ Local Dev    │ Very Easy       │
│ dev     │ Azure AD     │ H2 File       │ Dev Team     │ Moderate        │
│ test    │ Azure AD     │ H2 Test       │ Automation   │ Moderate        │
│ prod    │ Azure AD     │ PostgreSQL    │ Production   │ Complex         │
└─────────┴──────────────┴───────────────┴──────────────┴─────────────────┘
```

---

## 🛠️ Common Commands

```powershell
# Build only (no tests)
mvn clean compile

# Build with tests
mvn clean package

# Build without tests
mvn clean package -DskipTests

# Run in local mode
set SPRING_PROFILES_ACTIVE=local & mvn spring-boot:run

# Run in dev mode with SSO
set SPRING_PROFILES_ACTIVE=dev & mvn spring-boot:run

# Run tests
mvn clean test

# Run tests with specific profile
mvn clean test -Dspring.profiles.active=test

# Stop application
# Press Ctrl+C in terminal

# Check running on port 8080
netstat -ano | findstr :8080

# Kill process on port 8080
taskkill /PID <PID> /F
```

---

## ⚠️ Troubleshooting Quick Fixes

| Issue | Solution |
|-------|----------|
| OAuth2 not working | Check Azure credentials in environment variables |
| Basic auth not working | Verify `SPRING_PROFILES_ACTIVE=local` |
| Database error | Check database URL, username, password |
| Port 8080 in use | Change port or kill process on 8080 |
| Profile not recognized | Verify spelling: `local`, `dev`, `test`, `prod` |
| No login button | Check if correct profile is active |
| H2 Console not available | H2 Console only available in local profile |

---

## 📚 Documentation Files

For detailed information, refer to:

| Document | Purpose |
|----------|---------|
| `SSO_SETUP_README.md` | Complete setup guide for all profiles |
| `SSO_CONFIG_GUIDE.md` | Technical configuration details |
| `CONFIGURATION_CHECKLIST.md` | Step-by-step setup verification |
| `IMPLEMENTATION_SUMMARY.md` | Overview of all changes |
| `.env.example` | All environment variables |

---

## 🔑 Azure Portal Required Values

When setting up Azure AD, you'll need:

1. **Tenant ID** (Directory ID)
   - Location: Azure AD → Overview → Directory ID

2. **Client ID** (Application ID)
   - Location: App Registration → Overview → Application ID

3. **Client Secret**
   - Location: App Registration → Certificates & secrets

4. **Redirect URI**
   - Set to: `http://localhost:8080/login/oauth2/code/azure` (dev)
   - Or: `https://prod.example.com/login/oauth2/code/azure` (prod)

5. **API Permissions**
   - Required: `email`, `profile`, `openid`
   - Graph: `https://graph.microsoft.com/.default`

---

## 💾 Saving Your Configuration

### Create .env.local for your setup
```bash
# Copy template
copy .env.example .env.local

# Edit .env.local with your values (DON'T commit this!)
# Set environment variables from this file

# Ensure .gitignore includes .env.local
# DON'T share credentials!
```

---

## 📞 Getting Help

1. **Check logs**: Look for error messages when application starts
2. **Read documentation**: Start with `SSO_SETUP_README.md`
3. **Use checklist**: Follow `CONFIGURATION_CHECKLIST.md`
4. **Verify variables**: Check all environment variables are set
5. **Review credentials**: Ensure Azure portal values are correct

---

## ✅ Quick Verification Checklist

- [ ] Can run `mvn clean` successfully
- [ ] Can run `mvn compile` successfully
- [ ] Local profile starts without errors
- [ ] Can access `http://localhost:8080` with local profile
- [ ] Can login with admin/admin in local mode
- [ ] Can access H2 Console at `http://localhost:8080/h2-console`
- [ ] Can access Swagger UI at `/swagger-ui/index.html`
- [ ] All YAML files are in correct locations
- [ ] All environment variables documented in `.env.example`
- [ ] PostgreSQL driver uncommented in pom.xml
- [ ] Spring Security dependency added in pom.xml

---

## 📊 Profile Decision Tree

```
┌─ Do you want to use local development? ─ YES → Use: local
│                                               Login: admin/admin
│
└─ NO → Do you have Azure AD? ─ YES ─┬─ For development? → Use: dev
        │                             ├─ For testing? → Use: test
        │                             └─ For production? → Use: prod
        │
        └─ NO → Set up Azure AD first (see Configuration Checklist)
```

---

## 🎓 Learning Path

1. **Start**: Run local profile (non-SSO)
   - Takes 2-3 minutes
   - No prerequisites

2. **Next**: Set up Azure AD for dev profile
   - Takes 15-20 minutes
   - Requires Azure account

3. **Then**: Configure test profile
   - Takes 5 minutes
   - Uses same Azure AD app or separate one

4. **Finally**: Deploy with prod profile
   - Takes 30+ minutes
   - Requires PostgreSQL, separate Azure AD app

---

## 🔒 Security Reminders

⚠️ **NEVER** commit credentials to Git  
⚠️ **NEVER** share `.env.local` files  
⚠️ **NEVER** hardcode secrets in files  
⚠️ **ALWAYS** use environment variables  
⚠️ **ALWAYS** rotate credentials periodically  
⚠️ **ALWAYS** use HTTPS in production  
⚠️ **ALWAYS** enable MFA on Azure accounts  

---

**Last Updated**: October 2024  
**Quick Reference Version**: 1.0  
**For Questions**: See Documentation Files

