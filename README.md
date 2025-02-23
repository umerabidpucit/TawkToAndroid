# TawkToAndroid

## Overview
This project is an Android application built using modern development practices. It focuses on efficient data management and a clean, maintainable architecture.

## Architecture
The project follows the **MVVM (Model-View-ViewModel)** architecture pattern, ensuring separation of concerns and testability. The flow is structured as follows:

- **View (Activity/Fragment):** Observes the ViewModel for data changes and updates the UI.
- **ViewModel:** Fetches data by invoking UseCases and prepares it for the View.
- **UseCase:** Contains the business logic, interacting with the Repository layer.
- **Repository:** Abstracts data sources (local database and remote API) and provides data to the UseCases.
- **Repository Implementation:** Manages data retrieval logic from network and Room database.

## Project Structure

```
com.umtech.tawkandroid
│
├── common              # Utilities (NetworkMonitor, NetworkUtils)
│
├── data                # Data layer (models, API, database)
│   ├── api             # Network calls
│   ├── model           # Data models (User, UserEntity, UserDetails, UserDetailEntity)
│   ├── remote          # Remote data handling
│   └── repository      # Database (AppDatabase)
│
├── di                  # Dependency Injection (AppModule)
│
├── domain              # Business logic
│   └── usecase         # UseCases (FetchUserUseCase, FetchUserDetailUseCase, SearchUseCase)
│
├── presentation        # UI layer
│   ├── ui              # Screens and components (MainScreen, UserDetailsScreen, UserItem)
│   └── viewmodel       # ViewModels
│
```

## Data Layer
- **Remote Data Source:** Retrieves user data from a network API.
- **Local Data Source:** Uses Room database (`AppDatabase`) to cache user information.
- **Flow Support:** The data layer uses **Kotlin Flows** to observe database changes reactively, ensuring real-time updates to the UI.

### Models
- **User:** Represents the network model for user data.
- **UserEntity:** Represents the local Room database model.
- **UserDetails:** Additional user information from the API.
- **UserDetailEntity:** Local representation of user details for Room.

## Features
- **User Listing:** Fetches and paginates user data from the API.
- **User Notes:** Displays and searches notes associated with users, stored locally.
- **Search:** Local search that matches `UserDao.login` and `UserDetailDao.notes`. Results are shown only if a matching login exists in both databases.
- **Reactive Data Updates:** Real-time updates to user data and notes using Kotlin Flows.

## Tech Stack
- **Kotlin:** Primary programming language.
- **Room:** Local database for caching user data.
- **Retrofit:** For network operations.
- **Coroutines:** For asynchronous operations.
- **Flows:** For reactive programming and real-time data updates.
- **Paging:** For efficient, paginated data loading.
- **Koin:** For dependency injection.
- **Jetpack Components:** ViewModel, LiveData, etc.

## How to Build
1. Clone the repository.
2. Open the project in Android Studio.
3. Sync Gradle files.
4. Run the app on an emulator or device.

