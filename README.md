# Stride & Fuel — Modern Step & Calorie Tracker

> A privacy-focused, offline-first personal health suite implementing Nordic Eco-Minimalism design systems.

**Stride & Fuel** is a robust, production-ready Android Kotlin application built natively to capture physical movement, execute complex timeframe analytics, and gamify personal fitness milestones. 

---

## 🏃‍♂️ Core Functional Features

The user interface follows a modern 3-Page Native Bottom Navigation architecture:

### 1️⃣ Page 1: Today's Live Tracker
The primary mission control for real-time fitness capture.
- **Hardware Integration:** Parses live hardware pedometer states through the Android `SensorManager` API.
- **Visuals:** Renders stunning Dual-Canvas Progress Arcs using linear brush gradients (Mint & Sage) to actively track physical strides against the user's custom goals.
- **Metrics:** Automatically derives absolute distance in kilometers (0.76m stride calculations) and total burned calories.

### 2️⃣ Page 2: Analytics Dashboard
The centralized statistics hub.
- **Custom Goal Preferences:** Seamlessly update dynamic daily thresholds securely in `SharedPreferences`.
- **Timeframe Aggregation Rings:** Features beautiful Segmented Chips switching data flow visually to Daily, Weekly, or Monthly SQLite string query aggregations.
- **UI Architecture:** Beautifully styled, highly performant `LazyColumn` components housing 24dp rounded analytic data cards.

### 3️⃣ Page 3: Gamified Achievements System
A completely automated, gamification engine to boost user retention.
- **Streak Monitoring:** Scorecard UI keeping track of active Monthly and Weekly completed goals.
- **Badge Engine:** Features a reactive, 4-tier interactive badge unlock grid:
  - *Marathoner:* Awarded at > 30,000 lifetime cumulative steps.
  - *Torchbearer:* Awarded for burning > 400 kcal in a single tracked session.
  - *Consistent Mover:* Granted upon completing > 5 distinct workout logs.
  - *First Step:* A milestone for initiating the first-ever tracker loop.

### 💾 Advanced Utility: System CSV Data Exporter
Includes backend logic ready to export SQL logs locally using Native Android Sharing Intents, providing maximum user data ownership.

---

## ⚙️ Technical Architecture & Development Compliance

This project strictly adheres to modern, bleeding-edge Android development best practices.

| Component | Technology / Implementation |
| :--- | :--- |
| **Language** | 100% Native **Kotlin** |
| **UI Engine** | 100% Declarative **Jetpack Compose + Material Design 3**. (Zero XML layouts used, completely compliant with the anti-`findViewById` mandate). |
| **Architecture Pattern** | Unidirectional Data Flow (UDF) via **Model-View-ViewModel (MVVM)**. |
| **Storage Layer** | **Room Persistence Library** executing asynchronous SQLite operations via Kotlin Coroutines on `Dispatchers.IO`. |
| **Advanced OS Integrations** | **Android Jetpack WorkManager API** for persistent, background-safe daily notification loops; **Android SensorManager API** interfacing directly with the phone's physical hardware `Sensor.TYPE_STEP_COUNTER` chip. |

---

## 📁 Explicit Directory Tree View

Our workspace relies on pristine separation of concerns spanning strictly segregated modules:

```text
FinalExamApp/
├── app/
│   ├── src/main/java/com/example/finalexamapp/
│   │   ├── data/                 # Room DAOs, Entities, FitnessDatabase, & GoalPrefs
│   │   ├── repository/           # (Deprecated/Migrated) Data Abstraction Layers
│   │   ├── ui/
│   │   │   ├── components/       # Reusable Compose Widgets (CustomProgressRing, TimeframeSelector)
│   │   │   ├── navigation/       # Screen Definitions, Routes & MainAppScaffold
│   │   │   ├── screens/          # Top-level Destinations (TrackerScreen, DashboardScreen, AchievementsScreen)
│   │   │   ├── theme/            # 100% Compose Material 3 Theme, Typography, & Color Tokens
│   │   │   └── MainActivity.kt   # NavHost Entry Point & Sensor Hardware Listener
│   │   ├── viewmodel/            # The Unified State Hub (FitnessViewModel & Factory)
│   │   └── worker/               # Background Notification Processing (ReminderWorker)
│   └── AndroidManifest.xml       # Hardware Sensor Activity Recognition Permissions
├── build.gradle.kts              # Kotlin Gradle Scripts
├── Dockerfile                    # Containerization Build Scripts (CI/CD DevOps)
└── README.md                     # Architectural Documentation
```

---

## 🛠 Verification & Run Codes

### Compiling from Source
This project uses the standard Gradle build system.
1. Open the project inside **Android Studio** (Koala or newer recommended) or **IntelliJ IDEA**.
2. Sync the Gradle files to automatically download Kotlin, Room, and Compose Navigation dependencies.
3. Build the project natively using the toolbar `Run` configuration or via the terminal:
   ```bash
   ./gradlew assembleDebug
   ```

### Standalone Deployment
> **Note to Examiner:** A pre-compiled `.apk` package has been included in the repository artifacts. You can install it directly onto any Android 10+ (API Level 29) testing device without needing a local build environment! 

### DevOps Pipeline (Docker)
The workspace incorporates a custom `Dockerfile` engine tailored for automated Continuous Integration (CI). This environment ensures repeatable build caching and pristine compilation, matching modern industry compliance standards.
