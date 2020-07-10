# MoviesApp

This App shows an infinite list of popular persons in the tv industry and some basic information about them with a list of profile images if availble.

## Feature
* [View List of popular people with Pagination.](https://github.com/EsmaeelNabil/MoviesApp/tree/master/app/src/main/java/com/esmaeel/moviesapp/ui/PopularPersonsPage)
* [View Person Details Page.](https://github.com/EsmaeelNabil/MoviesApp/tree/master/app/src/main/java/com/esmaeel/moviesapp/ui/PersonDetailsPage)
* [Grid list of Person Images.](https://github.com/EsmaeelNabil/MoviesApp/tree/master/app/src/main/java/com/esmaeel/moviesapp/ui/PersonDetailsPage)
* [View Person's Image in Full screen.](https://github.com/EsmaeelNabil/MoviesApp/tree/master/app/src/main/java/com/esmaeel/moviesapp/ui/FullImagePage)
* [Save images to Gallary.](https://github.com/EsmaeelNabil/MoviesApp/blob/master/app/src/main/java/com/esmaeel/moviesapp/Utils/ImageSaver.kt)

## Built with
  - Kotlin
  - Dagger-Hilt for dependency injection
  - MVVM architectural pattern
  - Retrofit + okHttp3
  - Coroutines + Flow
  - LiveData - ViewModels
  - [Kotlin Extension Functions](https://github.com/EsmaeelNabil/MoviesApp/blob/master/app/src/main/java/com/esmaeel/moviesapp/Utils/Extensions.kt)
  - [Registering callbacks for an Activity Results](https://github.com/EsmaeelNabil/MoviesApp/blob/master/app/src/main/java/com/esmaeel/moviesapp/ui/PersonDetailsPage/PersonDetailsActivity.kt) -- [Docs](https://developer.android.com/training/basics/intents/result#register)
  - [Unit Tests, Mockito](https://github.com/EsmaeelNabil/MoviesApp/tree/master/app/src/test/java/com/esmaeel/moviesapp)
 
## Possible Future work.
* if the app gets bigger it might be better to do those this.
* Converting Activities into `Fragments` with one main activity or more depending on the scenarios.
* Using `Navigation Component` with SafeArgs to handle Data communication between them.
* Adding a `BaseActivity` + `BaseFragments` with common functionalties.


## You Need [Android Studio v4](https://developer.android.com/studio#downloads) or up to be able to build the source code.



## Sample APK
* [Google Drive APK](https://drive.google.com/file/d/1r4CJTikZ7ZtjVt9C7AkDDlZX_HNHoRA_/view?usp=sharing)





