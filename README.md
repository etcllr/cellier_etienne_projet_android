# Project Title

This is a movie catalog application developed in Kotlin and Java for Android. The application allows users to view a list of movies, save them to a local database, and view detailed information about each movie.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Android Studio Iguana | 2023.2.1 Patch 2 or later
- JDK 8 or later
- Android SDK

### Installing

1. Clone the repository: `git clone https://github.com/etcllr/your-repo-url.git`
2. Open the project in Android Studio.
3. Run the project on an emulator or a real device.

## Features

- View a list of movies from an API.
- Save movies to a local database using Room.
- View detailed information about each movie.
- Navigate between different screens using Android Navigation Component.

## Project Components

1. **MainActivity.kt** : The main activity of your application. It initializes the database and configures the toolbar and the floating action button. The floating action button opens the `MovieActivity`.

2. **CatalogActivity.kt** : This activity displays a list of movies retrieved from the local database. It uses a `RecyclerView` to display the movies and a button to return to the main activity.

3. **MovieActivity.kt** : This activity retrieves a list of movies from an external API, displays them in a `RecyclerView`, and offers the possibility to save these movies in the local database. It also uses notifications to inform the user when the movies are saved.

4. **AppDatabase.kt** : This is the local database of your application. It uses Room for data persistence.

5. **activity_main.xml, activity_catalog.xml, activity_movie.xml** : These are the XML layout files for your activities. They define the user interface of your activities.

6. **MoviesAdapter.kt, CatalogEntity.kt, MovieEntity.kt** : These are helper classes used to adapt the movie data to the `RecyclerView` and to define the structure of the movie data.

## Built With

- [Kotlin](https://kotlinlang.org/) - The main programming language.
- [Java](https://www.java.com/) - Also used in some specific parts of the application.
- [Room](https://developer.android.com/training/data-storage/room) - For data persistence.
- [Retrofit](https://square.github.io/retrofit/) - For network requests.
- [Gson](https://github.com/google/gson) - A Java serialization/deserialization library to convert Java Objects into JSON and back.
- [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - For asynchronous programming.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.

## Authors

- **Etienne Cellier** - *Initial work* - [etcllr](https://github.com/etcllr)
