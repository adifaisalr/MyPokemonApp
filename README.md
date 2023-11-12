# MyPokemonApp
Simple App to browse pokemon using [pokeapi](https://pokeapi.co/) API

Features :
- Show list of pokemons
- Catch a pokemon (save to local db)
- Release a captured pokemon (remove from local db)
- Rename a pokemon (add fibonacci postfix)

## Prerequisite
- Android Studio (Girrafe or later)
- JDK version 17
- Minimum SDK : 31 (for android core splashscreen library)

## Architecture
Clean Architecture with layers :
- Domain : Use Case, Repository Interface, Model
- Data : Repository Implementation, DAO, API Service
- Presentation : Activity, Fragment, ViewModel

## Tech Stacks
- Jetpack Compose + navigation compose
- API Call : Retrofit + Gson
- Local DB : Room 
- Dependency Injection : Hilt
- Image Loader : Coil (for jetpack compose)
- Network logging : Flipper
