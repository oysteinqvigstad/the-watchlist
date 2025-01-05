# TheWatchlist

## About

TheWatchlist is a solution to a common hassle: keeping track of what movies you want to watch and where you are in TV shows. This mobile app stands out by narrowing the focus on tracking viewing progress, without the unnecessary extras of online communities, recommendations, ads, and so on

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
