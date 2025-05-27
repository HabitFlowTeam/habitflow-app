# HabitFlow App

The official repository for **HabitFlow**, a mobile application designed to help users build and
maintain habits through **gamification** and **social engagement**.

## ✒️ Authors

- Esteban Gaviria Zambrano – A00396019
- Juan David Colonia Aldana – A00395956
- Juan Manuel Díaz Moreno – A00394477
- Miguel Angel Gonzalez Arango – A00395687
- Pablo Fernando Pineda Patiño – A00395831

## 🧭 Sprints

### 🚀 Sprint 1

In the first sprint, we laid the foundation of the application, focusing on core user functionality
and system architecture:

- **User authentication:** Login, registration, and password recovery
- **App navigation:** Seamless flow between screens
- **Profile screen:** Initial implementation with dynamic and static components

### ⚙️ Sprint 2

The second sprint centered on database integration and feature completion for key modules:

- **User profile:** Fully functional with dynamic data display
- **Home screen:** Populated with real-time database content
- **Habit management:**
    - View all habits for the current user
    - Create, edit, and delete habits (delete functionality has a known bug with occasional issues)
- **Statistics:**
    - Personal stats (static view)
    - Global stats (fully interactive)
- **Backend preparation:** Infrastructure set for article list implementation in the next sprint

## 📐 Project Architecture

**HabitFlow** is an Android application developed using **Kotlin** and **Jetpack Compose**,
following the **MVVM (Model-View-ViewModel)** pattern and **Clean Architecture** principles.  
The project is organized using a **feature-based structure**, promoting scalability and
maintainability.

### 📁 Project Structure

```text
habitflow_app/
├── core/                  # Shared components and utilities
│   ├── database/          # Database configuration
│   ├── di/                # App-level dependency injection setup
│   ├── exceptions/        # Custom exception classes
│   ├── network/           # Networking and API service configuration
│   ├── ui/                # Shared UI components
│   │   ├── components/    # Reusable UI elements
│   │   └── theme/         # Themes, color schemes, and typography
│   ├── validation/        # Input validation utilities
│   └── utils/             # Utility classes and extensions
├── domain/                # Domain layer (business logic)
│   ├── models/            # Domain models/entities
│   ├── repositories/      # Repository interfaces
│   └── usecases/          # Application use cases
├── features/              # Application features/modules
│   ├── articles/          # Articles module
│   ├── authentication/    # Authentication module
│   ├── category/          # Category module
│   ├── gamification/      # Gamification module
│   ├── habits/            # Habits tracking module
│   └── profile/           # User profile module
└── navigation/            # App navigation configuration
```

## 🔄 Data Flow Overview

1. **UI (Screens)**: Captures user interactions and observes UI state from the ViewModel.
2. **ViewModel**: Handles UI events, coordinates use cases, and maintains screen state.
3. **UseCase**: Executes specific business logic using repositories.
4. **Repository**: Serves as an abstraction over data sources and coordinates data retrieval.
5. **DataSource**: Interacts directly with APIs or local databases.

## 🛠️ Core Technologies

- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt
- **Navigation**: Jetpack Navigation Compose
- **Database**: Supabase
- **Networking**: Retrofit

## 📜 Roles and Permissions

### Roles

1. **ADMIN**: Full access to all models in the application.
2. **USER**: Limited access to profiles.
3. **PUBLIC**: Basic permissions for creating users and profiles.

### Permissions by Model

| Collection                   | ADMIN | USER                                              | PUBLIC         |
|------------------------------|-------|---------------------------------------------------|----------------|
| **directus_users**           | CRUDS | –                                                 | Create, Delete |
| **profiles**                 | CRUDS | Read, Create, Update, Delete                      | Create         |
| **categories**               | CRUDS | Read, Update                                      | –              |
| **week_days**                | CRUDS | –                                                 | –              |
| **articles**                 | CRUDS | Read, Create, Update, Delete                      | –              |
| **articles_liked**           | CRUDS | Read, Create, Delete                              | –              |
| **articles_saved**           | CRUDS | –                                                 | –              |
| **habits**                   | CRUDS | Read (own), Create, Update (`is_deleted`), Delete | –              |
| **habits_days**              | CRUDS | Read, Create, Update, Delete                      | –              |
| **habits_tracking**          | CRUDS | Read, Create, Update                              | –              |
| **active_user_habits**       | Read  | Read (own)                                        | –              |
| **user_habit_calendar_view** | Read  | Read (own)                                        | –              |
| **user_articles_view**       | Read  | Read (own)                                        | –              |
| **user_habit_tracking_view** | Read  | Read (own)                                        | –              |
| **ranked_articles_view**     | Read  | Read (own)                                        | –              |

> `CRUDS` = Create, Read, Update, Delete, Share
> 
> `(own)` = Data access restricted to the current user only (`user_id = $CURRENT_USER`)

This configuration ensures that the **ADMIN** role has unrestricted access, the **USER** role has
limited permissions for profiles, and the **PUBLIC** role can create users and profiles.

## 🎥 Feature Demo Videos

- [FIRST SPRINT](https://youtube.com/shorts/6DV_3rcoUUM?si=5ZLLSyCo3XZZiW75)
- [SECOND SPRINT](https://youtu.be/6xCnMkiKkec?si=yufU6KzC-tV1_qrP)

## 📦 APK Downloads

- [FIRST SPRINT](https://drive.google.com/drive/folders/1csEQLhb_Hns3Ei4YnyuGl97zQszYbhs7?usp=sharing)
- [SECOND SPRINT](https://drive.google.com/drive/folders/17nLwdt2PZY5kiONoguB8fyAd4gAT7m-A)
