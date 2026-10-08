# 📖 Documentation Index - SSO & Non-SSO Configuration

Welcome! This document serves as a navigation guide for all SSO and Non-SSO login configuration documentation.

---

## 🎯 Quick Navigation

### I want to...

- **🏃 Get Started Immediately**: Go to [QUICK_REFERENCE.md](#quick_reference)
- **📚 Learn Complete Setup**: Go to [SSO_SETUP_README.md](#sso_setup_readme)
- **✅ Verify My Setup**: Go to [CONFIGURATION_CHECKLIST.md](#configuration_checklist)
- **🔧 Understand Technical Details**: Go to [SSO_CONFIG_GUIDE.md](#sso_config_guide)
- **📋 See What Was Done**: Go to [IMPLEMENTATION_SUMMARY.md](#implementation_summary)
- **🔑 Configure Environment Variables**: Go to [.env.example](#env_example)

---

## 📑 Documentation Files Overview

### <a name="quick_reference"></a>1. **QUICK_REFERENCE.md**
**When to use**: When you need quick commands and don't want to read full documentation

**Contains**:
- Copy-paste ready commands for each profile
- Quick start for local, dev, test, and production
- Environment variables quick lookup
- Common troubleshooting fixes
- Quick profile comparison table
- Access URLs for all environments
- Database connection strings
- Learning path flowchart

**Best for**: Developers who already understand SSO and just need quick reference

**Reading time**: 5-10 minutes

---

### <a name="sso_setup_readme"></a>2. **SSO_SETUP_README.md**
**When to use**: When you want comprehensive step-by-step instructions

**Contains**:
- Quick start for each environment
- Configuration profiles overview
- Setup instructions for each profile (local, dev, test, prod)
- Azure AD setup prerequisites
- Environment variables reference
- Troubleshooting guide with detailed solutions
- Security best practices
- Resource links for external documentation
- Command reference guide

**Best for**: First-time setup, team onboarding, comprehensive learning

**Reading time**: 30-45 minutes

---

### <a name="configuration_checklist"></a>3. **CONFIGURATION_CHECKLIST.md**
**When to use**: When you want to verify your setup is complete

**Contains**:
- Pre-setup requirements checklist
- Local development setup checklist (step-by-step)
- Azure AD setup checklist with screenshot locations
- Dev/Test/Prod setup checklists
- Common issues with detailed solutions
- Security checklist
- Profile switching guide
- After-setup next steps
- Support troubleshooting

**Best for**: Setup verification, following step-by-step guides, problem solving

**Reading time**: 1-2 hours (active setup time)

---

### <a name="sso_config_guide"></a>4. **SSO_CONFIG_GUIDE.md**
**When to use**: When you need detailed technical understanding

**Contains**:
- Complete environment profiles overview
- Detailed profile descriptions (local, dev, test, prod)
- Base configuration explanation (application.yaml)
- Environment variable fallback chain explanation
- Database configuration details (H2 vs PostgreSQL)
- Logging configuration guide
- Configuration priority chain
- Security considerations
- Troubleshooting with technical details

**Best for**: System architects, advanced configuration, deep understanding

**Reading time**: 20-30 minutes

---

### <a name="implementation_summary"></a>5. **IMPLEMENTATION_SUMMARY.md**
**When to use**: When you want to see what was implemented

**Contains**:
- Overview of SSO and Non-SSO implementation
- Complete file listing with descriptions
- Configuration profile overview
- Key features summary
- Quick start commands
- Environment variables summary
- File organization diagram
- Next steps for users
- Testing the configuration
- Dependency summary
- Verification checklist

**Best for**: Project managers, team leads, understanding what was done

**Reading time**: 15-20 minutes

---

### <a name="env_example"></a>6. **.env.example**
**When to use**: When you need to configure environment variables

**Contains**:
- All environment variables documented
- Grouped by environment (local, dev, test, prod)
- Example values for each variable
- Fallback variables explained
- Generic vs specific variables
- Optional JVM settings
- Detailed notes for each configuration section
- Security best practices section

**Best for**: Configuration, setting up environment, variable reference

**Reading time**: 10-15 minutes

---

### 7. **application.yaml** (Base Configuration)
**Location**: `src/main/resources/application.yaml`

**Contains**:
- Common configuration for all profiles
- Plugin Identity (Entra/AAD) settings
- KramaX BPM configuration
- Camunda BPM engine settings
- Workflow engine configuration
- Swagger/OpenAPI settings
- Base logging configuration

**Best for**: Understanding base configuration applied to all profiles

---

### 8. **application-local.yml** (Local Development)
**Location**: `src/main/resources/application-local.yml`

**Contains**:
- Non-SSO basic authentication configuration
- H2 database configuration
- OAuth2 autoconfiguration exclusions
- H2 console enabling
- DEBUG logging settings
- Security user configuration

**Best for**: Local development without Azure AD

---

### 9. **application-dev.yml** (Development SSO)
**Location**: `src/main/resources/application-dev.yml`

**Contains**:
- Azure AD OAuth2 configuration
- H2 database for development
- Development-specific JIRA settings
- DEBUG logging for development
- Environment variable placeholders
- OAuth2 client registration details

**Best for**: Development team using Azure AD

---

### 10. **application-test.yml** (Testing SSO)
**Location**: `src/main/resources/application-test.yml`

**Contains**:
- Azure AD OAuth2 configuration for testing
- Separate H2 test database
- Test-specific JIRA settings
- Reduced logging (WARN level)
- Test database isolation

**Best for**: Automated testing with Azure AD

---

### 11. **application-prod.yml** (Production SSO)
**Location**: `src/main/resources/application-prod.yml`

**Contains**:
- Azure AD OAuth2 for production
- PostgreSQL database configuration
- Connection pooling setup
- Schema validation (no updates)
- Minimal logging (WARN level)
- Production-grade database settings

**Best for**: Production deployment

---

### 12. **pom.xml** (Modified)
**Location**: Project root

**Changes made**:
- Uncommented PostgreSQL driver
- Added Spring Security dependency
- Added comments for security dependencies
- Reorganized dependency ordering

**Best for**: Understanding project dependencies

---

## 🗺️ Documentation Flowchart

```
START
  │
  ├─ Never set up before? → QUICK_REFERENCE.md → SSO_SETUP_README.md → CONFIGURATION_CHECKLIST.md
  │
  ├─ Want to understand everything? → SSO_SETUP_README.md → SSO_CONFIG_GUIDE.md
  │
  ├─ Just need quick reference? → QUICK_REFERENCE.md
  │
  ├─ Need to set up specific environment? → CONFIGURATION_CHECKLIST.md
  │
  ├─ Want to understand implementation? → IMPLEMENTATION_SUMMARY.md
  │
  ├─ Need to configure variables? → .env.example
  │
  └─ Need technical details? → SSO_CONFIG_GUIDE.md
```

---

## 👥 Documentation by Role

### **👨‍💻 Developer (First Time)**
1. Read: QUICK_REFERENCE.md (5 min)
2. Read: SSO_SETUP_README.md → Local Development section (10 min)
3. Run: Commands from QUICK_REFERENCE.md (5 min)
4. Check: Application runs successfully
5. If issues: CONFIGURATION_CHECKLIST.md → Troubleshooting

### **👨‍💼 Developer (Experienced)**
1. Skim: QUICK_REFERENCE.md (2 min)
2. Copy: Commands from QUICK_REFERENCE.md
3. If issues: CONFIGURATION_CHECKLIST.md → Troubleshooting

### **🏗️ System Architect**
1. Read: IMPLEMENTATION_SUMMARY.md (15 min)
2. Read: SSO_CONFIG_GUIDE.md (20 min)
3. Review: application*.yml files (10 min)
4. Review: .env.example (5 min)

### **👔 Project Manager**
1. Read: IMPLEMENTATION_SUMMARY.md (15 min)
2. Skim: QUICK_REFERENCE.md (5 min)
3. Share: SSO_SETUP_README.md with team

### **🔧 DevOps/Infrastructure**
1. Read: SSO_CONFIG_GUIDE.md (20 min)
2. Focus: application-prod.yml and database configuration
3. Review: .env.example for production variables
4. Read: CONFIGURATION_CHECKLIST.md → Production section

### **👨‍🏫 Team Lead (Onboarding)**
1. Read: IMPLEMENTATION_SUMMARY.md (15 min)
2. Read: SSO_SETUP_README.md (30 min)
3. Share: QUICK_REFERENCE.md with new team members
4. Share: .env.example template
5. Have team complete: CONFIGURATION_CHECKLIST.md

---

## 📊 Documentation Map

```
ROOT LEVEL (Project Documentation)
├── QUICK_REFERENCE.md                    ← Start here for quick commands
├── SSO_SETUP_README.md                   ← Complete setup guide
├── CONFIGURATION_CHECKLIST.md            ← Step-by-step verification
├── IMPLEMENTATION_SUMMARY.md             ← What was implemented
├── .env.example                          ← Environment variables
└── pom.xml                               ← Dependencies (modified)

src/main/resources/ (Configuration Files)
├── application.yaml                      ← Base configuration
├── application-local.yml                 ← Local non-SSO
├── application-dev.yml                   ← Dev SSO
├── application-test.yml                  ← Test SSO
├── application-prod.yml                  ← Production SSO
└── SSO_CONFIG_GUIDE.md                   ← Technical guide

KEY LOCATIONS:
├── Configuration files: src/main/resources/application*.yml
├── Environment template: .env.example in project root
└── Documentation: All .md files in project root or src/main/resources/
```

---

## 🔄 Reading Paths by Goal

### **Goal: Set Up Local Development in 15 Minutes**
1. QUICK_REFERENCE.md → Local Development section (2 min)
2. Copy-paste commands (3 min)
3. Verify H2 console at http://localhost:8080/h2-console (5 min)
4. Done! ✅

### **Goal: Set Up Azure AD SSO in 30 Minutes**
1. QUICK_REFERENCE.md → Azure AD section (2 min)
2. SSO_SETUP_README.md → Development Setup with Azure AD (20 min)
3. CONFIGURATION_CHECKLIST.md → Development Setup (5 min)
4. Run application (3 min)

### **Goal: Understand Full Implementation**
1. IMPLEMENTATION_SUMMARY.md (15 min)
2. SSO_SETUP_README.md (30 min)
3. SSO_CONFIG_GUIDE.md (25 min)
4. Review YAML files (10 min)
5. Total: ~1.5 hours

### **Goal: Set Up Production**
1. QUICK_REFERENCE.md → Production Build (2 min)
2. SSO_SETUP_README.md → Production Environment Setup (20 min)
3. CONFIGURATION_CHECKLIST.md → Production Setup (30 min)
4. .env.example → Production variables (10 min)
5. SSO_CONFIG_GUIDE.md → Production details (15 min)
6. Total: ~1.5 hours

### **Goal: Troubleshoot Issues**
1. QUICK_REFERENCE.md → Troubleshooting section (2 min)
2. CONFIGURATION_CHECKLIST.md → Common Issues section (5 min)
3. SSO_CONFIG_GUIDE.md → Troubleshooting section (10 min)
4. Check application logs (5 min)

---

## 📋 Document Checklist

Use this to verify you have all documentation:

- [x] QUICK_REFERENCE.md
- [x] SSO_SETUP_README.md
- [x] CONFIGURATION_CHECKLIST.md
- [x] SSO_CONFIG_GUIDE.md
- [x] IMPLEMENTATION_SUMMARY.md
- [x] .env.example
- [x] INDEX.md (this file)
- [x] application.yaml
- [x] application-local.yml
- [x] application-dev.yml
- [x] application-test.yml
- [x] application-prod.yml
- [x] pom.xml (modified)

---

## 🔗 Cross-References

### By Topic

**Local Development (Non-SSO)**
- → QUICK_REFERENCE.md "Local Development"
- → SSO_SETUP_README.md "Local Development Setup"
- → CONFIGURATION_CHECKLIST.md "Local Development Setup"
- → application-local.yml configuration file

**Azure AD Setup**
- → SSO_SETUP_README.md "Development Setup with Azure AD SSO"
- → CONFIGURATION_CHECKLIST.md "Development Setup with Azure AD SSO"
- → .env.example "DEV_AZURE_*" section

**Database Configuration**
- → SSO_CONFIG_GUIDE.md "Database Configuration"
- → CONFIGURATION_CHECKLIST.md database sections
- → application-dev.yml, application-prod.yml

**Environment Variables**
- → .env.example (complete list)
- → SSO_CONFIG_GUIDE.md "Environment Variable Fallback Chain"
- → QUICK_REFERENCE.md "Environment Variables Needed"

**Troubleshooting**
- → QUICK_REFERENCE.md "Troubleshooting Quick Fixes"
- → CONFIGURATION_CHECKLIST.md "Common Issues & Solutions"
- → SSO_CONFIG_GUIDE.md "Troubleshooting"
- → SSO_SETUP_README.md "Troubleshooting"

**Security**
- → SSO_SETUP_README.md "Security Best Practices"
- → SSO_CONFIG_GUIDE.md "Security Considerations"
- → CONFIGURATION_CHECKLIST.md "Security Checklist"
- → QUICK_REFERENCE.md "Security Reminders"

---

## 📞 Need Help?

### Quick Help
1. **QUICK_REFERENCE.md** - For fast answers
2. **QUICK_REFERENCE.md "Troubleshooting Quick Fixes"** - For common issues

### Detailed Help
1. **CONFIGURATION_CHECKLIST.md** - Follow step-by-step
2. **SSO_SETUP_README.md** - For comprehensive guide
3. **SSO_CONFIG_GUIDE.md** - For technical details

### Not Finding Answer?
1. Check all "Troubleshooting" sections across documents
2. Verify environment variables are correctly set
3. Check application logs for error messages
4. Review Azure portal configuration
5. Verify all YAML files are in correct locations

---

## 📚 External Resources

Refer to these when documentation doesn't cover your situation:

- **Spring Boot Profiles**: https://spring.io/blog/2015/02/13/boot-strap-your-spring-boot-application
- **Azure AD OAuth2**: https://learn.microsoft.com/en-us/azure/active-directory/develop/v2-oauth2-auth-code-flow
- **Spring Security**: https://spring.io/projects/spring-security
- **Camunda BPM**: https://docs.camunda.org
- **KramaX BPM**: https://vodafone.github.io/kramax-bpm/

---

## ✅ Before You Start

Make sure you have:
- [x] Java 21+ installed
- [x] Maven installed
- [x] Code editor/IDE
- [x] Git (optional but recommended)
- [x] Access to project files
- [x] Azure account (for SSO setup, optional for local)

---

## 🎓 Suggested Learning Order

### For Complete Beginners
1. QUICK_REFERENCE.md (2 min overview)
2. SSO_SETUP_README.md "Quick Start" (3 min)
3. Attempt local setup (5 min)
4. CONFIGURATION_CHECKLIST.md "Local Development" if issues (varies)
5. Once working, read SSO_SETUP_README.md fully (30 min)
6. Then explore other profiles (dev, test, prod)

### For Experienced Developers
1. Glance at QUICK_REFERENCE.md (2 min)
2. Copy relevant commands and run (5 min)
3. Check documentation only if issues arise

### For System Administrators
1. IMPLEMENTATION_SUMMARY.md (15 min)
2. SSO_CONFIG_GUIDE.md (20 min)
3. CONFIGURATION_CHECKLIST.md "Production Setup" (30 min)
4. Review all application-*.yml files (20 min)

---

## 📝 Document Maintenance

**Last Updated**: October 2024  
**Current Version**: 1.0  
**Status**: Complete and ready for use  

**To Update This Document**:
- When adding new configuration files, update the file listing
- When changing setup procedures, update relevant guides
- Keep QUICK_REFERENCE.md most up-to-date as it's most frequently used
- Maintain version consistency across all documents

---

## 🎯 Success Metrics

✅ You're ready when you can:
- [ ] Explain the difference between local and SSO profiles
- [ ] Set up local development in under 10 minutes
- [ ] Configure environment variables correctly
- [ ] Start the application and log in successfully
- [ ] Access all major application URLs
- [ ] Understand where to find configuration files
- [ ] Know how to switch between profiles

---

**Navigation Complete!**  
**Select a document above and start your configuration journey.**

Need something specific? Use Ctrl+F to search this document.

---

**Happy Coding! 🚀**

