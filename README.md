# 🌱 Java_Spring

> A learning project built with **Java Spring Boot**, featuring a personal portfolio as the homepage and a simple CRUD (게시판) system where users can create, edit, and delete posts.  
> Developed as part of daily Java Spring lessons at **Sungkyul University** with guidance from my professor.

---

## 💡 About the Project

This repository contains a **Spring Boot web application** that combines a **portfolio website** and a **simple post board** (`게시판`).  
The goal of this project is to **learn how Java Spring works in real practice** — including MVC structure, data handling, and integration with a database.

The home page displays **my personal portfolio**, while the post system allows:

- ✏️ Creating new posts
- 🧾 Viewing post details
- 🧰 Editing or deleting posts
- 💾 Storing data using **SQLite**

---

## ⚙️ Tech Stack

| Layer            | Technologies                                                                   |
| ---------------- | ------------------------------------------------------------------------------ |
| **Backend**      | Java, Spring Boot, Spring MVC                                                  |
| **Frontend**     | Thymeleaf, Bootstrap, Tailwind CSS, Animate.css                                |
| **JS Libraries** | CounterUp, Easing, Isotope, Lightbox, OwlCarousel, Typed.js, Waypoints, WOW.js |
| **Database**     | SQLite                                                                         |
| **IDE / Tools**  | Visual Studio Code, Git, GitHub                                                |

---

## 😎 Features

- 🧍 Portfolio as the home page
- 🗂️ Simple post board (CRUD operations)
- 🗃️ Data stored in SQLite
- 🎨 Responsive front-end with modern animations
- 🧱 Follows MVC architecture pattern
- 🚀 Can be run directly from VS Code

---

## 🧰 Project Structure

```
Java_Spring/
├── src/
│ ├── main/
│ │ ├── java/ # Java source files (controllers, services, etc.)
│ │ ├── resources/
│ │ │ ├── templates/ # Thymeleaf HTML templates
│ │ │ └── static/ # CSS, JS, and image files
│ └── test/ # Test files
├── build.gradle or pom.xml
├── README.md
└── .gitignore
```

---

## 🚀 Run Locally

You can easily run this project using **Visual Studio Code**.

### Prerequisites

- Install **Java JDK (17 or higher)**
- Install **VS Code** and **Spring Boot Extension Pack**
- Clone this repository

### Steps

# Extensions :

- cweijan.dbclient-jdbc
- cweijan.vscode-mysql-client2
- vmware.vscode-boot-dev-pack
- vscjava.vscode-java-pack

```bash
# 1️⃣ Clone the repository
git clone https://github.com/wynaung19/Java_Spring.git

# 2️⃣ Open the folder in VS Code
cd Java_Spring

# 3️⃣ Run the project
# make sure you already done setting up database
# Use VS Code Spring Boot Dashboard
open dashboard and click run
#or:
./gradlew bootRun
# or if using Maven:
mvn spring-boot:run
```
