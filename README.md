# SKU Board System - Spring Boot Web Application

> A production-ready web application built with **Spring Boot 3.5.8** featuring secure user authentication, multi-user session management, and a full-featured board system with pagination and search capabilities.  
> Developed as part of Java Spring course at **Sungkyul University** demonstrating enterprise-level web development patterns.

---

## 📋 Project Overview

This is a **full-stack Spring Boot application** that implements:

- **Secure user authentication** with BCrypt password encryption
- **Multi-user session management** using UUID-based session tracking
- **Board/Blog system** with CRUD operations, pagination, and search
- **File upload capabilities** with configurable size limits
- **Input validation** using Jakarta Bean Validation
- **Production-ready features** including session management and security best practices

**Developer:** Wai Yan Naung  
**Framework:** Spring Boot 3.5.8  
**Java Version:** 21  
**Database:** MySQL 8.x

---

## 🎯 Key Features

### 1. User Authentication & Authorization

- Secure user registration with encrypted password storage (BCrypt)
- Session-based authentication with UUID tracking for concurrent multi-user support
- Login/logout functionality with proper session management
- Protected routes requiring authentication before access

### 2. Board Management System

- Create, view, and list board posts with rich metadata
- Server-side validation for content integrity (title max 200 chars, content max 5000 chars)
- Search functionality with case-insensitive keyword filtering
- Pagination support (3 posts per page, configurable)
- Automatic date formatting (dd-MM-yyyy)
- Author name automatically assigned from logged-in user session

### 3. Multi-User Session Support

- Independent session management per user using UUID
- Concurrent user support without session conflicts
- Each user maintains their own authenticated session
- Secure session cookie handling with configurable timeout

### 4. File Upload System

- Configurable file size limits (max 10MB per file, 30MB per request)
- Static file serving for uploaded content
- Secure file storage in designated upload directory

### 5. Production-Ready Architecture

- Layered architecture: Controller → Service → Repository
- DTO pattern for clean data transfer
- Builder pattern for entity creation
- Input validation with @Valid annotations
- Exception handling for authentication failures
- Server-side defaults for views/likes counters

---

## 🏗️ Technology Stack

| Layer               | Technology                                                                     |
| ------------------- | ------------------------------------------------------------------------------ |
| **Backend**         | Spring Boot 3.5.8, Spring MVC, Spring Data JPA                                 |
| **Frontend**        | Thymeleaf, Bootstrap 4.5.2, HTML5, CSS3, JavaScript                            |
| **Database**        | MySQL 8.x with JPA/Hibernate ORM                                               |
| **Security**        | Spring Security (BCrypt password hashing), Session Management                  |
| **Build Tool**      | Maven 3.x                                                                      |
| **Template Engine** | Thymeleaf                                                                      |
| **Validation**      | Jakarta Bean Validation API                                                    |
| **Monitoring**      | Spring Boot Actuator                                                           |
| **JS Libraries**    | CounterUp, Easing, Isotope, Lightbox, OwlCarousel, Typed.js, Waypoints, WOW.js |
| **IDE / Tools**     | Visual Studio Code, Git, GitHub                                                |

---

## Features

---

## 📊 Database Schema

### **Member Table**

```sql
CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,  -- BCrypt encrypted
    age VARCHAR(50) NOT NULL,
    mobile VARCHAR(50) NOT NULL,
    address VARCHAR(255)
);
```

### **Board Table**

```sql
CREATE TABLE board (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT(5000) NOT NULL,
    user VARCHAR(255) NOT NULL,           -- Author name (from session)
    newdate VARCHAR(50) NOT NULL,         -- Format: dd-MM-yyyy
    count VARCHAR(50) NOT NULL DEFAULT '0',   -- View count
    likec VARCHAR(50) NOT NULL DEFAULT '0',   -- Like count
    password VARCHAR(255) NOT NULL,       -- Post password (for edit/delete)
    email VARCHAR(255) NOT NULL,          -- Author email
    mobile VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    age VARCHAR(50) NOT NULL DEFAULT '0'
);
```

