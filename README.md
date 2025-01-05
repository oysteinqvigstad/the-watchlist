# The Watchlist

## About

TheWatchlist is a streamlined mobile application designed to help users track their entertainment consumption without unnecessary extras. The app provides a clean and straightforward way to manage both movies and TV shows in separate watchlists. For TV series, users can track their progress at both episode and season levels, while movies can be organized into "To Watch" and "History" categories.

This project was developed as part of the Mobile Programming (PROG2007) course at NTNU during the 2023 academic year. The project team consisted of Idar Mykløy, Marthe Sjaaeng, Thea Urke Remøy, and Øystein Qvigstad. The application was built using Kotlin and Jetpack Compose, integrating TheMovieDB API for content information.

https://github.com/user-attachments/assets/9643245c-7fc8-4dc9-8e8d-591ed1249a1e



## Main Features

- Search for movies and shows
- Add movies and shows to watchlist
- Separate watchlists for movies and shows
- Category management (watching, to watch, history)
- Sort media by recent updates
- Detailed view for TV shows
- Track episodes and seasons progress
- In-app notifications for new episodes
- Share watchlist functionality

## Technical Stack

- **Language:** Kotlin
- **Framework:** Jetpack Compose with Material Design
- **Development Environment:** Android Studio
- **Database:** Room (SQLite)
- **External API:** TheMovieDB API (TMDB) with holgerbrandl's wrapper library
- **Image Loading:** Coil library
- **Design Tool:** Figma

## Architecture

The application follows a three-layer architecture:
1. UI Layer
2. Data Layer
3. Persistence Layer

Implements:
- Unidirectional Data Flow (UDF)
- Repository Pattern
- Dependency Injection
