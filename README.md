# weather-test-app

How demo-app works:
1. Can search for cities using HereMaps Suggestions Api
2. Can get Current location on a button click and fill search field automatically
3. Submit button appears when users click on a location
4. On the “Weather forecast” screen the user should be able to see the current weather for
the location at the top of the screen and the selected location.
5. Can go back to select another location.
6. Can pull to refresh the info on the screen.
7. Can see the weather forecast for a week.
8. The APIs that should be used can be found here: https://developer.here.com.

Technology (libs) used: 
- MVVM pattern
- DI Hilt
- Network handling - Retrofit, Gson, OkHttp logging
- Asyncronous req handling - Coroutines Flow, MutableStateFlow/StateFlow
- Navigation components
- SwipeRefreshLayout from androidx
- Glide image handling 
- Google location (play-services-location)