---

## 🚀 Setup & Installation

### Prerequisites

- **Java JDK 21** or higher
- **MySQL Server 8.x**
- **Maven 3.6+** (or use included `mvnw.cmd`)
- **VS Code** with Spring Boot Extension Pack (recommended)

### Required VS Code Extensions

- `vmware.vscode-boot-dev-pack` - Spring Boot Dev Pack
- `vscjava.vscode-java-pack` - Java Extension Pack
- `cweijan.vscode-mysql-client2` - MySQL Client
- `cweijan.dbclient-jdbc` - Database Client

### Step 1: Clone/Download Project

```bash
# Clone the repository
git clone https://github.com/wynaung19/Java_Spring.git

# Navigate to project directory
cd Java_Spring
```

### Step 2: Configure Database

1. **Create MySQL database:**

```sql
CREATE DATABASE spring CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. **Update database credentials** in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/spring?serverTimezone=Asia/Seoul
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD  # Change this
```

3. Tables will be auto-created on first run (`spring.jpa.hibernate.ddl-auto=update`)

### Step 3: Build Project

```bash
# Windows
mvnw.cmd clean install

# Linux/Mac
./mvnw clean install
```

### Step 4: Run Application

**Option 1: Using VS Code Spring Boot Dashboard**

- Open project in VS Code
- Open Spring Boot Dashboard (left sidebar)
- Click ▶️ Run button next to `SkuApplication`

**Option 2: Using Maven Command**

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

**Option 3: Using Terminal**

```bash
java -jar target/sku-0.0.1-SNAPSHOT.jar
```

Application URL: **http://localhost:8080**

---

## 📁 Project Structure

```
sku/
├── src/main/
│   ├── java/com/waiyannaung/sku/
│   │   ├── SkuApplication.java              # 🚀 Application entry point
│   │   ├── controller/
│   │   │   ├── BlogController.java          # 📝 Board CRUD & pagination
│   │   │   ├── MemberController.java        # 🔐 Authentication & sessions
│   │   │   ├── FileController.java          # 📁 File upload handling
│   │   │   ├── BlogRestController.java      # 🌐 REST API endpoints
│   │   │   └── SkuController.java           # 🧪 Test endpoints
│   │   ├── model/
│   │   │   ├── domain/
│   │   │   │   ├── Board.java               # 📋 Board entity (JPA)
│   │   │   │   └── Member.java              # 👤 Member entity (JPA)
│   │   │   ├── repository/
│   │   │   │   ├── BoardRepository.java     # 🗄️ Board data access
│   │   │   │   └── MemberRepository.java    # 🗄️ Member data access
│   │   │   └── service/
│   │   │       ├── BlogService.java         # 💼 Board business logic
│   │   │       ├── MemberService.java       # 💼 Authentication logic
│   │   │       ├── AddArticleRequest.java   # 📦 Board DTO
│   │   │       └── AddMemberRequest.java    # 📦 Member DTO
│   │   └── config/                          # ⚙️ Configuration classes
│   └── resources/
│       ├── application.properties           # ⚙️ Configuration
│       ├── templates/                       # 🎨 Thymeleaf views
│       │   ├── board_list.html             # List with pagination
│       │   ├── board_view.html             # Post detail view
│       │   ├── board_write.html            # Create post form
│       │   ├── login.html                  # Login page
│       │   ├── join_new.html               # Registration form
│       │   ├── join_end.html               # Success page
│       │   ├── index.html                  # Homepage
│       │   └── error_page/
│       │       └── article_error.html      # Error handling
│       └── static/                         # 🎨 Static resources
│           ├── css/                        # Bootstrap & styles
│           ├── js/                         # JavaScript libraries
│           ├── img/                        # Images
│           └── upload/                     # User uploads
├── pom.xml                                 # 📦 Maven dependencies
├── mvnw.cmd / mvnw                         # Maven wrapper
└── README.md                               # 📖 This file
```

