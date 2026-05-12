# ই-বন্ধন (E-Bondhon)

College Information Android App — similar to "E-Bondhon" (ই-বন্ধন) by Comilla Victoria Government College.

## Tech Stack

- **Mobile App**: Native Android — Java (minimum SDK 21, target SDK 34)
- **Architecture**: MVVM (ViewModel + LiveData)
- **Networking**: Retrofit2 + OkHttp3
- **Image Loading**: Glide
- **Local Storage**: SharedPreferences (version cache) + JSON file cache in Internal Storage
- **UI Components**: Material Design 3 (AndroidX)
- **Bengali Font**: Hind Siliguri
- **Navigation**: Intent-based with Activity + Fragment

## Features

- 🏫 College information and history
- 👥 Faculty & department listings with teacher profiles
- 📋 Council information
- 📞 Important telephone numbers
- 📸 Photo gallery
- 📄 PRL (Post-Retirement Leave) records by year
- 📧 Contact information with direct call/email/web links
- 🔄 Offline support with data caching
- 🇧🇩 Full Bengali language UI with Hind Siliguri font

## Building

1. Open the project in Android Studio
2. Sync Gradle
3. Build and run on an emulator or device (API 21+)

## API Endpoints

The app connects to a backend admin panel via these endpoints:

| Endpoint | Description |
|----------|-------------|
| `GET /api/version` | Check data version |
| `GET /api/data` | Full app data JSON |
| `GET /api/college` | College info items |
| `GET /api/departments` | Faculty & department list |
| `GET /api/teachers/{deptId}` | Teachers by department |
| `GET /api/council` | Council data |
| `GET /api/prl` | PRL year list |
| `GET /api/gallery` | Photo gallery URLs |
| `GET /api/contact` | Contact information |

## Project Structure

```
app/src/main/java/com/ebondhon/
├── activities/        # All Activity classes
├── adapters/          # RecyclerView & GridView adapters
├── models/            # Data models (Gson serializable)
├── network/           # Retrofit API service & client
├── utils/             # Cache, version check, font utilities
└── viewmodels/        # MVVM ViewModels
```
