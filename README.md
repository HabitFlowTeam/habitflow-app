# HabitFlow App

The official repository for **HabitFlow**, a mobile application designed to help users build and
maintain habits through **gamification** and **social engagement**.

## ✒️ Authors

- Esteban Gaviria Zambrano – A00396019
- Juan David Colonia Aldana – A00395956
- Juan Manuel Díaz Moreno – A00394477
- Miguel Angel Gonzalez Arango – A00395687
- Pablo Fernando Pineda Patiño – A00395831

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
│   ├── network/           # Networking and API service configuration
│   ├── ui/                # Shared UI components
│   │   ├── components/    # Reusable UI elements
│   │   └── theme/         # Themes, color schemes, and typography
│   └── utils/             # Utility classes and extensions
├── domain/                # Domain layer (business logic)
│   ├── models/            # Domain models/entities
│   ├── repositories/      # Repository interfaces
│   └── usecases/          # Application use cases
├── features/              # Application features/modules
│   ├── articles/          # Articles module
│   ├── authentication/    # Authentication module
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

#### **Roles**
1. **ADMIN**: Full access to all models in the application.
2. **USER**: Limited permissions to create users and profiles.

#### **Permissions by Model**

- **directus_users**:
    - **ADMIN**: Read, Create, Update, Delete, Share
    - **USER**: Create
- **directus_roles**:
    - **ADMIN**: Read, Create, Update, Delete, Share
    - **USER**: Read
- **profiles**:
    - **ADMIN**: Read, Create, Update, Delete, Share
    - **USER**: Create
- **categories**:
    - **ADMIN**: Read, Create, Update, Delete, Share
    - **USER**: No access
- **week_days**:
    - **ADMIN**: Read, Create, Update, Delete, Share
    - **USER**: No access
- **articles**:
    - **ADMIN**: Read, Create, Update, Delete, Share
    - **USER**: No access
- **articles_liked**:
    - **ADMIN**: Read, Create, Update, Delete, Share
    - **USER**: No access
- **articles_saved**:
    - **ADMIN**: Read, Create, Update, Delete, Share
    - **USER**: No access
- **habits**:
    - **ADMIN**: Read, Create, Update, Delete, Share
    - **USER**: No access
- **habits_days**:
    - **ADMIN**: Read, Create, Update, Delete, Share
    - **USER**: No access
- **habits_tracking**:
    - **ADMIN**: Read, Create, Update, Delete, Share
    - **USER**: No access

This configuration ensures that the `ADMIN` role has unrestricted access to all models, while the `USER` role is restricted to creating users and profiles only.