---

## 🌐 Application Routes

### Public Routes

| Route              | Method | Description             |
| ------------------ | ------ | ----------------------- |
| `/`                | GET    | 🏠 Homepage (portfolio) |
| `/member_login`    | GET    | 🔐 Login page           |
| `/join_new`        | GET    | ✍️ Registration page    |
| `/api/members`     | POST   | 📝 User registration    |
| `/api/login_check` | POST   | ✅ Authentication       |

### Protected Routes (Login Required)

| Route              | Method | Description                           |
| ------------------ | ------ | ------------------------------------- |
| `/board_list`      | GET    | 📋 List posts (paginated, searchable) |
| `/board_view/{id}` | GET    | 👁️ View single post                   |
| `/board_write`     | GET    | ✍️ Create post form                   |
| `/api/boards`      | POST   | 💾 Submit new post                    |
| `/api/logout`      | GET    | 🚪 Logout & clear session             |

---

## 🔐 Security Features

### 1. Password Encryption (BCrypt)

```java
// One-way hash - cannot be decrypted
String encodedPassword = passwordEncoder.encode(rawPassword);
// Stored in DB: $2a$10$N9qo8uLOickgx2ZMRZoMye...
```

### 2. Multi-User Session Management

```java
// Each user gets unique UUID session
String sessionId = UUID.randomUUID().toString();
session.setAttribute("userId", sessionId);        // Tracking ID
session.setAttribute("userName", member.getName()); // Display name
session.setAttribute("email", member.getEmail());   // Email
```

### 3. Route Protection

```java
// Redirect to login if not authenticated
if (session.getAttribute("userId") == null) {
    return "redirect:/member_login";
}
```

### 4. Server-Side Data Binding

```java
// Author assigned from session (prevents client manipulation)
String userName = (String) session.getAttribute("userName");
request.setUser(userName);  // Not from form input
```

---

## 📝 How to Use

### 1️⃣ Register Account

1. Go to http://localhost:8080/join_new
2. Fill in: Name, Email, Password, Age, Mobile, Address
3. Submit → Password encrypted with BCrypt
4. Success page displayed

### 2️⃣ Login

1. Go to http://localhost:8080/member_login
2. Enter email and password
3. Session created (UUID + user info)
4. Redirected to board list

### 3️⃣ Create Post

1. Click **"글쓰기"** (Write) button
2. Login required (auto-redirect if not logged in)
3. Fill form:
   - **Title** (max 200 chars) ✅ Required
   - **Password** (for post security)
   - **Content** (max 5000 chars) ✅ Required
4. Author auto-set from session
5. Date auto-set (dd-MM-yyyy)
6. Submit → Validated & saved

### 4️⃣ Search Posts

- Enter keyword → Searches titles (case-insensitive)
- Clear search → Shows all posts

### 5️⃣ Navigate Pages

- 3 posts per page
- Use numbered pagination links
- Previous/Next buttons

### 6️⃣ Logout

- Click **"로그아웃"** button
- Session cleared
- Redirected to login

---

## 🔧 Configuration

### Database (`application.properties`)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/spring?serverTimezone=Asia/Seoul
spring.datasource.username=root
spring.datasource.password=123123  # Change this
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Session Settings

```properties
server.servlet.session.timeout=300s           # 5 minutes
server.servlet.session.cookie.secure=true     # HTTPS only
```

### File Upload

```properties
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=30MB
spring.servlet.multipart.location=./src/main/resources/static/upload
```

---

## 🧪 Testing Checklist

### Authentication

- ✅ Register → Password encrypted in DB
- ✅ Login success → Session created
- ✅ Login fail → Error message
- ✅ Duplicate email → Prevented
- ✅ Logout → Session cleared

### Board System

