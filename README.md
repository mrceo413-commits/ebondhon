# ই-বন্ধন (E-Bondhon)

College Information Android App — similar to "E-Bondhon" (ই-বন্ধন) by Comilla Victoria Government College.

## Tech Stack

### Mobile App
- **Platform**: Native Android — Java (minimum SDK 21, target SDK 34)
- **Architecture**: MVVM (ViewModel + LiveData)
- **Networking**: Retrofit2 + OkHttp3
- **Image Loading**: Glide
- **Local Storage**: SharedPreferences (version cache) + JSON file cache in Internal Storage
- **UI Components**: Material Design 3 (AndroidX)
- **Bengali Font**: Hind Siliguri
- **Navigation**: Intent-based with Activity + Fragment

### Admin Panel
- **Backend**: Node.js + Express
- **Database**: SQLite (via Sequelize ORM)
- **Admin UI**: EJS + Bootstrap 5
- **File Uploads**: Multer
- **Auth**: Session-based with configurable credentials

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

### Android App
1. Open the project in Android Studio
2. Sync Gradle
3. Build and run on an emulator or device (API 21+)

### Admin Panel
```bash
cd admin-panel
cp .env.example .env    # Edit credentials as needed
npm install
npm run seed            # Populate sample data
npm start               # Runs on http://localhost:3000
```
Default login: `admin` / `admin123` (configurable in `.env`)

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

admin-panel/
├── server.js          # Main Express server
├── seed.js            # Database seed script
├── config/            # Database configuration
├── models/            # Sequelize models (12 models)
├── routes/            # API + admin + auth routes
├── views/             # EJS templates (layout, pages, partials)
├── public/            # CSS, JS, uploads
└── .env.example       # Environment config template
```
