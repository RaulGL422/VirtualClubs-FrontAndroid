# VirtualClubs

VirtualClubs is an Android application built with Jetpack Compose that manages virtual sports clubs.  
It allows you to create and manage clubs, organize sports events, handle classes, students, attendance, and much more.

---

## 🛠 **Technologies Used**
- **Jetpack Compose** – Modern UI toolkit for Android
- **Hilt** – Dependency Injection
- **Retrofit** – HTTP client for API REST communication
- **DataStore** – Local data storage (preferences, tokens)
- **Kotlin** – Entirely written in Kotlin

---

## 📂 **Project Structure**
es.virtualclubs
├── data
│ ├── local.datastore # Local storage (e.g., UserPreferences, AppPreferences)
│ ├── remote
│ │ ├── api # Retrofit API interfaces
│ │ └── dto # Data Transfer Objects
│ └── repository.impl # Repository implementations that use Retrofit or local data
├── di # Hilt modules (NetworkModule, RepositoryModule, etc.)
├── domain
│ ├── model # Clean internal models
│ ├── repository # Repository interfaces
│ └── usecase # Use cases (business logic)
├── presentation
│ ├── components # Reusable Compose UI components (AppBar, Buttons, TextFields, etc.)
│ ├── handlers # ErrorHandler, NotificationHandler, UiMessage
│ ├── navigation # NavGraph and Destinations
│ ├── screens
│ │ ├── auth # Login, Register screens & ViewModels
│ │ └── settings # Settings screen & ViewModel
│ └── theme # Theme configuration (Color.kt, Shape.kt, Theme.kt, Type.kt)
├── utils # Constants, extension functions, helpers
├── VirtualClubsMainApp.kt # Application entry point (setContent, NavHost)
└── MainActivity.kt # Main activity

---

## 🚀 **Build & Run**
> This project is Android-only.  
> Make sure you have Android Studio **Giraffe** or newer and the latest Kotlin & Compose plugins installed.

1. Clone the repository (private)
2. Open with Android Studio
3. Connect your device or use an emulator
4. Press **Run** ▶️

The app will fetch data via API REST from your Spring Boot backend.

---

## ✏️ **Author**
Made with ❤️ by **Raul Galindo Lopez**  
[LinkedIn – Raul Galindo Lopez](https://www.linkedin.com/in/raul-gldev/)  
Year: **2025**

---

## 📄 **License**
This is a **private, closed-source project**.  
All rights reserved © 2025 Raul Galindo Lopez.  
Not intended for open source distribution.

> Note: The project is for personal/professional use and passive income generation.