- ✅ Create post → Author from session
- ✅ Validation → Empty title rejected
- ✅ Date format → dd-MM-yyyy
- ✅ Search → Case-insensitive
- ✅ Pagination → Works correctly

### Security

- ✅ Protected route → Redirects to login
- ✅ Multi-user → Independent sessions
- ✅ Session timeout → Works as configured

---

## 🐛 Troubleshooting

### App Won't Start

**MySQL connection error:**

- ✅ MySQL running: `mysql -u root -p`
- ✅ Database exists: `CREATE DATABASE spring;`
- ✅ Credentials correct in `application.properties`

**Port 8080 in use:**

```bash
# Windows: Kill process
netstat -ano | findstr :8080
taskkill /PID <pid> /F

# Or change port
server.port=8081
```

### Login Issues

**Correct credentials but login fails:**

- ✅ Check PasswordEncoder bean configured
- ✅ Verify user in DB: `SELECT * FROM member WHERE email='test@test.com';`
- ✅ Password should start with `$2a$` or `$2b$`

**Session lost immediately:**

- ✅ Enable cookies in browser
- ✅ If HTTP (not HTTPS): `server.servlet.session.cookie.secure=false`

### Board Errors

**500 Error creating post:**

- ✅ Session has `userName`: Check login sets it
- ✅ All NOT NULL fields have values
- ✅ Validation passes (title/content not blank)

**Author shows UUID:**

- ✅ Fixed: Now uses `userName` from session
- ✅ Check `board_write.html`: `th:value="${userName}"`

---

## 📚 Key Learnings

This project demonstrates:

- ✅ **Spring Boot 3.5.8** - Auto-configuration, starters
- ✅ **Spring MVC** - Controllers, request mapping
- ✅ **Spring Data JPA** - Repositories, entities, queries
- ✅ **Spring Security** - BCrypt, authentication
- ✅ **Thymeleaf** - Server-side templates
- ✅ **MySQL & Hibernate** - ORM, database management
- ✅ **Session Management** - Multi-user support with UUID
- ✅ **Input Validation** - Bean Validation API
- ✅ **Layered Architecture** - Controller → Service → Repository
- ✅ **DTO Pattern** - Clean data transfer
- ✅ **Security Best Practices** - Password hashing, session security

---

## 🔄 Future Enhancements

### Planned Features

- [ ] Edit/delete posts with password verification
- [ ] View counter increment
- [ ] Like/unlike functionality
- [ ] Comment system
- [ ] User profile pages
- [ ] Post categories
- [ ] Rich text editor (Markdown)
- [ ] Email verification
- [ ] Password reset

### Technical Upgrades

- [ ] Unit & integration tests (JUnit 5)
- [ ] REST API with JWT
- [ ] Role-based access control
- [ ] Database migrations (Flyway)
- [ ] Redis session store
- [ ] Caching layer
- [ ] Docker containerization
- [ ] CI/CD pipeline

---

## 👨‍💻 Author

**Wai Yan Naung**  
Computer Science Student  
Sungkyul University

📧 Email: [Your Email]  
🐙 GitHub: https://github.com/wynaung19

---

## 🙏 Acknowledgments

- **Professor** - Spring Boot instruction and guidance
- **Sungkyul University** - Academic resources
- **Spring Boot Team** - Excellent framework
- **Bootstrap** - UI framework
- **MySQL Community** - Database system

### References

- [Spring Boot Docs](https://docs.spring.io/spring-boot/)
- [Spring Data JPA](https://spring.io/guides/gs/accessing-data-jpa/)
- [Thymeleaf](https://www.thymeleaf.org/)
- [Bootstrap](https://getbootstrap.com/)

---

## 📄 License

Educational project for university coursework.  
Not licensed for commercial use.

---

**Last Updated:** December 13, 2025  
**Version:** 1.0.0  
**Status:** ✅ Production-Ready

---

_For support, check troubleshooting section or contact via GitHub issues._
