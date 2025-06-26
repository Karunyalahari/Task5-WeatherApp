/*
Weather App - Final Submission (Task 5)
Developed using Kotlin + Android Studio
*/

// MainActivity.kt
package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    WeatherScreen()
                }
            }
        }
    }
}

@Composable
fun WeatherScreen() {
    val scope = rememberCoroutineScope()
    var city by remember { mutableStateOf("") }
    var weather by remember { mutableStateOf<WeatherResponse?>(null) }

    val api = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("Enter city") })
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            scope.launch {
                weather = try {
                    api.getWeather(city, "metric", YOUR_API_KEY)
                } catch (e: Exception) {
                    null
                }
            }
        }) {
            Text("Get Weather")
        }
        Spacer(modifier = Modifier.height(16.dp))
        weather?.let {
            Text("Temperature: ${it.main.temp} °C")
            Text("Condition: ${it.weather[0].description}")
        }
    }
}

// WeatherApi.kt
package com.example.weatherapp

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): WeatherResponse
}

// WeatherResponse.kt
package com.example.weatherapp

data class WeatherResponse(
    val weather: List<Weather>,
    val main: Main
)

data class Weather(
    val description: String
)

data class Main(
    val temp: Float
)

// build.gradle (Module)
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
}

android {
    namespace 'com.example.weatherapp'
    compileSdk 34

    defaultConfig {
        applicationId "com.example.weatherapp"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    buildFeatures {
        compose true
    }
    composeOptions {
        kotlinCompilerExtensionVersion '1.5.2'
    }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.activity:activity-compose:1.8.0'
    implementation 'androidx.compose.material3:material3:1.1.0'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
}

// README.md
# Weather App
A simple Android app to fetch current weather using OpenWeatherMap API.

## Features
- Enter city name and fetch weather
- Displays temperature and condition
- Built with Kotlin and Jetpack Compose

## Setup
1. Clone the repo
2. Replace `YOUR_API_KEY` with your OpenWeatherMap key
3. Build and run in Android Studio

## Deployment
- Release APK can be generated via `Build > Generate Signed Bundle`
- Ready for Play Store submission

## Author
ApexPlanet Internship - Final Task 5 Submission

---

**Note:** You must insert your OpenWeatherMap API key in `MainActivity.kt`.
```kotlin
api.getWeather(city, "metric", "YOUR_API_KEY")
```
Replace `"YOUR_API_KEY"` with your actual API key.

Would you like a ZIP of the project folder or help deploying it on Play Store / GitHub?
